package alert;

import javax.jms.*;
/* 
    Main App will manage doctors and patients 
    - listen to bothe doctors and patient producers
    - choose the best doctor  by multiple selectors 
    - alert the doctor
*/

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;



public class App {
    private java.util.List<Doctor> validDoctors = new java.util.ArrayList<>();
    Session session = null;
    
    public App() {
        initialize();
    }

    public void initialize() {
        ConnectionFactory connFactory = null;
        Connection conn = null;
        MessageConsumer doctorConsumer = null;
        MessageConsumer patientConsumer = null;
        boolean useTransaction = false;

        try {
            connFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            conn = connFactory.createConnection();
            conn.start();
            session = conn.createSession(useTransaction, Session.AUTO_ACKNOWLEDGE);


            // listent to doctors ...
            Topic doctorsTopic = session.createTopic("DoctorsTopic");
            doctorConsumer = session.createConsumer(doctorsTopic);
            doctorConsumer.setMessageListener(this::handleDoctorMsgListner);
            
            // listent to patients ...
            Topic patientsTopic = session.createTopic("PatientTopic");
            patientConsumer = session.createConsumer(patientsTopic);
            patientConsumer.setMessageListener(this::handlePatientMsgListner);
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDoctorMsgListner(Message message) {
        if(message instanceof MapMessage) {
            try {
                MapMessage doctorsData = (MapMessage) message;
                String docID = doctorsData.getString("ID");
                String docZone = doctorsData.getString("ZONE");
                boolean docIsValid = doctorsData.getBoolean("IS_VALID");
                Destination replyTo = doctorsData.getJMSReplyTo();

                System.out.println("One doctor joind the topic");
                System.out.println("Received Doctor Data: #" + docID + ", Zone-" + docZone + ", IsValid: " + docIsValid);
                System.out.println("waiting reply to :" + replyTo);

                // create Doctor
                Doctor doctor = new Doctor(docID, docZone, docIsValid, replyTo);
                
                // add doctor to list doctors
                addValidDoctor(doctor);
                System.out.println("Successfully added to valid list");
                System.out.println("Number of valid doctors: " + validDoctors.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}

    private void handlePatientMsgListner(Message message) {
                if (message instanceof MapMessage) {
                    MapMessage mapMessage = (MapMessage) message;
                    try {
                        // Retrieve temperature and heart rate from the MapMessage
                        double temperature = mapMessage.getDouble("temperature");
                        int heartRate = mapMessage.getInt("heart-rate");
                        String zone = mapMessage.getString("zone");
        
                        System.out.println("Received Patient Data:");
                        System.out.println("Temperature: " + temperature + " °C");
                        System.out.println("Heart Rate: " + heartRate + " bpm");
                        System.out.println("Zone: Zone-" + zone + " bpm");
        
                        // Check if the values are outside of the normal range
                        if (temperature > 38 || temperature < 36 || heartRate > 100 || heartRate < 60) {
                            // alert doctors => lunche DoctorProducer to produce alert messages to DoctorConsumer
                            System.out.println("Patient in trouble - Alert medical staff or call emergency services.");
                            
                            // allert the main app for urgent patient
                            notifyDoctor(zone);
                    } else {
                        System.out.println("Patient is in a stable condition.");
                    }
        
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
    }
    
    public java.util.List<Doctor> getValidDoctors() {
        return validDoctors;
    }

    public void addValidDoctor(Doctor doctor) {
        this.validDoctors.add(doctor);
    }

    public void notifyDoctor(String patientZone) {
        // choose the best match doctor
        // check by zone
        Doctor chosenDoctor = null;

        for (Doctor doctor : validDoctors) {
            if(doctor.getZone().equalsIgnoreCase(patientZone)) {
                chosenDoctor = doctor;
                break;
            }
        }

        if(chosenDoctor!=null) {
            MessageProducer producer = null;
            try {
                // get the tmp queue of the doctor
                Queue tmpQueue = (Queue) chosenDoctor.getDestination();
                System.out.println("queue: " + tmpQueue);
                producer = session.createProducer(tmpQueue);
                TextMessage msg = session.createTextMessage("Urgent case! Please attend immediately.");
                producer.send(msg);
                System.out.println("Sent urgent alert to Doctor #" + chosenDoctor.getID());
            } catch (JMSException e) {
                System.out.println("Something went wrong!");
                // System.out.println("ERROR -> " + e);

            }finally {
                try {
                    producer.close();
                } catch (JMSException jmsEx) {
                    jmsEx.printStackTrace();
                }
            }

            
        }else {
            System.out.println("No valid doctors!");
        }

    }

    public static void main(String[] args) {
        new App();
    }


}

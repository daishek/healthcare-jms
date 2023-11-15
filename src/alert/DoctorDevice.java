package alert;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/* 
    Note: Doctor Device can be eather producer or consumer
    Produce its validation
    Listen to main app's response or alert 
*/

public class DoctorDevice {
    public DoctorDevice(String doctorID, String doctorZone) {
        this(doctorID, doctorZone, true);
    }

    public DoctorDevice(String doctorID, String doctorZone, boolean isValid) {
        ConnectionFactory connFactory;
        Connection conn = null;
        Session session = null;
        Destination destination;
        boolean useTransaction = false;
        MessageProducer producer = null;

        try {
            connFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            conn = connFactory.createConnection();
            conn.start();

            System.out.println("|------------------------");
            System.out.println("| DoctorID: #" + doctorID);
            System.out.println("| Doctor Zone: " + doctorID);
            System.out.println("|------------------------");

            session = conn.createSession(useTransaction, Session.AUTO_ACKNOWLEDGE);

            /* produce doctor's validation */
            destination = session.createTopic("MedicalSystem.Doctors." + doctorID);
            producer = session.createProducer(destination);

            MapMessage doctorData = session.createMapMessage();
            doctorData.setString("ID", doctorID);
            doctorData.setString("ZONE", doctorZone);
            doctorData.setBoolean("IS_VALID", isValid);

            // create temp queue so the main app contact the doctor device using this unique queue
            Queue tempQueue = session.createTemporaryQueue();
            doctorData.setJMSReplyTo(tempQueue);

            producer.send(doctorData);


            /* liten to main app's response */
            // listen to the temp queue...
            MessageConsumer alertConsumer = session.createConsumer(tempQueue);
            System.out.println("listening to temp queue...");
            alertConsumer.setMessageListener(message -> {
                try {
                        // alert message here
                        System.out.println("***********************");
                        System.out.println("Recieved an alert");
                        System.out.println("***********************");
 
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            });
            
        } catch (JMSException jmsEx) {
            jmsEx.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new DoctorDevice("132", "C");
    }
}

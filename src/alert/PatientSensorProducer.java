package alert;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.UUID;

public class PatientSensorProducer {
    public PatientSensorProducer(float temperature, int hearRate, String zone) {
        ConnectionFactory connFactory;
        Connection conn = null;
        boolean useTransaction = false;
        MessageProducer producer = null;
        Session session = null;

        String patientID = UUID.randomUUID().toString();
        try {
            connFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            conn = connFactory.createConnection();
            conn.start();

            System.out.println("|------------------------");
            System.out.println("| PatientID: #" + patientID);
            System.out.println("| Patient Zone: " + zone);
            System.out.println("| Patient Temperature: " + temperature);
            System.out.println("| Patient Heart beat: " + hearRate);
            System.out.println("|------------------------");

            session = conn.createSession(useTransaction, Session.AUTO_ACKNOWLEDGE);
            Destination queue = session.createTopic("MedicalSystem.Patients.>" + patientID );
            producer = session.createProducer(queue);

            
            MapMessage sensorData = session.createMapMessage();
            sensorData.setFloat("temperature", temperature);
            sensorData.setInt("heart-rate", hearRate);
            sensorData.setString("zone", zone);
            producer.send(sensorData);


        } catch (JMSException jmsEx) {

        }finally {
            try {
                producer.close();
                session.close();
                conn.close();
                
            } catch (JMSException jmsEx) {
                jmsEx.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new PatientSensorProducer(28, 40, "C");
    }
    
}

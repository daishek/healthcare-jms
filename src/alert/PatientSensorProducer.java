package alert;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class PatientSensorProducer {
    public PatientSensorProducer(float temperature, int hearRate, String zone) {
        ConnectionFactory connFactory;
        Connection conn = null;
        boolean useTransaction = false;
        MessageProducer producer = null;
        Session session = null;
        try {
            connFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            conn = connFactory.createConnection();
            conn.start();

            session = conn.createSession(useTransaction, Session.AUTO_ACKNOWLEDGE);
            Destination queue = session.createTopic("PatientTopic");
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

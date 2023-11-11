package alert;

import java.util.Properties;
import java.util.Random;

import javax.jms.*;
import javax.naming.Context;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

public class PatientSensorConsumer {
    private TextMessage message;
    private Session session;
    public PatientSensorConsumer() {    
        ActiveMQConnectionFactory connFactory;
        Connection conn = null;
        session = null;
        Destination destination;
        MessageConsumer consumer = null;
        int clientNumber = new Random().nextInt();
        try {
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
            connFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            connFactory.setTrustAllPackages(true);
            conn = connFactory.createConnection();
            conn.start();


            destination = new ActiveMQQueue("PatientQueue");
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(destination);

            System.out.println("Listning...");
            consumer.setMessageListener(new PatientConsumerMsgListner());
        }catch (JMSException jmsEx) {
            jmsEx.printStackTrace();

        }
    }

    public static void main(String[] args) {
        new PatientSensorConsumer();
    }
}

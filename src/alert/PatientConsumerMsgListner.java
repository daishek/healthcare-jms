package alert;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class PatientConsumerMsgListner implements MessageListener {
    @Override
    public void onMessage(Message message) {
        if (message instanceof MapMessage) {
            MapMessage mapMessage = (MapMessage) message;
            try {
                // Retrieve temperature and heart rate from the MapMessage
                double temperature = mapMessage.getDouble("temperature");
                int heartRate = mapMessage.getInt("heart-rate");

                System.out.println("Received Sensor Data:");
                System.out.println("Temperature: " + temperature + " °C");
                System.out.println("Heart Rate: " + heartRate + " bpm");

                // Check if the values are outside of the normal range
                if (temperature > 38 || temperature < 36 || heartRate > 100 || heartRate < 60) {
                    // alert doctors => lunche DoctorProducer to produce alert messages to DoctorConsumer
                    System.out.println("Patient in trouble - Alert medical staff or call emergency services.");
                    // new DoctorDevice();
                    // allert the main app for urgent patient
            } else {
                System.out.println("Patient is in a stable condition.");
            }

            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}

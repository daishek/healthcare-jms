package alert;

import javax.jms.Destination;

public class Doctor {
    private String id;
    private String zone;
    private boolean isValid;
    private Destination destination;

    // Constructor
    public Doctor(String id, String zone, boolean isValid, Destination destination) {
        this.id = id;
        this.zone = zone;
        this.isValid = isValid;
        this.destination = destination;
    }

    // Getter methods
    public String getID() {
        return id;
    }

    public String getZone() {
        return zone;
    }

    public Destination getDestination() {
        return destination;
    }

}
package sg.edu.rp.c302.c302l12displayincident;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Date;

public class Incident implements Serializable {
    private Date date;
    private String type;
    private double latitude;
    private double longitude;
    private String message;


    public Incident(String type,double latitude, double longitude, String message) {
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;

    }

    public Incident() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}

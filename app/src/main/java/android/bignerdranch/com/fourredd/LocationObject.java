package android.bignerdranch.com.fourredd;

/**
 * Initializes a location object for use in the four activity
 */

import java.sql.Timestamp;

/**
 * Created by Mike on 12/8/15.
 */
public class LocationObject {
    double latitude, longitude;
    String user;
    Timestamp mTimestamp;


    public LocationObject(String user, double latitude, double longitude, Timestamp timestamp){

        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mTimestamp = timestamp;

    }


    public LocationObject(String user, double latitude, double longitude){

        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mTimestamp = null;

    }


    public LocationObject(){

        this.user = "";
        this.latitude = -1;
        this.longitude = -1;
        this.mTimestamp = null;

    }
}
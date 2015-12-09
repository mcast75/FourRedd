package android.bignerdranch.com.fourredd;

import java.util.ArrayList;

/**
 * Created by Mike on 12/8/15.
 */
public class LocationForum {
    ArrayList<LocationObject> allLocs;

    public LocationForum(){

        this.allLocs = new ArrayList<LocationObject>();
    }

    public void addLocation(LocationObject locationObject){

        this.allLocs.add(locationObject);

    }

    public ArrayList<LocationObject> getAllLocations(){
        return this.allLocs;
    }
}

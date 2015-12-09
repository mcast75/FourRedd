package android.bignerdranch.com.fourredd;

import java.io.IOException;

/**
 * Created by Mike on 12/8/15.
 */
public interface GetLocationForumCallback {

    public abstract void done(LocationForum returnedLocationForum) throws IOException;

}

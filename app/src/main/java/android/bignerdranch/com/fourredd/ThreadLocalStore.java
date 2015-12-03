package android.bignerdranch.com.fourredd;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Mike on 12/3/15.
 */
public class ThreadLocalStore {

    public static final String SP_NAME = "threadDetails";
    SharedPreferences threadLocalDatabase;

    public ThreadLocalStore(Context context){
        threadLocalDatabase = context.getSharedPreferences(SP_NAME,1);

    }

    public void storeThreadData(Thread thread){
        SharedPreferences.Editor spEditor = threadLocalDatabase.edit();
        spEditor.putInt("threadID", thread.id);
        spEditor.putString("username", thread.user);
        spEditor.putString("title", thread.title);
        spEditor.putString("text", thread.text);
        spEditor.putInt("num_like", thread.like);
        spEditor.putInt("num_dislikes", thread.dislikes);

        spEditor.commit();
    }

    public Thread getCurrentThread(){
        int threadID = threadLocalDatabase.getInt("threadID", -1);
        String username = threadLocalDatabase.getString("username", "");
        String title = threadLocalDatabase.getString("title", "");
        String text = threadLocalDatabase.getString("text", "");
        int like = threadLocalDatabase.getInt("num_like", -1);
        int dislikes = threadLocalDatabase.getInt("num_dislikes", -1);


        Thread storedThread = new Thread(username, title, like, dislikes, threadID, text);
        return storedThread;

    }

    public void clearThreadData(){
        SharedPreferences.Editor spEditor = threadLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

}

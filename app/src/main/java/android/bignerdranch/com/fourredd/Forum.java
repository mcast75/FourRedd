package android.bignerdranch.com.fourredd;

import java.util.ArrayList;

/**
 * Created by Mike on 12/1/15.
 */
public class Forum {

    ArrayList<Thread> allThreads;

    public Forum(){

        allThreads = new ArrayList<Thread>();
    }

    public void addThread(Thread thread){

        allThreads.add(thread);

    }

    public ArrayList<Thread> getAllThreads(){
        return allThreads;
    }
}

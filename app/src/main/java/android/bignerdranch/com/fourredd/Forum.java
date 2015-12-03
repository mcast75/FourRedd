package android.bignerdranch.com.fourredd;

import java.util.ArrayList;

/**
 * Created by Mike on 12/1/15.
 */
public class Forum {

    ArrayList<Thread> allThreads;

    public Forum(){

        this.allThreads = new ArrayList<Thread>();
    }

    public void addThread(Thread thread){

        this.allThreads.add(thread);

    }

    public ArrayList<Thread> getAllThreads(){
        return this.allThreads;
    }
}

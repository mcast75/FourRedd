package android.bignerdranch.com.fourredd;

/**
 * This program is designed to manage all the threads returned from the database in an ArrayList.
 */

import java.util.ArrayList;

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

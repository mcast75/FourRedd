package android.bignerdranch.com.fourredd;

/**
 * Created by Mike on 11/24/15.
 */
public class Thread {
    int like;
    String user, title, text;


    public Thread(String user, String title, int like, String text){

        this.user = user;
        this.title = title;
        this.like = like;
        this.text = text;

    }


    public Thread(String user, String title, String text){

        this.user = user;
        this.title = title;
        this.text = text;
        this.like = 0;

    }




    public Thread(){

        this.user = "";
        this.title = "";
        this.like = 0;
        this.text = "";

    }
}
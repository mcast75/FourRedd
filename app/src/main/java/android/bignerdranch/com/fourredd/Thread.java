package android.bignerdranch.com.fourredd;

/**
 * Creates new thread object
 */

/**
 * Created by Mike on 11/24/15.
 */
public class Thread {
    int like, id, dislikes;
    String user, title, text;


    public Thread(String user, String title, int like, int dislikes, int id, String text){

        this.id = id;
        this.user = user;
        this.title = title;
        this.like = like;
        this.text = text;
        this.dislikes = dislikes;

    }


    public Thread(String user, String title, String text){

        this.id = 0;
        this.user = user;
        this.title = title;
        this.text = text;
        this.like = 0;
        this.dislikes = 0;

    }




    public Thread(){
        this.id = 0;
        this.user = "";
        this.title = "";
        this.like = 0;
        this.text = "";
        this.dislikes = 0;

    }

    public void addLike(){
        this.like++;
    }

    public void addDislike(){
        this.dislikes++;
    }

}
package android.bignerdranch.com.fourredd;

/**
 * Created by Mike on 12/3/15.
 */
public class Comment {

    int like, commentID, threadID;
    String user, text;


    public Comment(int commentID, String user, int like, int threadID, String text){

        this.commentID = commentID;
        this.threadID = threadID;
        this.user = user;
        this.like = like;
        this.text = text;

    }


    public Comment(int threadID, String user, String text){

        this.commentID = 0;
        this.threadID = threadID;
        this.user = user;
        this.text = text;
        this.like = 0;

    }




    public Comment(){
        this.commentID = 0;
        this.threadID = 0;
        this.user = "";
        this.like = 0;
        this.text = "";

    }


}

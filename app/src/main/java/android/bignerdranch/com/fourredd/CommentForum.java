package android.bignerdranch.com.fourredd;

import java.util.ArrayList;

/**
 * Created by Mike on 12/3/15.
 */
public class CommentForum {


    ArrayList<Comment> allComments;

    public CommentForum(){

        this.allComments = new ArrayList<Comment>();
    }

    public void addComment(Comment comment){

        this.allComments.add(comment);

    }

    public ArrayList<Comment> getAllComments(){
        return this.allComments;
    }

}

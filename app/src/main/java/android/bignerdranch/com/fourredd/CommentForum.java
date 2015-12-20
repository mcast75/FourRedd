package android.bignerdranch.com.fourredd;

/**
 * This program is designed to manage all the comments returned from the database in an ArrayList.
 */

import java.util.ArrayList;

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

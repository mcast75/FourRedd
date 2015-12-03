package android.bignerdranch.com.fourredd;


import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddComment extends AppCompatActivity implements View.OnClickListener {

    EditText commentPost;
    TextView makeComment, cancelComment, threadTitle;
    UserLocalStore mUserLocalStore;
    ThreadLocalStore mThreadLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        commentPost = (EditText) findViewById(R.id.commentPost);
        threadTitle = (TextView) findViewById(R.id.threadTitle);

        makeComment = (TextView) findViewById(R.id.makeComment);
        cancelComment = (TextView) findViewById(R.id.cancelComment);

        makeComment.setOnClickListener(this);
        cancelComment.setOnClickListener(this);

        mUserLocalStore = new UserLocalStore(this);
        mThreadLocalStore = new ThreadLocalStore(this);

        threadTitle.setText(mThreadLocalStore.getCurrentThread().title);

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.makeComment:


                String user = mUserLocalStore.getLoggedInUser().name;
                String text =  commentPost.getText().toString();
                int threadID = mThreadLocalStore.getCurrentThread().id;


                Comment temp = new Comment(threadID, user, text);

                Log.d("Register", "ValueTHREAD: \n\n\n\n\n name"+temp.user+ "   post" + temp.text + "    ID  " + threadID);



                makeComment(temp);


                break;

            case R.id.cancelThread:

                startActivity(new Intent(this, Redd.class));
                break;

            case R.id.bLogout2:
                startActivity(new Intent(this, Login.class));
                break;

        }


    }


    private void makeComment(Comment comment){
        ServerRequests serverRequests = new ServerRequests((this));
        serverRequests.storeCommentDataInBackground(comment, new GetCommentCallback() {
            @Override
            public void done(Comment returnedComment) {
                startActivity(new Intent(AddComment.this, Comments.class));
            }

        });
    }


}



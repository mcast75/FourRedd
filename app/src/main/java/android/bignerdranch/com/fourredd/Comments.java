package android.bignerdranch.com.fourredd;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Comments extends AppCompatActivity implements View.OnClickListener {

    Button bFour, bHome, bLogout, bRedd;
    UserLocalStore mUserLocalStore;
    TextView threadTitle, comments, createComment, numLikes, numDislikes;
    Context mContext;
    private View.OnClickListener mClickListener;
    ArrayList<Comment> temp;
    ThreadLocalStore mThreadLocalStore;
    Thread currentThread;
    CommentForum commentForum;
    CheckBox cbLike, cbDislike;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);


        bLogout = (Button) findViewById(R.id.bLogout2);
        bRedd = (Button) findViewById(R.id.bRedd2);
        bFour = (Button) findViewById(R.id.bFour2);
        bHome = (Button) findViewById(R.id.bHome2);
        comments = (TextView) findViewById(R.id.comments);
        threadTitle = (TextView) findViewById(R.id.threadTitle);
        createComment = (TextView) findViewById(R.id.createComment);
        numLikes = (TextView) findViewById(R.id.numLikes);
        numDislikes = (TextView) findViewById(R.id.numDislikes);

        cbLike = (CheckBox) findViewById(R.id.bLike);
        cbDislike = (CheckBox) findViewById(R.id.bDisLike);

        bFour.setOnClickListener(this);
        bHome.setOnClickListener(this);
        bRedd.setOnClickListener(this);
        createComment.setOnClickListener(this);

        mThreadLocalStore = new ThreadLocalStore(this);

        temp = new ArrayList<Comment>();

        mClickListener = this;

        mContext = this;

        currentThread = mThreadLocalStore.getCurrentThread();
        commentForum = null;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_redd, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart(){
        super.onStart();
        getCommentForum(commentForum, mThreadLocalStore.getCurrentThread());
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.bFour2:
                startActivity(new Intent(this, Four.class));
                break;

            case R.id.bHome2:
                startActivity(new Intent(this, HomeActivity.class));
                break;

            case R.id.bRedd2:
                mThreadLocalStore.clearThreadData();
                startActivity(new Intent(this, Redd.class));
                break;

            case R.id.createComment:
                startActivity(new Intent(this, AddComment.class));
                break;


        }

    }


    public void onCheckboxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()){
            case R.id.bLike:
                if(checked) {
                    Thread temp = mThreadLocalStore.getCurrentThread();
                    Log.d("ADebugTag", "ThreadLikes!!!!!: \n" + temp.like);

                    temp.addLike();
                    numLikes.setText(temp.like + "");
                    mThreadLocalStore.clearThreadData();
                    mThreadLocalStore.storeThreadData(temp);
                    Log.d("ADebugTag", "ThreadLikes!!!!!: \n" + mThreadLocalStore.getCurrentThread().like);

                    cbLike.setClickable(false);
                    cbDislike.setClickable(false);
                    updateThread(mThreadLocalStore.getCurrentThread());
                }
                    //send to database
                break;

            case R.id.bDisLike:
                if(checked) {
                    Thread temp = mThreadLocalStore.getCurrentThread();
                    temp.addDislike();
                    Log.d("ADebugTag", "ThreadLikes!!!!!: \n" + temp.dislikes);
                    numDislikes.setText(temp.dislikes + "");
                    mThreadLocalStore.clearThreadData();
                    mThreadLocalStore.storeThreadData(temp);
                    cbLike.setClickable(false);
                    cbDislike.setClickable(false);
                    updateThread(mThreadLocalStore.getCurrentThread());

                }
                //send to database
                break;
        }
    }


    private void updateThread(Thread thread){
        ServerRequests serverRequests = new ServerRequests((this));
        serverRequests.storeThreadLikeDataInBackground(thread, new GetThreadCallback() {
            @Override
            public void done(Thread returnedUser) {

            }

        });
    }


    private void getCommentForum(CommentForum commentForum, final Thread thread){
        ServerRequests serverRequests = new ServerRequests((this));
        serverRequests.fetchCommentForumInBackground(commentForum, thread, new GetCommentForumCallback() {
            @Override
            public void done(CommentForum returnedCommentForum) {

                TableLayout table = (TableLayout) findViewById(R.id.table);
                int i = 0;
                temp = returnedCommentForum.getAllComments();

                threadTitle.setText(mThreadLocalStore.getCurrentThread().title);
                numLikes.setText(mThreadLocalStore.getCurrentThread().like + "");
                numDislikes.setText(mThreadLocalStore.getCurrentThread().dislikes + "");



                TableRow row = new TableRow(mContext);
                table.addView(row);
                row.setId(i);
                row.setClickable(true);
                LinearLayout ll = new LinearLayout(mContext);
                row.addView(ll);


                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setPadding(0, 0, 0, 70);

                LinearLayout ll2 = new LinearLayout(mContext);
                ll.setPadding(0, 0, 0, 40);
                ll.addView(ll2);
                TextView tvTitle = new TextView(mContext);
                Point size = new Point();
                getWindowManager().getDefaultDisplay().getSize(size);
                int width = size.x;
                tvTitle.setText(mThreadLocalStore.getCurrentThread().text);
                tvTitle.setLayoutParams(new LinearLayout.LayoutParams(width - 10
                        , ViewGroup.LayoutParams.WRAP_CONTENT));
                tvTitle.setTextColor(Color.BLACK);
                tvTitle.setTextSize(24);
                ll2.addView(tvTitle);
                TextView line = new TextView(mContext);
                line.setTextSize(2);
                line.setTextColor(Color.BLACK);
                line.setText("_____________________________________________________________________" +
                        "_______________________________________________________________________" +
                        "_______________________________________________________________________" +
                        "_______________________________________________________________________" +
                        "_______________________________________________________________________" +
                        "_______________________________________________________________________" +
                        "_______________________________________________________________________" +
                        "_______________________________________________________________________" +
                        "_______________________________________________________________________" +
                        "_______________________________________________________________________" +
                        "______________________________________________________________________");


                LinearLayout ll3 = new LinearLayout(mContext);
                ll.addView(ll3);
                TextView space = new TextView(mContext);
                space.setText("      ");
                ll3.addView(space);
                TextView comments = new TextView(mContext);
                comments.setText("Submitted by: " + mThreadLocalStore.getCurrentThread().user);
                comments.setTextSize(20);
                ll3.addView(comments);

                TextView tvNumLike = new TextView(mContext);
                tvNumLike.setText("                      (Original Post) ");
                tvNumLike.setTextSize(20);
                ll3.addView(tvNumLike);
                ll.addView(line);



                for (i = 0; i < temp.size(); i++) {


                    row = new TableRow(mContext);
                    table.addView(row);
                    row.setId(i);
                    row.setClickable(true);
//                    Log.d("ADebugTag", "LOG ID!!!!!!!!!!!!!!: \n" + row.getId());
                    ll = new LinearLayout(mContext);
                    row.addView(ll);
                    row.setOnClickListener(Comments.this);


                    ll.setOrientation(LinearLayout.VERTICAL);
                    ll.setPadding(0, 0, 0, 70);

                    ll2 = new LinearLayout(mContext);
                    ll.setPadding(0, 0, 0, 40);
                    ll.addView(ll2);
                    tvTitle = new TextView(mContext);
                    tvTitle.setText(temp.get(i).text);
                    Point size2 = new Point();
                    getWindowManager().getDefaultDisplay().getSize(size2);
                    int width2 = size2.x;
                    tvTitle.setText(temp.get(i).text);
                    tvTitle.setLayoutParams(new LinearLayout.LayoutParams(width2 - 10
                            , ViewGroup.LayoutParams.WRAP_CONTENT));

                    tvTitle.setTextColor(Color.BLACK);
                    tvTitle.setTextSize(24);
                    ll2.addView(tvTitle);
                    line = new TextView(mContext);
                    line.setTextSize(2);
                    line.setTextColor(Color.BLACK);
                    line.setText("_____________________________________________________________________" +
                            "_______________________________________________________________________" +
                            "_______________________________________________________________________" +
                            "_______________________________________________________________________" +
                            "_______________________________________________________________________" +
                            "_______________________________________________________________________" +
                            "_______________________________________________________________________" +
                            "_______________________________________________________________________" +
                            "_______________________________________________________________________" +
                            "_______________________________________________________________________" +
                            "______________________________________________________________________");


                    ll3 = new LinearLayout(mContext);
                    ll.addView(ll3);
                    space = new TextView(mContext);
                    space.setText("      ");
                    ll3.addView(space);
                    comments = new TextView(mContext);
                    comments.setText("Submitted by: " + temp.get(i).user);
                    comments.setTextSize(20);
                    ll3.addView(comments);
                    ll.addView(line);



                }
            }


        });
    }
}

package android.bignerdranch.com.fourredd;

/**
 * Allows users view and post threads as well as like or dislike threads
 * Buttons allow the user to navigate to either the create new thread activity or view the comments for a specific thread
 * Buttons to navigate to the four and home activites are present
 * server requests pulls the threads and orders them by how recently they were created
 */
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.DrawableRes;
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
import android.widget.ImageView;
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

public class Redd extends AppCompatActivity implements View.OnClickListener {

    Button bFour, bHome, bLogout;
    UserLocalStore mUserLocalStore;
    TextView createThread, comments;
    Forum forum;
    Context mContext;
    private View.OnClickListener mClickListener;
    ArrayList<Thread> temp;
    ThreadLocalStore mThreadLocalStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redd);


        bLogout = (Button) findViewById(R.id.bLogout2);
        bFour = (Button) findViewById(R.id.bFour2);
        bHome = (Button) findViewById(R.id.bHome2);
        createThread = (TextView) findViewById(R.id.createThread);
        comments = (TextView) findViewById(R.id.comments);

        //comments.setOnClickListener(this);
        bLogout.setOnClickListener(this);
        bFour.setOnClickListener(this);
        bHome.setOnClickListener(this);
        createThread.setOnClickListener(this);

        mThreadLocalStore = new ThreadLocalStore(this);

        temp = new ArrayList<Thread>();

        mClickListener = this;

        mUserLocalStore = new UserLocalStore(this);

        mContext = this;

        forum = null;
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
        getForum(forum);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bLogout2:

                mUserLocalStore.clearUserData();
                mUserLocalStore.setUserLoggedIn(false);

                startActivity(new Intent(this, Login.class));
                break;

            case R.id.bFour2:
                startActivity(new Intent(this, Four.class));
                break;

            case R.id.bHome2:
                startActivity(new Intent(this, HomeActivity.class));
                break;

            case R.id.createThread:
                startActivity(new Intent(this, CreateThread.class));
                break;

            default:
                int id = v.getId();

                mThreadLocalStore.storeThreadData(temp.get(id));

                Log.d("ADebugTag", "CURRENT THREAD!!!!!!!!!!!!!!: \n" + mThreadLocalStore.getCurrentThread().title);


                startActivity(new Intent(this, Comments.class));
                break;





        }

    }

    private void getForum(Forum forum){
        ServerRequests serverRequests = new ServerRequests((this));
        serverRequests.fetchForumInBackground(forum, new GetForumCallback() {
            @Override
            public void done(Forum returnedForum) {

                TableLayout table = (TableLayout) findViewById(R.id.table);
                int i = 0;
                temp = returnedForum.getAllThreads();



                for(i =0; i < temp.size(); i++){



                    TableRow row = new TableRow(mContext);
                    table.addView(row);
                    row.setId(i);
                    row.setClickable(true);
                    LinearLayout ll = new LinearLayout(mContext);
                    row.addView(ll);
                    row.setOnClickListener(Redd.this);


                    ll.setOrientation(LinearLayout.VERTICAL);
                    ll.setPadding(0, 0, 0, 70);

                    LinearLayout ll2 = new LinearLayout(mContext);
                    ll.setPadding(0, 0, 0, 40);
                    ll.addView(ll2);
                    TextView tvTitle = new TextView(mContext);
                    tvTitle.setText(temp.get(i).title);

                    Point size2 = new Point();
                    getWindowManager().getDefaultDisplay().getSize(size2);
                    int width2 = size2.x;
                    tvTitle.setLayoutParams(new LinearLayout.LayoutParams(width2 - 10
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
                    comments.setText("Comments");
                    comments.setLinksClickable(true);
                    comments.setTag("comments");
                    comments.setId(R.id.comments);
                    comments.setTextSize(20);
                    comments.setTextColor(Color.BLUE);
                    comments.setPaintFlags(comments.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    ll3.addView(comments);

                    TextView space2 = new TextView(mContext);
                    space2.setText("                                           ");
                    ll3.addView(space2);
                    ImageView thumbsup = new ImageView(mContext);
                    ll3.addView(thumbsup);
                    android.view.ViewGroup.LayoutParams layoutParams = thumbsup.getLayoutParams();
                    layoutParams.width = 30;
                    layoutParams.height = 30;
                    thumbsup.setLayoutParams(layoutParams);
                    thumbsup.setImageResource(R.drawable.thumbsup);


                    TextView tvNumLike = new TextView(mContext);
                    tvNumLike.setText("Likes(" + temp.get(i).like + ")      ");
                    tvNumLike.setTextSize(20);
                    ll3.addView(tvNumLike);

                    ImageView thumbsdown = new ImageView(mContext);
                    ll3.addView(thumbsdown);
                    android.view.ViewGroup.LayoutParams layoutParams2 = thumbsdown.getLayoutParams();
                    layoutParams2.width = 30;
                    layoutParams2.height = 30;
                    thumbsdown.setLayoutParams(layoutParams2);
                    thumbsdown.setImageResource(R.drawable.thumbsdown);
                    TextView tvNumDislikes = new TextView(mContext);
                    tvNumDislikes.setText("Dislikes(" + temp.get(i).dislikes + ")");
                    tvNumDislikes.setTextSize(20);
                    ll3.addView(tvNumDislikes);

                    ll.addView(line);






                }
            }


        });
    }
}

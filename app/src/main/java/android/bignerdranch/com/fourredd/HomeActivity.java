package android.bignerdranch.com.fourredd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button bFour, bRedd, bLogout;
    UserLocalStore mUserLocalStore;
    ArrayList<Thread> temp;
    TextView name, thread1, thread2, thread3, threadLikes1, threadLikes2, threadLikes3, threadDislikes1, threadDislikes2, threadDislikes3;
    Forum mForum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        name = (TextView) findViewById(R.id.homeName);

        thread1 =(TextView) findViewById(R.id.thread1);
        thread2 =(TextView) findViewById(R.id.thread2);
        thread3 =(TextView) findViewById(R.id.thread3);

        threadLikes1 =(TextView) findViewById(R.id.threadLikes1);
        threadLikes2 =(TextView) findViewById(R.id.threadLikes2);
        threadLikes3 =(TextView) findViewById(R.id.threadLikes3);

        threadDislikes1 = (TextView) findViewById(R.id.threadDislikes1);
        threadDislikes2 = (TextView) findViewById(R.id.threadDislikes2);
        threadDislikes3 = (TextView) findViewById(R.id.threadDislikes3);


        bLogout = (Button) findViewById(R.id.bLogout1);
        bFour = (Button) findViewById(R.id.bFour1);
        bRedd = (Button) findViewById(R.id.bRedd1);


        bLogout.setOnClickListener(this);
        bFour.setOnClickListener(this);
        bRedd.setOnClickListener(this);

        temp = new ArrayList<Thread>();

        mUserLocalStore = new UserLocalStore(this);
        mForum = new Forum();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bLogout1:

                mUserLocalStore.clearUserData();
                mUserLocalStore.setUserLoggedIn(false);

                startActivity(new Intent(this, Login.class));
                break;

            case R.id.bFour1:
                startActivity(new Intent(this, Four.class));
                break;

            case R.id.bRedd1:
                startActivity(new Intent(this, Redd.class));
                break;

        }

    }

    @Override
    public void onStart(){
        super.onStart();

        getForum(mForum);

    }


    private void getForum(Forum forum) {
        ServerRequests serverRequests = new ServerRequests((this));
        serverRequests.fetchForumInBackground(forum, new GetForumCallback() {
            @Override
            public void done(Forum returnedForum) {

                temp = returnedForum.getAllThreads();

                name.setText(mUserLocalStore.getLoggedInUser().name);

                thread1.setText(temp.get(0).title);
                threadLikes1.setText(temp.get(0).like+"");
                threadDislikes1.setText(temp.get(0).dislikes+"");

                thread2.setText(temp.get(1).title);
                threadLikes2.setText(temp.get(1).like+"");
                threadDislikes2.setText(temp.get(1).dislikes+"");

                thread3.setText(temp.get(2).title);
                threadLikes3.setText(temp.get(2).like+"");
                threadDislikes3.setText(temp.get(2).dislikes+"");

            }


        });
    }
}

package android.bignerdranch.com.fourredd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Redd extends AppCompatActivity implements View.OnClickListener {

    Button bFour, bHome, bLogout;
    UserLocalStore mUserLocalStore;
    TextView createThread;
    Forum forum;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redd);


        bLogout = (Button) findViewById(R.id.bLogout2);
        bFour = (Button) findViewById(R.id.bFour2);
        bHome = (Button) findViewById(R.id.bHome2);
        createThread = (TextView) findViewById(R.id.createThread);


        bLogout.setOnClickListener(this);
        bFour.setOnClickListener(this);
        bHome.setOnClickListener(this);
        createThread.setOnClickListener(this);


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

        }

    }

    private void getForum(Forum forum){
        ServerRequests serverRequests = new ServerRequests((this));
        serverRequests.fetchForumInBackground(forum, new GetForumCallback() {
            @Override
            public void done(Forum returnedForum) {

                TableLayout table = (TableLayout) findViewById(R.id.table);
                int i = 0;
                ArrayList<Thread> temp = returnedForum.getAllThreads();

                for(i =0; i < temp.size(); i++){

                    TableRow row = new TableRow(mContext);
                    table.addView(row);
                    LinearLayout ll = new LinearLayout(mContext);
                    row.addView(ll);
                    ll.setOrientation(LinearLayout.HORIZONTAL);

                    TextView tvTitle = new TextView(mContext);
                    tvTitle.setText(temp.get(i).title);
                    TextView tvUser = new TextView(mContext);
                    tvTitle.setText(temp.get(i).text);
                    TextView tvText = new TextView(mContext);
                    tvTitle.setText(temp.get(i).text);
                    TextView tvNumLike = new TextView(mContext);
                    tvTitle.setText(temp.get(i).like);

                    ll.addView(tvTitle);
                    ll.addView(tvUser);
                    ll.addView(tvText);
                    ll.addView(tvNumLike);

                }
            }


        });
    }
}

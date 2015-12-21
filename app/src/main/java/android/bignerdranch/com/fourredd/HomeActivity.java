package android.bignerdranch.com.fourredd;
/**
 * Displays the users name and has a logout button
 * Uses server request to pull the three most recent threads and locations from the data base
 * Has buttons which start the Redd and Four activities
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button bFour, bRedd, bLogout;
    UserLocalStore mUserLocalStore;
    ArrayList<Thread> temp;
    ArrayList<LocationObject> temp2;
    Context mContext;
    TextView name, thread1, thread2, thread3, threadLikes1, threadLikes2, threadLikes3, threadDislikes1, threadDislikes2, threadDislikes3;
    Forum mForum;
    LocationForum mLocationForum;


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
        mLocationForum = new LocationForum();
        mContext = this;
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

        getForum(mForum, mLocationForum);

    }


    private void getForum(Forum forum, LocationForum locationForum) {
        ServerRequests serverRequests = new ServerRequests((this));
        serverRequests.fetchForumInBackground(forum, new GetForumCallback() {
            @Override
            public void done(Forum returnedForum) {

                temp = returnedForum.getAllThreads();

                name.setText(mUserLocalStore.getLoggedInUser().name);

                thread1.setText(temp.get(0).title);
                thread1.setTextColor(Color.BLACK);
                threadLikes1.setText(temp.get(0).like + "");
                threadDislikes1.setText(temp.get(0).dislikes+"");

                thread2.setText(temp.get(1).title);
                thread2.setTextColor(Color.BLACK);
                threadLikes2.setText(temp.get(1).like + "");
                threadDislikes2.setText(temp.get(1).dislikes+"");

                thread3.setText(temp.get(2).title);
                thread3.setTextColor(Color.BLACK);
                threadLikes3.setText(temp.get(2).like+"");
                threadDislikes3.setText(temp.get(2).dislikes+"");

            }


        });

        ServerRequests serverRequests2 = new ServerRequests((this));
        serverRequests2.fetchLocationForumInBackground(locationForum, new GetLocationForumCallback() {
            @Override
            public void done(LocationForum returnedForum) throws IOException {

                temp2 = returnedForum.getAllLocations();

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(mContext, Locale.getDefault());
                String city = " ";
                String state = " ";
                String known = " ";


                TextView tvTitle1 = (TextView) findViewById(R.id.location1);
                TextView tvTitle2 = (TextView) findViewById(R.id.location2);
                TextView tvTitle3 = (TextView) findViewById(R.id.location3);

                if (geocoder != null) {

                    addresses = geocoder.getFromLocation(temp2.get(0).latitude, temp2.get(0).longitude, 1);

                    if (addresses == null || addresses.size()  == 0) {

                        tvTitle1.setText(temp2.get(0).user + " has checked in at an unknown location ("  +(int) temp2.get(0).latitude + "," + (int) temp2.get(0).longitude+")");

                    } else {
                        city = addresses.get(0).getLocality();
                        state = addresses.get(0).getAdminArea();
                        known = addresses.get(0).getFeatureName();

                        tvTitle1.setText(temp2.get(0).user + " has checked in at " + known + " in " + city + ", " + state);
                    }

                    tvTitle1.setTextColor(Color.BLACK);

                    if (temp2.size() >= 2) {
                        addresses = geocoder.getFromLocation(temp2.get(1).latitude, temp2.get(1).longitude, 1);

                        if (addresses == null || addresses.size()  == 0) {

                            tvTitle2.setText(temp2.get(1).user + " has checked in at an unknown location ("  +(int) temp2.get(1).latitude + "," + (int) temp2.get(1).longitude+")");

                        } else {
                            city = addresses.get(0).getLocality();
                            state = addresses.get(0).getAdminArea();
                            known = addresses.get(0).getFeatureName();

                            tvTitle2.setText(temp2.get(1).user + " has checked in at " + known + " in " + city + ", " + state);
                        }

                        tvTitle2.setTextColor(Color.BLACK);

                    }

                    if (temp2.size() >= 3) {
                        addresses = geocoder.getFromLocation(temp2.get(2).latitude, temp2.get(2).longitude, 1);

                        if (addresses == null || addresses.size()  == 0) {

                            tvTitle3.setText(temp2.get(2).user + " has checked in at an unknown location ("  +(int) temp2.get(2).latitude + "," + (int) temp2.get(2).longitude+")");

                        } else {
                            city = addresses.get(0).getLocality();
                            state = addresses.get(0).getAdminArea();
                            known = addresses.get(0).getFeatureName();

                            tvTitle3.setText(temp2.get(0).user + " has checked in at " + known + " in " + city + ", " + state);
                        }

                        tvTitle3.setTextColor(Color.BLACK);

                    }
                }

            }
        });
    }
}

package android.bignerdranch.com.fourredd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Four extends AppCompatActivity implements View.OnClickListener, OnMyLocationButtonClickListener,
        OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private Marker mMarker;

    private GoogleMap mMap;

    private double mLatitude = 41.7440468;
    private double mLongitude = -72.6920349;

    LocationObject mLocationObject;


    Button bHome, bRedd, bLogout, shareLocation;
    UserLocalStore mUserLocalStore;

    Timestamp mTimestamp;

    ArrayList<LocationObject> temp;

    Context mContext;

    LocationForum mLocationForum;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //bLogout = (Button) findViewById(R.id.bLogout3);
        bHome = (Button) findViewById(R.id.bHome3);
        bRedd = (Button) findViewById(R.id.bRedd3);
        shareLocation = (Button) findViewById(R.id.shareLoc);

        //bLogout.setOnClickListener(this);
        bHome.setOnClickListener(this);
        bRedd.setOnClickListener(this);
        shareLocation.setOnClickListener(this);


        mUserLocalStore = new UserLocalStore(this);

        temp = new ArrayList<LocationObject>();

        mContext = this;

        mLocationForum = null;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_four, menu);
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

//        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        getLocationForum(mLocationForum);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.shareLoc:

                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                double latitude = mLatitude;
                double longitude = mLongitude;

                final LatLng newLoc = new LatLng(latitude, longitude);
                Marker add = mMap.addMarker(new MarkerOptions().position(newLoc).draggable(false));
                add.setVisible(true);
                add.setTitle("TESTS");

                mLocationObject = new LocationObject(mUserLocalStore.getLoggedInUser().name, latitude, longitude);

                makeLocation(mLocationObject);

                startActivity(new Intent(this, Four.class));

                break;

            case R.id.bHome3:
                startActivity(new Intent(this, HomeActivity.class));
                break;

            case R.id.bRedd3:
                startActivity(new Intent(this, Redd.class));
                break;

        }

    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);

//        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        if (location != null) {
//            double longitude = location.getLongitude();
//            double latitude = location.getLatitude();
        final LatLng newLoc = new LatLng(mLatitude, mLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLoc, 7));
//        }

        enableMyLocation();
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
//            Log.d("ADEBUGTAG", "Start Map:  \n" + "Enters Location");
            mLatitude = location.getLatitude();
//            Log.d("ADEBUGTAG", "LATITUDE:  \n" + mLatitude);
            mLongitude = location.getLongitude();
//            Log.d("ADEBUGTAG", "Longitude:  \n" + mLongitude);
        }
    };






    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }


    }

    @Override
    public boolean onMyLocationButtonClick() {
        enableMyLocation();
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    private void makeLocation(LocationObject locationObject){
        ServerRequests serverRequests = new ServerRequests((this));
        serverRequests.storeLocationDataInBackground(locationObject, new GetLocationCallback() {
            @Override
            public void done(LocationObject returnedLocation) {
            }

        });
    }


    private void getLocationForum(LocationForum forum){
        ServerRequests serverRequests = new ServerRequests((this));
        serverRequests.fetchLocationForumInBackground(forum, new GetLocationForumCallback() {
            @Override
            public void done(LocationForum returnedForum) throws IOException {

                TableLayout table = (TableLayout) findViewById(R.id.table);
                table.setPadding(0,20,0,0);
                int i = 0;
                temp = returnedForum.getAllLocations();


                for (i = 0; i < temp.size(); i++) {


                    final LatLng newLoc = new LatLng(temp.get(i).latitude, temp.get(i).longitude);
                    Marker add = mMap.addMarker(new MarkerOptions().position(newLoc).draggable(false));
                    add.setVisible(true);
                    add.setTitle(temp.get(i).user);

                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(mContext, Locale.getDefault());
                    String city = " ";
                    String state = " ";
                    String known = " ";


                    TableRow row = new TableRow(mContext);
                    table.addView(row);

                    LinearLayout ll = new LinearLayout(mContext);
                    row.addView(ll);



                    ll.setOrientation(LinearLayout.VERTICAL);
                    ll.setPadding(0, 0, 0, 70);

                    LinearLayout ll2 = new LinearLayout(mContext);
                    ll.setPadding(0, 0, 0, 40);
                    ll.addView(ll2);
                    TextView tvTitle = new TextView(mContext);

                    if (geocoder != null) {
                        addresses = geocoder.getFromLocation(temp.get(i).latitude, temp.get(i).longitude, 1);
                        city = addresses.get(0).getLocality();
                        state = addresses.get(0).getAdminArea();
                        known = addresses.get(0).getFeatureName();
                        Log.d("ADEBUGTAG", "ANDROID LOCATION:  \n" + temp.get(i).user + " has checked in at Latitude " + (int) temp.get(i).latitude + " and Longitude " + (int) temp.get(i).longitude);
                        Log.d("ADEBUGTAG", "ANDROID LOCATION:  \n" + temp.get(i).user + " has checked in at City " + city + " and state " + state);
                        tvTitle.setText("   " + temp.get(i).user + " has checked in at " + city + ", " + state);
                    }

//                    Log.d("ADEBUGTAG", "ANDROID LOCATION:  \n" + temp.get(i).user + " has checked in at Latitude " + (int) temp.get(i).latitude + " and Longitude " + (int) temp.get(i).longitude);

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


                    ll.addView(line);


                }
            }


        });
    }



}

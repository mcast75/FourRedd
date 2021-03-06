package android.bignerdranch.com.fourredd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsSatellite;
import android.location.LocationManager;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

    LocationObject mLocationObject;


    Button bHome, bRedd, bLogout, shareLocation;
    UserLocalStore mUserLocalStore;

    Timestamp mTimestamp;

    ArrayList<LocationObject> temp;

    Context mContext;

    LocationForum mLocationForum;

    double init_latitude = 41.747078;
    double init_longitude = -72.69256;

    String errorMessage = "";



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
//        double longitude = location.getLongitude();
//        double latitude = location.getLatitude();
//
//        Log.d("ADEBUGTAG", "ANDROID LOCATION:  \n" + longitude + " - LONGITUDE   " + latitude + " - LATITUDE");

        getLocationForum(mLocationForum);


        //Log.d("ADEBUGTAG", "ANDROID LOCATION:  \n" + mLocationForum);
        //Log.d("ADEBUGTAG", "ANDROID ONONONIONLBYUVLHFLBFILE:  \n" + "buibgortunbiorbnriobntroib");





    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.shareLoc:

                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

                try{
                    Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();

                    final LatLng newLoc = new LatLng(latitude, longitude);
                    Marker add = mMap.addMarker(new MarkerOptions().position(newLoc).draggable(false));
                    add.setVisible(true);
                    add.setTitle(mUserLocalStore.getLoggedInUser().name);


                    mLocationObject = new LocationObject(mUserLocalStore.getLoggedInUser().name, latitude, longitude);

                    makeLocation(mLocationObject);

                    startActivity(new Intent(this, Four.class));
                }catch (SecurityException securityException){

                    errorMessage = "Service Not Available - Unable To Get Current Location";
                    Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
                    toast.show();

                }

//                double longitude = location.getLongitude();
//                double latitude = location.getLatitude();
//
//                final LatLng newLoc = new LatLng(latitude, longitude);
//                Marker add = mMap.addMarker(new MarkerOptions().position(newLoc).draggable(false));
//                add.setVisible(true);
//                add.setTitle(mUserLocalStore.getLoggedInUser().name);
//
//
//                mLocationObject = new LocationObject(mUserLocalStore.getLoggedInUser().name, latitude, longitude);
//
//                makeLocation(mLocationObject);
//
//                startActivity(new Intent(this, Four.class));


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

        double longitude;
        double latitude;

        mMap = map;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        try{
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(location == null){

                longitude = init_longitude;
                latitude = init_latitude;

            }else {

                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            final LatLng newLoc = new LatLng(latitude, longitude);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLoc, 7));
        }catch (SecurityException securityException){

            errorMessage = "Service Not Available - Unable To Get Current Location";
            Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();

        }





        enableMyLocation();
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            //Log.d("ADEBUGTAG", "Start Map:  \n" + "Enters Location");
            init_latitude = location.getLatitude();
            //Log.d("ADEBUGTAG", "LATITUDE:  \n" + latitude);
            init_longitude = location.getLongitude();
            //Log.d("ADEBUGTAG", "Longitude:  \n" + longitude);
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
                table.setPadding(0,15,0,0);
                int i = 0;
                temp = returnedForum.getAllLocations();


                for (i = 0; i < temp.size(); i++) {


                    final LatLng newLoc = new LatLng(temp.get(i).latitude, temp.get(i).longitude);
                    Marker add = mMap.addMarker(new MarkerOptions().position(newLoc).draggable(false));
                    add.setVisible(true);
                    add.setTitle(temp.get(i).user);

                    TableRow row = new TableRow(mContext);
                    table.addView(row);

                    LinearLayout ll = new LinearLayout(mContext);
                    row.addView(ll);


                    ll.setOrientation(LinearLayout.VERTICAL);
                    ll.setPadding(0, 0, 0, 70);

                    LinearLayout ll2 = new LinearLayout(mContext);
                    ll.setPadding(0, 0, 0, 40);
                    ll.addView(ll2);


                    ImageView pin = new ImageView(mContext);
                    ll2.addView(pin);
                    android.view.ViewGroup.LayoutParams layoutParams = pin.getLayoutParams();
                    layoutParams.width = 30;
                    layoutParams.height = 30;
                    pin.setPadding(5,0,0,0);
                    pin.setLayoutParams(layoutParams);
                    pin.setImageResource(R.drawable.pin);


                    TextView tvTitle = new TextView(mContext);

                    Geocoder geocoder;
                    List<Address> addresses = new ArrayList<Address>();

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;


                    try {

                        geocoder = new Geocoder(mContext, Locale.getDefault());
                        addresses = geocoder.getFromLocation(temp.get(i).latitude, temp.get(i).longitude, 1);

                    } catch (IOException ioException) {
                        // Catch network or other I/O problems.
                        errorMessage = "Geocoder Service Not Available";

                    } catch (IllegalArgumentException illegalArgumentException) {
                        // Catch invalid latitude or longitude values.
                        errorMessage = "Invalid Latitude and Longitude";
                        Toast toast = Toast.makeText(context, errorMessage, duration);
                        toast.show();

                    }


                    if (addresses == null || addresses.size() == 0) {

                        tvTitle.setText("   " + temp.get(i).user + " has checked in at an unknown location (" + (int) temp.get(i).latitude + "," + (int) temp.get(i).longitude + ")");

                    } else {
                        errorMessage = "Geocoder Is Online";
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String known = addresses.get(0).getFeatureName();
                        tvTitle.setText("   " + temp.get(i).user + " has checked in at " + known + " in " + city + ", " + state);
                        //Log.d("ADEBUGTAG", "ANDROID LOCATION:  \n" + temp.get(i).user + " has checked in at Latitude " + (int) temp.get(i).latitude + " and Longitude " + (int) temp.get(i).longitude);


                    }

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


                        ll.addView(line);


                }

                Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }

        });
    }



}

package android.bignerdranch.com.fourredd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Four extends AppCompatActivity implements View.OnClickListener, OnMyLocationButtonClickListener,
        OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private Marker mMarker;

    private GoogleMap mMap;

    private Context mContext;


    Button bHome, bRedd, bLogout, shareLocation;
    UserLocalStore mUserLocalStore;


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

        //bLogout.setOnClickListener(this);
        bHome.setOnClickListener(this);
        bRedd.setOnClickListener(this);

        mContext = this;

        mUserLocalStore = new UserLocalStore(this);
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

    public void addUserMarkers() throws JSONException {
        Log.d("ADEBUGTAG", "Longitude:  \n");
        JSONObject thing = new JSONObject();
        thing.put("latitude", 51.7474);
        thing.put("longitude", 82.6900);
        thing.put("username", "stevenyee");

        JSONArray results = new JSONArray();
        results.put(thing);
        int size = results.length();
//        TableLayout table = (TableLayout) findViewById(R.id.table);
        for (int i = 0; i < size; i++) {
            Log.d("ADEBUGTAG", "Latitiude:  \n" + results.getJSONObject(0).getDouble("latitude"));
            Log.d("ADEBUGTAG", "Longitude:  \n" + results.getJSONObject(0).getDouble("longitude"));
            Log.d("ADEBUGTAG", "Username:  \n" + results.getJSONObject(0).get("username").toString());
            Log.d("ADEBUGTAG", "MAPAA:  \n" + mMap);
            LatLng markerLocation = new LatLng(results.getJSONObject(0).getDouble("latitude"), results.getJSONObject(0).getDouble("longitude"));
            Marker temp = mMap.addMarker(new MarkerOptions().position(markerLocation).title(results.getJSONObject(0).get("username").toString()));
            Log.d("ADEBUGTAG", "MARKERS:  \n" + temp);
//  printMarkerInformation(table, temp);
        }
    }

    public void printMarkerInformation(TableLayout table, Marker marker) {
        TableRow row = new TableRow(mContext);
        row = new TableRow(mContext);
        table.addView(row);
        Log.d("ADebugTag", "LOG ID!!!!!!!!!!!!!!: \n" + marker.getTitle());
        LinearLayout ll = new LinearLayout(mContext);
        ll = new LinearLayout(mContext);
        row.addView(ll);

        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(0, 0, 0, 70);

        LinearLayout ll2 = new LinearLayout(mContext);
        ll.setPadding(0, 0, 0, 40);
        ll.addView(ll2);
        TextView tvTitle = new TextView(mContext);
        tvTitle.setText(marker.getTitle());
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
        comments.setText("Latitude and Longitude: " + marker.getPosition());
        comments.setTextSize(20);
        ll3.addView(comments);
        ll.addView(line);
    }


    @Override
    public void onStart() {
        super.onStart();

//        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        double longitude = location.getLongitude();
//        double latitude = location.getLatitude();

//        Log.d("ADEBUGTAG", "ANDROID LOCATION:  \n" + longitude + " - LONGITUDE   " + latitude + " - LATITUDE");





    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            case R.id.bLogout3:
//
//                mUserLocalStore.clearUserData();
//                mUserLocalStore.setUserLoggedIn(false);
//
//                startActivity(new Intent(this, Login.class));
//                break;

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
        enableMyLocation();
        try {
            addUserMarkers();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
//            Log.d("ADEBUGTAG", "Start Map:  \n" + "Enters Location");
//            double latitude = location.getLatitude();
//            Log.d("ADEBUGTAG", "LATITUDE:  \n" + latitude);
//            double longitude = location.getLongitude();
//            Log.d("ADEBUGTAG", "Longitude:  \n" + longitude);
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



}

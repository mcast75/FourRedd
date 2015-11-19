package android.bignerdranch.com.fourredd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Redd extends AppCompatActivity implements View.OnClickListener {

    Button bFour, bHome, bLogout;
    UserLocalStore mUserLocalStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redd);


        bLogout = (Button) findViewById(R.id.bLogout2);
        bFour = (Button) findViewById(R.id.bFour2);
        bHome = (Button) findViewById(R.id.bHome2);


        bLogout.setOnClickListener(this);
        bFour.setOnClickListener(this);
        bHome.setOnClickListener(this);


        mUserLocalStore = new UserLocalStore(this);
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

        }

    }
}

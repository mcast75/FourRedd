package android.bignerdranch.com.fourredd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Four extends AppCompatActivity implements View.OnClickListener {

    Button bHome, bRedd, bLogout;
    UserLocalStore mUserLocalStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);


        bLogout = (Button) findViewById(R.id.bLogout3);
        bHome = (Button) findViewById(R.id.bHome3);
        bRedd = (Button) findViewById(R.id.bRedd3);


        bLogout.setOnClickListener(this);
        bHome.setOnClickListener(this);
        bRedd.setOnClickListener(this);


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bLogout3:

                mUserLocalStore.clearUserData();
                mUserLocalStore.setUserLoggedIn(false);

                startActivity(new Intent(this, Login.class));
                break;

            case R.id.bHome3:
                startActivity(new Intent(this, HomeActivity.class));
                break;

            case R.id.bRedd3:
                startActivity(new Intent(this, Redd.class));
                break;

        }

    }
}

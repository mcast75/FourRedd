package android.bignerdranch.com.fourredd;

/**
 * Allows user to enter username and password to login to the app
 * Uses server request to pull login info and match it against inputted data
 * Moves user to the home acitivty if the information provided is correct
 * Has an option to register a new user account
 */

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button bLogin;
    EditText etUsername;
    EditText etPassword;
    UserLocalStore mUserLocalStore;
    TextView Register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLoginSuc);
        Register = (TextView) findViewById(R.id.RegisterNow);

        Register.setOnClickListener(this);

        bLogin.setOnClickListener(this);
        mUserLocalStore = new UserLocalStore(this);


    }


    @Override
    protected void onStart(){
        super.onStart();
/*
        if (authenticated() == true){
            displayUserDetails();
        }else{
            startActivity(new Intent(Login.this, Login.class));
        }
*/
    }

    private boolean authenticated(){
        return mUserLocalStore.getUserLoggedIn();
    }

    private void displayUserDetails(){
        User user = mUserLocalStore.getLoggedInUser();

        etUsername.setText(user.username);
        etPassword.setText(user.password);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bLoginSuc:

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();



                User user = new User(username,password);
                authenticate(user);
                break;

            case R.id.RegisterNow:
                startActivity(new Intent(this, Register.class));

        }
    }

    private void authenticate(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    logUserIn(returnedUser);

                }

            }
        });
    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void logUserIn(User returnedUser){
        mUserLocalStore.storeUserData(returnedUser);
        mUserLocalStore.setUserLoggedIn(true);

        startActivity(new Intent(this, HomeActivity.class));

    }


}

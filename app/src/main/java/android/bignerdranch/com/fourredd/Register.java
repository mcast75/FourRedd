package android.bignerdranch.com.fourredd;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Button bRegister;
    EditText etName, etUsername, etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.etName);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(this);
    }


    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bRegister:


                if ((etName.getText().toString() == "")|(etUsername.getText().toString() == "")|
                        (etPassword.getText().toString() == "")) {
                    showErrorMessage();



                }else{

                    Log.d("222Register", "Value:" + etName.getText().toString() + " " + etUsername.getText().toString() + " " + etPassword.getText().toString());
                    String name = etName.getText().toString();
                    String userName = etUsername.getText().toString();
                    String password = etPassword.getText().toString();


                    User user = new User(name, userName, password);

                    Log.d("333Register", "Value:" + user);



                    registerUser(user);


                    break;

                }


        }
    }



    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Register.this);
        dialogBuilder.setMessage("Please Fill In All Registration Fields");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void registerUser(User user){
        ServerRequests serverRequests = new ServerRequests((this));
        serverRequests.storeUserDataInBackground(user, new GetUserCallback(){
            @Override
            public void done(User returnedUser){
                startActivity(new Intent(Register.this, Login.class));
            }

        });
    }


}


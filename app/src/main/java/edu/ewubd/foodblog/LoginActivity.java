package edu.ewubd.foodblog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login, signup, exit;
    Database DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText)findViewById(R.id.login_email);
        password = (EditText)findViewById(R.id.login_password);
        signup = (Button) findViewById(R.id.linkSignup);
        login = (Button) findViewById(R.id.btnLogin);
        exit = (Button) findViewById(R.id.btnExit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LoginActivity.this.finish();
                //System.exit(0);
                LoginActivity.this.finishAffinity();
            }
        });
        DB = new Database(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = email.getText().toString();
                String pass1 = password.getText().toString();

                if (useremail.equals("")||pass1.equals(""))
                    Toast.makeText(LoginActivity.this,"Enter all the Field",Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkmailpass = DB.checkemailpassword(useremail,pass1);

                    if(checkmailpass==true){
                        DB.currentUser(useremail);
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }
}
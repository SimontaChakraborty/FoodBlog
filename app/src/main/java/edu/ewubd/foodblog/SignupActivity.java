package edu.ewubd.foodblog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    EditText name, email ,password1, password2, phone, address;
    Button signup, login, back;

    Database DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = (EditText)findViewById(R.id.suname);
        email = (EditText)findViewById(R.id.suemail);
        password1 = (EditText)findViewById(R.id.supassword1);
        password2 = (EditText)findViewById(R.id.supassword2);
        phone = (EditText)findViewById(R.id.suphone);
        address = (EditText)findViewById(R.id.suaddress);
        signup = (Button) findViewById(R.id.btnsignup);
        login = (Button) findViewById(R.id.linklogin);
        back = (Button) findViewById(R.id.btnback);

        DB = new Database(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = name.getText().toString();
                String useremail = email.getText().toString();
                String pass1 = password1.getText().toString();
                String pass2 = password2.getText().toString();
                String userphone = phone.getText().toString();
                String useradd = address.getText().toString();

                if (username.equals("") ||useremail.equals("")||pass1.equals("")||pass2.equals("")||userphone.equals("")||useradd.equals(""))
                    Toast.makeText(SignupActivity.this,"Enter all the Field",Toast.LENGTH_SHORT).show();
                else {
                    if (pass1.equals(pass2)) {
                        Boolean checkuser = DB.checkemail(useremail);
                        if (checkuser == false) {
                            Boolean insert = DB.insertData(username, useremail, pass1, pass2, userphone, useradd);
                            if (insert == true) {
                                Toast.makeText(SignupActivity.this, "Signup Sucussfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(SignupActivity.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignupActivity.this, "User Account Exixts! Please Login", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(SignupActivity.this, "Incorrect Password or email", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);

            }
        });

    }
}
package com.Project.veganandglutenfree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail,editTextPassword;
    private Button signIn,signUp,forgotPassword;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        signIn = findViewById(R.id.buttonSignIn);
        signUp = findViewById(R.id.buttonSignUp);
        forgotPassword = findViewById(R.id.buttonforgot);

        auth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userMail = editTextEmail.getText().toString();
                String userPassword = editTextPassword.getText().toString();

                if (!userMail.equals("") && !userPassword.equals(""))
                {
                    signin(userMail,userPassword);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Please enter an email and password"
                            , Toast.LENGTH_SHORT).show();
                }

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this,SignUp.class);
                startActivity(i);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this,ForgotActivity.class);
                startActivity(i);
            }
        });

    }

    public void signin(String email, String password)
    {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent i = new Intent(MainActivity.this,MainMenu.class);
                    Toast.makeText(MainActivity.this, "Sign in is successful"
                            , Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Mail or Password is not correct"
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
/*
    public void signInFirebase(String userMail,String userPassword){
        auth.signInWithEmailAndPassword(userMail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent i = new Intent(MainActivity.this,MainMenu.class);
                            startActivity(i);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Mail or Password is not correct"
                                    , Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
*/

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        if(user!= null) {

            Intent i = new Intent(MainActivity.this,MainMenu.class);
            startActivity(i);
            finish();

        }

    }


}
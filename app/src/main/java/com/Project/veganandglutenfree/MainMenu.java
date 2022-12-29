package com.Project.veganandglutenfree;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainMenu extends AppCompatActivity {


    Button rests;
    Button mapping;
    Button signOut;
    Button buisness;
    Button blogs;
    Button recipes;
    Button Userprofile;

    //private ActivityResultLauncher<Intent>activityResultLauncherForProfileActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //registerActivityForProfile();

        signOut = findViewById(R.id.button);
        mapping = findViewById(R.id.mapbutton);
        rests = findViewById(R.id.restbutton);
        buisness = findViewById(R.id.buisnessbutton);
        blogs = findViewById(R.id.blogbutton);
        recipes =findViewById(R.id.recipebutton);
        Userprofile = findViewById(R.id.buttonprofile);


        rests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenu.this, RestActivity.class);
                startActivity(i);
                finish();
            }
        });

        buisness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenu.this, BuisActivity.class);
                startActivity(i);
                finish();

            }
        });

        blogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenu.this, BlogActivity.class);
                startActivity(i);
                finish();
            }
        });

        recipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenu.this, RecipeActivity.class);
                startActivity(i);
                finish();
            }
        });


        mapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenu.this, MapActivity.class);
                startActivity(i);
                finish();
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MainMenu.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });
        Userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenu.this, ProfileActivity.class);

                //activityResultLauncherForProfileActivity.launch(i);
                
                startActivity(i);
                finish();
            }
        });

    }
/*
    public void registerActivityForProfile()
    {
        activityResultLauncherForProfileActivity
                = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {


                    }
                });
    }
*/
}
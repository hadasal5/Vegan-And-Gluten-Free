
package com.Project.veganandglutenfree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeProfile extends AppCompatActivity {

    private String clickRecipeID;

    private CircleImageView recipeProfileImage;
    private TextView recipeProfileName,recipeProfileType,recipeProfileUserName,recipeProfileServing,recipeProfileLevel
            ,recipeProfileIngredients,recipeProfileDirections,recipeProfilePrepTime;

    private DatabaseReference recipeRef;
    Button returnToRecipeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_profile);

        returnToRecipeList = findViewById(R.id.buttonreturnToRecipeList);

        returnToRecipeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecipeProfile.this, RecipeActivity.class);
                startActivity(i);
                finish();

            }
        });

        recipeRef = FirebaseDatabase.getInstance().getReference().child("recipe");

        clickRecipeID = getIntent().getExtras().get("recipeIDs").toString();

        recipeProfileImage = (CircleImageView) findViewById(R.id.recipe_profile_image);
        recipeProfileName = (TextView) findViewById(R.id.recipe_name);
        recipeProfileType = (TextView) findViewById(R.id.recipe_type);
        recipeProfileUserName = (TextView) findViewById(R.id.user_name_recipe);
        recipeProfileServing = (TextView) findViewById(R.id.recipe_serving);
        recipeProfileLevel = (TextView) findViewById(R.id.recipe_level);
        recipeProfileIngredients = (TextView) findViewById(R.id.recipe_ingredients);
        recipeProfileDirections = (TextView) findViewById(R.id.recipe_directions);
        recipeProfilePrepTime = (TextView) findViewById(R.id.recipe_prep_time);

        RetrieveRecipeInfo();


    }

    private void RetrieveRecipeInfo() {
        recipeRef.child(clickRecipeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((snapshot.exists()) && (snapshot.hasChild("image")))
                {
                    String recipeImage = snapshot.child("image").getValue().toString();
                    String recipeName = snapshot.child("name").getValue().toString();
                    String recipeUserName = snapshot.child("user_name").getValue().toString();
                    String recipeType = snapshot.child("type").getValue().toString();
                    String recipeServing = snapshot.child("serving").getValue().toString();
                    String recipeLevel = snapshot.child("level").getValue().toString();
                    String recipeIngredients = snapshot.child("ingredients").getValue().toString();
                    String recipeDirections = snapshot.child("Directions").getValue().toString();
                    String PrepTime = snapshot.child("prep_time").getValue().toString();

                    Picasso.get().load(recipeImage).placeholder(R.drawable.profile_image).into(recipeProfileImage);
                    recipeProfileName.setText(recipeName);
                    recipeProfileUserName.setText(recipeUserName);
                    recipeProfileType.setText(recipeType);
                    recipeProfileServing.setText(recipeServing);
                    recipeProfileLevel.setText(recipeLevel);
                    recipeProfileIngredients.setText(recipeIngredients);
                    recipeProfileDirections.setText(recipeDirections);
                    recipeProfilePrepTime.setText(PrepTime);


                }
                else
                {
                    String recipeName = snapshot.child("name").getValue().toString();
                    String recipeUserName = snapshot.child("user_name").getValue().toString();
                    String recipeType = snapshot.child("type").getValue().toString();
                    String recipeServing = snapshot.child("serving").getValue().toString();
                    String recipeLevel = snapshot.child("level").getValue().toString();
                    String recipeIngredients = snapshot.child("ingredients").getValue().toString();
                    String recipeDirections = snapshot.child("Directions").getValue().toString();
                    String PrepTime = snapshot.child("prep_time").getValue().toString();

                    recipeProfileName.setText(recipeName);
                    recipeProfileUserName.setText(recipeUserName);
                    recipeProfileType.setText(recipeType);
                    recipeProfileServing.setText(recipeServing);
                    recipeProfileLevel.setText(recipeLevel);
                    recipeProfileIngredients.setText(recipeIngredients);
                    recipeProfileDirections.setText(recipeDirections);
                    recipeProfilePrepTime.setText(PrepTime);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
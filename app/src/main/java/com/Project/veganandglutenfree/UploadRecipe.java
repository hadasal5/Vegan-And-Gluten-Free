package com.Project.veganandglutenfree;

import static io.reactivex.rxjava3.internal.util.NotificationLite.getValue;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadRecipe extends AppCompatActivity  {

    Button ReturnToMainRecipeListProfile;
    private CircleImageView recipeProfileImage;
    private EditText recipeProfileName,recipeProfileType
            ,recipeProfileLevel,recipeProfileServing,recipeProfilePrepTime
            ,recipeProfileUserName,recipeProfileIngredients,recipeProfileDirections;
    private Button UpdateRecipe;

    FirebaseDatabase database;
    DatabaseReference reference;

    DatabaseReference recipeRef;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    boolean imageControl = false;
    ActivityResultLauncher<Intent>activityResultLauncherForSelectImage2;
    Uri imageUri;

    int counter = 0;
    List<Recipes> recipesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_recipe);

        registerActivityForSelectImageRecipe();

        recipesList = new ArrayList<>();


        recipeProfileName = findViewById(R.id.recipe_profile_name);
        recipeProfileType = findViewById(R.id.recipe_profile_type);
        recipeProfileLevel = findViewById(R.id.recipe_profile_level);
        recipeProfileServing = findViewById(R.id.recipe_profile_serving);
        recipeProfilePrepTime = findViewById(R.id.recipe_profile_prep_time);
        recipeProfileDirections = findViewById(R.id.recipe_profile_directions);
        recipeProfileIngredients = findViewById(R.id.recipe_profile_ingredients);
        recipeProfileUserName = findViewById(R.id.recipe_profile_user_name);
        recipeProfileImage = findViewById(R.id.recipe_profile_image);

        ReturnToMainRecipeListProfile = findViewById(R.id.buttonReturnToMainRecipeListProfile);
        UpdateRecipe = findViewById(R.id.buttonUpdateRecipe);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        reference.child("recipe").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                counter = (int)dataSnapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        //getRecipeInfo();

        recipeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();


            }
        });
        ReturnToMainRecipeListProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UploadRecipe.this, RecipeList.class);
                startActivity(i);
                finish();
            }
        });

        UpdateRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {updateProfileRecipe(); }

        });
    }
    public void registerActivityForSelectImageRecipe(){
        activityResultLauncherForSelectImage2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent data = result.getData();

                        if (resultCode == RESULT_OK && data!= null)

                        {
                            imageUri = data.getData();
                            Picasso.get().load(imageUri).into(recipeProfileImage);
                            imageControl = true;
                        }
                        else
                        {
                            imageControl = false;
                        }
                    }
                });
    }
    /*
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if(requestCode ==1 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherForSelectImage.launch(intent);
            }

        }

    */
    public void updateProfileRecipe()
    {
/*
        recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot  s : dataSnapshot.getChildren()){
                    Recipes recipe = s.getValue(Recipes.class);
                    recipesList.add(recipe);
                    Log.e("list ", String.valueOf(recipesList.size()));
                    counter = recipesList.size();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */

        String userName = recipeProfileUserName.getText().toString();
        reference.child("recipe").child(String.valueOf(counter)).child("user_name").setValue(userName);

        String RecipeName = recipeProfileName.getText().toString();
        reference.child("recipe").child(String.valueOf(counter)).child("name").setValue(RecipeName);

        String RecipeType = recipeProfileType.getText().toString();
        reference.child("recipe").child(String.valueOf(counter)).child("type").setValue(RecipeType);

        String RecipeLevel = recipeProfileLevel.getText().toString();
        reference.child("recipe").child(String.valueOf(counter)).child("level").setValue(RecipeLevel);

        String RecipeServing = recipeProfileServing.getText().toString();
        reference.child("recipe").child(String.valueOf(counter)).child("serving").setValue(RecipeServing);

        String RecipePrepTime = recipeProfilePrepTime.getText().toString();
        reference.child("recipe").child(String.valueOf(counter)).child("prep_time").setValue(RecipePrepTime);

        String RecipeIngredients = recipeProfileIngredients.getText().toString();
        reference.child("recipe").child(String.valueOf(counter)).child("ingredients").setValue(RecipeIngredients);

        String RecipeDirections = recipeProfileDirections.getText().toString();
        reference.child("recipe").child(String.valueOf(counter)).child("Directions").setValue(RecipeDirections);

        if(imageControl)
        {

            UUID randomID = UUID.randomUUID();
            final String imageName = "images/"+randomID+ ".jpg";
            storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference myStorageRef = firebaseStorage.getReference(imageName);
                    myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String filePath = uri.toString();
                            reference.child("recipe").child(String.valueOf(counter)).child("image").setValue(filePath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(UploadRecipe.this,"Write to database is successful",Toast.LENGTH_SHORT).show();
                                    Picasso.get().load(filePath).into(recipeProfileImage);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UploadRecipe.this,"Write to database is not successful",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    });
                }
            });
        }
        else
        {
            //reference.child("recipe").child(String.valueOf(counter)).child("image").setValue("https://firebasestorage.googleapis.com/v0/b/vegan-and-gluten-free-ap-c4e06.appspot.com/o/images%2F88204a37-1eb3-4959-9cc8-fa051c8b6e23.jpg?alt=media&token=11ebcb72-1dfc-405a-843a-e3ee84090ea6");
            //recipeProfileImage.setImageResource(R.drawable.profile_image);
            reference.child("recipe").child(String.valueOf(counter)).child("image").setValue("null");
        }

        Intent intent = new Intent(UploadRecipe.this,RecipeActivity.class);
        //intent.putExtra("userName",userName);
        startActivity(intent);
        finish();

    }


    public void getRecipeInfo()
    {
        reference.child("recipe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String recipe_profile_name_s = snapshot.child("name").getValue().toString();
                String recipe_profile_type_s = snapshot.child("type").getValue().toString();
                String recipe_profile_level_s = snapshot.child("level").getValue().toString();
                String recipe_profile_serving_s = snapshot.child("serving").getValue().toString();
                String recipe_profile_prep_time_s = snapshot.child("prep_time").getValue().toString();
                String recipe_profile_user_name_s = snapshot.child("user_name").getValue().toString();
                String recipe_profile_ingredients_s = snapshot.child("ingredients").getValue().toString();
                String recipe_profile_directions_s = snapshot.child("Directions").getValue().toString();
                String recipe_profile_image_s = snapshot.child("image").getValue().toString();


                recipeProfileName.setText(recipe_profile_name_s);
                recipeProfileType.setText(recipe_profile_type_s);
                recipeProfileLevel.setText(recipe_profile_level_s);
                recipeProfileServing.setText(recipe_profile_serving_s);
                recipeProfilePrepTime.setText(recipe_profile_prep_time_s);
                recipeProfileUserName.setText(recipe_profile_user_name_s);
                recipeProfileIngredients.setText(recipe_profile_ingredients_s);
                recipeProfileDirections.setText(recipe_profile_directions_s);


                if(recipe_profile_image_s.equals("null"))
                {
                    recipeProfileImage.setImageResource(R.drawable.profile_image);
                }

                else
                {
                    Picasso.get().load(recipe_profile_image_s).into(recipeProfileImage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void imageChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncherForSelectImage2.launch(intent);
    }

}
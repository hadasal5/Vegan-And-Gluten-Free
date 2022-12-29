package com.Project.veganandglutenfree;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    Button backtoMainMenu;
    private CircleImageView userProfileImage;
    private EditText editTextUserNameProfile,editTextDietProfile
            ,editTextCityProfile,editTextGenderProfile;
    private Button buttonUpdate;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    boolean imageControl = false;
    ActivityResultLauncher<Intent>activityResultLauncherForSelectImage;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        registerActivityForSelectImage();

        editTextUserNameProfile = findViewById(R.id.user_name);
        editTextDietProfile = findViewById(R.id.user_diet);
        editTextCityProfile = findViewById(R.id.user_city);
        editTextGenderProfile = findViewById(R.id.user_gender);
        userProfileImage = findViewById(R.id.user_profile_image);

        backtoMainMenu = findViewById(R.id.buttonReturnToMainMenuFromProfile);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        getUserInfo();

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();


            }
        });
        backtoMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, MainMenu.class);
                startActivity(i);
                finish();
            }
        });
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }
    public void registerActivityForSelectImage(){
        activityResultLauncherForSelectImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        
                        if (resultCode == RESULT_OK && data!= null)

                            {
                                imageUri = data.getData();
                                Picasso.get().load(imageUri).into(userProfileImage);
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
    public void updateProfile()
    {
        String userName = editTextUserNameProfile.getText().toString();
        reference.child("user").child(firebaseUser.getUid()).child("userName").setValue(userName);

        String userDiet = editTextDietProfile.getText().toString();
        reference.child("user").child(firebaseUser.getUid()).child("diet").setValue(userDiet);

        String userGender = editTextGenderProfile.getText().toString();
        reference.child("user").child(firebaseUser.getUid()).child("gender").setValue(userGender);

        String userCity = editTextCityProfile.getText().toString();
        reference.child("user").child(firebaseUser.getUid()).child("city").setValue(userCity);

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
                            reference.child("user").child(auth.getUid()).child("image").setValue(filePath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ProfileActivity.this,"Write to database is successful",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProfileActivity.this,"Write to database is not successful",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    });
                }
            });

        }
        else
        {
            reference.child("user").child(auth.getUid()).child("image").setValue("null");
        }
        Intent intent = new Intent(ProfileActivity.this,MainMenu.class);
        intent.putExtra("userName",userName);
        startActivity(intent);
        finish();
    }


    public void getUserInfo()
    {
        reference.child("user").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                
                String name = snapshot.child("userName").getValue().toString();
                String diet = snapshot.child("diet").getValue().toString();
                String city = snapshot.child("city").getValue().toString();
                String gender = snapshot.child("gender").getValue().toString();
                String image = snapshot.child("image").getValue().toString();

                editTextDietProfile.setText(diet);
                editTextUserNameProfile.setText(name);
                editTextGenderProfile.setText(gender);
                editTextCityProfile.setText(city);

                if(image.equals("null"))
                {
                    userProfileImage.setImageResource(R.drawable.profile_image);
                }

                else
                {
                    Picasso.get().load(image).into(userProfileImage);
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
        activityResultLauncherForSelectImage.launch(intent);
    }

}
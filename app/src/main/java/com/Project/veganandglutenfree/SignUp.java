package com.Project.veganandglutenfree;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.Project.veganandglutenfree.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {

    private CircleImageView imageViewCircle;
    private TextInputEditText editTextEmailSignup,
            editTextPasswordSignUp,editTextUserNameSignup,editTextDietProfile
            ,editTextCityProfile,editTextGenderProfile;
    private Button enter;
    boolean imageControl = false;
    Uri imageUri;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    ActivityResultLauncher<Intent>activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        registerActivityForSelectImage();


        imageViewCircle = findViewById(R.id.imageViewCircle);
        editTextEmailSignup = findViewById(R.id.editTextEmailSignUp);
        editTextPasswordSignUp = findViewById(R.id.editTextPasswordSignUp);
        editTextUserNameSignup = findViewById(R.id.editTextUserNameSignUp);

        editTextDietProfile = findViewById(R.id.dietSignUp);
        editTextCityProfile = findViewById(R.id.citySignUp);
        editTextGenderProfile = findViewById(R.id.genderSignUp);


        enter = findViewById(R.id.buttonEnter);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();



        imageViewCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();

            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmailSignup.getText().toString();
                String password = editTextPasswordSignUp.getText().toString();
                String userName = editTextUserNameSignup.getText().toString();
                String userGender = editTextGenderProfile.getText().toString();
                String userDiet = editTextDietProfile.getText().toString();
                String userCity = editTextCityProfile.getText().toString();


                if (!email.equals("") && !password.equals("")&& !userName.equals("")&& !userGender.equals("")&& !userDiet.equals("")&& !userCity.equals(""))
                {
                    signup(email,password,userName,userGender,userDiet,userCity);
                }

            }
        });

    }
    public void registerActivityForSelectImage(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent data = result.getData();

                        if (resultCode == RESULT_OK && data!= null)

                        {
                            imageUri = data.getData();
                            Picasso.get().load(imageUri).into(imageViewCircle);
                            imageControl = true;
                        }
                        else
                        {
                            imageControl = false;
                        }
                    }
                });
    }

    public void imageChooser()
    {
       Intent intent = new Intent();
       intent.setType("image/*");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       activityResultLauncher.launch(intent);

    }

    public void signup(String email,String password, String userName,String userGender,String userDiet,String userCity)
    {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    reference.child("user").child(auth.getUid()).child("userName").setValue(userName);
                    reference.child("user").child(auth.getUid()).child("email").setValue(email);
                    reference.child("user").child(auth.getUid()).child("gender").setValue(userGender);
                    reference.child("user").child(auth.getUid()).child("diet").setValue(userDiet);
                    reference.child("user").child(auth.getUid()).child("city").setValue(userCity);

                    if(imageControl)
                    {
                        UUID randomID = UUID.randomUUID();
                        String imageName = "images/"+randomID+ ".jpg";
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
                                                Toast.makeText(SignUp.this,"Write to database is successful",Toast.LENGTH_SHORT).show();
                                                //Picasso.get().load(filePath).into(imageViewCircle);

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(SignUp.this,"Write to database is not successful",Toast.LENGTH_SHORT).show();

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

                    Intent intent = new Intent(SignUp.this,MainActivity.class);
                    intent.putExtra("userName",userName);
                    startActivity(intent);
                    finish();

                }
                else
                {
                    Toast.makeText(SignUp.this
                            , "There is a problem", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        if(user!= null) {

            Intent i = new Intent(SignUp.this,MainActivity.class);
            startActivity(i);
            finish();
        }

    }

}
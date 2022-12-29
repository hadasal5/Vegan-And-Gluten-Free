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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RestProfile extends AppCompatActivity  {

    private String clickRestID,restProfileLat,restProfileLon;


    private CircleImageView restProfileImage;
    private TextView restProfileName, restProfileAddress, restProfilePhone, restProfileHours, restProfileDesc,restProfileCategories,restProfileUrl;

    private DatabaseReference RestRef;
    Button returnToList, returnToMap, buttonNavigateRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_profile);

        buttonNavigateRest = findViewById(R.id.buttonNavigate);
        returnToList = findViewById(R.id.buttonreturnToRestList);
        returnToMap = findViewById(R.id.buttonreturnToMap);

        RestRef = FirebaseDatabase.getInstance().getReference().child("resturant");

        clickRestID = getIntent().getExtras().get("restIDs").toString();

        returnToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RestProfile.this, RestActivity.class);
                startActivity(i);
                finish();

            }
        });

        returnToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RestProfile.this, MapActivity.class);
                startActivity(i);
                finish();

            }
        });

        buttonNavigateRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*String lat = RestRef.child(clickRestID).child("lat").toString();
                String lon = RestRef.child(clickRestID).child("lon").().toString();
                Log.e("lat ", lat);
                Log.e("lon ", lon);*/
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q="+restProfileLat+","+restProfileLon+"&mode=1"));
                intent.setPackage("com.google.android.apps.maps");

                if(intent.resolveActivity(getPackageManager())!= null){
                    startActivity(intent);
                }


                //loadNavigationView(lat,lon);
                //Intent i = new Intent(RestProfile.this, MapActivity.class);
                //startActivity(i);
                //finish();

            }
        });



        //Toast.makeText(this, "Rest Id: "+clickRestID, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),"Rest ID: "+ clickRestID, Toast.LENGTH_SHORT).show();

        restProfileImage = (CircleImageView) findViewById(R.id.rest_profile_image);
        restProfileName = (TextView) findViewById(R.id.rest_name);
        restProfileAddress = (TextView) findViewById(R.id.rest_address);
        restProfilePhone = (TextView) findViewById(R.id.rest_phone);
        restProfileHours = (TextView) findViewById(R.id.rest_hours);
        restProfileDesc = (TextView) findViewById(R.id.rest_desc);
        restProfileCategories = (TextView) findViewById(R.id.rest_categories);
        restProfileUrl = (TextView) findViewById(R.id.rest_url);
        RetrieveRestInfo();


    }

    private void RetrieveRestInfo() {
        RestRef.child(clickRestID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((snapshot.exists()) && (snapshot.hasChild("image")))
                {
                    String restImage = snapshot.child("image").getValue().toString();
                    String restName = snapshot.child("name").getValue().toString();
                    String restAddress = snapshot.child("address").getValue().toString();
                    String restPhone = snapshot.child("phone").getValue().toString();
                    String restHours = snapshot.child("openinghours").getValue().toString();
                    String restDesc = snapshot.child("description").getValue().toString();
                    String restCategories = snapshot.child("tags").getValue().toString();
                    String restUrl = snapshot.child("url").getValue().toString();
                    String restLat = snapshot.child("lat").getValue().toString();
                    String restLon = snapshot.child("lon").getValue().toString();

                    Picasso.get().load(restImage).placeholder(R.drawable.profile_image).into(restProfileImage);
                    restProfileName.setText(restName);
                    restProfileAddress.setText(restAddress);
                    restProfilePhone.setText(restPhone);
                    restProfileHours.setText(restHours);
                    restProfileDesc.setText(restDesc);
                    restProfileCategories.setText(restCategories);
                    restProfileUrl.setText(restUrl);
                    restProfileLat= restLat;
                    restProfileLon= restLon;

                }
                else
                {
                    String restName = snapshot.child("name").getValue().toString();
                    String restAddress = snapshot.child("address").getValue().toString();
                    String restPhone = snapshot.child("phone").getValue().toString();
                    String restHours = snapshot.child("openinghours").getValue().toString();
                    String restDesc = snapshot.child("description").getValue().toString();
                    String restCategories = snapshot.child("tags").getValue().toString();
                    String restUrl = snapshot.child("url").getValue().toString();
                    String restLat = snapshot.child("lat").getValue().toString();
                    String restLon = snapshot.child("lon").getValue().toString();


                    restProfileName.setText(restName);
                    restProfileAddress.setText(restAddress);
                    restProfilePhone.setText(restPhone);
                    restProfileHours.setText(restHours);
                    restProfileDesc.setText(restDesc);
                    restProfileCategories.setText(restCategories);
                    restProfileUrl.setText(restUrl);
                    restProfileLat= restLat;
                    restProfileLon= restLon;
                }
                String restLink = restProfileUrl.getText().toString();
                String text = "קישור לאתר";
                SpannableString spannableString = new SpannableString(text);

                String phone = restProfilePhone.getText().toString();
                SpannableString spannableString2 = new SpannableString(phone);



                ClickableSpan span1 = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW
                                , Uri.parse(restLink));

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                    }
                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.BLUE);
                        ds.setUnderlineText(false);
                    }
                };

                ClickableSpan span2 = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {

                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                        startActivity(intent);
                    }
                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.BLUE);
                        ds.setUnderlineText(false);
                    }
                };

                spannableString.setSpan(span1, 0, 10
                        , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                restProfileUrl.setText(spannableString);
                restProfileUrl.setMovementMethod(LinkMovementMethod.getInstance());


                spannableString2.setSpan(span2, 0, phone.length()
                        , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                restProfilePhone.setText(spannableString2);
                restProfilePhone.setMovementMethod(LinkMovementMethod.getInstance());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
/*
    public void loadNavigationView(String lat,String lng){
        Uri navigation = Uri.parse("google.navigation:q="+lat+","+lng+"");
        Intent navigationIntent = new Intent(Intent.ACTION_VIEW, navigation);
        navigationIntent.setPackage("com.google.android.apps.maps");
        startActivity(navigationIntent);
    }

 */
}
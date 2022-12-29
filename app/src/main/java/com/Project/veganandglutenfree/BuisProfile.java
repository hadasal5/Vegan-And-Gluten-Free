
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

public class BuisProfile extends AppCompatActivity {

    private String clickBuisID,buisProfileLat,buisProfileLon;

    private CircleImageView buisProfileImage;
    private TextView buisProfileName, buisProfileAddress, buisProfilePhone, buisProfileDesc,buisProfileCategories,buisProfileUrl;

    private DatabaseReference BuisRef;
    Button returnToBuisList, returnToMapFromBuis,buttonNavigateRest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buis_profile);

        returnToBuisList = findViewById(R.id.buttonreturnToBuisList);
        returnToMapFromBuis = findViewById(R.id.buttonreturnToMapFromBuis);
        buttonNavigateRest = findViewById(R.id.buttonNavigateB);

        returnToBuisList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BuisProfile.this, BuisActivity.class);
                startActivity(i);
                finish();

            }
        });
        returnToMapFromBuis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BuisProfile.this, MapActivity.class);
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
                        Uri.parse("google.navigation:q=" + buisProfileLat + "," + buisProfileLon + "&mode=1"));
                intent.setPackage("com.google.android.apps.maps");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });


        BuisRef = FirebaseDatabase.getInstance().getReference().child("buisness");

        clickBuisID = getIntent().getExtras().get("buisIDs").toString();

        //Toast.makeText(this, "Rest Id: "+clickRestID, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),"Rest ID: "+ clickRestID, Toast.LENGTH_SHORT).show();

        buisProfileImage = (CircleImageView) findViewById(R.id.buis_profile_image);
        buisProfileName = (TextView) findViewById(R.id.buis_name);
        buisProfileAddress = (TextView) findViewById(R.id.buis_address);
        buisProfilePhone = (TextView) findViewById(R.id.buis_phone);
        buisProfileDesc = (TextView) findViewById(R.id.buis_desc);
        buisProfileCategories = (TextView) findViewById(R.id.buis_categories);
        buisProfileUrl = (TextView) findViewById(R.id.buis_url);
        RetrieveBuissInfo();


    }

    private void RetrieveBuissInfo() {
        BuisRef.child(clickBuisID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((snapshot.exists()) && (snapshot.hasChild("image")))
                {
                    String buisImage = snapshot.child("image").getValue().toString();
                    String buisName = snapshot.child("name").getValue().toString();
                    String buisAddress = snapshot.child("address").getValue().toString();
                    String buisPhone = snapshot.child("phone").getValue().toString();
                    String buisDesc = snapshot.child("description").getValue().toString();
                    String buisCategories = snapshot.child("tags").getValue().toString();
                    String buisUrl = snapshot.child("url").getValue().toString();
                    String buisLat = snapshot.child("lat").getValue().toString();
                    String buisLon = snapshot.child("lon").getValue().toString();

                    Picasso.get().load(buisImage).placeholder(R.drawable.profile_image).into(buisProfileImage);
                    buisProfileName.setText(buisName);
                    buisProfileAddress.setText(buisAddress);
                    buisProfilePhone.setText(buisPhone);
                    buisProfileDesc.setText(buisDesc);
                    buisProfileCategories.setText(buisCategories);
                    buisProfileUrl.setText(buisUrl);
                    buisProfileLat= buisLat;
                    buisProfileLon= buisLon;

                }
                else
                {
                    String buisName = snapshot.child("name").getValue().toString();
                    String buisAddress = snapshot.child("address").getValue().toString();
                    String buisPhone = snapshot.child("phone").getValue().toString();
                    String buisDesc = snapshot.child("description").getValue().toString();
                    String buisCategories = snapshot.child("tags").getValue().toString();
                    String buisUrl = snapshot.child("url").getValue().toString();
                    String buisLat = snapshot.child("lat").getValue().toString();
                    String buisLon = snapshot.child("lon").getValue().toString();


                    buisProfileName.setText(buisName);
                    buisProfileAddress.setText(buisAddress);
                    buisProfilePhone.setText(buisPhone);
                    buisProfileDesc.setText(buisDesc);
                    buisProfileCategories.setText(buisCategories);
                    buisProfileUrl.setText(buisUrl);
                    buisProfileLat= buisLat;
                    buisProfileLon= buisLon;
                }
                String buisLink = buisProfileUrl.getText().toString();
                String text = "קישור לאתר";
                SpannableString spannableString = new SpannableString(text);

                String phone = buisProfilePhone.getText().toString();
                SpannableString spannableString2 = new SpannableString(phone);


                ClickableSpan span1 = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW
                                , Uri.parse(buisLink));

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
                buisProfileUrl.setText(spannableString);
                buisProfileUrl.setMovementMethod(LinkMovementMethod.getInstance());

                spannableString2.setSpan(span2, 0, phone.length()
                        , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                buisProfilePhone.setText(spannableString2);
                buisProfilePhone.setMovementMethod(LinkMovementMethod.getInstance());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


}
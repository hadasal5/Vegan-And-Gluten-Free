
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

public class BlogProfile extends AppCompatActivity {

    private String clickBlogID;

    private CircleImageView blogProfileImage;
    private TextView blogProfileName, blogProfileDesc,blogProfileUrl;

    private DatabaseReference BlogRef;
    Button returnToBlogList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_profile);

        returnToBlogList = findViewById(R.id.buttonreturnToBlogList);

        returnToBlogList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BlogProfile.this, BlogActivity.class);
                startActivity(i);
                finish();

            }
        });

        BlogRef = FirebaseDatabase.getInstance().getReference().child("blogs");

        clickBlogID = getIntent().getExtras().get("blogIDs").toString();

        //Toast.makeText(this, "Rest Id: "+clickRestID, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),"Rest ID: "+ clickRestID, Toast.LENGTH_SHORT).show();

        blogProfileImage = (CircleImageView) findViewById(R.id.blog_profile_image);
        blogProfileName = (TextView) findViewById(R.id.blog_name);
        blogProfileDesc = (TextView) findViewById(R.id.blog_desc);
        blogProfileUrl = (TextView) findViewById(R.id.blog_url);
        RetrieveBlogInfo();


    }

    private void RetrieveBlogInfo() {
        BlogRef.child(clickBlogID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((snapshot.exists()) && (snapshot.hasChild("image")))
                {
                    String blogImage = snapshot.child("image").getValue().toString();
                    String blogName = snapshot.child("name").getValue().toString();
                    String blogDesc = snapshot.child("description").getValue().toString();
                    String blogUrl = snapshot.child("url").getValue().toString();

                    Picasso.get().load(blogImage).placeholder(R.drawable.profile_image).into(blogProfileImage);
                    blogProfileName.setText(blogName);
                    blogProfileDesc.setText(blogDesc);
                    blogProfileUrl.setText(blogUrl);

                }
                else
                {
                    String blogName = snapshot.child("name").getValue().toString();
                    String blogDesc = snapshot.child("description").getValue().toString();
                    String blogUrl = snapshot.child("url").getValue().toString();


                    blogProfileName.setText(blogName);
                    blogProfileDesc.setText(blogDesc);
                    blogProfileUrl.setText(blogUrl);
                }
                String blogLink = blogProfileUrl.getText().toString();
                String text = "קישור לאתר";
                SpannableString spannableString = new SpannableString(text);


                ClickableSpan span1 = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW
                                , Uri.parse(blogLink));

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

                spannableString.setSpan(span1, 0, 10
                        , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                blogProfileUrl.setText(spannableString);
                blogProfileUrl.setMovementMethod(LinkMovementMethod.getInstance());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}
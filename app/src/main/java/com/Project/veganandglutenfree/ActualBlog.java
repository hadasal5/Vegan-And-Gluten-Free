package com.Project.veganandglutenfree;
/* second activity */


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

public class ActualBlog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_blog);


        Intent intent = getIntent();
        int position = intent.getIntExtra("position",0);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ActualBlogFragment actualBlogFragment = new ActualBlogFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        actualBlogFragment.setArguments(bundle);


        fragmentTransaction.add(R.id.frameLayout,actualBlogFragment);

        fragmentTransaction.commit();

    }
}

package com.Project.veganandglutenfree;
        /* first activity */

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.FragmentManager;
        import androidx.fragment.app.FragmentTransaction;

        import android.os.Bundle;

public class BlogActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        BlogList BlogList = new BlogList();
        fragmentTransaction.add(R.id.frameLayoutblog, BlogList);
        fragmentTransaction.commit();

    }

}
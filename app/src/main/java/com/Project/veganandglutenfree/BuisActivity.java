
package com.Project.veganandglutenfree;
        /* first activity */

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.FragmentManager;
        import androidx.fragment.app.FragmentTransaction;

        import android.os.Bundle;

public class BuisActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buis);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        BuisList BuisList = new BuisList();
        fragmentTransaction.add(R.id.frameLayoutbuis,BuisList);
        fragmentTransaction.commit();

    }

}


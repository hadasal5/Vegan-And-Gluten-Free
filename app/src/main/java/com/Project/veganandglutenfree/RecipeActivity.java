
package com.Project.veganandglutenfree;
        /* first activity */

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.FragmentManager;
        import androidx.fragment.app.FragmentTransaction;

        import android.os.Bundle;

public class RecipeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RecipeList RecipeList = new RecipeList();
        fragmentTransaction.add(R.id.frameLayoutrecipe, RecipeList);
        fragmentTransaction.commit();

    }

}
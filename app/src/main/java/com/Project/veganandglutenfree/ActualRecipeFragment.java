package com.Project.veganandglutenfree;
/* ****second fragment**** */

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ActualRecipeFragment extends Fragment {

    ImageView imageViewRecipeF;

    public ActualRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_actual_recipe, container, false);

        imageViewRecipeF = view.findViewById(R.id.imageViewRecipe);
        Bundle bundle = getArguments();
        int position = bundle.getInt("position",0);

        if(position==0)
        {
            imageViewRecipeF.setImageResource(R.drawable.images);

        }

        return view;

    }
}
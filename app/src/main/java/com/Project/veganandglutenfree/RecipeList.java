
package com.Project.veganandglutenfree;
/* *****first fragment**** */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeList extends Fragment implements View.OnClickListener{
    private View RecipeView;
    private RecyclerView myRecipeList;

    private DatabaseReference recipeRef;
    /*private FirebaseAuth mAuth;
    private String currentRestID;*/




    public RecipeList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        RecipeView  = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        myRecipeList = (RecyclerView) RecipeView.findViewById(R.id.recipe_list);
        myRecipeList.setLayoutManager(new LinearLayoutManager(getContext()));


        recipeRef = FirebaseDatabase.getInstance().getReference().child("recipe");

        return RecipeView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        /*
        Button newBlockButton = (Button) getActivity().findViewById(
                R.id.buttonreturnToMainMenuFromRecipe);
        newBlockButton.setOnClickListener((View.OnClickListener) this);

        Button newBlockButton2 = (Button) getActivity().findViewById(
                R.id.uploadRecipe);
        newBlockButton2.setOnClickListener((View.OnClickListener) this);
        */
        Button button1 = view.findViewById(R.id.buttonreturnToMainMenuFromRecipe);
        Button button2 = view.findViewById(R.id.uploadRecipe);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonreturnToMainMenuFromRecipe:
                Intent intent = new Intent(getActivity(), MainMenu.class);
                startActivity(intent);
                break;
            case R.id.uploadRecipe:
                Intent intent2 = new Intent(getActivity(), UploadRecipe.class);
                startActivity(intent2);
                break;
            // Do this for all buttons.
        }
        /*
        Intent intent = new Intent(getActivity(), MainMenu.class);
        startActivity(intent);

        Intent intent2 = new Intent(getActivity(), UploadRecipe.class);
        startActivity(intent2);
        */

    }


    private void firebaseSearch(String searchtext)
    {
        String query = searchtext.toLowerCase();
        Query firebaseQuery = recipeRef.orderByChild("type").startAt(query).endAt("\uf8ff");

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Recipe>()
                        .setQuery(firebaseQuery, Recipe.class)
                        .build();

        FirebaseRecyclerAdapter<Recipe, RecipeViewHolder> adapter
                = new FirebaseRecyclerAdapter<Recipe, RecipeViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull RecipeViewHolder holder, int position, @NonNull Recipe model) {
                String recipeIDs = getRef(position).getKey();

                recipeRef.child(recipeIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("image"))
                        {
                            String recipe_image = snapshot.child("image").getValue().toString();
                            String recipe_name = snapshot.child("name").getValue().toString();
                            String user_name = snapshot.child("user_name").getValue().toString();
                            String recipe_type = snapshot.child("type").getValue().toString();

                            holder.recipeName.setText(recipe_name);
                            holder.recipeUserName.setText(user_name);
                            holder.recipeType.setText(recipe_type);
                            Picasso.get().load(recipe_image).placeholder(R.drawable.profile_image).into(holder.recipeImage);


                        }
                        else {
                            String recipe_name = snapshot.child("name").getValue().toString();
                            String recipe_type = snapshot.child("type").getValue().toString();
                            String user_name = snapshot.child("user_name").getValue().toString();
                            holder.recipeName.setText(recipe_name);
                            holder.recipeUserName.setText(user_name);
                            holder.recipeType.setText(recipe_type);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent = new Intent(getActivity(), RecipeProfile.class);
                        profileIntent.putExtra("recipeIDs",recipeIDs);
                        startActivity(profileIntent);
                    }
                });


            }
            @NonNull
            @Override
            public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reciptlistlayout,parent,false);
                RecipeViewHolder viewHolder= new RecipeViewHolder(view);
                return viewHolder;

            }

        };
        myRecipeList.setAdapter(adapter);
        adapter.startListening();

    }

        @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Recipe>()
                        .setQuery(recipeRef, Recipe.class)
                        .build();

        FirebaseRecyclerAdapter<Recipe, RecipeViewHolder> adapter
                = new FirebaseRecyclerAdapter<Recipe, RecipeViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull RecipeViewHolder holder, int position, @NonNull Recipe model) {
                String recipeIDs = getRef(position).getKey();

                recipeRef.child(recipeIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("image"))
                        {
                            String recipe_image = snapshot.child("image").getValue().toString();
                            String recipe_name = snapshot.child("name").getValue().toString();
                            String user_name = snapshot.child("user_name").getValue().toString();
                            String recipe_type = snapshot.child("type").getValue().toString();

                            holder.recipeName.setText(recipe_name);
                            holder.recipeUserName.setText(user_name);
                            holder.recipeType.setText(recipe_type);
                            Picasso.get().load(recipe_image).placeholder(R.drawable.profile_image).into(holder.recipeImage);


                        }
                        else {
                            String recipe_name = snapshot.child("name").getValue().toString();
                            String recipe_type = snapshot.child("type").getValue().toString();
                            String user_name = snapshot.child("user_name").getValue().toString();
                            holder.recipeName.setText(recipe_name);
                            holder.recipeUserName.setText(user_name);
                            holder.recipeType.setText(recipe_type);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent = new Intent(getActivity(), RecipeProfile.class);
                        profileIntent.putExtra("recipeIDs",recipeIDs);
                        startActivity(profileIntent);
                    }
                });


            }
            @NonNull
            @Override
            public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reciptlistlayout,parent,false);
                RecipeViewHolder viewHolder= new RecipeViewHolder(view);
                return viewHolder;

            }



        };
        myRecipeList.setAdapter(adapter);
        adapter.startListening();


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu_recipe,menu);
        MenuItem item = menu.findItem(R.id.search_firebase_recipe);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder
    {
        TextView recipeName,recipeType,recipeUserName;
        CircleImageView recipeImage;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);


            recipeName = itemView.findViewById(R.id.recipt_name);
            recipeUserName = itemView.findViewById(R.id.user_name_r);
            recipeType = itemView.findViewById(R.id.recipt_type);
            recipeImage = itemView.findViewById(R.id.recipe_image);



        }
    }
    private void moveToMainMenu () {

        Intent i = new Intent(getActivity(), MainMenu.class);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }

    private void moveToUploadRecipe () {

        Intent i = new Intent(getActivity(), UploadRecipe.class);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);

    }

}
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

import java.util.Arrays;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class RestList extends Fragment implements View.OnClickListener {
    private View RestView;
    private RecyclerView myRestList;

    private DatabaseReference resturantRef;


    public RestList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true); // Add this!

        // Inflate the layout for this fragment
        RestView  = inflater.inflate(R.layout.fragment_rest_list, container, false);

        myRestList = (RecyclerView) RestView.findViewById(R.id.rest_list);
        myRestList.setLayoutManager(new LinearLayoutManager(getContext()));

        resturantRef = FirebaseDatabase.getInstance().getReference().child("resturant");

        return RestView;

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button newBlockButton = (Button) getActivity().findViewById(
                R.id.buttonreturnToMainMenuFromBuis);
        newBlockButton.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), MainMenu.class);
        startActivity(intent);
    }


    private void firebaseSearch(String searchtext)
    {
        String query = searchtext.toLowerCase();
        Query firebaseQuery = resturantRef.orderByChild("type").startAt(query).endAt("\uf8ff");


        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Rests>()
                        .setQuery(firebaseQuery, Rests.class)
                        .build();

        FirebaseRecyclerAdapter<Rests, RestsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Rests, RestsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull RestsViewHolder holder, int position, @NonNull Rests model) {
                String restIDs = getRef(position).getKey();

                resturantRef.child(restIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("image"))
                        {
                            String rest_image = snapshot.child("image").getValue().toString();
                            String rest_name = snapshot.child("name").getValue().toString();
                            String rest_address = snapshot.child("address").getValue().toString();
                            String rest_type = snapshot.child("type").getValue().toString();

                            holder.restName.setText(rest_name);
                            holder.restAddress.setText(rest_address);
                            holder.restType.setText(rest_type);
                            Picasso.get().load(rest_image).placeholder(R.drawable.profile_image).into(holder.restImage);


                        }
                        else {
                            String rest_name = snapshot.child("name").getValue().toString();
                            String rest_address = snapshot.child("address").getValue().toString();
                            String rest_type = snapshot.child("type").getValue().toString();
                            holder.restName.setText(rest_name);
                            holder.restAddress.setText(rest_address);
                            holder.restType.setText(rest_type);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent = new Intent(getActivity(), RestProfile.class);
                        profileIntent.putExtra("restIDs",restIDs);
                        startActivity(profileIntent);
                    }
                });

            }
            @NonNull
            @Override
            public RestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restlistlayout,parent,false);
                RestsViewHolder viewHolder= new RestsViewHolder(view);
                return viewHolder;

            }

        };
        myRestList.setAdapter(adapter);
        adapter.startListening();
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Rests>()
                        .setQuery(resturantRef, Rests.class)
                        .build();

        FirebaseRecyclerAdapter<Rests, RestsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Rests, RestsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull RestsViewHolder holder, int position, @NonNull Rests model) {
                String restIDs = getRef(position).getKey();

                resturantRef.child(restIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("image"))
                        {
                            String rest_image = snapshot.child("image").getValue().toString();
                            String rest_name = snapshot.child("name").getValue().toString();
                            String rest_address = snapshot.child("address").getValue().toString();
                            String rest_type = snapshot.child("type").getValue().toString();

                            holder.restName.setText(rest_name);
                            holder.restAddress.setText(rest_address);
                            holder.restType.setText(rest_type);
                            Picasso.get().load(rest_image).placeholder(R.drawable.profile_image).into(holder.restImage);


                        }
                        else {
                            String rest_name = snapshot.child("name").getValue().toString();
                            String rest_address = snapshot.child("address").getValue().toString();
                            String rest_type = snapshot.child("type").getValue().toString();
                            holder.restName.setText(rest_name);
                            holder.restAddress.setText(rest_address);
                            holder.restType.setText(rest_type);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent = new Intent(getActivity(), RestProfile.class);
                        profileIntent.putExtra("restIDs",restIDs);
                        startActivity(profileIntent);
                    }
                });


            }
            @NonNull
            @Override
            public RestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restlistlayout,parent,false);
                RestsViewHolder viewHolder= new RestsViewHolder(view);
                return viewHolder;

            }



        };
        myRestList.setAdapter(adapter);
        adapter.startListening();


    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.search_firebase);
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



    public static class RestsViewHolder extends RecyclerView.ViewHolder
    {
        TextView restName,restAddress,restType;
        CircleImageView restImage;

        public RestsViewHolder(@NonNull View itemView) {
            super(itemView);

            restName = itemView.findViewById(R.id.rest_name);
            restAddress = itemView.findViewById(R.id.rest_address);
            restType = itemView.findViewById(R.id.rest_type);
            restImage = itemView.findViewById(R.id.rest_image);

        }
    }

    private void moveToNewActivity () {

        Intent i = new Intent(getActivity(), MainMenu.class);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);

    }



}


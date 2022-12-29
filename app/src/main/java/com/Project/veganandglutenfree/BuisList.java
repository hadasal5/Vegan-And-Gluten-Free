
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

public class BuisList extends Fragment implements View.OnClickListener{
    private View BuisView;
    private RecyclerView myBuisList;

    private DatabaseReference buisRef;





    public BuisList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        BuisView  = inflater.inflate(R.layout.fragment_buis_list, container, false);

        myBuisList = (RecyclerView) BuisView.findViewById(R.id.buis_list);
        myBuisList.setLayoutManager(new LinearLayoutManager(getContext()));

        buisRef = FirebaseDatabase.getInstance().getReference().child("buisness");

        return BuisView;

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
        Query firebaseQuery = buisRef.orderByChild("type").startAt(query).endAt("\uf8ff");


        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Buiss>()
                        .setQuery(firebaseQuery, Buiss.class)
                        .build();

        FirebaseRecyclerAdapter<Buiss, RestsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Buiss, RestsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull RestsViewHolder holder, int position, @NonNull Buiss model) {
                String buisIDs = getRef(position).getKey();

                buisRef.child(buisIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("image"))
                        {
                            String buis_image = snapshot.child("image").getValue().toString();
                            String buis_name = snapshot.child("name").getValue().toString();
                            String buis_address = snapshot.child("address").getValue().toString();
                            String buis_type = snapshot.child("type").getValue().toString();

                            holder.buisName.setText(buis_name);
                            holder.buisAddress.setText(buis_address);
                            holder.buisType.setText(buis_type);
                            Picasso.get().load(buis_image).placeholder(R.drawable.profile_image).into(holder.buisImage);


                        }
                        else {
                            String buis_name = snapshot.child("name").getValue().toString();
                            String buis_address = snapshot.child("address").getValue().toString();
                            String buis_type = snapshot.child("type").getValue().toString();
                            holder.buisName.setText(buis_name);
                            holder.buisAddress.setText(buis_address);
                            holder.buisType.setText(buis_type);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent = new Intent(getActivity(), BuisProfile.class);
                        profileIntent.putExtra("buisIDs",buisIDs);
                        startActivity(profileIntent);
                    }
                });


            }
            @NonNull
            @Override
            public RestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buislistlayout,parent,false);
                RestsViewHolder viewHolder= new RestsViewHolder(view);
                return viewHolder;

            }



        };
        myBuisList.setAdapter(adapter);
        adapter.startListening();
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Buiss>()
                        .setQuery(buisRef, Buiss.class)
                        .build();

        FirebaseRecyclerAdapter<Buiss, RestsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Buiss, RestsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull RestsViewHolder holder, int position, @NonNull Buiss model) {
                String buisIDs = getRef(position).getKey();

                buisRef.child(buisIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("image"))
                        {
                            String buis_image = snapshot.child("image").getValue().toString();
                            String buis_name = snapshot.child("name").getValue().toString();
                            String buis_address = snapshot.child("address").getValue().toString();
                            String buis_type = snapshot.child("type").getValue().toString();

                            holder.buisName.setText(buis_name);
                            holder.buisAddress.setText(buis_address);
                            holder.buisType.setText(buis_type);
                            Picasso.get().load(buis_image).placeholder(R.drawable.profile_image).into(holder.buisImage);


                        }
                        else {
                            String buis_name = snapshot.child("name").getValue().toString();
                            String buis_address = snapshot.child("address").getValue().toString();
                            String buis_type = snapshot.child("type").getValue().toString();
                            holder.buisName.setText(buis_name);
                            holder.buisAddress.setText(buis_address);
                            holder.buisType.setText(buis_type);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent = new Intent(getActivity(), BuisProfile.class);
                        profileIntent.putExtra("buisIDs",buisIDs);
                        startActivity(profileIntent);
                    }
                });


            }
            @NonNull
            @Override
            public RestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buislistlayout,parent,false);
                RestsViewHolder viewHolder= new RestsViewHolder(view);
                return viewHolder;

            }


        };
        myBuisList.setAdapter(adapter);
        adapter.startListening();

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu_buis,menu);
        MenuItem item = menu.findItem(R.id.search_firebase_buis);

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
        TextView buisName,buisAddress,buisType;
        CircleImageView buisImage;

        public RestsViewHolder(@NonNull View itemView) {
            super(itemView);


            buisName = itemView.findViewById(R.id.buis_name);
            buisAddress = itemView.findViewById(R.id.buis_address);
            buisType = itemView.findViewById(R.id.buis_type);
            buisImage = itemView.findViewById(R.id.buis_image);



        }
    }
    private void moveToNewActivity () {

        Intent i = new Intent(getActivity(), MainMenu.class);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);

    }

}
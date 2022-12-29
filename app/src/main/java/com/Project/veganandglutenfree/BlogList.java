
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

public class BlogList extends Fragment implements View.OnClickListener{
    private View BlogView;
    private RecyclerView myBlogList;

    private DatabaseReference blogRef;
    /*private FirebaseAuth mAuth;
    private String currentRestID;*/




    public BlogList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        BlogView  = inflater.inflate(R.layout.fragment_blog_list, container, false);

        myBlogList = (RecyclerView) BlogView.findViewById(R.id.blog_list);
        myBlogList.setLayoutManager(new LinearLayoutManager(getContext()));

        /*mAuth = FirebaseAuth.getInstance();
        currentRestID = mAuth.getCurrentUser().getUid();*/

        blogRef = FirebaseDatabase.getInstance().getReference().child("blogs");

        return BlogView;

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button newBlockButton = (Button) getActivity().findViewById(
                R.id.buttonreturnToMainMenuFromBlog);
        newBlockButton.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), MainMenu.class);
        startActivity(intent);
    }

    private void firebaseSearch(String searchtext) {
        String query = searchtext.toLowerCase();
        Query firebaseQuery = blogRef.orderByChild("type").startAt(query).endAt("\uf8ff");

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Blogs>()
                        .setQuery(firebaseQuery, Blogs.class)
                        .build();

        FirebaseRecyclerAdapter<Blogs, BlogsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Blogs, BlogsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull BlogsViewHolder holder, int position, @NonNull Blogs model) {
                String blogIDs = getRef(position).getKey();

                blogRef.child(blogIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("image"))
                        {
                            String blog_image = snapshot.child("image").getValue().toString();
                            String blog_name = snapshot.child("name").getValue().toString();
                            String blog_type = snapshot.child("type").getValue().toString();

                            holder.blogName.setText(blog_name);
                            holder.blogType.setText(blog_type);
                            Picasso.get().load(blog_image).placeholder(R.drawable.profile_image).into(holder.blogImage);


                        }
                        else {
                            String blog_name = snapshot.child("name").getValue().toString();
                            String blog_type = snapshot.child("type").getValue().toString();
                            holder.blogName.setText(blog_name);
                            holder.blogType.setText(blog_type);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent = new Intent(getActivity(), BlogProfile.class);
                        profileIntent.putExtra("blogIDs",blogIDs);
                        startActivity(profileIntent);
                    }
                });


            }
            @NonNull
            @Override
            public BlogsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bloglistlayout,parent,false);
                BlogsViewHolder viewHolder= new BlogsViewHolder(view);
                return viewHolder;

            }



        };
        myBlogList.setAdapter(adapter);
        adapter.startListening();


    }

        @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Blogs>()
                        .setQuery(blogRef, Blogs.class)
                        .build();

        FirebaseRecyclerAdapter<Blogs, BlogsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Blogs, BlogsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull BlogsViewHolder holder, int position, @NonNull Blogs model) {
                String blogIDs = getRef(position).getKey();

                blogRef.child(blogIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("image"))
                        {
                            String blog_image = snapshot.child("image").getValue().toString();
                            String blog_name = snapshot.child("name").getValue().toString();
                            String blog_type = snapshot.child("type").getValue().toString();

                            holder.blogName.setText(blog_name);
                            holder.blogType.setText(blog_type);
                            Picasso.get().load(blog_image).placeholder(R.drawable.profile_image).into(holder.blogImage);


                        }
                        else {
                            String blog_name = snapshot.child("name").getValue().toString();
                            String blog_type = snapshot.child("type").getValue().toString();
                            holder.blogName.setText(blog_name);
                            holder.blogType.setText(blog_type);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent = new Intent(getActivity(), BlogProfile.class);
                        profileIntent.putExtra("blogIDs",blogIDs);
                        startActivity(profileIntent);
                    }
                });


            }
            @NonNull
            @Override
            public BlogsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bloglistlayout,parent,false);
                BlogsViewHolder viewHolder= new BlogsViewHolder(view);
                return viewHolder;

            }



        };
        myBlogList.setAdapter(adapter);
        adapter.startListening();


    }

    public static class BlogsViewHolder extends RecyclerView.ViewHolder
    {
        TextView blogName,blogType;
        CircleImageView blogImage;

        public BlogsViewHolder(@NonNull View itemView) {
            super(itemView);


            blogName = itemView.findViewById(R.id.blog_name);
            blogType = itemView.findViewById(R.id.blog_type);
            blogImage = itemView.findViewById(R.id.blog_image);



        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu_blog,menu);
        MenuItem item = menu.findItem(R.id.search_firebase_blogs);
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

    private void moveToNewActivity () {

        Intent i = new Intent(getActivity(), MainMenu.class);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);

    }

}
package com.example.instagramclone.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramclone.Adapter.PostAdapter;
import com.example.instagramclone.Model.Posts;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Posts> postsList;

    private List<String> followingList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewPosts = view.findViewById(R.id.recycler_view_posts);
        recyclerViewPosts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);
        postsList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(),postsList);
        recyclerViewPosts.setAdapter(postAdapter);

        followingList = new ArrayList<>();
        checkFollowingUsers();

        return view;
    }

    private void checkFollowingUsers() {
        FirebaseDatabase.getInstance().getReference().child("follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followingList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    followingList.add(snapshot1.getKey());
                }

                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readPosts() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postsList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Posts posts = snapshot1.getValue(Posts.class);

                    for (String id : followingList){
                        if(posts.getPublisher().equals(id)){
                            postsList.add(posts);
                        }
                    }

                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
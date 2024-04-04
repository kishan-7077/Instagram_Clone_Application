package com.example.instagramclone.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context nContext;
    private List<User> musers;
    private boolean isFragment;
    private FirebaseUser firebaseUser;

    public UserAdapter(Context nContext, List<User> musers, boolean isFragment) {
        this.nContext = nContext;
        this.musers = musers;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nContext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        User user = musers.get(position);
        holder.follow.setVisibility(View.VISIBLE);
        holder.follow.setTextColor(Color.WHITE);

        holder.username.setText(user.getUsername());
        holder.name.setText(user.getName());

        Picasso.get().load(user.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);

        isFollowed(user.getId(),holder.follow);

        if (user.getId().equals(firebaseUser.getUid()))
            holder.follow.setVisibility(View.GONE);

    }

    private void isFollowed(String id, Button follow) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(id).exists())
                    follow.setText(R.string.following);

                else
                    follow.setText(R.string.follow);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return musers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imageProfile;
        public TextView username;
        public TextView name;
        public Button follow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.username);
            name = itemView.findViewById(R.id.full_name);
            follow = itemView.findViewById(R.id.btn_follow);



        }
    }
}

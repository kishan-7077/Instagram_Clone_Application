package com.example.instagramclone.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.Model.Posts;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.hendraanggrian.appcompat.socialview.widget.SocialTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Viewholder>{
    private Context mContext;
    private List<Posts> mPosts;

    private final FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Posts> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder,final int position) {


        Posts post = mPosts.get(position);
        Picasso.get().load(post.getImageUrl()).into(holder.postImage);

        holder.description.setText(post.getDescription());


        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPublisher()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null){
                    String imageUrl = user.getImageurl();
                    if(imageUrl.equals("default")){
                        holder.imageProfile.setImageResource(R.drawable.defaultprofile);
                    } else{
                        Picasso.get().load(imageUrl).into(holder.imageProfile);
                    }

                    holder.username.setText(user.getUsername());
                    holder.author.setText(user.getName());
                } else {
                    Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
                    holder.imageProfile.setImageResource(R.drawable.defaultprofile);
                    holder.username.setText("Unknown User");
                    holder.author.setText("Unknown");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        isLiked(post.getPostId(), holder.like);
        noOfLikes(post.getPostId(), holder.noOfLikes);

//        holder.like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(holder.like.getTag().equals("Like")){
//
//                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId())
//                            .child(firebaseUser.getUid()).setValue(true);
//                }else{
//                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId())
//                            .child(firebaseUser.getUid()).removeValue();
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        public ImageView imageProfile;
        public ImageView postImage;
        public ImageView like;
        public ImageView comment;
        public ImageView save;
        public ImageView more;
        public TextView username;
        public TextView noOfLikes;
        public TextView author;
        public TextView noOfComments;
        SocialTextView description;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.profile_image);
            postImage = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            more = itemView.findViewById(R.id.more);

            username = itemView.findViewById(R.id.username);
            noOfComments = itemView.findViewById(R.id.no_of_comments);
            noOfLikes = itemView.findViewById(R.id.no_of_likes);
            author = itemView.findViewById(R.id.author);
            description = itemView.findViewById(R.id.description);


        }
    }

    private void isLiked(String postId, ImageView imageView){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser1 = mAuth.getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser1.getUid()).exists()){
                    imageView.setImageResource(R.drawable.liked);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void noOfLikes(String postId,TextView text){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postId);

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText(dataSnapshot.getChildrenCount() + " likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

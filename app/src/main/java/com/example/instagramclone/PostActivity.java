package com.example.instagramclone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;

import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.instagramclone.Fragments.HomeFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import com.google.firebase.storage.UploadTask;
import com.hendraanggrian.appcompat.socialview.widget.SocialAutoCompleteTextView;

import java.util.HashMap;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private Uri imageUri;
    private String imageUrl;
    private TextView post;
    private ImageView close;
    private ImageView imageAdded;
    SocialAutoCompleteTextView description;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        post = findViewById(R.id.post);
        close = findViewById(R.id.close);
        imageAdded = findViewById(R.id.image_added);
        description = findViewById(R.id.description);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.progress_dialogue_box);
        dialog = builder.create();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        imageUri = uri;
                        imageAdded.setImageURI(uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                .build());

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

    }

    private void upload () {

        if (imageUri != null) {
            setDialog(true);
            StorageReference filePath = FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis() + "." + getFileExtention(imageUri));

            StorageTask<UploadTask.TaskSnapshot> uploadTask = filePath.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                    String postId = ref.push().getKey();

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("postid", postId);
                    map.put("imageUrl", imageUrl);
                    map.put("description", description.getText().toString());
                    map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                    ref.child(postId).setValue(map);

                    DatabaseReference mHashTagRef = FirebaseDatabase.getInstance().getReference().child("Hashtags");
                    List<String> hashtags = description.getHashtags();
                    if (!hashtags.isEmpty()) {
                        for (String tag : hashtags) {
                            map.clear();

                            map.put("tag", tag.toLowerCase());
                            map.put("postID", postId);

                            mHashTagRef.child(tag.toLowerCase()).setValue(map);
                        }
                    }
                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    imageAdded.setImageURI(imageUri);
                }
            });
        } else {
            Toast.makeText(this, "No image was selected", Toast.LENGTH_SHORT).show();
        }
    }
    private String getFileExtention (Uri uri){
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }

    private void setDialog ( boolean show){
        if (show) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }



}
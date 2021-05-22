package com.example.curetogether.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curetogether.R;
import com.example.curetogether.adapters.CommentAdapter;
import com.example.curetogether.model.Post;
import com.example.curetogether.utility.CustomDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private EditText text;
    private Button post;
    private CustomDialog dialog;
    private CommentAdapter adapter;
    private List<Post> comments = new ArrayList<>();
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        changeStatusBarColor();
        postId = getIntent().getStringExtra("id");
        adapter = new CommentAdapter(this, comments);
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        text = findViewById(R.id.text);
        post = findViewById(R.id.post);
        dialog = new CustomDialog(this);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePost();
            }
        });
        fetchData();
    }

    private void fetchData() {
        FirebaseDatabase.getInstance()
                .getReference()
                .child("comment")
                .child(postId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                        if (snapshot.exists()) {
                            Post comment = snapshot.getValue(Post.class);
                            comments.add(0, comment);
                            adapter.notifyItemInserted(0);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    private void savePost() {
        if (text.getText() == null) {
            return;
        }
        if (text.getText().toString().trim().equals("")) {
            return;
        }
        dialog.start();
        getNameAndSave();
    }

    private void getNameAndSave() {
        FirebaseDatabase.getInstance()
                .getReference()
                .child("user")
                .child(FirebaseAuth.getInstance().getUid())
                .child("userName")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            savePostInDb(snapshot.getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(CommentActivity.this, "Oops! Something went Wrong", Toast.LENGTH_SHORT).show();
                        dialog.stop();
                    }
                });
    }

    private void savePostInDb(String name) {
        Post post = new Post(
                text.getText().toString().trim(),
                FirebaseAuth.getInstance().getUid(),
                name,
                System.currentTimeMillis());
        FirebaseDatabase.getInstance()
                .getReference()
                .child("comment")
                .child(postId)
                .push()
                .setValue(post)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(CommentActivity.this, "Oops! Something went Wrong", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CommentActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                            text.setText("");
                        }
                        dialog.stop();
                    }
                });
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.green));
        }
    }
}
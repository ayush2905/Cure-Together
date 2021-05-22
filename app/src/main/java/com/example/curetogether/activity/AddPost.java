package com.example.curetogether.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.curetogether.R;
import com.example.curetogether.model.Post;
import com.example.curetogether.utility.CustomDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AddPost extends AppCompatActivity {

    private Button post;
    private EditText text;
    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        changeStatusBarColor();
        text = findViewById(R.id.text);
        post = findViewById(R.id.post);
        dialog = new CustomDialog(this);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePost();
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
                        Toast.makeText(AddPost.this, "Oops! Something went Wrong", Toast.LENGTH_SHORT).show();
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
        post.setComments(0L);
        FirebaseDatabase.getInstance()
                .getReference()
                .child("post")
                .push()
                .setValue(post)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(AddPost.this, "Oops! Something went Wrong", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddPost.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
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
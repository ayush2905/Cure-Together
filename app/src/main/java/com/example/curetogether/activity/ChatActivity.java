package com.example.curetogether.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.curetogether.R;
import com.example.curetogether.adapters.ChatAdapter;
import com.example.curetogether.model.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private Intent mIntent;
    private CircleImageView dp;
    private TextView name, lastSeen;
    private EditText msg;
    private FirebaseDatabase database;
    private String _name;
    private String uid;
    private String uri;
    private String chatId;
    private ChatAdapter adapter;
    private final List<Messages> messages = new ArrayList<>();
    private RecyclerView recycler;
    private boolean dontMakeOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setSupportActionBar(findViewById(R.id.toolbar));
        changeStatusBarColor();
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        setReferences();
        extractIntentData();
        setInfo();
        createChatIdIfNotCreated();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.green));
        }
    }

    private void extractIntentData() {
        _name = mIntent.getStringExtra("name");
        uid = mIntent.getStringExtra("id");
        uri = mIntent.getStringExtra("uri");
    }

    private void createChatIdIfNotCreated() {
        DatabaseReference reference =
                database.getReference()
                        .child("user")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("chat");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (Objects.equals(childSnapshot.getKey(), uid)) {
                                chatId = (String) childSnapshot.getValue();
                                fetchChat();
                                return;
                            }
                        }
                    }
                }
                createId();
            }

            private void createId() {
                DatabaseReference reference = database.getReference()
                        .child("chat")
                        .push();
                reference.setValue(true);
                chatId = reference.getKey();
                database.getReference()
                        .child("user")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("chat")
                        .child(uid)
                        .setValue(chatId);
                database.getReference()
                        .child("user")
                        .child(uid)
                        .child("chat")
                        .child(FirebaseAuth.getInstance().getUid())
                        .setValue(chatId);
                fetchChat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchChat() {
        if (chatId == null) {
            Toast.makeText(this, "Some error occurred", Toast.LENGTH_LONG).show();
            return;
        }
        DatabaseReference reference = database.getReference()
                .child("chat")
                .child(chatId);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    String sender = null, msg = null;
                    Long time = null;
                    Long type = null;
                    boolean nullExits = false;
                    if (dataSnapshot.child("sender").exists()) {
                        sender = Objects.requireNonNull(dataSnapshot.child("sender").getValue()).toString();
                    } else {
                        nullExits = true;
                    }
                    if (dataSnapshot.child("message").exists()) {
                        msg = Objects.requireNonNull(dataSnapshot.child("message").getValue()).toString();
                    } else {
                        nullExits = true;
                    }
                    if (dataSnapshot.child("time").exists()) {
                        time = (Long) dataSnapshot.child("time").getValue();
                    } else {
                        nullExits = true;
                    }
                    if (dataSnapshot.child("type").exists()) {
                        type = (Long) dataSnapshot.child("type").getValue();
                    } else {
                        nullExits = true;
                    }
                    if (!nullExits) {
                        messages.add(new Messages(sender, msg, type, time));
                        adapter.notifyItemInserted(messages.size() - 1);
                        recycler.scrollToPosition(messages.size() - 1);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setReferences() {
        database = FirebaseDatabase.getInstance();
        mIntent = getIntent();
        dp = findViewById(R.id.dp);
//        LinearLayout nameAndLastSeen = findViewById(R.id.nameAndLastSeen);
//        nameAndLastSeen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDescription();
//            }
//        });
        lastSeen = findViewById(R.id.lastSeen);
        name = findViewById(R.id.name);
        lastSeen.setVisibility(View.GONE);
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(this, messages);
        recycler.setAdapter(adapter);
        msg = findViewById(R.id.message);
        ImageButton send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String message = msg.getText().toString();
        msg.setText("");
        if (!message.trim().equals("")) {
            HashMap<String, Object> msgInfo = new HashMap<>();
            msgInfo.put("sender", FirebaseAuth.getInstance().getUid());
            msgInfo.put("message", message);
            msgInfo.put("type", 0);
            msgInfo.put("time", System.currentTimeMillis());
            database.getReference().child("chat").child(chatId).push().updateChildren(msgInfo);
            database.getReference().child("notification").push().setValue(uid);
        }
    }

    private void setInfo() {
        name.setText(_name);
    }
}
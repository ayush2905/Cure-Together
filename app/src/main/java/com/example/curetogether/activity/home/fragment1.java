package com.example.curetogether.activity.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curetogether.R;
import com.example.curetogether.adapters.ChatListAdapter;
import com.example.curetogether.model.People;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class fragment1 extends Fragment {

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private FirebaseDatabase database;
    private final List<People> peopleList = new ArrayList<>();

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2_layout, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return view;
        initialSetup();
        return view;
    }

    private void initialSetup() {
        if (FirebaseAuth.getInstance().getUid() == null)
            return;
        database = FirebaseDatabase.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatListAdapter(getContext(), peopleList);
        recyclerView.setAdapter(adapter);
        getPeopleList();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recyclerView.getAdapter() == null) {
            initialSetup();
        }
    }

    private void getPeopleList() {
        database.getReference()
                .child("user")
                .child(FirebaseAuth.getInstance().getUid())
                .child("chat")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                        People person = new People();
                        if (snapshot.exists()) {
                            String uid = snapshot.getKey();
                            person.setId(uid);
                            String chatId = Objects.requireNonNull(snapshot.getValue()).toString();
                            person.setChatId(chatId);
                            getNameAndLastMessage(person, chatId);
                        }
                    }

                    private void getNameAndLastMessage(People person, String chatId) {
                        database.getReference().child("chat")
                                .child(chatId)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            if (snapshot.hasChildren()) {
                                                if (person.getName() == null)
                                                    getName(person);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                    }

                    private void getName(People person) {
                        database.getReference().child("user")
                                .child(person.getId())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            person.setName(snapshot.child("userName").getValue().toString());
                                            addToList(person);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
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

    private void addToList(People person) {
        if (person.getId().equals(FirebaseAuth.getInstance().getUid()))
            return;
        peopleList.add(person);
        adapter.notifyItemInserted(peopleList.size() - 1);
    }
}

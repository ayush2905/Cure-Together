package com.example.curetogether.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curetogether.R;
import com.example.curetogether.adapters.FindPeopleAdapter;
import com.example.curetogether.model.People;
import com.example.curetogether.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class fragment2 extends Fragment {

    private RecyclerView recyclerView;
    private FindPeopleAdapter adapter;
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
        adapter = new FindPeopleAdapter(getContext(), peopleList);
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
                .orderByChild("userRecovered")
                .equalTo(HomeActivity.sufferingFrom)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                        People person = new People();
                        Log.i("DATA", "IN METHOD");
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);
                            person.setId(user.getUserId());
                            Log.i("DATA", person.getId());
                            person.setName(user.getUserName());
                            Log.i("DATA", person.getName());
                            addToList(person);
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

    private void addToList(People person) {
        if (person.getId().equals(FirebaseAuth.getInstance().getUid()))
            return;
        peopleList.add(person);
        adapter.notifyItemInserted(peopleList.size() - 1);
    }
}

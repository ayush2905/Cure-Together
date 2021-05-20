package com.example.curetogether.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curetogether.ChatActivity;
import com.example.curetogether.R;
import com.example.curetogether.model.People;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.CustomVH> {

    private final Context context;
    private final List<People> peopleList;
    private final HashMap<String, String> lastMessage = new HashMap<>();
    private final HashMap<String, String> timeOfMessage = new HashMap<>();
    private final Set<String> lastMessageListenerSet = new HashSet<>();

    public ChatListAdapter(Context context, List<People> peopleList) {
        this.context = context;
        this.peopleList = peopleList;
    }

    @NonNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_chat_option, parent, false);
        return new CustomVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomVH holder, int position) {
        holder.name.setText(peopleList.get(position).getName());
        if (lastMessage.containsKey(peopleList.get(position).getChatId())) {
            holder.box.setVisibility(View.VISIBLE);
            holder.lastMsg.setVisibility(View.VISIBLE);
            holder.lastMsg.setText(lastMessage.get(peopleList.get(position).getChatId()));
        } else {
            holder.box.setVisibility(View.VISIBLE);
            fetchLastMessage(position);
            holder.lastMsg.setVisibility(View.GONE);
        }
        if (timeOfMessage.containsKey(peopleList.get(position).getChatId())) {
            holder.time.setVisibility(View.VISIBLE);
            holder.time.setText(timeOfMessage.get(peopleList.get(position).getChatId()));
        } else {
            fetchLastMessage(position);
            holder.time.setVisibility(View.GONE);
        }
    }

    private String getDate(Long value) {
        Date date = new Date(value);
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm MMM d", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {

        TextView name, time, lastMsg;
        LinearLayout box;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            lastMsg = itemView.findViewById(R.id.lastMsg);
            box = itemView.findViewById(R.id.box);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startChat(getAdapterPosition());
                }
            });
        }
    }

    private void fetchLastMessage(final int position) {
        if (lastMessageListenerSet.contains(peopleList.get(position).getChatId()))
            return;
        FirebaseDatabase.getInstance()
                .getReference()
                .child("chat")
                .child(peopleList.get(position).getChatId())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (dataSnapshot.exists()) {
                            lastMessage.put(peopleList.get(position).getChatId(), Objects.requireNonNull(dataSnapshot.child("message").getValue()).toString());
                            timeOfMessage.put(peopleList.get(position).getChatId(), getDate((Long) dataSnapshot.child("time").getValue()));
                            notifyItemChanged(position);
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
        lastMessageListenerSet.add(peopleList.get(position).getChatId());
    }

    private void startChat(final int adapterPosition) {
        final Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("name", peopleList.get(adapterPosition).getName());
        intent.putExtra("id", peopleList.get(adapterPosition).getId());
        /*if (dpUri.containsKey(contacts.get(adapterPosition).getNumber())) {
            intent.putExtra(RECEIVER_DP_URI, Objects.requireNonNull(dpUri.get(contacts.get(adapterPosition).getNumber())).toString());
        } else {
            intent.putExtra(RECEIVER_DP_URI, "");
        }*/
        context.startActivity(intent);
    }
}

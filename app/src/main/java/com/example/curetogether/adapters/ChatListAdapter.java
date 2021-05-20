package com.example.curetogether.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curetogether.R;
import com.example.curetogether.model.People;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.CustomVH> {

    private final Context context;
    private final List<People> peopleList;

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
        holder.lastMsg.setText("Recovered from " + "_DISEASE_");
        holder.time.setText(getDate(peopleList.get(position).getTime()));
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

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            lastMsg = itemView.findViewById(R.id.lastMsg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startChat(getAdapterPosition());
                }
            });
        }
    }

    private void startChat(final int adapterPosition) {
//        final Intent intent = new Intent(context, ChatActivity.class);
//        intent.putExtra(RECEIVER_NAME, contacts.get(adapterPosition).getName());
//        intent.putExtra(RECEIVER_NUMBER, contacts.get(adapterPosition).getNumber());
//        intent.putExtra(RECEIVER_UID, contacts.get(adapterPosition).getUid());
//        if (dpUri.containsKey(contacts.get(adapterPosition).getNumber())) {
//            intent.putExtra(RECEIVER_DP_URI, Objects.requireNonNull(dpUri.get(contacts.get(adapterPosition).getNumber())).toString());
//        } else {
//            intent.putExtra(RECEIVER_DP_URI, "");
//        }
//        context.startActivity(intent);
    }
}

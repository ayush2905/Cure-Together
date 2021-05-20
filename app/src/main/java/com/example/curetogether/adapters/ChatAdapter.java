package com.example.curetogether.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curetogether.model.Messages;
import com.example.curetogether.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.CustomVH> {

    Context context;
    List<Messages> messages;

    public ChatAdapter(Context context, List<Messages> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_view, parent, false);
        return new CustomVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomVH holder, int position) {
        TextView msg, time;
        if(messages.get(position).getSENDER().equals(FirebaseAuth.getInstance().getUid())) {
            holder.boxReceived.setVisibility(View.GONE);
            holder.boxSent.setVisibility(View.VISIBLE);
            msg = holder.msgSent;
            time = holder.timeSent;
        }
        else {
            holder.boxSent.setVisibility(View.GONE);
            holder.boxReceived.setVisibility(View.VISIBLE);
            msg = holder.msgReceived;
            time = holder.timeReceived;
        }
        msg.setText(messages.get(position).getMESSAGE());
        time.setText(getTime(messages.get(position).getTIME()));
    }

    private String getTime(Long time) {
        TimeZone timeZone = TimeZone.getDefault();
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm", Locale.getDefault());
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {

        LinearLayout boxSent, boxReceived;
        TextView msgSent, timeSent, msgReceived, timeReceived;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            boxSent = itemView.findViewById(R.id.boxSent);
            msgSent = itemView.findViewById(R.id.messageSent);
            timeSent = itemView.findViewById(R.id.timeSent);
            boxReceived = itemView.findViewById(R.id.boxReceived);
            msgReceived = itemView.findViewById(R.id.messageReceived);
            timeReceived = itemView.findViewById(R.id.timeReceived);
        }
    }
}

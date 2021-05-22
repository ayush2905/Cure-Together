package com.example.curetogether.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curetogether.R;
import com.example.curetogether.activity.ChatActivity;
import com.example.curetogether.model.Post;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CustomVH> {

    private final Context context;
    private final List<Post> comments;

    public CommentAdapter(Context context, List<Post> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @NotNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CustomVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CommentAdapter.CustomVH holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView time;
        private final TextView text;
        private final LinearLayout layout;

        public CustomVH(@NonNull @NotNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            text = itemView.findViewById(R.id.text);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startChat(getAdapterPosition());
                }
            });
        }

        public void bindData(int pos) {
            name.setText(comments.get(pos).getName());
            time.setText(getDate(comments.get(pos).getTime()));
            text.setText(comments.get(pos).getText());
        }

    }

    private String getDate(Long value) {
        Date date = new Date(value);
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm MMM d yyyy", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }

    private void startChat(int adapterPosition) {
        if (comments.get(adapterPosition).getId().equals(FirebaseAuth.getInstance().getUid())) {
            Toast.makeText(context, "It's You", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("name", comments.get(adapterPosition).getName());
        intent.putExtra("id", comments.get(adapterPosition).getId());
        context.startActivity(intent);
    }
}

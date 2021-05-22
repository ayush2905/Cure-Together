package com.example.curetogether.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curetogether.R;
import com.example.curetogether.activity.ChatActivity;
import com.example.curetogether.activity.CommentActivity;
import com.example.curetogether.model.Post;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.CustomVH> {

    private Context context;
    private List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @NotNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_post, parent, false);
        return new CustomVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostAdapter.CustomVH holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {

        private LinearLayout layout;
        private Button button;
        private TextView name, time, text;

        public CustomVH(@NonNull @NotNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            button = itemView.findViewById(R.id.comment);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            text = itemView.findViewById(R.id.text);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startChat(getAdapterPosition());
                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seeComments(getAdapterPosition());
                }
            });
        }

        public void bindData(int pos) {
            name.setText(posts.get(pos).getName());
            time.setText(getDate(posts.get(pos).getTime()));
            text.setText(posts.get(pos).getText());
        }
    }

    private void seeComments(int adapterPosition) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra("id", posts.get(adapterPosition).getPostId());
        context.startActivity(intent);
    }

    private String getDate(Long value) {
        Date date = new Date(value);
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm MMM d yyyy", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }

    private void startChat(int adapterPosition) {
        if (posts.get(adapterPosition).getId().equals(FirebaseAuth.getInstance().getUid())) {
            Toast.makeText(context, "It's You", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("name", posts.get(adapterPosition).getName());
        intent.putExtra("id", posts.get(adapterPosition).getId());
        context.startActivity(intent);
    }
}

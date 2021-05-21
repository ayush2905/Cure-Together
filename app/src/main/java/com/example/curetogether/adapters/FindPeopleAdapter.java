package com.example.curetogether.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curetogether.activity.ChatActivity;
import com.example.curetogether.R;
import com.example.curetogether.activity.home.HomeActivity;
import com.example.curetogether.model.People;

import java.util.List;

public class FindPeopleAdapter extends RecyclerView.Adapter<FindPeopleAdapter.CustomVH> {

    private final Context context;
    private final List<People> peopleList;

    public FindPeopleAdapter(Context context, List<People> peopleList) {
        this.context = context;
        this.peopleList = peopleList;
    }

    @NonNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_people, parent, false);
        return new CustomVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomVH holder, int position) {
        holder.name.setText(peopleList.get(position).getName());
        holder.disease.setText("Recovered from " + HomeActivity.sufferingFrom);
    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {

        TextView name, disease;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            disease = itemView.findViewById(R.id.disease);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startChat(getAdapterPosition());
                }
            });
        }
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

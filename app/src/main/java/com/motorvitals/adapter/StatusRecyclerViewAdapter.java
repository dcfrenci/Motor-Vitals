package com.motorvitals.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.motorvitals.R;
import com.motorvitals.classes.Motorcycle;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StatusRecyclerViewAdapter extends RecyclerView.Adapter<StatusRecyclerViewAdapter.StatusViewHolder> {
    private final ArrayList<Motorcycle> motorcycles;
    private final Fragment fragment;

    public StatusRecyclerViewAdapter(ArrayList<Motorcycle> motorcycles, Fragment fragment) {
        this.motorcycles = motorcycles;
        this.fragment = fragment;
    }

    @NonNull
    @NotNull
    @Override
    public StatusRecyclerViewAdapter.StatusViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.status_row_recycler_view, viewGroup, false);
        return new StatusRecyclerViewAdapter.StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StatusRecyclerViewAdapter.StatusViewHolder holder, int position) {
        holder.setTitle(motorcycles.get(position).getNameView(holder.getTitle()));
        holder.setRecyclerView(motorcycles.get(position).getElementsWithStatusRecyclerView(holder.getRecyclerView(), fragment, position));
    }

    @Override
    public int getItemCount() {
        return motorcycles.size();
    }

    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private RecyclerView recyclerView;

        public StatusViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.status_motorcycle_title);
            recyclerView = itemView.findViewById(R.id.status_elem_recycler_view);
        }

        public void setTitle(TextView title) {
            this.title = title;
        }

        public void setRecyclerView(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        public TextView getTitle() {
            return title;
        }

        public RecyclerView getRecyclerView() {
            return recyclerView;
        }
    }
}

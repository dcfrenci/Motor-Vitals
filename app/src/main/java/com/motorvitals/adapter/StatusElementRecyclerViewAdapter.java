package com.motorvitals.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.motorvitals.R;
import com.motorvitals.classes.Element;
import com.motorvitals.classes.ElementList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StatusElementRecyclerViewAdapter extends RecyclerView.Adapter<StatusElementRecyclerViewAdapter.StatusViewHolder> {
    private final ArrayList<Element> elementStatus;
    private final ArrayList<ElementList> elementLists;
    private final Integer motorcycleIndex;

    public StatusElementRecyclerViewAdapter(ArrayList<Element> elementStatus, ArrayList<ElementList> elementLists, Integer motorcycleIndex) {
        this.elementStatus = elementStatus;
        this.elementLists = elementLists;
        this.motorcycleIndex = motorcycleIndex;
    }

    @NonNull
    @NotNull
    @Override
    public StatusElementRecyclerViewAdapter.StatusViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.status_element_row_recycler_view, viewGroup, false);
        return new StatusElementRecyclerViewAdapter.StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StatusElementRecyclerViewAdapter.StatusViewHolder holder, int position) {
        holder.setTitle(elementStatus.get(position).getTextView(holder.getTitle()));
//        holder.setDayInterval(elementStatus.get(position).getDaysInterval(holder.getDayInterval()));
        holder.setKmInterval(elementStatus.get(position).getKmInterval(holder.getKmInterval()));
    }

    @Override
    public int getItemCount() {
        return elementStatus.size();
    }

    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView dayInterval;
        private ImageView dayImage;
        private TextView kmInterval;
        private ImageView kmImage;


        public StatusViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.status_elem_title);
            dayInterval = itemView.findViewById(R.id.status_elem_day);
            dayImage = itemView.findViewById(R.id.status_elem_day_image);
            kmInterval = itemView.findViewById(R.id.status_elem_km);
            kmImage = itemView.findViewById(R.id.status_elem_km_image);
        }

        public TextView getTitle() {
            return title;
        }

        public void setTitle(TextView title) {
            this.title = title;
        }

        public TextView getDayInterval() {
            return dayInterval;
        }

        public void setDayInterval(TextView dayInterval) {
            this.dayInterval = dayInterval;
        }

        public TextView getKmInterval() {
            return kmInterval;
        }

        public void setKmInterval(TextView kmInterval) {
            this.kmInterval = kmInterval;
        }
    }
}

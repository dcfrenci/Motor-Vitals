package com.motorvitals.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.motorvitals.R;
import com.motorvitals.classes.Motorcycle;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MotorcycleRecycleViewAdapter extends RecyclerView.Adapter<MotorcycleRecycleViewAdapter.MotorcycleViewHolder> {
    private Fragment fragment;
    private ArrayList<Motorcycle> motorcycles;
    public MotorcycleRecycleViewAdapter(Fragment fragment, ArrayList<Motorcycle> motorcycles) {
        this.fragment = fragment;
        this.motorcycles = motorcycles;
    }

    @NonNull
    @Override
    public MotorcycleRecycleViewAdapter.MotorcycleViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int position) {
        // This is where you inflate the layout (giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.motorclycle_row_recycler_view, viewGroup, false);
        return new MotorcycleRecycleViewAdapter.MotorcycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull MotorcycleRecycleViewAdapter.MotorcycleViewHolder holder, int position) {
        // Assigning values to the views we created in the recycler_view_row layout file based on the position of the view
        holder.setImageView(motorcycles.get(position).getPhotoView(holder.getImageView()));
        holder.setTextTitle(motorcycles.get(position).getNameView(holder.getTextTitle()));
        holder.setTextDescription(motorcycles.get(position).getDescriptionView(holder.getTextDescription()));
    }

    @Override
    public int getItemCount() {
        // The recycler view just wants to know the number of items you want displayed
        return motorcycles.size();
    }

    public static class MotorcycleViewHolder extends RecyclerView.ViewHolder {
        // Grabbing the views from our recycler_view_row layout file
        private ImageView imageView;
        private TextView textTitle;
        private TextView textDescription;
        public MotorcycleViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cardMotorcycleImageView);
            textTitle = itemView.findViewById(R.id.titleTextView);
            textDescription = itemView.findViewById(R.id.descriptionTextView);
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public void setTextTitle(TextView textTitle) {
            this.textTitle = textTitle;
        }

        public void setTextDescription(TextView textDescription) {
            this.textDescription = textDescription;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getTextTitle() {
            return textTitle;
        }

        public TextView getTextDescription() {
            return textDescription;
        }
    }
}

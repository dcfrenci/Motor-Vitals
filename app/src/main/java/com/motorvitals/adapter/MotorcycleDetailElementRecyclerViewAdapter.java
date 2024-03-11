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
import com.motorvitals.classes.Element;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MotorcycleDetailElementRecyclerViewAdapter extends RecyclerView.Adapter<MotorcycleDetailElementRecyclerViewAdapter.MotorcycleViewHolder> {
    private Fragment fragment;
    private ArrayList<Element> elementArrayList;

    public MotorcycleDetailElementRecyclerViewAdapter(Fragment fragment, ArrayList<Element> elementArrayList) {
        this.fragment = fragment;
        this.elementArrayList = elementArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public MotorcycleDetailElementRecyclerViewAdapter.MotorcycleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.motorcycle_detail_element_row_recycler_view, viewGroup, false);
        return new MotorcycleDetailElementRecyclerViewAdapter.MotorcycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MotorcycleDetailElementRecyclerViewAdapter.MotorcycleViewHolder holder, int position) {
        holder.setImageViewHolder(elementArrayList.get(position).getImageView(holder.getImageViewHolder()));
        holder.setTitleViewHolder(elementArrayList.get(position).getTextView(holder.getTitleViewHolder()));
    }

    @Override
    public int getItemCount() {
        return elementArrayList.size();
    }

    public static class MotorcycleViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView titleView;

        public MotorcycleViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.card_element_image);
            titleView = itemView.findViewById(R.id.card_element_title);
        }

        public ImageView getImageViewHolder() {
            return imageView;
        }

        public void setImageViewHolder(ImageView imageView) {
            this.imageView = imageView;
        }

        public TextView getTitleViewHolder() {
            return titleView;
        }

        public void setTitleViewHolder(TextView titleView) {
            this.titleView = titleView;
        }
    }
}

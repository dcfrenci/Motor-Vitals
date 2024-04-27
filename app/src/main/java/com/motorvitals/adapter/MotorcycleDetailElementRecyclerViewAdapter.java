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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MotorcycleDetailElementRecyclerViewAdapter extends RecyclerView.Adapter<MotorcycleDetailElementRecyclerViewAdapter.MotorcycleViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    private final ArrayList<Element> elementArrayList;
    private final int position;

    public MotorcycleDetailElementRecyclerViewAdapter(RecyclerViewInterface recyclerViewInterface, ArrayList<Element> elementArrayList, int position) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.elementArrayList = elementArrayList;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @NonNull
    @NotNull
    @Override
    public MotorcycleDetailElementRecyclerViewAdapter.MotorcycleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.motorcycle_detail_element_row_recycler_view, viewGroup, false);
        return new MotorcycleDetailElementRecyclerViewAdapter.MotorcycleViewHolder(view, recyclerViewInterface, getPosition());
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

        public MotorcycleViewHolder(@NonNull @NotNull View itemView, RecyclerViewInterface recyclerViewInterface, int position) {
            super(itemView);
            imageView = itemView.findViewById(R.id.card_element_image);
            titleView = itemView.findViewById(R.id.card_element_title);

            itemView.setOnClickListener(click -> {
                if (recyclerViewInterface != null) {
                    int positionElement = getAdapterPosition();
                    if (positionElement != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onCardClick(position, positionElement);
                    }
                }
            });

            itemView.setOnLongClickListener(longClick -> {
                if (itemView.findViewById(R.id.card_element_delete).getVisibility() == View.GONE) {
                    itemView.findViewById(R.id.card_element_delete).setVisibility(View.VISIBLE);
                } else {
                    itemView.findViewById(R.id.card_element_delete).setVisibility(View.GONE);
                }
                return true;
            });

            itemView.findViewById(R.id.card_element_delete).setOnClickListener(click -> {
                if (recyclerViewInterface != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    recyclerViewInterface.onCardDelete(position, getAdapterPosition());
                }
            });
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

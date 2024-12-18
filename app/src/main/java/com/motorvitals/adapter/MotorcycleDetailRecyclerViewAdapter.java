package com.motorvitals.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.motorvitals.R;
import com.motorvitals.classes.ElementList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MotorcycleDetailRecyclerViewAdapter extends RecyclerView.Adapter<MotorcycleDetailRecyclerViewAdapter.MotorcycleViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private final Fragment fragment;
    private final ArrayList<ElementList> multipleElementList;

    public MotorcycleDetailRecyclerViewAdapter(RecyclerViewInterface recyclerViewInterface, Fragment fragment, ArrayList<ElementList> multipleElementList) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.fragment = fragment;
        this.multipleElementList = multipleElementList;
    }

    @NonNull
    @NotNull
    @Override
    public MotorcycleDetailRecyclerViewAdapter.MotorcycleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.motorcycle_detail_row_recycler_view, viewGroup, false);
        return new MotorcycleDetailRecyclerViewAdapter.MotorcycleViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MotorcycleDetailRecyclerViewAdapter.MotorcycleViewHolder holder, int position) {
        holder.setListNameText(multipleElementList.get(position).getTitleView(holder.getListNameText()));
        holder.setRecyclerViewElements(multipleElementList.get(position).getElementsRecyclerView(holder.getRecyclerViewElements(), fragment, holder.getAdapterPosition(), recyclerViewInterface));
    }

    @Override
    public int getItemCount() {
        return multipleElementList.size();
    }
    public static class MotorcycleViewHolder extends RecyclerView.ViewHolder {
        private TextView listNameText;
        private final ImageView dropDownImage;
        private RecyclerView recyclerViewElements;

        public MotorcycleViewHolder(@NonNull @NotNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            listNameText = itemView.findViewById(R.id.list_title_card);
            recyclerViewElements = itemView.findViewById(R.id.list_element_cards);
            dropDownImage = itemView.findViewById(R.id.list_button_image_card);
            Button addElementButton = itemView.findViewById(R.id.list_add_element_button);

            dropDownImage.setOnClickListener(click -> {
                if (itemView.findViewById(R.id.list_container_layout).getVisibility() == View.GONE) {
                    itemView.findViewById(R.id.list_container_layout).setVisibility(View.VISIBLE);
                    dropDownImage.setRotation(180);
                } else {
                    itemView.findViewById(R.id.list_container_layout).setVisibility(View.GONE);
                    dropDownImage.setRotation(0);
                }
            });
            addElementButton.setOnClickListener(click -> {
                if (recyclerViewInterface != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onCardClick(position, RecyclerView.NO_POSITION);
                    }
                }
            });
            itemView.setOnLongClickListener(longClick -> {
                if (itemView.findViewById(R.id.list_delete_image).getVisibility() == View.GONE) {
                    itemView.findViewById(R.id.list_delete_image).setVisibility(View.VISIBLE);
                } else {
                    itemView.findViewById(R.id.list_delete_image).setVisibility(View.GONE);
                }
                return true;
            });
            itemView.findViewById(R.id.list_delete_image).setOnClickListener(click -> {
                if (recyclerViewInterface != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        new AlertDialog.Builder(click.getContext())
                                .setCancelable(true)
                                .setTitle("Are you sure to delete this list ?")
                                .setMessage("If you confirm the list all the contained elements will be deleted")
                                .setPositiveButton("Confirm", (dialog, which) -> recyclerViewInterface.onCardDelete(position, RecyclerView.NO_POSITION))
                                .setNegativeButton(android.R.string.cancel, (dialog, which) -> itemView.findViewById(R.id.list_delete_image).setVisibility(View.GONE))
                                .show();
                    }
                }
            });
        }

        public void setListNameText(TextView listNameText) {
            this.listNameText = listNameText;
        }

        public void setRecyclerViewElements(RecyclerView recyclerViewElements) {
            this.recyclerViewElements = recyclerViewElements;
        }

        public TextView getListNameText() {
            return listNameText;
        }

        public RecyclerView getRecyclerViewElements() {
            return recyclerViewElements;
        }
    }
}

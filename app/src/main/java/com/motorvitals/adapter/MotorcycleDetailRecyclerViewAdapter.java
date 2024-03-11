package com.motorvitals.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.motorvitals.R;
import com.motorvitals.classes.Element;
import com.motorvitals.classes.ElementList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MotorcycleDetailRecyclerViewAdapter extends RecyclerView.Adapter<MotorcycleDetailRecyclerViewAdapter.MotorcycleViewHolder> {
    private Fragment fragment;
    private ArrayList<ElementList> multipleElementList;

    public MotorcycleDetailRecyclerViewAdapter(Fragment fragment, ArrayList<ElementList> multipleElementList) {
        this.fragment = fragment;
        this.multipleElementList = multipleElementList;
    }

    @NonNull
    @NotNull
    @Override
    public MotorcycleDetailRecyclerViewAdapter.MotorcycleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.motorcycle_detail_row_recycler_view, viewGroup, false);
        return new MotorcycleDetailRecyclerViewAdapter.MotorcycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MotorcycleDetailRecyclerViewAdapter.MotorcycleViewHolder holder, int position) {
        holder.setListNameText(multipleElementList.get(position).getTitleView(holder.getListNameText()));
//        holder.setDropDownButton(multipleElementList.get(position).get);
//        holder.setRecyclerViewElements(multipleElementList.get(position).getElements());


    }

    @Override
    public int getItemCount() {
        return multipleElementList.size();
    }
    public static class MotorcycleViewHolder extends RecyclerView.ViewHolder {
        TextView listNameText;
        ImageButton dropDownButton;
        RecyclerView recyclerViewElements;

        public MotorcycleViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            listNameText = itemView.findViewById(R.id.list_title_card);
            dropDownButton = itemView.findViewById(R.id.list_button_image_card);
            recyclerViewElements = itemView.findViewById(R.id.list_element_cards);
        }

        public void setListNameText(TextView listNameText) {
            this.listNameText = listNameText;
        }

        /*public void setDropDownButton(ImageButton dropDownButton) {
            this.dropDownButton = dropDownButton;
        }

        public void setRecyclerViewElements(RecyclerView recyclerViewElements) {
            this.recyclerViewElements = recyclerViewElements;
        }*/

        public TextView getListNameText() {
            return listNameText;
        }

        /*public ImageButton getDropDownButton() {
            return dropDownButton;
        }

        public RecyclerView getRecyclerViewElements() {
            return recyclerViewElements;
        }*/
    }
}

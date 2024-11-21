package com.motorvitals.adapter;

import android.content.res.Resources;
import android.graphics.Color;
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
import com.motorvitals.classes.ElementList;
import com.motorvitals.classes.User;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class StatusElementRecyclerViewAdapter extends RecyclerView.Adapter<StatusElementRecyclerViewAdapter.StatusViewHolder> {
    private final Fragment fragment;
    private final ArrayList<Element> elementStatus;
    private final ArrayList<ElementList> elementLists;
    private final Integer motorcycleIndex;
    // TODO - Make the user not static and using the correct color selected in the profile fragment
    private static final User user = new User("Generic");

    public StatusElementRecyclerViewAdapter(Fragment fragment, ArrayList<Element> elementStatus, ArrayList<ElementList> elementLists, Integer motorcycleIndex) {
        this.fragment = fragment;
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
        return new StatusElementRecyclerViewAdapter.StatusViewHolder(view, fragment);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StatusElementRecyclerViewAdapter.StatusViewHolder holder, int position) {
        holder.setTitle(elementStatus.get(position).getTextView(holder.getTitle()));
        holder.setDayInterval(elementStatus.get(position).getDaysInterval(holder.getDayInterval()));
        holder.setKmInterval(elementStatus.get(position).getKmInterval(holder.getKmInterval()));

        holder.setImageStateColor(holder.getDayImage(), elementStatus.get(position).getDayInterval(), elementStatus.get(position).getNumberDays());
        holder.setImageStateColor(holder.getKmImage(), elementStatus.get(position).getKmInterval(), elementStatus.get(position).getNumberKm());
    }

    @Override
    public int getItemCount() {
        return elementStatus.size();
    }

    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        private final Fragment fragment;
        private TextView title;
        private TextView dayInterval;
        private ImageView dayImage;
        private TextView kmInterval;
        private ImageView kmImage;


        public StatusViewHolder(@NonNull @NotNull View itemView, Fragment fragment) {
            super(itemView);

            this.title = itemView.findViewById(R.id.status_elem_title);
            this.dayInterval = itemView.findViewById(R.id.status_elem_day);
            this.dayImage = itemView.findViewById(R.id.status_elem_day_image);
            this.kmInterval = itemView.findViewById(R.id.status_elem_km);
            this.kmImage = itemView.findViewById(R.id.status_elem_km_image);
            this.fragment = fragment;
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

        public ImageView getDayImage() {
            return dayImage;
        }

        public void setDayImage(ImageView dayImage) {
            this.dayImage = dayImage;
        }

        public ImageView getKmImage() {
            return kmImage;
        }

        public void setKmImage(ImageView kmImage) {
            this.kmImage = kmImage;
        }

        private void setImageStateColor(ImageView imageView, @NonNull HashMap<String, Integer> interval, int value) {
            imageView.setColorFilter(generateColor(value, interval).toArgb());
        }

        private Color generateColor(Integer value, @NonNull HashMap<String, Integer> interval) {
            Integer minValue = interval.get("min");
            Integer medValue = interval.get("med");
            Integer maxValue = interval.get("max");
            Color minColor = user.getColor("min");
            Color medColor = user.getColor("med");
            Color maxColor = user.getColor("max");

            if (value.compareTo(minValue) <= 0)
                return minColor;
            if (value.compareTo(medValue) == 0)
                return medColor;
            if (value.compareTo(maxValue) >= 0)
                return maxColor;

            if (value.compareTo(minValue) > 0 && value.compareTo(medValue) < 0) {
                return gradientColor(minColor, medColor);
            } else {
                return gradientColor(medColor, maxColor);
            }
        }

        private Color gradientColor(Color x, Color y) {
            double blending = 0.5;
            double inverse_blending = 1 - blending;
            int red = (int) (x.red() * blending + y.red() * inverse_blending);
            int green = (int) (x.green() * blending + y.green() * inverse_blending);
            int blue = (int) (x.blue() * blending + y.blue() * inverse_blending);
            return Color.valueOf(red, green, blue);
        }
    }
}

package com.example.wanderwheels;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VanAdapter extends RecyclerView.Adapter<VanAdapter.VanViewHolder> {
    private final List<Van> vanList;
    private Context context;

    public VanAdapter(List<Van> vanList) {
        this.vanList = vanList;
    }

    @NonNull
    @Override
    public VanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_van, parent, false);
        return new VanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VanViewHolder holder, int position) {
        Van van = vanList.get(position);
        holder.bind(van);
    }

    @Override
    public int getItemCount() {
        return vanList.size();
    }

    class VanViewHolder extends RecyclerView.ViewHolder {
        private final ImageView vanImage;
        private final TextView vanTag;
        private final TextView vanName;
        private final TextView vanDetails;
        private final TextView currentPrice;
        private final RatingBar ratingBar;
        private final TextView reviewCount;
        private final LinearLayout featuresContainer;
        private final Button viewButton;

        public VanViewHolder(@NonNull View itemView) {
            super(itemView);

            vanImage = itemView.findViewById(R.id.vanImage);
            vanTag = itemView.findViewById(R.id.vanTag);
            vanName = itemView.findViewById(R.id.vanName);
            vanDetails = itemView.findViewById(R.id.vanDetails);
            currentPrice = itemView.findViewById(R.id.currentPrice);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            reviewCount = itemView.findViewById(R.id.reviewCount);
            featuresContainer = itemView.findViewById(R.id.featuresContainer);
            viewButton = itemView.findViewById(R.id.viewButton);
        }

        public void bind(Van van) {
            vanImage.setImageResource(van.getImageResId());

            if (van.getTagText() != null && !van.getTagText().isEmpty()) {
                vanTag.setText(van.getTagText());
                vanTag.setBackgroundTintList(ContextCompat.getColorStateList(context, van.getTagColor()));
                vanTag.setVisibility(View.VISIBLE);
            } else {
                vanTag.setVisibility(View.GONE);
            }

            vanName.setText(van.getName());
            vanDetails.setText(context.getString(R.string.van_details_format,
                    van.getSleepCount(), van.getVanClass()));

            currentPrice.setText(context.getString(R.string.price_format, van.getCurrentPrice()));

            ratingBar.setRating(van.getRating());
            reviewCount.setText(context.getString(R.string.review_count_format,
                    van.getRating(), van.getReviewCount()));

            featuresContainer.removeAllViews();
            for (String feature : van.getFeatures()) {
                TextView featureView = (TextView) LayoutInflater.from(context)
                        .inflate(R.layout.item_feature, featuresContainer, false);
                featureView.setText(feature);
                featuresContainer.addView(featureView);
            }

            viewButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, BookingActivity.class);
                intent.putExtra("vanName", van.getName());
                intent.putExtra("vanPrice", van.getCurrentPrice());
                context.startActivity(intent);
            });
        }
    }
}
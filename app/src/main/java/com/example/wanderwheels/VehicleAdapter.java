package com.example.wanderwheels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    private List<Vehicle> fullVehicleList;
    private List<Vehicle> filteredVehicleList;

    public VehicleAdapter(List<Vehicle> vehicles) {
        this.fullVehicleList = vehicles;
        this.filteredVehicleList = new ArrayList<>(vehicles);
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle, parent, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle vehicle = filteredVehicleList.get(position);
        holder.bind(vehicle);
    }

    @Override
    public int getItemCount() {
        return filteredVehicleList.size();
    }

    public void filterVehicles(String category) {
        filteredVehicleList.clear();

        if (category.equals("All Vehicles")) {
            filteredVehicleList.addAll(fullVehicleList);
        } else {
            for (Vehicle vehicle : fullVehicleList) {
                if (vehicle.getCategory().equals(category)) {
                    filteredVehicleList.add(vehicle);
                }
            }
        }

        notifyDataSetChanged();
    }

    static class VehicleViewHolder extends RecyclerView.ViewHolder {
        private final ImageView vehicleImage;
        private final TextView vehicleTag;
        private final TextView vehicleName;
        private final RatingBar ratingBar;
        private final TextView ratingText;
        private final TextView sleepCount;
        private final ImageView feature1Icon;
        private final TextView feature1Text;
        private final ImageView feature2Icon;
        private final TextView feature2Text;
        private final TextView priceText;
        private final Button bookButton;

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);

            vehicleImage = itemView.findViewById(R.id.vehicleImage);
            vehicleTag = itemView.findViewById(R.id.vehicleTag);
            vehicleName = itemView.findViewById(R.id.vehicleName);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ratingText = itemView.findViewById(R.id.ratingText);
            sleepCount = itemView.findViewById(R.id.sleepCount);
            feature1Icon = itemView.findViewById(R.id.feature1Icon);
            feature1Text = itemView.findViewById(R.id.feature1Text);
            feature2Icon = itemView.findViewById(R.id.feature2Icon);
            feature2Text = itemView.findViewById(R.id.feature2Text);
            priceText = itemView.findViewById(R.id.priceText);
            bookButton = itemView.findViewById(R.id.bookButton);
        }

        public void bind(Vehicle vehicle) {
            Context context = itemView.getContext();

            // Set vehicle image
            vehicleImage.setImageResource(vehicle.getImageResId());

            // Set tag
            vehicleTag.setText(vehicle.getTagText());
            vehicleTag.setBackgroundTintList(ContextCompat.getColorStateList(context, vehicle.getTagColor()));

            // Set vehicle name
            vehicleName.setText(vehicle.getName());

            // Set rating
            ratingBar.setRating(vehicle.getRating());
            ratingText.setText(vehicle.getRating() + " (" + vehicle.getReviewCount() + " reviews)");

            // Set features
            sleepCount.setText("Sleeps " + vehicle.getSleepCount());
            feature1Icon.setImageResource(vehicle.getFeature1Icon());
            feature1Text.setText(vehicle.getFeature1Text());
            feature2Icon.setImageResource(vehicle.getFeature2Icon());
            feature2Text.setText(vehicle.getFeature2Text());

            // Set price
            priceText.setText("$" + vehicle.getPrice());

            // Set booking button click listener
            bookButton.setOnClickListener(v -> {
                Toast.makeText(context,
                        "Booking " + vehicle.getName() + " at $" + vehicle.getPrice() + "/day",
                        Toast.LENGTH_SHORT).show();
                // TODO: Implement booking functionality
            });
        }
    }
}
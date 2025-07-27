package com.mobicom.s18.nutritrack_mco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodSearchAdapter extends RecyclerView.Adapter<FoodSearchAdapter.FoodViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(FoodItem item);
    }

    private List<FoodItem> foodList;
    private OnItemClickListener listener;
    private Context context;

    public FoodSearchAdapter(Context context, List<FoodItem> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateList(List<FoodItem> newList) {
        this.foodList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_result, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem item = foodList.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        private final TextView foodName;
        private final TextView foodDetails;
        private final CardView cardView;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodNameText);
            foodDetails = itemView.findViewById(R.id.foodDetailsText);
            cardView = itemView.findViewById(R.id.foodCard);
        }

        public void bind(FoodItem item, OnItemClickListener listener) {
            foodName.setText(item.getName());
            foodDetails.setText(String.format("Per 100g: %.0f kcal | %.1fg P | %.1fg C | %.1fg F",
                    item.getCalories(), item.getProtein(), item.getCarbs(), item.getFats()));

            cardView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}


package com.mohnage7.bakingapp.recipedetails;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mohnage7.bakingapp.R;
import com.mohnage7.bakingapp.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter
        extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private final List<Ingredient> ingredientList;

    IngredientsAdapter(List<Ingredient> items) {
        ingredientList = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredients_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        holder.ingredientsTxTview.setText(String.format("\u25CF %s (%s %s)", ingredient.getIngredient(), ingredient.getQuantity(), ingredient.getMeasure()));
        holder.itemView.setTag(ingredientList.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredients_txt_view)
        TextView ingredientsTxTview;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }
    }

}

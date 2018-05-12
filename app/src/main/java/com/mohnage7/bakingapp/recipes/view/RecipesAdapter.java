package com.mohnage7.bakingapp.recipes.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohnage7.bakingapp.R;
import com.mohnage7.bakingapp.model.Recipes;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohnage7 on 2/28/2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.MoviesViewHolder> {
    private List<Recipes> mRecipesList;
    private Context mContext;
    private OnRecipeClickListener onRecipeClickListener;

    RecipesAdapter(List<Recipes> recipeList, Context context, OnRecipeClickListener onRecipeClickListener) {
        mRecipesList = recipeList;
        mContext = context;
        this.onRecipeClickListener = onRecipeClickListener;
    }


    void updateRecipesAdapter(List<Recipes> recipeList) {
        this.mRecipesList = recipeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MoviesViewHolder holder, int position) {
        final Recipes recipe = mRecipesList.get(position);
        // set movie title
        holder.recipeNameTxtView.setText(recipe.getName());
        // set serving count
        holder.servingsTxtView.setText(String.format("%s %d", mContext.getString(R.string.servings), recipe.getServings()));
        // set movie image
        holder.loadImage(recipe.getImage());
        // on movie clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecipeClickListener.onRecipeClicked(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecipesList.size();
    }

    public interface OnRecipeClickListener {
        void onRecipeClicked(Recipes movie);
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_img_view)
        ImageView movieImageView;
        @BindView(R.id.recipe_title_txt_view)
        TextView recipeNameTxtView;
        @BindView(R.id.servings_count_txt_view)
        TextView servingsTxtView;

        MoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * this method loads the image from Cache if it failed it load it again from the network
         *
         * @param imageUrl image to load
         */
        void loadImage(final String imageUrl) {
            if (imageUrl != null && !imageUrl.isEmpty())
                Picasso.with(mContext)
                        .load(imageUrl)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(movieImageView, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                //Try again online if cache failed
                                Picasso.with(mContext)
                                        .load(imageUrl)
                                        .placeholder(R.mipmap.ic_launcher)
                                        .into(movieImageView, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                            }

                                            @Override
                                            public void onError() {
                                                Log.v("Picasso", "Could not fetch image");
                                            }
                                        });
                            }
                        });
            else
                Picasso.with(mContext)
                        .load(R.mipmap.ic_launcher)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(movieImageView);
        }
    }
}

package com.mohnage7.bakingapp.recipedetails;

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
import com.mohnage7.bakingapp.model.Step;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohnage7 on 2/28/2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.MoviesViewHolder> {
    private List<Step> mStepsList;
    private Context mContext;
    private OnRecipeClickListener onRecipeClickListener;

    StepsAdapter(List<Step> mStepsList, Context context, OnRecipeClickListener onRecipeClickListener) {
        this.mStepsList = mStepsList;
        mContext = context;
        this.onRecipeClickListener = onRecipeClickListener;
    }


    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MoviesViewHolder holder, int position) {
        final Step step = mStepsList.get(position);
        // set movie title
        holder.recipeNameTxtView.setText(step.getShortDescription());
        // set movie image
        holder.loadImage(step.getThumbnailURL());
        // on movie clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecipeClickListener.onStepClicked(step);
            }
        });
    }

    public void chooseDefaultStep() {
        if (mStepsList != null && mStepsList.size() > 0)
            onRecipeClickListener.onStepClicked(mStepsList.get(0));
    }

    @Override
    public int getItemCount() {
        return mStepsList.size();
    }

    public interface OnRecipeClickListener {
        void onStepClicked(Step step);
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_img_view)
        ImageView movieImageView;
        @BindView(R.id.recipe_title_txt_view)
        TextView recipeNameTxtView;

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
                        //     .placeholder(R.drawable.movie_place_holder)
                        .into(movieImageView, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                //Try again online if cache failed
                                Picasso.with(mContext)
                                        .load(imageUrl)
                                        //               .placeholder(R.drawable.movie_place_holder)
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
        }
    }
}

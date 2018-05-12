package com.mohnage7.bakingapp.recipes.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.mohnage7.bakingapp.R;
import com.mohnage7.bakingapp.model.Recipes;
import com.mohnage7.bakingapp.recipedetails.RecipeDetailsActivity;
import com.mohnage7.bakingapp.recipes.RecipeContract;
import com.mohnage7.bakingapp.recipes.presenter.RecipesPresenter;
import com.mohnage7.bakingapp.utils.InternetConnection;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mohnage7.bakingapp.utils.Constants.LIST_STATE;
import static com.mohnage7.bakingapp.utils.Constants.RECIPE;

public class RecipesActivity extends AppCompatActivity implements RecipeContract.IRecipesView, RecipesAdapter.OnRecipeClickListener {

    @BindView(R.id.loading_view)
    public AVLoadingIndicatorView loadingIndicatorView;
    @BindView(R.id.movies_recycler_view)
    RecyclerView mRecipesRecyclerView;
    private RecipesAdapter mRecipesAdapter;
    private GridLayoutManager layoutManager;
    private Parcelable mListState;
    // The Idling Resource which will be null in production.
    @Nullable
    private MyIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link MyIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new MyIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        // bind views
        ButterKnife.bind(this);
        // create @mIdlingResource instance
        getIdlingResource();
        // get recipes from server
        getRecipesListFromServer();
    }

    private void setRecyclerView(List<Recipes> mRecipesList) {
        //create adapter
        mRecipesAdapter = new RecipesAdapter(mRecipesList, this, this);
        // init layout manger
        // change number of movies in a single row according to phone orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // in case it's portrait
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            // in case it's landscape
            layoutManager = new GridLayoutManager(this, 4);
        }
        // set layout manger
        mRecipesRecyclerView.setLayoutManager(layoutManager);
        // set movies adapter
        mRecipesRecyclerView.setAdapter(mRecipesAdapter);
        // hide loading dialog
        loadingIndicatorView.smoothToHide();
        // restore list state ( position )
        if (mListState != null) {
            layoutManager.onRestoreInstanceState(mListState);
        }
    }

    private void getRecipesListFromServer() {
        setIdlingResource(false);
        if (InternetConnection.isNetworkAvailable(this)) {
            loadingIndicatorView.smoothToShow();
            // get presenter reference
            RecipesPresenter moviesPresenter = new RecipesPresenter(this);
            // make service call
            moviesPresenter.getRecipes();

        } else {
            loadingIndicatorView.smoothToHide();
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void setRecipes(List<Recipes> recipesList) {
        // getting recycler ready
        setRecyclerView(recipesList);
        // notify Instrumental test that it can be continue running
        setIdlingResource(true);
    }

    /**
     * The IdlingResource is null in production as set by the @Nullable annotation which means
     * the value is allowed to be null.
     * <p>
     * If the idle state is true, Espresso can perform the next action.
     * If the idle state is false, Espresso will wait until it is true before
     * performing the next action.
     */
    public void setIdlingResource(boolean isIdl) {
        if (mIdlingResource != null)
            mIdlingResource.setIdleState(isIdl);
    }

    @Override
    public void onFailure() {
        // hide loading dialog
        loadingIndicatorView.smoothToHide();
        // alert user with fail message
        Toast.makeText(this, R.string.recipes_load_failure, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // save recycler state
        if (layoutManager != null) {
            mListState = layoutManager.onSaveInstanceState();
            outState.putParcelable(LIST_STATE, mListState);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
            mListState = savedInstanceState.getParcelable(LIST_STATE);
    }

    @Override
    public void onRecipeClicked(Recipes recipe) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE, recipe);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

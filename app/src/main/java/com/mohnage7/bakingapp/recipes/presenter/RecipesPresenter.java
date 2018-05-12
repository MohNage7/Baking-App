package com.mohnage7.bakingapp.recipes.presenter;


import android.support.annotation.NonNull;

import com.mohnage7.bakingapp.model.Recipes;
import com.mohnage7.bakingapp.network.Api;
import com.mohnage7.bakingapp.network.ApiClient;
import com.mohnage7.bakingapp.recipes.RecipeContract;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohnage7 on 3/1/2018.
 */

public class RecipesPresenter implements RecipeContract.IRecipesPresenter {

    private RecipeContract.IRecipesView iRecipesView;

    public RecipesPresenter(RecipeContract.IRecipesView iRecipesView) {
        this.iRecipesView = iRecipesView;
    }

    @Override
    public void getRecipes() {
        Api popularMoviesAPi = ApiClient.getClient();
        Call<List<Recipes>> call = popularMoviesAPi.getRecipes();
        call.enqueue(new Callback<List<Recipes>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipes>> call, @NonNull Response<List<Recipes>> response) {

                List<Recipes> recipesList = response.body();
                if (recipesList != null)
                    iRecipesView.setRecipes(recipesList);
                else
                    iRecipesView.onFailure();
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipes>> call, @NonNull Throwable t) {
                iRecipesView.onFailure();
            }
        });

    }
}

package com.mohnage7.bakingapp.recipes;


import com.mohnage7.bakingapp.model.Recipes;

import java.util.List;

/**
 * Created by mohnage7 on 3/1/2018.
 * Interface that defines the interaction between Presenter and the view
 */

public class RecipeContract {


    public interface IRecipesView {
        void setRecipes(List<Recipes> moviesList);

        void onFailure();
    }

    public interface IRecipesPresenter {
        void getRecipes();
    }
}

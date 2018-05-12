package com.mohnage7.bakingapp.network;


import com.mohnage7.bakingapp.model.Recipes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("baking.json")
    Call<List<Recipes>> getRecipes();


}

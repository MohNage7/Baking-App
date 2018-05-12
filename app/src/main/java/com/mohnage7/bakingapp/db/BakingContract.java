package com.mohnage7.bakingapp.db;


import android.net.Uri;


/**
 * Defines table and column names for the baking database. This class is not necessary, but keeps
 * the code organized.
 */
public class BakingContract {

    /*
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website. A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * Play Store.
     */
    public static final String CONTENT_AUTHORITY = "com.mohnage7.bakingapp";


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_INGREDIENTS = "ingredients";


    /* Inner class that defines the table contents of the recipes table */
    public static final class IngredientsEntry {

        /* The base INGREDIENTS_URI used to query the recipe table from the content provider */
        public static final Uri INGREDIENTS_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_INGREDIENTS)
                .build();
        public static final String COLUMN_ID = "id";
        public static final String QUANTITY = "quantity";
        public static final String MEASURE = "measure";
        public static final String INGREDIENT = "ingredient";
        /* Used internally as the name of our recipes table. */
        static final String TABLE_NAME = "ingredients";

    }
}
package com.mohnage7.bakingapp.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Manages a local database for weather data.
 */
public class BakingDbHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "baking.db";

    private static final int DATABASE_VERSION = 1;

    public BakingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     *
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our weather data.
         */
        final String SQL_CREATE_INGREDIENTS_TABLE =

                "CREATE TABLE " + BakingContract.IngredientsEntry.TABLE_NAME + " (" +
                        BakingContract.IngredientsEntry.COLUMN_ID + " INTEGER, " +
                        BakingContract.IngredientsEntry.INGREDIENT + " REAL NOT NULL, " +
                        BakingContract.IngredientsEntry.MEASURE + " REAL NOT NULL, " +
                        BakingContract.IngredientsEntry.QUANTITY + " LONG)";

        /*
         * execute SQL with the execSQL method of our SQLite database object.
         */
        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BakingContract.RecipeEntry.TABLE_NAME);
//        onCreate(sqLiteDatabase);
    }
}
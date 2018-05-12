package com.mohnage7.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.mohnage7.bakingapp.R;
import com.mohnage7.bakingapp.db.BakingContract;

import static com.mohnage7.bakingapp.db.BakingContract.BASE_CONTENT_URI;
import static com.mohnage7.bakingapp.db.BakingContract.PATH_INGREDIENTS;

public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    static final String EXTRA_RECIPE_NAME = "recipe_name";

    private Context mContext;
    private Cursor mCursor;

    GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;

    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        // Get get all ingredients
        Uri PLANT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();
        if (mCursor != null) mCursor.close();
        mCursor = mContext.getContentResolver().query(
                PLANT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the ListView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(position);
        // get index
        int measureIndex = mCursor.getColumnIndex(BakingContract.IngredientsEntry.MEASURE);
        int ingredientsIndex = mCursor.getColumnIndex(BakingContract.IngredientsEntry.INGREDIENT);
        int quantityIndex = mCursor.getColumnIndex(BakingContract.IngredientsEntry.QUANTITY);
        // get values with index
        String measure = mCursor.getString(measureIndex);
        String ingredients = mCursor.getString(ingredientsIndex);
        String quantity = mCursor.getString(quantityIndex);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget);
        // gather ingredients with quantity and measure
        String ingredientsDesc = String.format("\u25CF %s (%s %s)", ingredients, quantity, measure);

        // Update view
        views.setTextViewText(R.id.widget_recipe_name, ingredientsDesc);

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

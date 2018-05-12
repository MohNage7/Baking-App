package com.mohnage7.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.mohnage7.bakingapp.R;

import static com.mohnage7.bakingapp.widget.GridRemoteViewsFactory.EXTRA_RECIPE_NAME;

public class RecipeService extends IntentService {

    /**
     * An {@link IntentService} subclass for handling asynchronous task requests in
     * a service on a separate handler thread.
     */

    public static final String ACTION_UPDATE_INGREDIENTS_WIDGETS = "com.mohnage7.bakingapp.action.update_ingredients_widgets";

    public RecipeService() {
        super("PlantWateringService");
    }

    /**
     * Starts this service to perform ingredients widget update action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionIngredients(Context context, String name) {
        Intent intent = new Intent(context, RecipeService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENTS_WIDGETS);
        intent.putExtra(EXTRA_RECIPE_NAME, name);
        context.startService(intent);
    }


    /**
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        final String recipeName = intent.getStringExtra(EXTRA_RECIPE_NAME);
        startActionIngredients(recipeName);
    }


    private void startActionIngredients(String recipeName) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        // get available widgets ids
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
        //Now update all widgets
        RecipeWidgetProvider.updateRecipesWidgets(this, appWidgetManager, recipeName, appWidgetIds);
    }


}


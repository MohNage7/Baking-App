package com.mohnage7.bakingapp.recipedetails;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.mohnage7.bakingapp.R;
import com.mohnage7.bakingapp.db.BakingContract;
import com.mohnage7.bakingapp.model.Ingredient;
import com.mohnage7.bakingapp.model.Recipes;
import com.mohnage7.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mohnage7.bakingapp.utils.Constants.RECIPE;
import static com.mohnage7.bakingapp.widget.RecipeService.startActionIngredients;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepsActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeDetailsActivity extends AppCompatActivity implements StepsAdapter.OnRecipeClickListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.item_ingredients_list)
    RecyclerView mIngredientsRecycler;
    @BindView(R.id.item_steps_list)
    RecyclerView mStepsRecyclerView;
    @BindView(R.id.recipe_details_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.scroll_container_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.fab_add_to_widget)
    FloatingActionButton addToWidgetFab;

    private Recipes mRecipe;
    private StepsAdapter stepsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        ButterKnife.bind(this);


        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }

        setupRecyclerView();
        setActionbar();
        hideAndShowFabWhileScrolling();

    }

    private void hideAndShowFabWhileScrolling() {
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    addToWidgetFab.hide();
                } else {
                    addToWidgetFab.show();
                }
            }
        });
    }

    private void setActionbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(mRecipe.getName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        mRecipe = getIntent().getParcelableExtra(RECIPE);
        if (mRecipe != null) {
            mIngredientsRecycler.setAdapter(new IngredientsAdapter(mRecipe.getIngredients()));
            stepsAdapter = new StepsAdapter(mRecipe.getSteps(), this, this);
            mStepsRecyclerView.setAdapter(stepsAdapter);
        }

        if (mTwoPane)
            stepsAdapter.chooseDefaultStep();

    }


    @Override
    public void onStepClicked(Step step) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(StepsFragment.STEP, step);
            StepsFragment fragment = new StepsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepsActivity.class);
            // set receipt name
            step.setReceiptName(mRecipe.getName());
            intent.putExtra(StepsFragment.STEP, step);
            startActivity(intent);
        }
    }


    /**
     * this action method adds recipe to widget if it's not added before
     * and deletes movie from widget if it's added before
     */
    @OnClick(R.id.fab_add_to_widget)
    public void onAddToWidgetClick() {
        if (checkIfRecipeNotAddedBefore()) {
            if (checkIfAnyRecipeIsAddedBefore()) {
                deleteRecipeFromWidget(false);
                addIngredientsToWidget();
            } else
                addIngredientsToWidget();

        } else
            deleteRecipeFromWidget(true);

        startActionIngredients(this, mRecipe.getName());
    }


    /**
     * this method adds the movie to the db if it doesn't exists and notify user
     */
    private void addIngredientsToWidget() {
        List<ContentValues> mValueList = new ArrayList<>();
        for (int i = 0; i < mRecipe.getIngredients().size(); i++) {
            Ingredient ingredient = mRecipe.getIngredients().get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(BakingContract.IngredientsEntry.COLUMN_ID, String.valueOf(mRecipe.getId()));
            contentValues.put(BakingContract.IngredientsEntry.INGREDIENT, ingredient.getIngredient());
            contentValues.put(BakingContract.IngredientsEntry.MEASURE, ingredient.getMeasure());
            contentValues.put(BakingContract.IngredientsEntry.QUANTITY, String.valueOf(ingredient.getQuantity()));
            mValueList.add(contentValues);
        }

        ContentValues[] contentValuesArray = new ContentValues[mValueList.size()];
        mValueList.toArray(contentValuesArray);
        int rows = getContentResolver().bulkInsert(BakingContract.IngredientsEntry.INGREDIENTS_URI, contentValuesArray);

        // update UI
        if (rows > 0)
            updateUI(true);
        else
            Toast.makeText(this, R.string.recipe_add_fail, Toast.LENGTH_SHORT).show();


    }

    /**
     * this method deletes recipe from  db
     */
    private void deleteRecipeFromWidget(boolean showMessage) {
        String selectionArgs = BakingContract.IngredientsEntry.COLUMN_ID + " = " + mRecipe.getId();
        int rowNum = getContentResolver().delete(BakingContract.IngredientsEntry.INGREDIENTS_URI, selectionArgs, null);
        if (rowNum != 0 && showMessage)
            updateUI(false);
    }

    /**
     * @return true if the recipe doesn't added before in the db and false otherwise
     */

    private boolean checkIfRecipeNotAddedBefore() {
        String selectionArgs = BakingContract.IngredientsEntry.COLUMN_ID + " = " + mRecipe.getId();
        // get movie from content provider by it's id
        Cursor cursor = getContentResolver().
                query(BakingContract.IngredientsEntry.INGREDIENTS_URI,
                        null,
                        selectionArgs,
                        null,
                        null,
                        null);
        // if cursor !=null and cursor count == 0 return true otherwise false
        return (cursor != null ? cursor.getCount() : 0) == 0;
    }

    /**
     * @return true if the movie doesn't added before in the db and false otherwise
     */
    private boolean checkIfAnyRecipeIsAddedBefore() {
        // get movie from content provider by it's id
        Cursor cursor = getContentResolver().
                query(BakingContract.IngredientsEntry.INGREDIENTS_URI,
                        null,
                        null,
                        null,
                        null,
                        null);
        // if cursor !=null and cursor count == 0 return true otherwise false
        return (cursor != null ? cursor.getCount() : 0) > 0;
    }

    /**
     * this method change icon according to add or delete action and display toast for the user
     *
     * @param isAdded this field defines the action that method will take
     */
    private void updateUI(boolean isAdded) {
        int message;
        // change button color
        if (isAdded) {
            message = R.string.add_to_widget_success;
        } else {
            message = R.string.recipe_delete_success;
        }
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }
}


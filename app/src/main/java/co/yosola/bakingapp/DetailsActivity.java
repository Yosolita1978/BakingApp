package co.yosola.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import Widget.RecipeWidgetProvider;
import co.yosola.bakingapp.Model.Ingredients;
import co.yosola.bakingapp.Model.Recipe;
import co.yosola.bakingapp.Model.Steps;
import co.yosola.bakingapp.Utils.Constants;
import timber.log.Timber;

public class DetailsActivity extends AppCompatActivity implements StepsFragment.OnStepClickListener {

    Recipe recipe;
    private Context context;
    public boolean isTablet;
    public static ArrayList<Steps> stepsList;
    public static ArrayList<Ingredients> mIngredientsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        context = getApplicationContext();

        Intent intent = getIntent();
        recipe = intent.getParcelableExtra("Recipe");
        setTitle(recipe.getName());
        mIngredientsList = recipe.getIngredients();
        stepsList = recipe.getSteps();


        FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle ingredientsBundle = new Bundle();
        ingredientsBundle.putParcelable("Recipe", recipe);

        Bundle stepsBundle = new Bundle();
        stepsBundle.putParcelable("Recipe", recipe);

        if (findViewById(R.id.tablet_layout) != null) {
            isTablet = true;
            //Pass Over the bundle to the Ingredients Fragment
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setArguments(ingredientsBundle);

            fragmentManager.beginTransaction().replace(R.id.ingredients_tablet_fragment_container, ingredientsFragment).commit();

            //Pass Over the bundle to the Steps Fragment
            StepsFragment stepsFragment = new StepsFragment();
            stepsFragment.setArguments(stepsBundle);

            fragmentManager.beginTransaction().replace(R.id.steps_tablet_fragment_container, stepsFragment).commit();

        } else {
            isTablet = false;
            //Pass Over the bundle to the Ingredients Fragment
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setArguments(ingredientsBundle);

            fragmentManager.beginTransaction().replace(R.id.ingredients_fragment_container, ingredientsFragment).commit();

            //Pass Over the bundle to the Steps Fragment
            StepsFragment stepsFragment = new StepsFragment();
            stepsFragment.setArguments(stepsBundle);

            fragmentManager.beginTransaction().replace(R.id.steps_fragment_container, stepsFragment).commit();
        }

    }

    @Override
    public void onClick(Steps step) {
        if (isTablet) {
            Bundle stepsVideoBundle = new Bundle();
            stepsVideoBundle.putParcelable("StepOnClick", step);
            stepsVideoBundle.putBoolean("isTablet", isTablet);
            stepsVideoBundle.putParcelable("Recipe", recipe);

            ExoPlayerFragment expoFragment = new ExoPlayerFragment();
            expoFragment.setArguments(stepsVideoBundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.expovideo_tablet_fragment, expoFragment).commit();
        }
        else {

            Intent intent = new Intent(DetailsActivity.this, ExoPlayerActivity.class);
            intent.putExtra("StepOnClick", step);
            intent.putExtra("isTablet", isTablet);
            intent.putExtra("Recipe", recipe);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.widget_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option
        switch (item.getItemId()) {
            // Respond to a click
            case R.id.widget_menu:
                sendToWidget();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // sending this list of ingredients to the widget
    public void sendToWidget(){

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(mIngredientsList);
        prefsEditor.putString("IngredientsList_Widget", json);
        prefsEditor.apply();

        Context context = this.getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, RecipeWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_list);

        Toast.makeText(getApplicationContext(), R.string.added_to_widget, Toast.LENGTH_SHORT).show();

    }

}


package co.yosola.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

import co.yosola.bakingapp.Model.Recipe;
import co.yosola.bakingapp.Model.Steps;
import timber.log.Timber;

public class DetailsActivity extends AppCompatActivity implements StepsFragment.OnStepClickListener {

    Recipe recipe;
    private Context context;
    public boolean isTablet;
    public static ArrayList<Steps> stepsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        context = getApplicationContext();

        Intent intent = getIntent();
        recipe = intent.getParcelableExtra("Recipe");
        setTitle(recipe.getName());
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

    }
}


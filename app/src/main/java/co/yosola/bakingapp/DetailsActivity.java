package co.yosola.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import co.yosola.bakingapp.Model.Recipe;

public class DetailsActivity extends AppCompatActivity {

    Recipe recipe;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        context = getApplicationContext();

        Intent intent = getIntent();
        recipe = intent.getParcelableExtra("Recipe");
        setTitle(recipe.getName());


        FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle ingredientsBundle = new Bundle();
        ingredientsBundle.putParcelable("Recipe", recipe);

        //Pass Over the bundle to the Ingredients Fragment
        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        ingredientsFragment.setArguments(ingredientsBundle);

        fragmentManager.beginTransaction().replace(R.id.ingredients_fragment_container, ingredientsFragment).commit();

    }
}

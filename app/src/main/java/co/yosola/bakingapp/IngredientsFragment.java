package co.yosola.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import co.yosola.bakingapp.Adapters.IngredientsAdapter;
import co.yosola.bakingapp.Model.Ingredients;
import co.yosola.bakingapp.Model.Recipe;
import timber.log.Timber;


//This fragment doesn't need a OnClickHandler because the IngredientsAdapter doesn't have one

public class IngredientsFragment extends Fragment {

    RecyclerView mIngredientRecyclerView;

    private TextView ingredientsLabel;
    private ArrayList<Ingredients> ingredientsList;
    private Recipe recipe;

    private static final String KEY_INGREDIENTS_LIST = "ingredients";

    public IngredientsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Inflate the Ingredients fragment layout
        View rootView = inflater.inflate(R.layout.fragment_ingredients_details, container, false);

        // Bind the views
        ingredientsLabel = rootView.findViewById(R.id.ingredients_label);
        ingredientsLabel.setText(R.string.ingredients);

        mIngredientRecyclerView = rootView.findViewById(R.id.recyclerview_ingredients);

        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            recipe = getArguments().getParcelable("Recipe");

            ingredientsList = new ArrayList<>();
            ingredientsList = recipe.getIngredients();

            if (savedInstanceState != null)
            {

                ingredientsList = savedInstanceState.getParcelableArrayList(KEY_INGREDIENTS_LIST);
            }

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mIngredientRecyclerView.setLayoutManager(mLayoutManager);
            Timber.d("IngredientsList" + ingredientsList.size());

            IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(ingredientsList, getContext());
            mIngredientRecyclerView.setAdapter(ingredientsAdapter);

        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
        outState.putParcelableArrayList(KEY_INGREDIENTS_LIST, ingredientsList);
        super.onSaveInstanceState(outState);
    }



}

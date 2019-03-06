package co.yosola.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

import co.yosola.bakingapp.Adapters.RecipeAdapter;
import co.yosola.bakingapp.Model.Recipe;
import co.yosola.bakingapp.Utils.NetworkUtils;
import timber.log.Timber;

public class MainFragment extends Fragment implements RecipeAdapter.RecipesAdapterOnClickHandler {

    RecyclerView mRecyclerView;

    private RecipeAdapter mRecipeAdapter;
    private URL url;
    private ArrayList<Recipe> recipeList;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;


    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);
        mErrorMessageDisplay = rootView.findViewById(R.id.errors_view);
        mLoadingIndicator = rootView.findViewById(R.id.progress_bar);
        mRecyclerView = rootView.findViewById(R.id.list);

        if(MainActivity.isTablet){
            GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            mRecyclerView.setHasFixedSize(true);;
            Timber.d(String.valueOf(MainActivity.isTablet));
        } else {
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            Timber.d(String.valueOf(MainActivity.isTablet));
        }

        //initialize adapter and set to the recycler view object
        recipeList = new ArrayList<>();
        mRecipeAdapter = new RecipeAdapter(recipeList, this, getContext());
        mRecyclerView.setAdapter(mRecipeAdapter);

        if(!isOnline()){
            showErrorMessage();
            mErrorMessageDisplay.setText(R.string.detail_error_message);
        }

        url = NetworkUtils.buildURl();
        new DownloadDataTask().execute(url);

        return rootView;

    }

    //The method to check if there is internet connection
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    //The method for show the error message
    private void showErrorMessage() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    //Bring the Json and bind it with the adapter
    private void showData(ArrayList<Recipe> recipes) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        recipeList = recipes;
        mRecipeAdapter.setData(recipes);
    }

    @Override
    public void onClick(Recipe recipe) {
        //Timber.d(String.valueOf(recipe.getName()));
        Toast.makeText(getContext(), recipe.getName(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra("Recipe", recipe);
        startActivity(intent);

    }


    //AsyncTask to fetch the data in a different thread
    public class DownloadDataTask extends AsyncTask<URL, Void, ArrayList<Recipe>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Recipe> doInBackground(URL... urls) {
            final URL mUrl;
            recipeList = null;

            if (urls.length == 0) {
                mUrl = NetworkUtils.buildURl();;
            } else{
                mUrl = urls[0];
            }

            try {
                recipeList = NetworkUtils.fetchRecipeData(mUrl);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return recipeList;
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipes) {
            mErrorMessageDisplay.setVisibility(View.GONE);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            if (recipes != null) {
                super.onPostExecute(recipes);
                showData(recipes);
                Timber.d(String.valueOf(recipes.size()));
            } else {
                showErrorMessage();
                Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_LONG).show();
            }
        }
    }

}

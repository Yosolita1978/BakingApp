package co.yosola.bakingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.net.URL;
import java.util.ArrayList;

import co.yosola.bakingapp.DownloadAsyncTask.AsyncListner;
import co.yosola.bakingapp.Model.Recipe;
import co.yosola.bakingapp.Utils.NetworkUtils;
import timber.log.Timber;

public class MainFragment extends Fragment implements AsyncListner {

    RecyclerView mRecyclerView;

    private RecipeAdapter mRecipeAdapter;
    private URL url;

    public static ArrayList<Recipe> recipeList;

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

        //Check the internet acces
        if (isOnline()) {

            RecyclerView.LayoutManager manager;
            if (MainActivity.isTablet) {
                manager = new GridLayoutManager(getActivity(), 2);
            } else {
                manager = new LinearLayoutManager(getActivity());
            }

            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.setHasFixedSize(true);
            url = NetworkUtils.buildURl();
            new DownloadAsyncTask(this).execute(url);
            Timber.d(String.valueOf(MainActivity.isTablet));
        } else {
            showErrorMessage();
            mErrorMessageDisplay.setText(R.string.detail_error_message);
        }

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


    @Override
    public void returnRecipe(ArrayList<Recipe> recipes) {

        mRecipeAdapter = new RecipeAdapter(recipes, new RecipeAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(Recipe recepie) {
                Toast.makeText(getActivity(), recepie.getName(), Toast.LENGTH_LONG).show();
            }
        });

        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setAdapter(mRecipeAdapter);
        mRecipeAdapter.notifyDataSetChanged();
        recipeList = recipes;
    }

    @Override
    public void onRefresh() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        downloadRecipe();
    }


    private void downloadRecipe() {
        new DownloadAsyncTask(this).execute(url);
        mRecipeAdapter.notifyDataSetChanged();
    }


}

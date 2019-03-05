package co.yosola.bakingapp;

import android.os.AsyncTask;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

import co.yosola.bakingapp.Model.Recipe;
import co.yosola.bakingapp.Utils.NetworkUtils;

public class DownloadAsyncTask extends AsyncTask<URL, Void, ArrayList<Recipe>> {

    private AsyncListner mListner;

    public interface AsyncListner {
        void returnRecipe(ArrayList<Recipe> recepies);

        void onRefresh();
    }

    public DownloadAsyncTask(AsyncListner listner) {
        mListner = listner;
    }

    @Override
    protected ArrayList<Recipe> doInBackground(URL... urls) {

        final URL mUrl;

        if (urls.length == 0) {
            mUrl = NetworkUtils.buildURl();;
        } else{
            mUrl = urls[0];
        }

        try {
            ArrayList<Recipe> json = NetworkUtils.fetchRecipeData(mUrl);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Recipe> recepies) {
        super.onPostExecute(recepies);
        mListner.returnRecipe(recepies);

    }


}

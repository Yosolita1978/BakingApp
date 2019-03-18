package Widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import co.yosola.bakingapp.Model.Ingredients;
import co.yosola.bakingapp.R;

public class RecipeWidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private List<Ingredients> mIngredientsList;

    public RecipeWidgetViewFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        loadData();
    }

    @Override
    public void onDataSetChanged() {
        loadData();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        if (mIngredientsList == null || mIngredientsList.size() == 0) {
            return 0;
        } else {
            return mIngredientsList.size();
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredients ingredient = mIngredientsList.get(position);

        RemoteViews itemView = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_item);

        itemView.setTextViewText(R.id.ingredient_quantity, ingredient.getQuantity());
        itemView.setTextViewText(R.id.ingredient_measure, ingredient.getMeasure());
        itemView.setTextViewText(R.id.ingredient_name, ingredient.getIngredient());

        return itemView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void loadData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredients>>() {}.getType();
        String gsonString = sharedPreferences.getString("IngredientsList_Widget", "");
        mIngredientsList = gson.fromJson(gsonString, type);
    }
}



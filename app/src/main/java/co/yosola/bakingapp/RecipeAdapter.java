package co.yosola.bakingapp;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import co.yosola.bakingapp.Model.Recipe;
import timber.log.Timber;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {


    private ArrayList<Recipe> mRecipesList;
    private Context mContext;


    //I need this to set the ListitemClickLister;
    private RecipesAdapterOnClickHandler mRecipeClickHandler;


    public interface RecipesAdapterOnClickHandler
    {
        void onClick(Recipe recipe);
    }


    public RecipeAdapter(ArrayList<Recipe> recipes, RecipesAdapterOnClickHandler recipeClickHandler, Context context) {
        this.mRecipesList = recipes;
        this.mRecipeClickHandler = recipeClickHandler;
        this.mContext = context;
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        RecipeAdapterViewHolder viewHolder = new RecipeAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int i) {
        final Recipe recipeItem = mRecipesList.get(i);

        //Download image using picasso library
        if (recipeItem.getImage().isEmpty()) {
            holder.imageView.setImageResource(R.drawable.recipedefault);

        } else {
            Picasso.get().load(recipeItem.getImage())
                    .placeholder(R.drawable.recipedefault)
                    .into(holder.imageView);
        }

        //Setting text view name and servings
        holder.nameTextView.setText(recipeItem.getName());
        holder.servingsTextView.setText(recipeItem.getServings());

    }

    @Override
    public int getItemCount() {
        if (mRecipesList == null) {
            return 0;
        } else {
            return mRecipesList.size();
        }
    }

    public void setData(ArrayList<Recipe> recipes) {
        if (this.mRecipesList != null) {
            this.mRecipesList.clear();
        }
        this.mRecipesList = recipes;
        notifyDataSetChanged();
    }


    public void clear() {
        mRecipesList.clear();
    }


    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView nameTextView;
        TextView servingsTextView;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.recipe_image);
            this.nameTextView = (TextView) itemView.findViewById(R.id.recipe_name);
            this.servingsTextView = (TextView) itemView.findViewById(R.id.recipe_servings_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = mRecipesList.get(adapterPosition);
            mRecipeClickHandler.onClick(recipe);
            //Timber.d(recipe.getName() + "inside the adapter");
        }

    }


}

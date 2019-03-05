package co.yosola.bakingapp;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import co.yosola.bakingapp.Model.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.CustomViewHolder> {


    final private ListItemClickListener mListItemClickListener;

    private ArrayList<Recipe> mRecipesList;
    private Context mContext;


    //Interface for the Adapter
    public interface ListItemClickListener {

        void onListItemClick(Recipe recipe);

    }


    public RecipeAdapter(ArrayList<Recipe> recipes, ListItemClickListener listItemClickListener) {
        this.mRecipesList = recipes;
        this.mListItemClickListener = listItemClickListener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_list_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Recipe recipeItem = mRecipesList.get(i);

        //Download image using picasso library
        if (recipeItem.getImage().isEmpty()) {
            customViewHolder.imageView.setImageResource(R.drawable.recipedefault);

        } else {
            Picasso.get().load(recipeItem.getImage())
                    .placeholder(R.drawable.recipedefault)
                    .into(customViewHolder.imageView);
        }

        //Setting text view name and servings
        customViewHolder.nameTextView.setText(recipeItem.getName());
        customViewHolder.servingsTextView.setText(recipeItem.getServings());

    }

    @Override
    public int getItemCount() {
        return (null != mRecipesList ? mRecipesList.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView nameTextView;
        TextView servingsTextView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.recipe_image);
            this.nameTextView = (TextView) itemView.findViewById(R.id.recipe_name);
            this.servingsTextView = (TextView) itemView.findViewById(R.id.recipe_servings_content);
        }

        public void bind(final Recipe recipe, final ListItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onListItemClick(recipe);
                }
            });
        }

    }



}

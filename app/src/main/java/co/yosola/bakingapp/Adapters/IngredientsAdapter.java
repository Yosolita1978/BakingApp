package co.yosola.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import co.yosola.bakingapp.Model.Ingredients;
import co.yosola.bakingapp.R;

//This adapter doesn't need a OnClick method.

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder> {

    private ArrayList<Ingredients> mIngredientsList;
    private Context mContext;


    public IngredientsAdapter(ArrayList<Ingredients> ingredientsList, Context context) {
        this.mIngredientsList = ingredientsList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public IngredientsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.ingredient_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        IngredientsAdapterViewHolder viewHolder = new IngredientsAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapterViewHolder holder, int i) {
        //Binding data
        final Ingredients ingredientsView = mIngredientsList.get(i);

        holder.nameText.setText(ingredientsView.getIngredient());
        holder.quantityText.setText(ingredientsView.getQuantity());
        holder.measureText.setText(ingredientsView.getMeasure());
        holder.measureText.setAllCaps(false);

    }

    @Override
    public int getItemCount() {
        if (mIngredientsList == null) {
            return 0;
        } else {
            return mIngredientsList.size();
        }
    }

    public void setIngredientsData(ArrayList<Ingredients> ingredients) {
        if (this.mIngredientsList != null) {
            this.mIngredientsList.clear();
        }
        this.mIngredientsList = ingredients;
        notifyDataSetChanged();
    }


    public class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView nameText;
        TextView quantityText;
        TextView measureText;

        public IngredientsAdapterViewHolder(View itemView) {
            super(itemView);
            this.nameText = (TextView) itemView.findViewById(R.id.ingredient_name);
            this.quantityText = (TextView) itemView.findViewById(R.id.ingredient_quantity);
            this.measureText = (TextView) itemView.findViewById(R.id.ingredient_measure);
        }
    }

}

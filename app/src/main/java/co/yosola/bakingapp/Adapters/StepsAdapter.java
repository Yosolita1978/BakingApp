package co.yosola.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import co.yosola.bakingapp.Model.Recipe;
import co.yosola.bakingapp.Model.Steps;
import co.yosola.bakingapp.R;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsAdapterViewHolder> {


    private ArrayList<Steps> mStepsList;
    private Context mContext;


    //I need this to set the ListitemClickLister;
    private StepsAdapterOnClickHandler mStepsAdapterClickHandler;


    public interface StepsAdapterOnClickHandler
    {
        void onClick(Steps steps);
    }

    public StepsAdapter(ArrayList<Steps> steps, StepsAdapterOnClickHandler stepsClickHandler) {
        this.mStepsList = steps;
        this.mStepsAdapterClickHandler = stepsClickHandler;

    }

    @Override
    public StepsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.step_list_items;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        StepsAdapterViewHolder viewHolder = new StepsAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StepsAdapterViewHolder holder, int i) {
        final Steps stepRow = mStepsList.get(i);

        //Setting text view name
        holder.nameTextView.setText(stepRow.getShortDescription());

        int stepId = Integer.valueOf(stepRow.getId());
        int stepnumber = stepId + 1;
        String stepNumber = String.valueOf(stepnumber);

        holder.numberTextView.setText(stepNumber);

    }

    @Override
    public int getItemCount() {
        if (mStepsList == null) {
            return 0;
        } else {
            return mStepsList.size();
        }
    }

    public void setData(ArrayList<Steps> steps) {
        if (this.mStepsList != null) {
            this.mStepsList.clear();
        }
        this.mStepsList = steps;
        notifyDataSetChanged();
    }


    public void clear() {
        mStepsList.clear();
    }


    public class StepsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nameTextView;
        TextView numberTextView;

        public StepsAdapterViewHolder(View itemView) {
            super(itemView);
            this.nameTextView = (TextView) itemView.findViewById(R.id.step_name);
            this.numberTextView = (TextView) itemView.findViewById(R.id.step_number);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Steps steps = mStepsList.get(adapterPosition);
            mStepsAdapterClickHandler.onClick(steps);
        }

    }


}

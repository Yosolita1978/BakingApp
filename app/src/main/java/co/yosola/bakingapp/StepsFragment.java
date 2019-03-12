package co.yosola.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.yosola.bakingapp.Adapters.StepsAdapter;
import co.yosola.bakingapp.Model.Recipe;
import co.yosola.bakingapp.Model.Steps;
import timber.log.Timber;

public class StepsFragment extends Fragment implements StepsAdapter.StepsAdapterOnClickHandler {


    RecyclerView mRecyclerView;
    TextView mStepLabel;

    ArrayList<Steps> stepsArrayList;

    Recipe recipe;

    // Final Strings to store state information about the list of steps and list index
    public static final String STEPS_LIST = "steps_list";


    // Define a new interface OnStepsClickListener that triggers a callback in the host activity
    OnStepClickListener mCallback;

    // OnStepsClickListener interface, calls a method in the host activity named onStepSelected
    public interface OnStepClickListener
    {
        void onClick(Steps step);
    }

    public StepsFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Inflate the Steps fragment layout
        View rootView = inflater.inflate(R.layout.fragment_steps_detail, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerview_steps);
        mStepLabel = rootView.findViewById(R.id.steps_label);
        mStepLabel.setText(R.string.steps_label);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            recipe = getArguments().getParcelable("Recipe");

            stepsArrayList = recipe.getSteps();

        }

        if (savedInstanceState != null)
        {
            //Restore the fragment's state here
            stepsArrayList = savedInstanceState.getParcelableArrayList(STEPS_LIST);
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Timber.d(String.valueOf(stepsArrayList.size()));

        StepsAdapter stepsAdapter = new StepsAdapter(stepsArrayList, this);
        mRecyclerView.setAdapter(stepsAdapter);

        // Return the root view
        return rootView;
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try
        {
            mCallback = (OnStepClickListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepSelectedListener");
        }
    }

    @Override
    public void onClick(Steps step) {
        mCallback.onClick(step);

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
        outState.putParcelableArrayList(STEPS_LIST, stepsArrayList);
        super.onSaveInstanceState(outState);
    }
}
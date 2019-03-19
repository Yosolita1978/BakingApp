package co.yosola.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import co.yosola.bakingapp.Model.Recipe;
import co.yosola.bakingapp.Model.Steps;

public class ExoPlayerActivity extends AppCompatActivity {

    public static final String STEPS_LIST = "list";
    public static final String KEY_EXPO_FRAGMENT = "expoFragment";
    public boolean isTablet;
    public int stepIndex;
    public ArrayList<Steps> stepsArrayList;
    public ExoPlayerFragment exoPlayerFragment;
    private Steps stepClicked;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayervideo);


        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            exoPlayerFragment = new ExoPlayerFragment();
            Intent intent = getIntent();

            if (intent != null) {
                stepClicked = intent.getExtras().getParcelable("StepOnClick");
                stepIndex = Integer.valueOf(stepClicked.getId());

                recipe = intent.getExtras().getParcelable("Recipe");
                setTitle(recipe.getName());
                isTablet = getIntent().getBooleanExtra("isTablet", false);
                stepsArrayList = recipe.getSteps();


                Bundle bundle = new Bundle();
                bundle.putParcelable("StepOnClick", stepClicked);
                bundle.putBoolean("isTablet", isTablet);
                bundle.putParcelable("Recipe", recipe);

                exoPlayerFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.expovideo_fragment, exoPlayerFragment).commit();
            }
        } else {
            stepsArrayList = savedInstanceState.getParcelableArrayList(STEPS_LIST);
            exoPlayerFragment = (ExoPlayerFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_EXPO_FRAGMENT);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(STEPS_LIST, stepsArrayList);
        getSupportFragmentManager().putFragment(outState, KEY_EXPO_FRAGMENT, exoPlayerFragment);
        super.onSaveInstanceState(outState);
    }

}
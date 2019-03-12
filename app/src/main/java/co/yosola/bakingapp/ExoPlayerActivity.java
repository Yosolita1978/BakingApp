package co.yosola.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import co.yosola.bakingapp.Model.Recipe;
import co.yosola.bakingapp.Model.Steps;

public class ExoPlayerActivity extends AppCompatActivity {


    private Steps stepClicked;
    private Recipe recipe;
    public boolean isTablet;
    public int stepIndex;
    public ArrayList<Steps> stepsArrayList;
    public static final String STEPS_LIST = "steps_list";
    public static final String KEY_EXOPLAYER = "exoplayer";
    public ExoPlayerFragment exoPlayerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayervideo);

        //Fragment savedInstanceState code based on this github example:
        //https://github.com/nnjoshi14/android-poc/blob/master/FragmentState/app/src/main/java/com/njoshi/androidpoc/fragmentstate/MainActivity.java
        if (savedInstanceState == null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            exoPlayerFragment = new ExoPlayerFragment();

            if (getIntent() != null && getIntent().getExtras() != null) {
                stepClicked = getIntent().getExtras().getParcelable("StepOnClick");
                stepIndex = Integer.valueOf(stepClicked.getId());
                recipe = getIntent().getExtras().getParcelable("Recipe");
                setTitle(recipe.getName());
                isTablet = getIntent().getBooleanExtra("isTablet", false);
                stepsArrayList = recipe.getSteps();

                Bundle bundle = new Bundle();
                bundle.putParcelable("Steps", stepClicked);
                bundle.putBoolean("isTablet", isTablet);
                bundle.putParcelable("Recipe", recipe);

                exoPlayerFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.expovideo_fragment, exoPlayerFragment).commit();
            } }
        else
        {
            stepsArrayList = savedInstanceState.getParcelableArrayList(STEPS_LIST);
            exoPlayerFragment = (ExoPlayerFragment) getSupportFragmentManager().getFragment(savedInstanceState,KEY_EXOPLAYER);

        }}


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putParcelableArrayList(STEPS_LIST, stepsArrayList);
        getSupportFragmentManager().putFragment(outState,KEY_EXOPLAYER, exoPlayerFragment);
        super.onSaveInstanceState(outState);
    }
}
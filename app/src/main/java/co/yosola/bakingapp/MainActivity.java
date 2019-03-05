package co.yosola.bakingapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    public static boolean isTablet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Timber
        Timber.plant(new Timber.DebugTree());

        if (savedInstanceState == null) {
            if (findViewById(R.id.tablet_view) != null) {
                isTablet = true;
                FragmentManager fragmentManager = getSupportFragmentManager();
                MainFragment recipeFragment = new MainFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.tablet_view, recipeFragment)
                        .commit();
            } else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                MainFragment recipeFragment = new MainFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.phone_view, recipeFragment)
                        .commit();
            }
        }

    }
}

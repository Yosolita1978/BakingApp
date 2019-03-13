package co.yosola.bakingapp;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import co.yosola.bakingapp.Model.Recipe;
import co.yosola.bakingapp.Model.Steps;
import timber.log.Timber;

public class ExoPlayerFragment extends Fragment {

    public ArrayList<Steps> stepsArrayList;
    Steps stepClicked;
    Recipe recipe;
    SimpleExoPlayer mExoPlayer;
    SimpleExoPlayerView mPlayerView;
    ImageView thumbnailUrlImage;
    public int stepIndex;
    private long mPlayerPosition;
    String videoUrl;
    Uri videoUrl_Parse;
    Uri thumbnailUrl_Parse;
    String thumbnailUrl;
    Button previousButton;
    Button nextButton;
    TextView stepDescription;
    String stepStringDescription;
    private boolean autoPlay = true;
    boolean isTablet;

    public static final String KEY_POSITION = "position";
    public static final String STEPS_LIST = "steps_list";
    public static final String STEP_INSTRUCTIONS = "description";
    public static final String KEY_AUTOPLAY = "autoplay";

    public ExoPlayerFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_exoplayervideo, container, false);
        // Bind the views
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerview);
        thumbnailUrlImage = (ImageView) rootView.findViewById(R.id.thumbnail_url);
        previousButton = (Button) rootView.findViewById(R.id.previous_button);
        nextButton = (Button) rootView.findViewById(R.id.next_button);
        stepDescription = (TextView) rootView.findViewById(R.id.step_description);

        Bundle bundle = this.getArguments();

        if (bundle != null)
        {
            stepClicked = bundle.getParcelable("StepOnClick");
            if (stepClicked != null)
            {
                //Track whether to display a two-pane or single-pane UI
                stepIndex = Integer.valueOf(stepClicked.getId());
                isTablet = getArguments().getBoolean("isTablet");
                recipe = getArguments().getParcelable("Recipe");
                stepsArrayList = recipe.getSteps();

                videoUrl = stepClicked.getVideoURL();
                Timber.d(videoUrl);

                videoUrl_Parse = Uri.parse(videoUrl);

                thumbnailUrl = stepClicked.getThumbnailURL();
                thumbnailUrl_Parse = Uri.parse(thumbnailUrl);


                stepStringDescription = stepClicked.getDescription();

                Timber.d(stepStringDescription);

                stepDescription.setText(stepStringDescription);

                if (thumbnailUrl != null)
                {
                    Picasso.get()
                            .load(thumbnailUrl_Parse)
                            .placeholder(R.drawable.recipedefault)
                            .into(thumbnailUrlImage);
                }
            }

        }

        return rootView;
    }

}
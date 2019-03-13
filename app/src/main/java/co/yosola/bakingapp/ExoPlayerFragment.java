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
    ImageView thumbnail;
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
    public static final String STEP = "StepClicked";
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
        thumbnail = (ImageView) rootView.findViewById(R.id.thumbnail);
        previousButton = (Button) rootView.findViewById(R.id.previous_button);
        nextButton = (Button) rootView.findViewById(R.id.next_button);
        stepDescription = (TextView) rootView.findViewById(R.id.step_description);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            stepClicked = bundle.getParcelable("StepOnClick");
            if (stepClicked != null) {

                stepIndex = Integer.valueOf(stepClicked.getId());
                isTablet = getArguments().getBoolean("isTablet");
                recipe = getArguments().getParcelable("Recipe");
                stepsArrayList = recipe.getSteps();

                videoUrl = stepClicked.getVideoURL();


                videoUrl_Parse = Uri.parse(videoUrl);
                Timber.d(videoUrl_Parse + "");

                thumbnailUrl = stepClicked.getThumbnailURL();
                thumbnailUrl_Parse = Uri.parse(thumbnailUrl);


                stepStringDescription = stepClicked.getDescription();

                //Timber.d(stepStringDescription);

                stepDescription.setText(stepStringDescription);

                toggleButtons(isTablet);
                nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (stepIndex < stepsArrayList.size() - 1) {
                            stepIndex++;
                            stepClicked = stepsArrayList.get(stepIndex);
                            stepDescription.setText(stepClicked.getDescription());
                            //Extract the video uri from the current step
                            videoUrl = stepClicked.getVideoURL();
                            videoUrl_Parse = Uri.parse(videoUrl);
                            Timber.d(videoUrl_Parse + "");
                            mExoPlayer.release();
                            mExoPlayer = null;
                            //Call initializePlayer() by passing the new video uri
                            initializePlayer(videoUrl_Parse);
                        }
                    }
                });

                previousButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (stepIndex > 0) {
                            stepIndex--;
                            stepClicked = stepsArrayList.get(stepIndex);
                            stepDescription.setText(stepClicked.getDescription());
                            //Extract the video uri from the current step
                            videoUrl = stepClicked.getVideoURL();
                            videoUrl_Parse = Uri.parse(videoUrl);
                            Timber.d(videoUrl_Parse + "");
                            mExoPlayer.release();
                            mExoPlayer = null;
                            //Call initializePlayer() by passing the new video uri
                            initializePlayer(videoUrl_Parse);
                        }
                    }
                });
            }

            if (savedInstanceState != null) {
                stepClicked = savedInstanceState.getParcelable(STEP);
                mPlayerPosition = savedInstanceState.getLong(KEY_POSITION);
                autoPlay = savedInstanceState.getBoolean(KEY_AUTOPLAY, true);
            }
        }


        return rootView;
    }


    //Helper method to show or not the buttons in the fragment.
    public void toggleButtons(boolean isTablet){
        if(isTablet){
            previousButton.setVisibility(View.INVISIBLE);
            nextButton.setVisibility(View.INVISIBLE);
        } else{
            previousButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);

        }
    }

    //ExoPlayer code based on: https://codelabs.developers.google.com/codelabs/exoplayer-intro/#2
    public void initializePlayer(Uri videoUrl) {

        checkThumbnail(videoUrl);

        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer((SimpleExoPlayer) mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), String.valueOf(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(videoUrl,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            if (mPlayerPosition != C.TIME_UNSET) {
                mExoPlayer.seekTo(mPlayerPosition);
            }
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    //Helper method to check if there is a url in that specific step
    public void checkThumbnail(Uri videoUrl){
        if(videoUrl == null || String.valueOf(videoUrl).isEmpty()){
            mPlayerView.setVisibility(View.INVISIBLE);
            thumbnail.setVisibility(View.VISIBLE);
            thumbnail.setImageDrawable(getResources().getDrawable(R.drawable.nosignal));
        } else {
            mPlayerView.setVisibility(View.VISIBLE);
            thumbnail.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23 || mExoPlayer == null) {
            initializePlayer(videoUrl_Parse);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
        }
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23 || mExoPlayer != null) {
            mExoPlayer.getCurrentPosition();
        }
        releasePlayer();
    }


    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //First Hide other objects (listview or recyclerview), better hide them using Gone.
            stepDescription.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            previousButton.setVisibility(View.GONE);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width =  RelativeLayout.LayoutParams.MATCH_PARENT;
            params.height= RelativeLayout.LayoutParams.MATCH_PARENT;
            mPlayerView.setLayoutParams(params);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //unhide your objects here.
            stepDescription.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            previousButton.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width =  RelativeLayout.LayoutParams.MATCH_PARENT;
            params.height=600;
            mPlayerView.setLayoutParams(params);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
        outState.putLong(KEY_POSITION, mPlayerPosition);
        outState.putParcelable(STEP, stepClicked);
        outState.putBoolean(KEY_AUTOPLAY, autoPlay);
        super.onSaveInstanceState(outState);
    }

}
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
            stepClicked = getArguments().getParcelable("StepOnClick");
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
            if (isTablet)
            {
                previousButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.INVISIBLE);

            } else
            {
                previousButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                //Next and previous buttons code based on the following stackoverflow thread:
                //https://stackoverflow.com/questions/45253477/implementing-next-button-in-audio-player-android
                nextButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (stepIndex < stepsArrayList.size() - 1)
                        {
                            //Add or subtract the position in 1
                            stepIndex++;
                            //Using the position, get the current step from the steps list
                            stepClicked = stepsArrayList.get(stepIndex);
                            stepDescription.setText(stepClicked.getDescription());
                            //Extract the video uri from the current step
                            videoUrl = stepClicked.getVideoURL();
                            //Log.d("VideoUrlNext ", stepClicked.getVideoUrl());
                            Timber.d(videoUrl);
                            videoUrl_Parse = Uri.parse(videoUrl);
                            mExoPlayer.release();
                            mExoPlayer = null;
                            //Call initializePlayer() by passing the new video uri
                            initializePlayer(videoUrl_Parse);
                        }
                    }
                });

                previousButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (stepIndex > 0)
                        {
                            stepIndex--;
                            //Using the position, get the current step from the steps list
                            stepClicked = stepsArrayList.get(stepIndex);
                            stepDescription.setText(stepClicked.getDescription());
                            //Extract the video uri from the current step
                            videoUrl = stepClicked.getVideoURL();
                            Timber.d(videoUrl);
                            videoUrl_Parse = Uri.parse(videoUrl);
                            //Call initializePlayer() by passing the new video uri
                            initializePlayer(videoUrl_Parse);
                        }
                    }
                });
            }
        }
        if (savedInstanceState != null)
        {
            stepsArrayList = savedInstanceState.getParcelableArrayList(STEPS_LIST);
            mPlayerPosition = savedInstanceState.getLong(KEY_POSITION);
            autoPlay = savedInstanceState.getBoolean(KEY_AUTOPLAY, true);
        }

        return rootView;
    }

    //ExoPlayer code based on: https://codelabs.developers.google.com/codelabs/exoplayer-intro/#2
    public void initializePlayer(Uri videoUrl)
    {
        if (mExoPlayer == null)
        {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer((SimpleExoPlayer) mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(videoUrl,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            if (mPlayerPosition != C.TIME_UNSET)
            {
                mExoPlayer.seekTo(mPlayerPosition);
            }
            mExoPlayer.setPlayWhenReady(autoPlay);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (Util.SDK_INT > 23 || mExoPlayer == null)
        {
            initializePlayer(videoUrl_Parse);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mExoPlayer != null)
        {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
        }
        if (Util.SDK_INT <= 23)
        {
            releasePlayer();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null))
        {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (Util.SDK_INT > 23 || mExoPlayer != null)
        {
            mExoPlayer.getCurrentPosition();
        }
        releasePlayer();
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer()
    {
        if (mExoPlayer != null)
        {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            autoPlay = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    //Code to implement ExoPlayer full screen on a landscape mobile layout. Based on the most upvoted answer in this post:
    //https://stackoverflow.com/questions/46713761/how-to-play-video-full-screen-in-landscape-using-exoplayer/46736838#46736838
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            //First Hide other objects (listview or recyclerview), better hide them using Gone.
            stepDescription.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            previousButton.setVisibility(View.GONE);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width =  RelativeLayout.LayoutParams.MATCH_PARENT;
            params.height= RelativeLayout.LayoutParams.MATCH_PARENT;
            mPlayerView.setLayoutParams(params);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
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
        outState.putParcelableArrayList(STEPS_LIST, stepsArrayList);
        outState.putLong(KEY_POSITION, mPlayerPosition);
        outState.putString(STEP_INSTRUCTIONS, stepStringDescription);
        outState.putBoolean(KEY_AUTOPLAY, autoPlay);
        super.onSaveInstanceState(outState);
    }
}
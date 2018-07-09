package com.yash1300.trailblazerr.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.yash1300.trailblazerr.R;
import com.yash1300.trailblazerr.Utils.RealmController;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BeginSessionActivity extends AppCompatActivity {

    @BindView(R.id.chronometer_secs) TextView seconds;
    @BindView(R.id.chronometer_mins) TextView minutes;
    @BindView(R.id.chronometer_hours) TextView hours;
    @BindView(R.id.start_btn) LinearLayout start;
    @BindView(R.id.pause_btn) LinearLayout pause;
    @BindView(R.id.start_img) ImageView startImg;
    @BindView(R.id.pause_img) ImageView pauseImg;
    @BindView(R.id.start_btn_text) TextView startText;
    @BindView(R.id.pause_btn_text) TextView pauseText;
    @BindView(R.id.speaker_symbol) ImageView speaker;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.sub_title) TextView subtitle;
    @BindView(R.id.play_to_begin) RelativeLayout playFirstTime;
    @BindView(R.id.home_button) ImageView homeButton;
    @BindView(R.id.play_to_begin_txt) TextView play2beginTxt;

    boolean started = false, firstTime = true;
    Handler mHandler, foreverHandler, pauseHandler;
    Runnable runnable, foreverRunnable, pauseRunnable;
    long pauseSeconds = 0;
    long totalPauseDuration = 0;
    long pauseStartTime = 0;
    long startTime = 0;
    long millis, secs, mins, heures = 0;
    long foreverMillis, foreverSecs, foreverMins, foreverHeures = 0;

    MediaPlayer mediaPlayer;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
        saveDataInDB();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_session);
        ButterKnife.bind(this);

        SharedPreferences preferences = getSharedPreferences("mainCache", MODE_PRIVATE);
        Boolean firstTime = preferences.getBoolean("firstTime", true);

        if (firstTime) {
            TapTargetView.showFor(BeginSessionActivity.this,
                    TapTarget.forView(homeButton, "Save the chant-history and Back to home page!", "After you're done chanting, press this to go back to home screen and add the current duration of chanting to history")
                            .outerCircleColor(R.color.apple_green)
                            .outerCircleAlpha(0.9f)
                            .transparentTarget(true), new TapTargetView.Listener() {
                        @Override
                        public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                            super.onTargetDismissed(view, userInitiated);
                            TapTargetView.showFor(BeginSessionActivity.this,
                                    TapTarget.forView(start, "To start/resume chanting!")
                                            .outerCircleColor(R.color.apple_green)
                                            .outerCircleAlpha(0.9f)
                                            .transparentTarget(true), new TapTargetView.Listener() {

                                        @Override
                                        public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                                            super.onTargetDismissed(view, userInitiated);
                                            TapTargetView.showFor(BeginSessionActivity.this,
                                                    TapTarget.forView(pause, "To pause chanting!")
                                                            .outerCircleColor(R.color.apple_green)
                                                            .outerCircleAlpha(0.9f)
                                                            .transparentTarget(true));
                                        }
                                    });
                        }
                    });
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstTime", false);
            editor.apply();
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.chant);
        foreverHandler = new Handler();
        mHandler = new Handler();
        pauseHandler = new Handler();

        runnable = new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                millis = System.currentTimeMillis() - startTime;
                secs = (millis/1000);
                mins = secs/60;
                heures = mins/60;
                updateTime();
                mHandler.postDelayed(runnable, 10L);
            }
        };

        foreverRunnable = new Runnable() {
            @Override
            public void run() {
                foreverMillis = System.currentTimeMillis() - startTime;
                foreverSecs = (foreverMillis/1000);
                foreverMins = foreverSecs/60;
                foreverHeures = foreverMins/60;
                updateTime();
                foreverHandler.postDelayed(foreverRunnable, 10L);
            }
        };

        pauseRunnable = new Runnable() {
            @Override
            public void run() {
                pauseSeconds = (System.currentTimeMillis() - pauseStartTime)/1000;
                pauseHandler.postDelayed(pauseRunnable, 10L);
            }
        };

        playFirstTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                pause();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                saveDataInDB();
                startActivity(new Intent(BeginSessionActivity.this, MainActivity.class));
            }
        });

    }

    private void start() {
        if (!started) {
            started = true;
            mediaPlayer.start();
            startText.setTextColor(getResources().getColor(R.color.apple_green));
            pauseText.setTextColor(Color.parseColor("#757575"));
            title.setTextColor(Color.parseColor("#2e2e2e"));
            subtitle.setTextColor(Color.parseColor("#838383"));
            playFirstTime.setBackgroundColor(Color.parseColor("#D9D9D9"));
            play2beginTxt.setText("Now Chanting...");
            startImg.setColorFilter(getResources().getColor(R.color.apple_green), PorterDuff.Mode.SRC_ATOP);
            pauseImg.setColorFilter(Color.parseColor("#757575"), PorterDuff.Mode.SRC_ATOP);
            speaker.setColorFilter(Color.parseColor("#3E3E3E"), PorterDuff.Mode.SRC_ATOP);
            if (firstTime) {
                firstTime = false;
                foreverHandler.postDelayed(foreverRunnable, 10L);
            }
            if (startTime == 0)
                startTime = System.currentTimeMillis();
            mHandler.postDelayed(runnable, 10L);
            totalPauseDuration += pauseSeconds;
            pauseHandler.removeCallbacks(pauseRunnable);
        } else {
            Toast.makeText(BeginSessionActivity.this, "Already running", Toast.LENGTH_SHORT).show();
        }
    }

    private void pause() {
        if (!firstTime) {
            started = false;
            mediaPlayer.pause();
            mHandler.removeCallbacks(runnable);
            pauseStartTime = System.currentTimeMillis();
            pauseHandler.postDelayed(pauseRunnable, 10L);
            startText.setTextColor(Color.parseColor("#757575"));
            startText.setText("Resume");
            pauseText.setTextColor(getResources().getColor(R.color.apple_green));
            title.setTextColor(Color.parseColor("#882e2e2e"));
            subtitle.setTextColor(Color.parseColor("#88838383"));
            playFirstTime.setBackgroundColor(getResources().getColor(R.color.apple_green));
            play2beginTxt.setText("Resume Chanting");
            startImg.setColorFilter(Color.parseColor("#757575"), PorterDuff.Mode.SRC_ATOP);
            pauseImg.setColorFilter(getResources().getColor(R.color.apple_green), PorterDuff.Mode.SRC_ATOP);
            speaker.setColorFilter(Color.parseColor("#9E9E9E"), PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void updateTime() {
        if (started) {
            seconds.setText(String.format("%02d", ((2*secs)-foreverSecs-totalPauseDuration)%60));
            minutes.setText(String.format(" %02d ", ((2*secs)-foreverSecs-totalPauseDuration)/60));
            hours.setText(String.format("%02d", ((2*secs)-foreverSecs-totalPauseDuration)/3600));
        }
    }

    private void saveDataInDB() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(new Date());
        int duration = Integer.parseInt(seconds.getText().toString().trim())
                + (Integer.parseInt(minutes.getText().toString().trim())*60)
                + (Integer.parseInt(hours.getText().toString().trim())*3600);
        RealmController.getInstance().addOrUpdateASession(date, Long.valueOf(duration));
    }
}

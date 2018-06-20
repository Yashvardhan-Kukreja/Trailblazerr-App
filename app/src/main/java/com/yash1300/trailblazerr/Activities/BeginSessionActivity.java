package com.yash1300.trailblazerr.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yash1300.trailblazerr.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BeginSessionActivity extends AppCompatActivity {

    @BindView(R.id.chronometer_secs) TextView seconds;
    @BindView(R.id.chronometer_mins) TextView minutes;
    @BindView(R.id.chronometer_hours) TextView hours;
    @BindView(R.id.start_btn) ImageView start;
    @BindView(R.id.pause_btn) ImageView pause;
    @BindView(R.id.start_btn_text) TextView startText;
    @BindView(R.id.pause_btn_text) TextView pauseText;
    @BindView(R.id.speaker_symbol) ImageView speaker;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.sub_title) TextView subtitle;
    @BindView(R.id.play_to_begin) RelativeLayout playFirstTime;
    @BindView(R.id.home_button) ImageView homeButton;

    boolean started = false;
    Handler mHandler;
    Runnable runnable;
    long startTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_session);
        ButterKnife.bind(this);

        mHandler = new Handler();

        runnable = new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                long millis = System.currentTimeMillis() - startTime;
                long secs = millis/1000;
                System.out.println(Long.toString(secs));
                long mins = secs/60;
                long heures = mins/60;
                seconds.setText(String.format("%02d", secs%60));
                minutes.setText(String.format(" %02d ", mins));
                hours.setText(String.format("%02d", heures));
                mHandler.postDelayed(runnable, 10L);
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
                startActivity(new Intent(BeginSessionActivity.this, MainActivity.class));
            }
        });

    }

    private void start() {
        if (!started) {
            started = true;
            startText.setTextColor(getResources().getColor(R.color.apple_green));
            pauseText.setTextColor(Color.parseColor("#757575"));
            title.setTextColor(Color.parseColor("#2e2e2e"));
            subtitle.setTextColor(Color.parseColor("#838383"));
            start.setColorFilter(getResources().getColor(R.color.apple_green), PorterDuff.Mode.SRC_ATOP);
            pause.setColorFilter(Color.parseColor("#757575"), PorterDuff.Mode.SRC_ATOP);
            speaker.setColorFilter(Color.parseColor("#3E3E3E"), PorterDuff.Mode.SRC_ATOP);
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            mHandler.postDelayed(runnable, 10L);
        } else {
            Toast.makeText(BeginSessionActivity.this, "Already running", Toast.LENGTH_SHORT).show();
        }
    }

    private void pause() {
        started = false;
        startText.setTextColor(Color.parseColor("#757575"));
        pauseText.setTextColor(getResources().getColor(R.color.apple_green));
        title.setTextColor(Color.parseColor("#882e2e2e"));
        subtitle.setTextColor(Color.parseColor("#88838383"));
        start.setColorFilter(Color.parseColor("#757575"), PorterDuff.Mode.SRC_ATOP);
        pause.setColorFilter(getResources().getColor(R.color.apple_green), PorterDuff.Mode.SRC_ATOP);
        speaker.setColorFilter(Color.parseColor("#9E9E9E"), PorterDuff.Mode.SRC_ATOP);
        mHandler.removeCallbacks(runnable);
        startTime = System.currentTimeMillis();

    }
}

package com.yash1300.trailblazerr.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yash1300.trailblazerr.Models.DaimokuSession;
import com.yash1300.trailblazerr.R;
import com.yash1300.trailblazerr.Utils.RealmController;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.begin_daimoku_button) FrameLayout beginDaimoku;
    @BindView(R.id.today_duration) TextView todayDurationView;
    @BindView(R.id.yesterday_duration) TextView yesterdayDurationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        long todayDuration=0, yesterdayDuration=0;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String todayDate = simpleDateFormat.format(new Date());

        RealmResults<DaimokuSession> history = RealmController.getInstance().getHistory();

        if (history.size() >= 1) {
            todayDuration = RealmController.getInstance().findSessionByDate(todayDate).getDuration_in_secs();
            yesterdayDuration = RealmController.getInstance().yesterdaySession(todayDate).getDuration_in_secs();
        }

        String finalTodayString = finalMinutesOrSecondsString(todayDuration);
        String finalYesterdayString = finalMinutesOrSecondsString(yesterdayDuration);

        todayDurationView.setText(finalTodayString);
        yesterdayDurationView.setText(finalYesterdayString);

        beginDaimoku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BeginSessionActivity.class));
            }
        });

    }

    @NonNull
    private String finalMinutesOrSecondsString(Long seconds) {
        if (seconds >= 60) {
            long minutes = seconds/60;
            long remainingSecs = seconds%60;
            String secondPartMin = "minutes", secondPartSec = "seconds";
            if (minutes == 1) {
                secondPartMin = "minute";
            }
            if (remainingSecs == 1) {
                secondPartSec = "second";
            }
            if (remainingSecs == 0) {
                return (Long.toString(minutes) + secondPartMin);
            }
            return (String.format("%d %s, %d %s", minutes, secondPartMin, remainingSecs, secondPartSec));
        }
        return (Long.toString(seconds) + " seconds");
    }
}

package com.yash1300.trailblazerr.Activities;

import android.content.Intent;
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
        Toast.makeText(this, history.toString(), Toast.LENGTH_SHORT).show();
        System.out.println(history.toString());
        System.out.println(todayDate.toString());

        if (history.size() >= 1) {
            todayDuration = RealmController.getInstance().findSessionByDate(todayDate).getDuration_in_secs();
            yesterdayDuration = RealmController.getInstance().yesterdaySession(todayDate).getDuration_in_secs();
        }

        todayDurationView.setText(Long.toString(todayDuration) + " seconds");
        yesterdayDurationView.setText(Long.toString(yesterdayDuration) + " seconds");

        beginDaimoku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BeginSessionActivity.class));
            }
        });

    }
}

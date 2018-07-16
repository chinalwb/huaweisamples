package com.wliu.huawei.samples;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import com.wliu.huawei.samples.sun_rise_set.SunRiseSetView;

public class MainActivity extends AppCompatActivity {

    private SunRiseSetView sunRiseSetView;

    private SeekBar sunseekBar, pathseekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sunRiseSetView = findViewById(R.id.sunrisesetview);
        sunseekBar = findViewById(R.id.sunseekbar);

        sunseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sunRiseSetView.animateSun(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        pathseekBar = findViewById(R.id.pathseekbar);

        pathseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sunRiseSetView.setAngle(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
package com.ken.carracing;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class SettingActivity extends Activity {
    private SeekBar sbVolume;
    private Button btnSave, btnLogout;
    private AudioManager audioManager;
    private TextView txtVolume;
    private Switch sMute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        sbVolume = findViewById(R.id.seekbar_sound);
        btnSave = findViewById(R.id.button_save);
        btnLogout = findViewById(R.id.button_logout);
        sMute = findViewById(R.id.switch_mute);
        txtVolume = findViewById(R.id.txt_Volume);

        sbVolume.setMax(maxVolume);
        sbVolume.setProgress(currentVolume);
        updateVolumeText(currentVolume, maxVolume);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("volume_level", progress);
                editor.apply();

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                updateVolumeText(progress, maxVolume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        sMute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                if (isChecked) {
                    int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    editor.putInt("saved_volume_level", currentVolume);
                    editor.putBoolean("is_muted", true);
                    editor.apply();
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    sbVolume.setProgress(0);
                    updateVolumeText(0, maxVolume);
                } else {
                    int savedVolume = preferences.getInt("saved_volume_level", audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2);
                    editor.putBoolean("is_muted", false);
                    editor.apply();
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, savedVolume, 0);
                    sbVolume.setProgress(savedVolume);
                    updateVolumeText(savedVolume, maxVolume);
                }
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isMuted = preferences.getBoolean("is_muted", false);
        sMute.setChecked(isMuted);
        if (isMuted) {
            sbVolume.setProgress(0);
            updateVolumeText(0, maxVolume);
        } else {
            updateVolumeText(currentVolume, maxVolume);
        }
    }

    private void updateVolumeText(int currentVolume, int maxVolume) {
        int volumePercentage = (int) (((float) currentVolume / maxVolume) * 100);
        txtVolume.setText("Volume: " + volumePercentage + "%");
    }
}

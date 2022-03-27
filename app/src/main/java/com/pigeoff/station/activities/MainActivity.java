package com.pigeoff.station.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pigeoff.station.R;
import com.pigeoff.station.fragment.GameFragment;
import com.pigeoff.station.fragment.SettingsBottomSheetFragment;
import com.pigeoff.station.util.Utils;

public class MainActivity extends AppCompatActivity {
    private final String GAME_FRAGMENT_TAG = "gamefragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateUI();
    }

    private void updateUI() {
        GameFragment gameFragment = new GameFragment();

        BottomSheetDialogFragment settingsSheet = new SettingsBottomSheetFragment();

        if (getSupportFragmentManager().findFragmentByTag(GAME_FRAGMENT_TAG) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, gameFragment, GAME_FRAGMENT_TAG)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentByTag(GAME_FRAGMENT_TAG))
                    .replace(R.id.frameLayout, gameFragment, GAME_FRAGMENT_TAG).commit();
        }

        gameFragment.setOnSettingsBtnClickListener(new GameFragment.OnSettingsBtnClickListener() {
            @Override
            public void onSettingsBtnClickListener() {
                settingsSheet.show(getSupportFragmentManager(), SettingsBottomSheetFragment.TAG);
            }
        });

        gameFragment.setOnRestartActionListener(new GameFragment.OnRestartActionListener() {
            @Override
            public void onRestartActionListener() {
                updateUI();
            }
        });

        // Show bottom sheet when first start
        final SharedPreferences pref = getSharedPreferences(Utils.PREF, Context.MODE_PRIVATE);
        boolean firstStart = pref.getBoolean(Utils.KEY_STARTUP, true);
        if (firstStart) {
            Log.i("First start ?", String.valueOf(firstStart));
            settingsSheet.show(getSupportFragmentManager(), SettingsBottomSheetFragment.TAG);
        }
    }
}
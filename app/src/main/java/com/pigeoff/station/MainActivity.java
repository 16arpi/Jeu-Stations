package com.pigeoff.station;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GameFragment gameFragment = new GameFragment();
        final BottomSheetDialogFragment settingsSheet = new SettingsBottomSheetFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, gameFragment).commit();
        gameFragment.setOnSettingsBtnClickListener(new GameFragment.OnSettingsBtnClickListener() {
            @Override
            public void onSettingsBtnClickListener() {
                settingsSheet.show(getSupportFragmentManager(), SettingsBottomSheetFragment.TAG);
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
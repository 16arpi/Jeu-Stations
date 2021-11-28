package com.pigeoff.station.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.pigeoff.station.R;
import com.pigeoff.station.data.PartieState;
import com.pigeoff.station.util.Utils;

import java.util.ArrayList;

public class SettingsBottomSheetFragment extends BottomSheetDialogFragment {
    public static final String TAG = "SettingsBottomSheet";

    private SettingsBottomSheetFragment fragment;
    private SharedPreferences pref;
    private ImageButton btnClose;
    private TextView txtViewWebsite;
    private TextView txtViewPlay;
    private Switch switchSuggestion;
    private Switch switchStorage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragment = this;
        pref = requireContext().getSharedPreferences(Utils.PREF, Context.MODE_PRIVATE);
        btnClose = view.findViewById(R.id.imageButtonClose);
        txtViewWebsite = view.findViewById(R.id.textViewWebsite);
        txtViewPlay = view.findViewById(R.id.textViewPlaystore);
        switchSuggestion = view.findViewById(R.id.switchSuggestions);
        switchStorage = view.findViewById(R.id.switchStorage);

        btnClose.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.dismiss();
            }
        });

        // Setting switch state and action
        boolean state = pref.getBoolean(Utils.KEY_SUGGESTION, true);
        switchSuggestion.setChecked(state);
        switchSuggestion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.edit().putBoolean(Utils.KEY_SUGGESTION, isChecked).apply();
            }
        });

        // Setting switch state and action
        boolean storage = pref.getBoolean(Utils.KEY_SAVED, true);
        switchStorage.setChecked(storage);
        switchStorage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PartieState partieState = new PartieState(requireContext());
                if (!isChecked) {
                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle(R.string.dialog_storage_t)
                            .setMessage(R.string.dialog_storage_p)
                            .setNegativeButton(R.string.dialog_storage_btn_no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    switchStorage.setChecked(true);
                                }
                            })
                            .setPositiveButton(R.string.dialog_storage_btn_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    partieState.setSavedOnOff(isChecked);
                                    partieState.setSavedScore(0);
                                    partieState.setSavedStationSuccession(new ArrayList<>());
                                }
                            })
                            .show();
                } else {
                    partieState.setSavedOnOff(isChecked);
                }
            }
        });

        txtViewWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = requireContext().getString(R.string.website_url);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        txtViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = requireContext().getString(R.string.playstore_url);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        pref.edit().putBoolean(Utils.KEY_STARTUP, false).apply();
    }
}

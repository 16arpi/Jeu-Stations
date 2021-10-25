package com.pigeoff.station;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SettingsBottomSheetFragment extends BottomSheetDialogFragment {
    public static final String TAG = "SettingsBottomSheet";

    private SettingsBottomSheetFragment fragment;
    private SharedPreferences pref;
    private ImageButton btnClose;
    private TextView txtViewWebsite;
    private TextView txtViewPlay;

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

        btnClose.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.dismiss();
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

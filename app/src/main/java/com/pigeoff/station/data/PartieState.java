package com.pigeoff.station.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pigeoff.station.util.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PartieState {
    private boolean savedOnOff;
    private List<Integer> savedStationSuccession;
    private Gson gsonBuilder;
    private Type savedStationsListType;
    private int savedScore;
    private final SharedPreferences pref;

    public PartieState(Context context) {
        // Setting up GSON Client
        savedStationsListType = new TypeToken<List<Integer>>(){}.getType();
        gsonBuilder = new GsonBuilder().create();

        // Setting up shared preferences
        pref = context.getSharedPreferences(Utils.PREF, Context.MODE_PRIVATE);
        savedOnOff = pref.getBoolean(Utils.KEY_SAVED, true);
        savedScore = pref.getInt(Utils.KEY_SAVED_SCORE, 0);

        // Setting up saved stations (with precaution)
        try {
            String stationsJSON = pref.getString(Utils.KEY_SAVED_STATIONS, "[]");
            savedStationSuccession = gsonBuilder.fromJson(stationsJSON, savedStationsListType);
        } catch (Exception e) {
            Log.i("ERROR", e.toString());
            savedStationSuccession = new ArrayList<>();
        }
    }

    public boolean getSavedOnOff() {
        return pref.getBoolean(Utils.KEY_SAVED, true);
    }

    public int getSavedScore() {
        return savedScore;
    }

    public List<Integer> getSavedStations() {
        return savedStationSuccession;
    }

    public void setSavedOnOff(boolean onOff) {
        savedOnOff = onOff;
        pref.edit().putBoolean(Utils.KEY_SAVED, onOff).apply();
    }

    public void setSavedScore(int score) {
        savedScore = score;
        pref.edit().putInt(Utils.KEY_SAVED_SCORE, score).apply();
    }

    public void setSavedStationSuccession(List<Station> stations) {
        List<Integer> stationsId = new ArrayList<>();

        for (Station s : stations) stationsId.add(0, s.getId());

        try {
            savedStationsListType = new TypeToken<List<Integer>>(){}.getType();
            String serializedStations = gsonBuilder.toJson(stationsId, savedStationsListType);

            savedStationSuccession = stationsId;
            pref.edit().putString(Utils.KEY_SAVED_STATIONS, serializedStations).apply();
        } catch (Exception e) {
            Log.i("ERROR", e.toString());
        }
    }
}

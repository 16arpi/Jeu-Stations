package com.pigeoff.station.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pigeoff.station.data.Station;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JSONClient {
    private static String jsonData;

    public JSONClient(Context context) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = context.getAssets().open("stations-ratp.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8 ));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (Exception e) {
            Log.i("Erreur reading file", e.toString());
        } finally {
            jsonData = sb.toString();
        }
    }

    public ArrayList<Station> getStationsFromJSON() {
        Gson g = new GsonBuilder().create();

        try {
            ArrayList<Station> finalStation = new ArrayList<>();
            Type listType = new TypeToken<List<Utils.Stat>>(){}.getType();
            List<Utils.Stat> list = g.fromJson(jsonData, listType);

            for (Utils.Stat s : list) {
                Station nS = new Station(s.id, s.station, s.lignes);
                finalStation.add(nS);
            }

            Log.i("STATION 0", finalStation.get(0).getStation());

            return new ArrayList<Station>(finalStation);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }
}

package com.pigeoff.station.util;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.snackbar.Snackbar;
import com.pigeoff.station.R;
import com.pigeoff.station.data.Station;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;

public class Utils {
    public static final String PREF = "sharedpref";
    public static final String KEY_SCORE = "shareddata";
    public static final String KEY_STARTUP = "sharedstart";
    public static final String KEY_SUGGESTION = "sharedsuggest";
    public static final String KEY_SAVED = "sharedstorage";
    public static final String KEY_SAVED_SCORE = "sharedstoragescore";
    public static final String KEY_SAVED_STATIONS = "sharedstoragestations";

    private static HashMap<Integer, Integer> colorsRessources = new HashMap<>();
    private static HashMap<Integer, String> lignesNames = new HashMap<>();
    private static List<Integer> completedLines = new ArrayList<>();

    static class Stat {
        Integer id;
        String station;
        List<Integer> lignes;
    }

    public Utils() {
        // setting up colors
        colorsRessources.put(1, R.color.m1);
        colorsRessources.put(2, R.color.m2);
        colorsRessources.put(3, R.color.m3);
        colorsRessources.put(4, R.color.m4);
        colorsRessources.put(5, R.color.m5);
        colorsRessources.put(6, R.color.m6);
        colorsRessources.put(7, R.color.m7);
        colorsRessources.put(8, R.color.m8);
        colorsRessources.put(9, R.color.m9);
        colorsRessources.put(10, R.color.m10);
        colorsRessources.put(11, R.color.m11);
        colorsRessources.put(12, R.color.m12);
        colorsRessources.put(13, R.color.m13);
        colorsRessources.put(14, R.color.m14);
        colorsRessources.put(33, R.color.m33);
        colorsRessources.put(77, R.color.m77);

        lignesNames.put(1, "1");
        lignesNames.put(2, "2");
        lignesNames.put(3, "3");
        lignesNames.put(4, "4");
        lignesNames.put(5, "5");
        lignesNames.put(6, "6");
        lignesNames.put(7, "7");
        lignesNames.put(8, "8");
        lignesNames.put(9, "9");
        lignesNames.put(10, "10");
        lignesNames.put(11, "11");
        lignesNames.put(12, "12");
        lignesNames.put(13, "13");
        lignesNames.put(14, "14");
        lignesNames.put(33, "3bis");
        lignesNames.put(77, "7bis");
    }

    public Integer getColorFromTwoStations(Station st1, Station st2) {
        for (int i : st1.getLignes()) {
            if (st2.getLignes().contains(i)) {
                return colorsRessources.get(i);
            }
        }
        return colorsRessources.get(1);
    }

    public static Integer getColorFromLigne(Integer ligne) {
        return colorsRessources.get(ligne);
    }

    public static Station getStationFromName(ArrayList<Station> stations, String query) {
        TreeMap<Integer, Station> probas = new TreeMap<>();

        // simplification of query
        query = simplifyComplexString(query);

        // simplification of stations name
        for (Station s : stations) {
            String simpleStationName = simplifyComplexString(s.getStation());
            int diff = new LevenshteinDistance().apply(simpleStationName, query);
            probas.put(diff, s);
        }

        Map.Entry best = probas.firstEntry();
        if ((int)best.getKey() < 4 || probas.size() < 4) {
            return (Station) best.getValue();
        } else {
            return null;
        }
    }

    public static List<Station> getThreeBestStations(List<Station> stations, List<Station> current, String query) {
        List<Station> suggestions = new ArrayList<>();
        TreeMap<Integer, Station> probas = new TreeMap<>();

        // simplification of query
        query = simplifyComplexString(query);

        // simplification of stations name
        int n = 0;
        for (Station s : stations) {
            String simpleStationName = simplifyComplexString(s.getStation());
            if (simpleStationName.contains(query) && n <3 && !current.contains(s)) {
                suggestions.add(s);
                ++n;
            }
        }

        return suggestions;
    }

    public static boolean isStationAcceptable(Station preStation, Station nextStation) {
        for (int i : preStation.getLignes()) {
            if (nextStation.getLignes().contains(i)) {
                return true;
            }
        }
        return false;
    }

    public List<Integer> isStationLastOfItsLine(
            List<Station> allStations,
            List<Station> playedStations,
            Station station) {

            HashMap<Integer, Integer> nbByLignes = nbOfStationsFromLignes(allStations);
            HashMap<Integer, Integer> nbByLignesCompteur = new HashMap<>();

            nbByLignesCompteur.put(1, 0);
            nbByLignesCompteur.put(2, 0);
            nbByLignesCompteur.put(3, 0);
            nbByLignesCompteur.put(4, 0);
            nbByLignesCompteur.put(5, 0);
            nbByLignesCompteur.put(6, 0);
            nbByLignesCompteur.put(7, 0);
            nbByLignesCompteur.put(8, 0);
            nbByLignesCompteur.put(9, 0);
            nbByLignesCompteur.put(10, 0);
            nbByLignesCompteur.put(11, 0);
            nbByLignesCompteur.put(12, 0);
            nbByLignesCompteur.put(13, 0);
            nbByLignesCompteur.put(14, 0);
            nbByLignesCompteur.put(33, 0);
            nbByLignesCompteur.put(77, 0);

            List<Station> checkedStations = new ArrayList<>();

            for (Station s : playedStations) {
                if (!checkedStations.contains(s)) {
                    for (Integer l : s.getLignes()) {
                        nbByLignesCompteur.put(l, nbByLignesCompteur.get(l) + 1);
                        String str = "ADD : ("+s.getStation()+")Ligne "+l+", nb stations : "+nbByLignesCompteur.get(l)+"/"+nbByLignes.get(l);
                        Log.i("STATIONS COMPTEUR", str);
                    }
                    checkedStations.add(s);
                }
            }

        return getCompletedLines(nbByLignes, nbByLignesCompteur, playedStations, station);
    }

    public static void showMessage(View parent, String txt) {
        Snackbar snack = Snackbar.make(parent, txt, Snackbar.LENGTH_SHORT);
        View view = snack.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snack.show();
    }

    public static String getAvailableLignesFromStation(Station station) {
        String pre = "Métro ligne(s) ";
        List<String> lignes = new ArrayList<>();
        for (Integer l : station.getLignes()) {
            lignes.add(lignesNames.get(l));
        }
        return pre+StringUtils.join(lignes, ", ");
    }

    public static Station findStationById(List<Station> stations, int id) {
        for (Station s : stations) {
            if (s.getId() == id) return s;
        }
        return null;
    }
    private static int autoCompleteDiff(String str1, String str2) {
        int min = Math.min(str1.length(), str2.length());
        int i = 0;
        int r = 0;
        while (i < min) {
            if (str1.charAt(i) == str2.charAt(i)) {
                r++;
            } else {
                break;
            }
            ++i;
        }
        return r;
    }

    private static String simplifyComplexString(String str) {
        // removing accents
        str = StringUtils.stripAccents(str);
        // removing capital letters
        str = str.toLowerCase(Locale.ROOT);
        //removing everything between letters and numbers
        str = str.replaceAll("\\W+", "");
        return str;
    }

    /* ===== TESTS ===== */
    private static HashMap<Integer, Integer> nbOfStationsFromLignes(List<Station> stations) {
        HashMap<Integer, Integer> compteur = new HashMap<>();
        compteur.put(1, 0);
        compteur.put(2, 0);
        compteur.put(3, 0);
        compteur.put(4, 0);
        compteur.put(5, 0);
        compteur.put(6, 0);
        compteur.put(7, 0);
        compteur.put(8, 0);
        compteur.put(9, 0);
        compteur.put(10, 0);
        compteur.put(11, 0);
        compteur.put(12, 0);
        compteur.put(13, 0);
        compteur.put(14, 0);
        compteur.put(33, 0);
        compteur.put(77, 0);

        for (Station s : stations) {
            for (Integer l : s.getLignes()) {
                compteur.put(l, compteur.get(l) + 1);
            }
        }
        return compteur;

    }

    private static List<Integer> getCompletedLines(
            HashMap<Integer, Integer> nbByLignes,
            HashMap<Integer, Integer> nbByLignesCompteur,
            List<Station> playedStations,
            Station station) {

            List<Integer> result = new ArrayList<>();
            for (Integer l : station.getLignes()) {
                if (nbByLignes.get(l).equals(nbByLignesCompteur.get(l))) {
                    int c = 0;
                    for (Station s : playedStations) {
                        if (s == station) c++;
                    }
                    if (c == 1) {
                        result.add(l);
                    }
                }
            }

            return result;

    }

    public static List<Station> getFourStationsHint(
            List<Station> stationsAll,
            List<Station> stationsPlayed,
            Station lastStation) {

        final HashMap<Integer, Integer> nbStationsByLines = nbOfStationsFromLignes(stationsAll);

        List<Station> fourStations = new ArrayList<>();

        // Cheking if there're enough stations to played (= more than 4)
        if (stationsAll.size()-stationsPlayed.size() > 6) {
            Collections.shuffle(stationsAll);
            int i = 0;
            while (fourStations.size() < 1 && i < stationsAll.size()) {
                if (isStationAcceptable(lastStation, stationsAll.get(i)) && !stationsPlayed.contains(stationsAll.get(i))) {
                    fourStations.add(stationsAll.get(i));
                }
                i++;
            }

            if (fourStations.size() == 1) {
                int y = 0;
                while (fourStations.size() < 4 && y < stationsAll.size() && !stationsPlayed.contains(stationsAll.get(y))) {
                    if (!isStationAcceptable(lastStation, stationsAll.get(y))) fourStations.add(stationsAll.get(y));
                    y++;
                }
            }
        }
        Collections.shuffle(fourStations);
        return fourStations;
    }
}

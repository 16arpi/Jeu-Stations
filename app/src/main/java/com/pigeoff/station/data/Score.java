package com.pigeoff.station.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.pigeoff.station.util.Utils;

public class Score {
    private int scorePartie;
    private int scoreSaved;
    private final SharedPreferences pref;

    public Score(Context context, TextView scoreTxt, TextView highScoreTxt) {
        pref = context.getSharedPreferences(Utils.PREF, Context.MODE_PRIVATE);
        scorePartie = 0;
        scoreSaved = pref.getInt(Utils.KEY_SCORE, 0);
        scoreTxt.setText(String.valueOf(scorePartie));
        highScoreTxt.setText(String.valueOf(scoreSaved));
    }

    public void saveScore(int score, TextView scoreTxt, TextView highScoreTxt) {
        scorePartie+=score;
        if (scorePartie > scoreSaved) {
            scoreSaved+=score;
            pref.edit().putInt(Utils.KEY_SCORE, scoreSaved).apply();
        }
        scoreTxt.setText(String.valueOf(scorePartie));
        highScoreTxt.setText(String.valueOf(scoreSaved));
    }

    public int getScorePartie() {
        return scorePartie;
    }

    public int getScoreSaved() {
        return scoreSaved;
    }
}

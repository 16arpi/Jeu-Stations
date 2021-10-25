package com.pigeoff.station;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Station {
    private Integer id;
    private String station;
    private List<Integer> lignes;
    private Integer color;

    public Station(Integer i, String s, List<Integer> l) {
        id = i;
        station = s;
        lignes = l;

        HashMap<Integer, Integer> colorsRessources = new HashMap<>();
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

        color = colorsRessources.get(lignes.get(0));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public List<Integer> getLignes() {
        return lignes;
    }

    public void setLignes(List<Integer> lignes) {
        this.lignes = lignes;
    }

    public Integer getColor() {
        return color;
    }
}

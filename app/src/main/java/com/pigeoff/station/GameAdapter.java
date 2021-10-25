package com.pigeoff.station;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_INTRO = 0;
    private static final int VIEW_STATION = 1;

    private final Context context;
    private ArrayList<Station> stations;
    private final Utils utils = new Utils();

    public GameAdapter(Context c, ArrayList<Station> s) {
        this.context = c;
        this.stations = s;
    }

    public void addStation(Station s) {
        stations.add(0, s);
        notifyDataSetChanged();
    }

    public Station currentStation() {
        if (stations.size() > 0) {
            return stations.get(0);
        } else {
            return null;
        }
    }

    public List<Station> currentAllStationS() {
        return stations;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return stations.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
       if (position == stations.size()) {
           return VIEW_INTRO;
       } else {
           return VIEW_STATION;
       }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewIntro = LayoutInflater.from(context).inflate(R.layout.adapter_intro, parent, false);
        View viewStation = LayoutInflater.from(context).inflate(R.layout.adapter_game, parent, false);
        switch (viewType) {
            case VIEW_INTRO:
                return new ViewHolderIntro(viewIntro);
            case VIEW_STATION:
                return new ViewHolder(viewStation);
        };
        return new ViewHolder(viewStation);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int index) {
        if (index == stations.size()) {

        }
        else {
            ViewHolder h = (ViewHolder) holder;

            int position = index;
            final Station station = stations.get(position);

            if (getItemCount() == 2) {
                h.viewDotTop.setVisibility(View.GONE);
                h.viewDotBottom.setVisibility(View.GONE);
            } else {
                if (position == 0) {
                    h.viewDotTop.setVisibility(View.VISIBLE);
                    h.viewDotBottom.setVisibility(View.GONE);

                    h.viewDotTop.setBackgroundColor(
                            ContextCompat.getColor(
                                    context, utils.getColorFromTwoStations(
                                            stations.get(position+1), station)));
                    h.viewDotBottom.setBackgroundColor(
                            ContextCompat.getColor(
                                    context, utils.getColorFromTwoStations(
                                            stations.get(position+1), station)));

                } else if (position == getItemCount()-2) {
                    h.viewDotTop.setVisibility(View.GONE);
                    h.viewDotBottom.setVisibility(View.VISIBLE);
                    h.viewDotTop.setBackgroundColor(
                            ContextCompat.getColor(
                                    context, utils.getColorFromTwoStations(
                                            stations.get(position-1), station)));
                    h.viewDotBottom.setBackgroundColor(
                            ContextCompat.getColor(
                                    context, utils.getColorFromTwoStations(
                                            station, station)));
                } else {
                    h.viewDotTop.setVisibility(View.VISIBLE);
                    h.viewDotBottom.setVisibility(View.VISIBLE);
                    h.viewDotTop.setBackgroundColor(
                            ContextCompat.getColor(
                                    context, utils.getColorFromTwoStations(
                                            stations.get(position+1), station)));
                    h.viewDotBottom.setBackgroundColor(
                            ContextCompat.getColor(
                                    context, utils.getColorFromTwoStations(
                                            stations.get(position-1), station)));

                }
            }

            h.textViewStation.setText(station.getStation());
            if (station.getLignes().size() > 1) {
                h.imgDot.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.point_correspondance));
                ImageViewCompat.setImageTintList(h.imgDot, null);
            } else {
                h.imgDot.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.point_normal));
                ImageViewCompat.setImageTintList(h.imgDot, ColorStateList.valueOf( ContextCompat.getColor(context, station.getColor())));
            }

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.showMessage(v, Utils.getAvailableLignesFromStation(station));
                }
            };
            h.textViewStation.setOnClickListener(listener);
            h.imgDot.setOnClickListener(listener);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgDot;
        private final TextView textViewStation;
        private final View viewDotTop;
        private final View viewDotBottom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDot = itemView.findViewById(R.id.imageViewLigne);
            textViewStation = itemView.findViewById(R.id.textViewStation);
            viewDotTop = itemView.findViewById(R.id.imageViewLineTop);
            viewDotBottom = itemView.findViewById(R.id.imageViewLineBottom);
        }

        public ImageView getImgDot() {
            return imgDot;
        }

        public TextView getTextViewStation() {
            return textViewStation;
        }
    }

    public static class ViewHolderIntro extends RecyclerView.ViewHolder {

        public ViewHolderIntro(@NonNull View itemView) {
            super(itemView);
        }
    }
}

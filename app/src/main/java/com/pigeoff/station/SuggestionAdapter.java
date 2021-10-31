package com.pigeoff.station;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuggestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_INTRO = 0;
    private static final int VIEW_STATION = 1;

    private final Context context;
    private List<Station> stations;
    private final Utils utils = new Utils();

    private OnSuggestionClick suggestionClick;

    public SuggestionAdapter(Context c, ArrayList<Station> s) {
        this.context = c;
        this.stations = s;
    }

    public void setStations(List<Station> sts) {
        if (!sts.equals(this.stations)) {
            int oldSize = this.stations.size();
            for (int i = 0; i < oldSize; ++i) {
                this.stations.remove(0);
                notifyItemRemoved(0);
            }
            this.stations = sts;
            notifyItemRangeChanged(0, this.stations.size());
        }
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
        return stations.size();
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
        View viewStation = LayoutInflater.from(context).inflate(R.layout.adapter_suggestion, parent, false);
        return new ViewHolder(viewStation);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int index) {
        ViewHolder h = (ViewHolder) holder;

        final Station station = stations.get(index);

        h.textViewStation.setText(Html.fromHtml(station.getStation()));
        h.layoutSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggestionClick.onSuggestionClick(station);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewStation;
        private final ConstraintLayout layoutSuggestion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStation = itemView.findViewById(R.id.textViewStation);
            layoutSuggestion = itemView.findViewById(R.id.layoutSuggestion);
        }

        public TextView getTextViewStation() {
            return textViewStation;
        }

        public ConstraintLayout getLayoutSuggestion() {
            return layoutSuggestion;
        }
    }

    public interface OnSuggestionClick {
        void onSuggestionClick(Station station);
    }

    public void setOnSuggestionClick(OnSuggestionClick listener) {
        this.suggestionClick = listener;
    }
}

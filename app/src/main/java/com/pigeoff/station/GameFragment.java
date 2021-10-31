package com.pigeoff.station;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.pigeoff.station.Utils;

import org.w3c.dom.Text;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.emitters.StreamEmitter;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class GameFragment extends Fragment {

    private ArrayList<Station> stations;
    private GameAdapter gameAdapter;
    private SuggestionAdapter suggestionAdapter;
    private RecyclerView recyclerAutoComplete;
    private RecyclerView recyclerView;
    private Score score;
    private OnSettingsBtnClickListener btnSettingsListener;
    private Utils utils;
    private SharedPreferences pref;

    private TextView textScore;
    private TextView textHighScore;
    private TextView textNbStations;
    private ImageButton btnSettings;
    private ImageButton btnHint;
    private KonfettiView konfettiView;

    public GameFragment() {
        this.btnSettingsListener = null;
        this.utils = new Utils();
    }

    public interface OnSettingsBtnClickListener {
        void onSettingsBtnClickListener();
    }

    public void setOnSettingsBtnClickListener(OnSettingsBtnClickListener listener) {
        this.btnSettingsListener = listener;
    }

    public void showCongrats(KonfettiView konfettiView, int[] colors, long duration) {
        konfettiView.build()
                .addColors(colors)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .streamFor(300,  duration);
    }

    public void askForHint() {
        // Making a list of 3 wrong stations and 1 right
        final List<Station> fourStations = Utils.getFourStationsHint(
                stations,
                gameAdapter.currentAllStationS(),
                gameAdapter.currentStation());

        // Creating the dialog with the list

        Log.i("Hint stations size", String.valueOf(fourStations.size()));

        if (fourStations.size() == 4) {
            List<String> hintStations = new ArrayList<>();
            for (Station s : fourStations) {
                hintStations.add(s.getStation());
            }

            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.dialog_hint_title)
                    .setItems(hintStations.toArray(new CharSequence[hintStations.size()]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Station selectedStation = fourStations.get(which);
                            if (Utils.isStationAcceptable(selectedStation, gameAdapter.currentStation())) {
                                gameAdapter.addStation(selectedStation);
                                score.saveScore(1, textScore, textHighScore);
                            } else {
                                score.saveScore(-1, textScore, textHighScore);
                                Utils.showMessage(getView(), getString(R.string.error_message_hint_fail));
                            }
                        }
                    })
                    .show();
        }
    }

    public void gameAction(boolean success, Station s) {
        if (s != null) {
            if (success) {
                List<Station> currentStations = gameAdapter.currentAllStationS();
                if (currentStations.contains(s)) {
                    gameAdapter.addStation(s);
                    score.saveScore(-1, textScore, textHighScore);
                } else {
                    gameAdapter.addStation(s);
                    List<Integer> completedLignes = utils.isStationLastOfItsLine(stations, gameAdapter.currentAllStationS(), s);
                    // If success
                    if (s.getLignes().size() == 1) {
                        score.saveScore(3, textScore, textHighScore);
                    } else {
                        score.saveScore(1, textScore, textHighScore);
                    }

                    // If last station of line
                    if (completedLignes.size() > 0) {
                        for (Integer l : completedLignes) {
                            showCongrats(
                                    konfettiView,
                                    new int[] {requireContext().getResources().getColor(Utils.getColorFromLigne(l))},
                                    500L);
                        }
                        String str = "+"+completedLignes.size()+" "+getString(R.string.error_message_completed_line);
                        Utils.showMessage(getView(), str);
                        score.saveScore(completedLignes.size()*10, textScore, textHighScore);
                    }
                    oneMoreStationFound();
                }
            } else {
                Utils.showMessage(getView(), getString(R.string.error_message_incorrect));
            }
        } else {
            Utils.showMessage(getView(), getString(R.string.error_message_nostation));
        }
    }

    private void oneMoreStationFound() {
        int count = 0;
        for (Station s : stations) {
            if (gameAdapter.currentAllStationS().contains(s)) {
                ++count;
            }
        }
        String txt = count+"/"+stations.size();
        textNbStations.setText(txt);

        if (stations.size() == gameAdapter.currentAllStationS().size()) {
            showCongrats(
                    konfettiView,
                    new int[] {requireContext().getResources().getColor(R.color.m1),
                    requireContext().getResources().getColor(R.color.m2),
                    requireContext().getResources().getColor(R.color.m4)},
                    StreamEmitter.INDEFINITE);
        }
    }

    private void getStationsSuggestion(String str) {
        if (str.length() > 3) {
            new Thread() {
                @Override
                public void run() {
                    List<Station> suggestions = Utils.getThreeBestStations(stations, gameAdapter.currentAllStationS(), str);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            suggestionAdapter.setStations(suggestions);
                        }
                    });
                }
            }.start();

        } else {
            suggestionAdapter.setStations(new ArrayList<>());
        }
    }

    private void onQueryRequest(String txt) {
        Station station = Utils.getStationFromName(stations, txt);

        Station current = gameAdapter.currentStation();
        if (current != null && station != null) {
            boolean success = Utils.isStationAcceptable(gameAdapter.currentStation(), station);
            gameAction(success, station);
        } else {
            gameAction(false, station);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Array stations
        JSONClient jsonClient = new JSONClient(requireContext());
        stations = jsonClient.getStationsFromJSON();
        pref = getActivity().getSharedPreferences(Utils.PREF, Context.MODE_PRIVATE);

        // Elements
        textScore = view.findViewById(R.id.textViewScore);
        textHighScore = view.findViewById(R.id.textViewHighScore);
        textNbStations = view.findViewById(R.id.textViewNbStations);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerAutoComplete = view.findViewById(R.id.recyclerViewAutoComplete);
        btnSettings = view.findViewById(R.id.imageButtonSettings);
        konfettiView = view.findViewById(R.id.viewKonfetti);
        btnHint = view.findViewById(R.id.imageButtonHint);
        final EditText editTextStation = view.findViewById(R.id.editTextStation);

        // Setting up scores
        score = new Score(requireContext(), textScore, textHighScore);

        // Setting up settings and hint buttons
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSettingsListener.onSettingsBtnClickListener();
            }
        });

        btnHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForHint();
            }
        });

        // Setting up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setReverseLayout(true);
        gameAdapter = new GameAdapter(getContext(), new ArrayList<Station>());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(gameAdapter);

        LinearLayoutManager layoutManagerSuggestion = new LinearLayoutManager(this.getContext());
        layoutManagerSuggestion.setReverseLayout(true);
        suggestionAdapter = new SuggestionAdapter(getContext(), new ArrayList<>());
        recyclerAutoComplete.setLayoutManager(layoutManagerSuggestion);
        recyclerAutoComplete.setAdapter(suggestionAdapter);

        // Setting up suggestion station click

        suggestionAdapter.setOnSuggestionClick(new SuggestionAdapter.OnSuggestionClick() {
            @Override
            public void onSuggestionClick(Station station) {
                editTextStation.setText("");
                editTextStation.requestFocus();
                onQueryRequest(station.getStation());
            }
        });

        // Setting up stations count
        boolean stop = false;
        while (!stop) {
            Station random = stations.get(new Random().nextInt(stations.size() - 1));
            if (random.getLignes().size() == 1) {
                gameAdapter.addStation(random);
                oneMoreStationFound();
                stop = true;
            }
        }

        editTextStation.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String txt = v.getText().toString();
                    editTextStation.setText("");
                    editTextStation.requestFocus();
                    onQueryRequest(txt);
                }

                recyclerView.scrollToPosition(0);
                return true;
            }
        });

        editTextStation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pref.getBoolean(Utils.KEY_SUGGESTION, true)) {
                    getStationsSuggestion(s.toString());
                } else {
                    getStationsSuggestion("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}

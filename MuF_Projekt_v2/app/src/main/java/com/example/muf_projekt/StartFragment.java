package com.example.muf_projekt;

import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.tomer.fadingtextview.FadingTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StartFragment extends Fragment implements View.OnClickListener {
    private MainViewModel mainViewModel;
    private Observer<AccelerationInformation> observer;
    Button startstopButton;
    TextView x_accTextView;
    TextView y_accTextView;
    TextView z_accTextView;
    TextView x_max_accTextView;
    TextView y_max_accTextView;
    TextView z_max_accTextView;
    FadingTextView fadingTextView;

    MediaPlayer player = new MediaPlayer();


    LineChart lineChart;
    ArrayList<Entry> x_accData;
    ArrayList<Entry> y_accData;
    ArrayList<Entry> z_accData;
    ArrayList<ILineDataSet> dataSets;
    int time_count = 0;

    float x_acc, y_acc, z_acc;
    float x_max = 0, y_max = 0, z_max = 0;
    float x_max_abs = 0, y_max_abs = 0, z_max_abs = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        fadingTextView = (FadingTextView) view.findViewById(R.id.fadingTextView);
        fadingTextView.setVisibility(View.INVISIBLE);
        fadingTextView.stop();

        startstopButton = (Button) view.findViewById(R.id.startstopButton);
        startstopButton.setOnClickListener(this);

        mainViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())
        ).get(MainViewModel.class);

        x_accTextView = view.findViewById(R.id.x_accTextView);
        y_accTextView = view.findViewById(R.id.y_accTextView);
        z_accTextView = view.findViewById(R.id.z_accTextView);

        x_max_accTextView = view.findViewById(R.id.x_max_accTextView);
        y_max_accTextView = view.findViewById(R.id.y_max_accTextView);
        z_max_accTextView = view.findViewById(R.id.z_max_accTextView);

        lineChart = (LineChart) view.findViewById(R.id.liveChart);
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setNoDataText("No Data");
        lineChart.setDrawBorders(true);
        lineChart.setBorderColor(Color.BLACK);
        lineChart.setBorderWidth(3);

        x_accData = new ArrayList<Entry>();
        y_accData = new ArrayList<Entry>();
        z_accData = new ArrayList<Entry>();

        getContext().deleteDatabase("app_db_2");
        File path = getContext().getDatabasePath("app_db_2");

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startstopButton:
                if (isObserverRunning()) {
                    //Observer is running
                    startstopButton.setText("Messung starten");
                    stopJeopardy();
                    fadingTextView.setVisibility(View.INVISIBLE);
                    fadingTextView.stop();
                    startstopSensorObservation();
                } else {
                    //Observer is not running
                    startstopButton.setText("Messung beenden");
                    playJeopardy();
                    fadingTextView.setVisibility(View.VISIBLE);
                    fadingTextView.restart();
                    x_accData.clear();
                    y_accData.clear();
                    z_accData.clear();
                    time_count = 0;

                    startstopSensorObservation();
                }
                break;

            default:
                break;
        }
    }

    public void startstopSensorObservation() {
        if (observer == null) {
            observer = (acclerationInformation) -> {

                x_acc = acclerationInformation.getX();
                y_acc = acclerationInformation.getY();
                z_acc = acclerationInformation.getZ();


                x_accTextView.setText(Math.round(100.0 * x_acc) / 100.0 + "m/s^2");
                y_accTextView.setText(Math.round(100.0 * y_acc) / 100.0 + "m/s^2");
                z_accTextView.setText(Math.round(100.0 * z_acc) / 100.0 + "m/s^2");

                x_accData.add(new Entry(time_count, x_acc));
                y_accData.add(new Entry(time_count, y_acc));
                z_accData.add(new Entry(time_count, z_acc));
                time_count = time_count + 1;

                LineDataSet x_lineDataSet = new LineDataSet(x_accData, "X-Acc");
                LineDataSet y_lineDataSet = new LineDataSet(y_accData, "Y-Acc");
                LineDataSet z_lineDataSet = new LineDataSet(z_accData, "Z-Acc");
                x_lineDataSet.setColor(Color.RED);
                y_lineDataSet.setColor(Color.BLUE);
                z_lineDataSet.setColor(Color.GREEN);
                x_lineDataSet.setDrawCircles(false);
                y_lineDataSet.setDrawCircles(false);
                z_lineDataSet.setDrawCircles(false);

                dataSets = new ArrayList<>();
                dataSets.add(x_lineDataSet);
                dataSets.add(y_lineDataSet);
                dataSets.add(z_lineDataSet);

                LineData data = new LineData(dataSets);
                lineChart.setData(data);
                lineChart.invalidate();

                //Live Feedback
                x_max_abs = Math.abs(x_acc);
                y_max_abs = Math.abs(y_acc);
                z_max_abs = Math.abs(z_acc);

                if (x_max_abs > x_max) {
                    x_max = x_max_abs;
                    x_max_accTextView.setText(Math.round(100.0 * x_max_abs) / 100.0 + " m/s^2");
                }
                if (y_max_abs > y_max) {
                    y_max = y_max_abs;
                    y_max_accTextView.setText(Math.round(100.0 * y_max_abs) / 100.0 + " m/s^2");
                }
                if (z_max_abs > z_max) {
                    z_max = z_max_abs;
                    z_max_accTextView.setText(Math.round(100.0 * z_max_abs) / 100.0 + " m/s^2");
                } else {
                    //x_accTextView.setBackgroundColor(Color.WHITE);
                    //y_accTextView.setBackgroundColor(Color.WHITE);
                    //z_accTextView.setBackgroundColor(Color.WHITE);
                }

                // Threshold für Live-Feedback
                // x-Axis
                if(x_max_abs > 5)
                {
                    x_accTextView.setBackgroundColor(Color.rgb(255, 0, 0));
                } else if (x_max_abs > 2) {
                    x_accTextView.setBackgroundColor(Color.rgb(255, 255, 0));
                }
                else{
                    x_accTextView.setBackgroundColor(Color.rgb(0, 255, 0));
                }

                // y-Axis
                if(y_max_abs > 5)
                {
                    y_accTextView.setBackgroundColor(Color.rgb(255, 0, 0));
                } else if (y_max_abs > 2) {
                    y_accTextView.setBackgroundColor(Color.rgb(255, 255, 0));
                }
                else{
                    y_accTextView.setBackgroundColor(Color.rgb(0, 255, 0));
                }

                // z-Axis
                if(z_max_abs > 5)
                {
                    z_accTextView.setBackgroundColor(Color.rgb(255, 0, 0));
                } else if (z_max_abs > 2) {
                    z_accTextView.setBackgroundColor(Color.rgb(255, 255, 0));
                }
                else{
                    z_accTextView.setBackgroundColor(Color.rgb(0, 255, 0));
                }



                //Datenbank
                AccelerationInformation accelerationInformation = new AccelerationInformation(x_acc, y_acc, z_acc);

                final Database database = ((MUFApplication) getActivity().getApplication()).getDataBase();

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        database.getDatapointTable().insert(accelerationInformation);
                    }
                });
                t.start();
                List<AccelerationInformation> pointList = database.getDatapointTable().getItems();

            };
            mainViewModel.accelerationLiveData.observe(getViewLifecycleOwner(), observer);

        } else {
            mainViewModel.accelerationLiveData.removeObserver(observer);
            observer = null; // reset state
        }
    }

    public boolean isObserverRunning() {
        if (observer == null)
            return false;
        return true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final NavController controller = Navigation.findNavController(view);
        view.findViewById(R.id.stats_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.navigate(StartFragmentDirections
                        .actionStartFragmentToStatsFragment()
                        .setDisplayString("Statistken & Messungen")
                );
            }
        });

    }

    public void playJeopardy() {
        try {
            player = new MediaPlayer();

            AssetFileDescriptor descriptor = getActivity().getAssets().openFd("Jeopardy.mp3");
            player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            player.prepare();
            player.setVolume(1f, 1f);
            player.setLooping(true);
            player.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopJeopardy() {
        try {
            player.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
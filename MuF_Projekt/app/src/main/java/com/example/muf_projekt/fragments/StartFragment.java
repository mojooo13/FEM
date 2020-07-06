package com.example.muf_projekt.fragments;

import android.graphics.Color;
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
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;

import com.example.muf_projekt.AccelerationInformation;
import com.example.muf_projekt.MainViewModel;
import com.example.muf_projekt.R;
import com.example.muf_projekt.fragments.StartFragmentDirections;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class StartFragment extends Fragment implements View.OnClickListener{
    private MainViewModel mainViewModel;
    private Observer<AccelerationInformation> observer;
    Button startstopButton;
    TextView x_accTextView;
    TextView y_accTextView;
    TextView z_accTextView;

    LineChart lineChart;
    ArrayList<Entry> x_accData;
    ArrayList<Entry> y_accData;
    ArrayList<Entry> z_accData;
    ArrayList<ILineDataSet> dataSets;
    int time_count = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        startstopButton = (Button) view.findViewById(R.id.startstopButton);
        startstopButton.setOnClickListener(this);

        mainViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())
        ).get(MainViewModel.class);

        x_accTextView = view.findViewById(R.id.x_accTextView);
        y_accTextView = view.findViewById(R.id.y_accTextView);
        z_accTextView = view.findViewById(R.id.z_accTextView);

        lineChart = (LineChart) view.findViewById(R.id.liveChart);
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setNoDataText("No Data");
        lineChart.setDrawBorders(true);
        lineChart.setBorderColor(Color.BLACK);
        lineChart.setBorderWidth(3);

        x_accData = new ArrayList<Entry>();
        y_accData = new ArrayList<Entry>();
        z_accData = new ArrayList<Entry>();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startstopButton:
                if(isObserverRunning()){
                    //Observer is running
                    startstopButton.setText("Messung starten");
                    startstopSensorObservation();
                }
                else{
                    //Observer is not running
                    startstopButton.setText("Messung beenden");
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

    public void startstopSensorObservation(){
        if(observer == null){
            observer = (acclerationInformation) -> {
                x_accTextView.setText(acclerationInformation.getX()+"m/s^2");
                y_accTextView.setText(acclerationInformation.getY()+"m/s^2");
                z_accTextView.setText(acclerationInformation.getZ()+"m/s^2");

                x_accData.add(new Entry(time_count, acclerationInformation.getX()));
                y_accData.add(new Entry(time_count, acclerationInformation.getY()));
                z_accData.add(new Entry(time_count, acclerationInformation.getZ()));
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
            };
            mainViewModel.accelerationLiveData.observe(getViewLifecycleOwner(), observer);

        }
        else {
            mainViewModel.accelerationLiveData.removeObserver(observer);
            observer = null; // reset state
        }
    }

    public boolean isObserverRunning(){
        if(observer == null)
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
                                .setDisplayString("ÜbergabeString")
                );
            }
        });

    }
}

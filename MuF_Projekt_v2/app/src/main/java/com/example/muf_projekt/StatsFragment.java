package com.example.muf_projekt;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends Fragment {

    private Database database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        database = ((MUFApplication) getActivity().getApplication()).getDataBase();

        Bundle args = getArguments();
        StatsFragmentArgs statsFragmentArgs = null;

        if(args != null){
            statsFragmentArgs = StatsFragmentArgs.fromBundle(args);
        }

        if(statsFragmentArgs != null) {
            TextView tv = view.findViewById(R.id.stats);
            tv.setText(statsFragmentArgs.getDisplayString());
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO:
        //ListView lv = view.findViewById(R.id.listMeasurements);
        TextView xMean = view.findViewById(R.id.xMean);
        TextView yMean = view.findViewById(R.id.yMean);
        TextView zMean = view.findViewById(R.id.zMean);
        LineChart lc = view.findViewById(R.id.statsGraph);

        ArrayList<Entry> x_accData;
        ArrayList<Entry> y_accData;
        ArrayList<Entry> z_accData;
        ArrayList<ILineDataSet> dataSets;
        int time_count = 0;

        float x_acc, y_acc, z_acc;

        int cnt = 0;
        // List<AccelerationInformation> test = (List<AccelerationInformation>) database.getDatapointTable().getItemsAsLiveData();

        database.getDatapointTable().getItemsAsLiveData().observe(this.getActivity(), accelerationInformation -> {
            for(int i = 0; i<accelerationInformation.size();i++){
                AccelerationInformation element = accelerationInformation.get(i);
                /*
                x_acc = element.getX();
                y_acc = element.getY();
                z_acc = element.getZ();


                x_accTextView.setText(x_acc+"m/s^2");
                y_accTextView.setText(y_acc+"m/s^2");
                z_accTextView.setText(z_acc+"m/s^2");


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
                lc.setData(data);
                lc.invalidate();
                */
            }
        });



        /*
        List x = (List) database.getDatapointTable().getItemsAsLiveData();
        List<AccelerationInformation> test = (List<AccelerationInformation>) database.getDatapointTable().getItemsAsLiveData();

        database.getDatapointTable().getItemsAsLiveData().observe(this.getActivity(), accelerationInformation -> {

        });

         */
    }

}

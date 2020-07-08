package com.example.muf_projekt;

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

        Database database = ((MUFApplication) getActivity().getApplication()).getDataBase();

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
        ListView lv = view.findViewById(R.id.listMeasurements);
        TextView xMean = view.findViewById(R.id.xMean);
        TextView yMean = view.findViewById(R.id.yMean);
        TextView zMean = view.findViewById(R.id.zMean);
        LineChart lc = view.findViewById(R.id.statsGraph);

        List<AccelerationInformation> test = (List<AccelerationInformation>) database.getDatapointTable().getItemsAsLiveData();

        database.getDatapointTable().getItemsAsLiveData().observe(this.getActivity(), accelerationInformation -> {
            List<AccelerationInformation> test2 = accelerationInformation;
        });

        /*
        List x = (List) database.getDatapointTable().getItemsAsLiveData();
        List<AccelerationInformation> test = (List<AccelerationInformation>) database.getDatapointTable().getItemsAsLiveData();

        database.getDatapointTable().getItemsAsLiveData().observe(this.getActivity(), accelerationInformation -> {

        });

         */
    }

}

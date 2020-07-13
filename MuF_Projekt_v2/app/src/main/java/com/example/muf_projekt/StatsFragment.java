package com.example.muf_projekt;

import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.IOException;
import java.util.ArrayList;

public class StatsFragment extends Fragment {

    PieChart pieChart;
    MediaPlayer player;
    private Database database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        pieChart = (PieChart)view.findViewById(R.id.piechart);
        PieDataSet pieDataSet = new PieDataSet(getPieData(),"Time spend.");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.animateXY(5000, 5000);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.invalidate();

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
        TextView xMeanLab = view.findViewById(R.id.xMean);
        TextView yMeanLab = view.findViewById(R.id.yMean);
        TextView zMeanLab = view.findViewById(R.id.zMean);

        TextView xMinLab = view.findViewById(R.id.xMin);
        TextView yMinLab = view.findViewById(R.id.yMin);
        TextView zMinLab = view.findViewById(R.id.zMin);

        TextView xMaxLab = view.findViewById(R.id.xMax);
        TextView yMaxLab = view.findViewById(R.id.yMax);
        TextView zMaxLab = view.findViewById(R.id.zMax);

        LineChart lcX = view.findViewById(R.id.statsGraphX);
        LineChart lcY = view.findViewById(R.id.statsGraphY);
        LineChart lcZ = view.findViewById(R.id.statsGraphZ);



        database.getDatapointTable().getItemsAsLiveData().observe(this.getActivity(), accelerationInformation -> {
            float x_acc, y_acc, z_acc;

            float xMin = 0;
            float xMax = 0;
            float yMin = 0;
            float yMax = 0;
            float zMin = 0;
            float zMax = 0;

            float xMean = 0;
            float yMean = 0;
            float zMean = 0;

            ArrayList<Entry> x_accData = new ArrayList<>();
            ArrayList<Entry> y_accData = new ArrayList<>();
            ArrayList<Entry> z_accData = new ArrayList<>();

            ArrayList<ILineDataSet> dataSetX;
            ArrayList<ILineDataSet> dataSetY;
            ArrayList<ILineDataSet> dataSetZ;

            int cnt = 0;

            // TODO: Sortieren der Liste, um die richtige Reihenfolge zu bekommen für die Darstellung

            for (int i = 0; i < accelerationInformation.size(); i++) {

                AccelerationInformation element = accelerationInformation.get(i);

                x_acc = element.getX();
                y_acc = element.getY();
                z_acc = element.getZ();

                x_accData.add(new Entry(i, x_acc));
                y_accData.add(new Entry(i, y_acc));
                z_accData.add(new Entry(i, z_acc));

                LineDataSet x_lineDataSet = new LineDataSet(x_accData, "X-Acc");
                LineDataSet y_lineDataSet = new LineDataSet(y_accData, "Y-Acc");
                LineDataSet z_lineDataSet = new LineDataSet(z_accData, "Z-Acc");

                x_lineDataSet.setColor(Color.RED);
                y_lineDataSet.setColor(Color.BLUE);
                z_lineDataSet.setColor(Color.GREEN);
                x_lineDataSet.setDrawCircles(false);
                y_lineDataSet.setDrawCircles(false);
                z_lineDataSet.setDrawCircles(false);

                dataSetX = new ArrayList<>();
                dataSetY = new ArrayList<>();
                dataSetZ = new ArrayList<>();

                dataSetX.add(x_lineDataSet);
                dataSetY.add(y_lineDataSet);
                dataSetZ.add(z_lineDataSet);

                LineData dataX = new LineData(dataSetX);
                LineData dataY = new LineData(dataSetY);
                LineData dataZ = new LineData(dataSetZ);

                lcX.setData(dataX);
                lcX.animateXY(3000,3000);
                lcX.invalidate();
                lcY.setData(dataY);
                lcX.animateXY(3000,3000);
                lcY.invalidate();
                lcZ.setData(dataZ);
                lcX.animateXY(3000,3000);
                lcZ.invalidate();

                // Calc Min / Max
                // x-Axis
                if (Math.abs(x_acc) < xMin){
                    xMin = x_acc;

                }
                if (Math.abs(x_acc) > xMax){
                    xMax = x_acc;
                }

                // y-Axis

                if (Math.abs(y_acc) < yMin){
                    yMin = y_acc;

                }
                if (Math.abs(y_acc) > yMax){
                    yMax = y_acc;
                }

                // z-Axis

                if (Math.abs(z_acc) < zMin){
                    zMin = z_acc;

                }
                if (Math.abs(z_acc) > zMax){
                    zMax = z_acc;
                }

                // Calc Mean
                xMean = (xMean + x_acc) / (i+1);
                yMean = (yMean + y_acc) / (i+1);
                zMean = (zMean + z_acc) / (i+1);

            }
            // Set Mean
            xMeanLab.setText(String.valueOf(xMean*100) + " m/s^2");
            yMeanLab.setText(String.valueOf(yMean*100) + " m/s^2");
            zMeanLab.setText(String.valueOf(zMean*100) + " m/s^2");

            // Set Min
            xMinLab.setText(String.valueOf(xMin) + " m/s^2");
            yMinLab.setText(String.valueOf(yMin) + " m/s^2");
            zMinLab.setText(String.valueOf(zMin) + " m/s^2");

            // Set Max
            xMaxLab.setText(String.valueOf(xMax) + " m/s^2");
            yMaxLab.setText(String.valueOf(yMax) + " m/s^2");
            zMaxLab.setText(String.valueOf(zMax) + " m/s^2");

        });

    }

    public void playJeopardy() {
        try {
            if (player.isPlaying()) {
                player.stop();
                player.release();
                player = new MediaPlayer();
            }

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

    private ArrayList getPieData(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(10, "Research"));
        entries.add(new PieEntry(3, "Email"));
        entries.add(new PieEntry(2, "Google Bugs"));
        entries.add(new PieEntry(5, "Programming"));
        entries.add(new PieEntry(3, "Beer"));
        entries.add(new PieEntry(3, "Beer & programming"));
        entries.add(new PieEntry(1, "Frustration"));
        entries.add(new PieEntry(1, "Celebration"));

        return entries;
    }

}



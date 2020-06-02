package com.example.muf_projekt;

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

public class StartFragment extends Fragment implements View.OnClickListener{
    private MainViewModel mainViewModel;
    private Observer<AccelerationInformation> observer;
    Button startstopButton;
    TextView x_accTextView;
    TextView y_accTextView;
    TextView z_accTextView;


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
                //controller.navigate(R.id.action_startFragment_to_statsFragment);
            }
        });

    }
}

package com.mobicom.s18.nutritrack_mco;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LogMealFragment extends Fragment {

    private Button saveLogBtn;
    private Button viewMealLogBtn;

    public LogMealFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_meal, container, false);

        saveLogBtn = view.findViewById(R.id.saveLogBtn);
        viewMealLogBtn = view.findViewById(R.id.viewMealLogBtn);

        viewMealLogBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LogMealActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
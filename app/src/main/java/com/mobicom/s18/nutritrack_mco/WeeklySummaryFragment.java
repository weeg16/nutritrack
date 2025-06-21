package com.mobicom.s18.nutritrack_mco;
import com.mobicom.s18.nutritrack_mco.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class WeeklySummaryFragment extends Fragment {
    public WeeklySummaryFragment() {
        super(R.layout.fragment_weekly_summary);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button detailedStatsBtn = view.findViewById(R.id.detailedStatsBtn);
        detailedStatsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WeeklySummaryActivity.class);
            startActivity(intent);
        });
    }

}
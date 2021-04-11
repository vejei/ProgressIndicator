package io.github.vejei.progressindicator.samples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.vejei.progressindicator.DeterminateCircleSweepDrawable;
import io.github.vejei.progressindicator.ProgressIndicatorDrawable;

public class DeterminateSamplesFragment extends Fragment {
    private List<ProgressIndicatorDrawable> indicators = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        indicators = Collections.singletonList(new DeterminateCircleSweepDrawable(getContext()));
        return inflater.inflate(R.layout.fragment_determinate_samples, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView sampleList = view.findViewById(R.id.sample_list);
        SeekBar progressSeekBar = view.findViewById(R.id.seek_bar_progress);
        IndicatorAdapter adapter = new IndicatorAdapter(indicators, false);

        sampleList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        sampleList.setAdapter(adapter);
        sampleList.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        sampleList.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.HORIZONTAL));

        progressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                adapter.onProgressChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
}

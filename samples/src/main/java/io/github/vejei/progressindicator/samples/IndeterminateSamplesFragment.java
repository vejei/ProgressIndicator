package io.github.vejei.progressindicator.samples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.vejei.progressindicator.ArcSurroundDrawable;
import io.github.vejei.progressindicator.CartoonSunDrawable;
import io.github.vejei.progressindicator.CirclePulsingDrawable;
import io.github.vejei.progressindicator.LineStretchDrawable;
import io.github.vejei.progressindicator.ProgressIndicatorDrawable;
import io.github.vejei.progressindicator.SunDrawable;

public class IndeterminateSamplesFragment extends Fragment {
    private List<ProgressIndicatorDrawable> indicators = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        indicators = Arrays.asList(new CirclePulsingDrawable(), new CartoonSunDrawable(),
                new SunDrawable(), new LineStretchDrawable(getContext()),
                new ArcSurroundDrawable());
        return inflater.inflate(R.layout.fragment_indeterminate_samples, container,
                false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IndicatorAdapter adapter = new IndicatorAdapter(indicators, true);

        RecyclerView sampleList = view.findViewById(R.id.sample_list);
        sampleList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        sampleList.setAdapter(adapter);
        sampleList.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        sampleList.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.HORIZONTAL));
    }
}

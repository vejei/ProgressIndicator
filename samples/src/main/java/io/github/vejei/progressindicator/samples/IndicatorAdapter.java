package io.github.vejei.progressindicator.samples;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.github.vejei.progressindicator.ProgressIndicatorDrawable;

public class IndicatorAdapter extends RecyclerView.Adapter<IndicatorAdapter.ViewHolder> {
    private final List<ProgressIndicatorDrawable> indicators;
    private final int layoutRes;
    private boolean indeterminate;
    private int progress;

    public IndicatorAdapter(List<ProgressIndicatorDrawable> indicators, boolean indeterminate) {
        this.indicators = indicators;
        this.layoutRes = indeterminate ? R.layout.item_indeterminate_indicator
                : R.layout.item_determinate_indicator;
        this.indeterminate = indeterminate;
    }

    @NonNull
    @Override
    public IndicatorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull IndicatorAdapter.ViewHolder holder, int position) {
        ProgressIndicatorDrawable indicatorDrawable = indicators.get(position);
        holder.setIndicator(indicatorDrawable);
        holder.setItemClickListener(indicatorDrawable);

        if (!indeterminate) {
            holder.setProgress(progress);
        }
    }

    @Override
    public int getItemCount() {
        return indicators.size();
    }

    public void onProgressChanged(int progress) {
        Log.d("Adapter", "progress changed: " + progress);
        this.progress = progress;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);

        }

        public void setIndicator(ProgressIndicatorDrawable indicator) {
            if (progressBar != null) {
                if (indeterminate) {
                    progressBar.setIndeterminateDrawable(indicator);
                } else {
                    progressBar.setProgressDrawable(indicator);
                }
            }
        }

        public void setProgress(int progress) {
            progressBar.setProgress(progress);
        }

        public void setItemClickListener(ProgressIndicatorDrawable indicator) {
            itemView.setOnClickListener((v) -> {
                Toast.makeText(itemView.getContext(), indicator.getClass().getSimpleName(),
                        Toast.LENGTH_SHORT).show();
            });
        }
    }
}

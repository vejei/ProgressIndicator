package io.github.vejei.progressindicator;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SunDrawable extends ProgressIndicatorDrawable {
    private static final int STROKE_WIDTH = 14;
    private static final int LINE_LENGTH = 25;
    private static final int SPACING = 10;
    private static final int LINE_COUNT = 8;
    private static final long ANIMATION_DURATION = 2000;

    private float centerX;
    private float centerY;
    private float circleRadius;
    private final Path innerCirclePath = new Path();
    private final Path outerCirclePath = new Path();
    private List<Line> lines = new ArrayList<>();

    private final ValueAnimator valueAnimator;
    private float rotateDegree;

    public SunDrawable() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setColor(Color.WHITE);

        valueAnimator = ValueAnimator.ofFloat(0f, 360f);
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotateDegree = (float) animation.getAnimatedValue();
                invalidateSelf();
            }
        });
    }

    @Override
    public void start() {
        if (valueAnimator != null) {
            if (valueAnimator.isRunning()) {
                valueAnimator.end();
            }
            valueAnimator.start();
        }
    }

    @Override
    public void stop() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.end();
        }
    }

    @Override
    public boolean isRunning() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            return true;
        }
        return super.isRunning();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        centerX = bounds.centerX();
        centerY = bounds.centerY();

        circleRadius = bounds.width() / 2f - LINE_LENGTH - SPACING - STROKE_WIDTH;

        float innerCirclePathRadius = circleRadius + STROKE_WIDTH + SPACING;
        float outerCirclePathRadius = innerCirclePathRadius + LINE_LENGTH;

        if (innerCirclePath != null) {
            innerCirclePath.addCircle(centerX, centerY, innerCirclePathRadius, Path.Direction.CW);
        }
        if (outerCirclePath != null) {
            outerCirclePath.addCircle(centerX, centerY, outerCirclePathRadius, Path.Direction.CW);
        }
        PathMeasure innerPathMeasure = new PathMeasure();
        innerPathMeasure.setPath(innerCirclePath, false);
        PathMeasure outerPathMeasure = new PathMeasure();
        outerPathMeasure.setPath(outerCirclePath, false);

        int innerPathPart = (int) (innerPathMeasure.getLength() / LINE_COUNT);
        int outerPathPart = (int) (outerPathMeasure.getLength() / LINE_COUNT);

        if (lines == null) {
            lines = new ArrayList<>();
        }
        lines.clear();
        for (int i = 0; i < LINE_COUNT; i++) {
            float[] startPosition = new float[2];
            float[] endPosition = new float[2];
            Line line = new Line();

            innerPathMeasure.getPosTan(innerPathPart * (i + 1), startPosition, null);
            outerPathMeasure.getPosTan(outerPathPart * (i + 1), endPosition, null);

            line.startX = startPosition[0];
            line.startY = startPosition[1];
            line.endX = endPosition[0];
            line.endY = endPosition[1];

            lines.add(line);
        }

        invalidateSelf();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.rotate(rotateDegree, centerX, centerY);
        canvas.drawCircle(centerX, centerY, circleRadius, paint);

        for (Line line : lines) {
            canvas.drawLine(line.startX, line.startY, line.endX, line.endY, paint);
        }
        canvas.restore();
    }

    private static class Line {
        float startX;
        float startY;
        float endX;
        float endY;
    }
}

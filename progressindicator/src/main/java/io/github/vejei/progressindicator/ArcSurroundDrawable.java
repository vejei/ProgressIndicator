package io.github.vejei.progressindicator;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;

public class ArcSurroundDrawable extends ProgressIndicatorDrawable {
    private static final int ANIMATION_DURATION = 1000;
    private static final float SWEEP_ANGLE = 90;
    private static final int STROKE_WIDTH = 16;

    private final RectF arcBounds = new RectF();
    private float circleCenterX;
    private float circleCenterY;
    private float circleRadius;
    private final ValueAnimator valueAnimator;
    private float arcStartAngle = -90;

    public ArcSurroundDrawable() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);

        valueAnimator = ValueAnimator.ofFloat(-90, 270);
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                arcStartAngle = (float) animation.getAnimatedValue();
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
        if (valueAnimator != null) {
            return valueAnimator.isRunning();
        } else {
            return super.isRunning();
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        paint.setColor(Color.GRAY);
        canvas.drawCircle(circleCenterX, circleCenterY, circleRadius, paint);
        paint.setColor(Color.BLUE);
        canvas.drawArc(arcBounds, arcStartAngle, SWEEP_ANGLE, false, paint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        circleRadius = Math.min(bounds.width(), bounds.height()) / 2f - STROKE_WIDTH;

        circleCenterX = bounds.centerX();
        circleCenterY = bounds.centerY();

        float left = circleCenterX - circleRadius;
        float top = circleCenterY - circleRadius;
        float right = circleCenterX + circleRadius;
        float bottom = circleCenterY + circleRadius;
        arcBounds.set(left, top, right, bottom);

        invalidateSelf();
    }

    @Override
    public int getIntrinsicWidth() {
        return !arcBounds.isEmpty() ? (int) arcBounds.width() : -1;
    }

    @Override
    public int getIntrinsicHeight() {
        return !arcBounds.isEmpty() ? (int) arcBounds.height() : -1;
    }
}

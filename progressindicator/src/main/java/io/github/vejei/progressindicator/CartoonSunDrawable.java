package io.github.vejei.progressindicator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;

public class CartoonSunDrawable extends ProgressIndicatorDrawable {
    private static final long ANIMATION_DURATION = 1500;

    private float centerX;
    private float centerY;
    private RectF rectBounds = new RectF();
    private float circleRadius;
    private final ValueAnimator valueAnimator;
    private float rotateDegree;

    public CartoonSunDrawable() {
        valueAnimator = ValueAnimator.ofFloat(0f, 360f);
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
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
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.rotate(rotateDegree, centerX, centerY);

        paint.setColor(Color.parseColor("#FF3D00"));
        canvas.drawRect(rectBounds, paint);

        canvas.rotate(45, centerX, centerY);
        canvas.drawRect(rectBounds, paint);

        paint.setColor(Color.WHITE);
        canvas.drawCircle(centerX, centerY, circleRadius, paint);

        canvas.restore();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        int width = bounds.width();
        float rectSize = (float) Math.sqrt(Math.pow(width, 2) / 2);
        float halfSize = rectSize / 2f;

        circleRadius = width / 4f;
        centerX = bounds.centerX();
        centerY = bounds.centerY();

        if (rectBounds == null) {
            rectBounds = new RectF();
        }
        rectBounds.set(centerX - halfSize, centerY - halfSize, centerX + halfSize,
                centerY + halfSize);

        invalidateSelf();
    }

    @Override
    public int getIntrinsicWidth() {
        return !getBounds().isEmpty() ? getBounds().width() : -1;
    }

    @Override
    public int getIntrinsicHeight() {
        return !getBounds().isEmpty() ? getBounds().height() : -1;
    }
}

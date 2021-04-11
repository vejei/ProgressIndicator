package io.github.vejei.progressindicator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.TypedValue;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;

public class DeterminateCircleSweepDrawable extends ProgressIndicatorDrawable {
    private static final @Dimension(unit = Dimension.DP) float STROKE_WIDTH_DP = 2;
    private static final long ANIMATION_DURATION = 2000;
    private static final float START_ANGLE = -90;

    private float centerX;
    private float centerY;
    private final float strokeWidth;
    private final RectF arcBounds = new RectF();
    private final ValueAnimator sweepAnimator;
    private float sweepAngle;

    public DeterminateCircleSweepDrawable(Context context) {
        strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, STROKE_WIDTH_DP,
                context.getResources().getDisplayMetrics());

        sweepAnimator = ValueAnimator.ofFloat(0, 360);
        sweepAnimator.setDuration(ANIMATION_DURATION);
        sweepAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                sweepAngle = (float) animation.getAnimatedValue();
                invalidateSelf();
            }
        });
    }

    @Override
    public void start() {
        if (sweepAnimator != null) {
            if (sweepAnimator.isStarted() || sweepAnimator.isRunning()) {
                sweepAnimator.end();
            }
            sweepAnimator.start();
        }
    }

    @Override
    public void stop() {
        if (sweepAnimator != null && sweepAnimator.isRunning()) {
            sweepAnimator.end();
        }
    }

    @Override
    public boolean isRunning() {
        if (sweepAnimator != null) {
            return sweepAnimator.isRunning();
        }
        return super.isRunning();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        canvas.drawCircle(centerX, centerY, arcBounds.width() / 2f, paint);
        paint.setColor(Color.parseColor("#99FFFFFF"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(arcBounds, START_ANGLE, sweepAngle, true, paint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        centerX = bounds.centerX();
        centerY = bounds.centerY();
        float arcBoundWidth = Math.min(bounds.width(), bounds.height()) - strokeWidth;
        float halfArcBoundWidth = arcBoundWidth / 2f;

        arcBounds.set(centerX - halfArcBoundWidth, centerY - halfArcBoundWidth,
                centerX + halfArcBoundWidth, centerY + halfArcBoundWidth);
        invalidateSelf();
    }

    @Override
    public boolean isIndeterminate() {
        return false;
    }

    @Override
    protected boolean onLevelChange(int level) {
        float progress = level / 10000f;
        sweepAnimator.setCurrentPlayTime((long) (progress * ANIMATION_DURATION));
        invalidateSelf();
        return true;
    }
}

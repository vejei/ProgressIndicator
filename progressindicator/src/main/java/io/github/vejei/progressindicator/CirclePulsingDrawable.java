package io.github.vejei.progressindicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CirclePulsingDrawable extends ProgressIndicatorDrawable {
    private static final int DEFAULT_CIRCLE_COUNT = 4;
    private static final float SCALE_MIN_MULTIPLE = 0f;
    private static final int SCALE_MAX_MULTIPLE = 3;
    private static final int DEFAULT_ANIMATION_DURATION = 3000;

    private final int circleCount;
    private final long animationDuration;
    private float centerX;
    private float centerY;
    private float maxRadius;
    private float minRadius;
    private final List<Animator> animators = new ArrayList<>();
    private final float[] radiusValues;
    private final int[] alphaValues;

    public CirclePulsingDrawable() {
        this(DEFAULT_CIRCLE_COUNT);
    }

    public CirclePulsingDrawable(int circleCount) {
        this(circleCount, DEFAULT_ANIMATION_DURATION);
    }

    public CirclePulsingDrawable(int circleCount, long animationDuration) {
        this.circleCount = circleCount;
        this.animationDuration = animationDuration;
        this.radiusValues = new float[circleCount];
        this.alphaValues = new int[circleCount];

        paint.setColor(Color.WHITE);
    }

    @Override
    public void start() {
        if (animators != null && !animators.isEmpty()) {
            for (Animator animator : animators) {
                if (animator == null) {
                    continue;
                }
                if (animator.isRunning()) {
                    animator.end();
                }
                animator.start();
            }
        }
    }

    @Override
    public void stop() {
        if (animators != null && !animators.isEmpty()) {
            for (Animator animator : animators) {
                animator.end();
            }
        }
    }

    @Override
    public boolean isRunning() {
        if (animators != null && !animators.isEmpty()) {
            for (Animator animator : animators) {
                if (animator.isRunning()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        centerX = bounds.centerX();
        centerY = bounds.centerY();

        maxRadius = Math.min(bounds.width(), bounds.height()) / 2f;
        minRadius = maxRadius / SCALE_MAX_MULTIPLE;
        createAnimators();

        invalidateSelf();
    }

    private void createAnimators() {
        stop();
        animators.clear();

        for (int i = 0; i < circleCount; i++) {
            long startDelay = i * animationDuration / circleCount;

            radiusValues[i] = 0;
            alphaValues[i] = 255;

            // The scale animation which change the radius of the circle.
            ValueAnimator scaleAnimator = ValueAnimator.ofFloat(SCALE_MIN_MULTIPLE,
                    SCALE_MAX_MULTIPLE);
            scaleAnimator.setDuration(animationDuration);
            scaleAnimator.setStartDelay(startDelay);
            scaleAnimator.setRepeatCount(ValueAnimator.INFINITE);
            scaleAnimator.setInterpolator(new DecelerateInterpolator());
            final int index = i;
            scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    radiusValues[index] = minRadius * (float) animation.getAnimatedValue();
                    invalidateSelf();
                }
            });

            ValueAnimator alphaAnimation = ValueAnimator.ofInt(255, 0);
            alphaAnimation.setDuration(animationDuration);
            alphaAnimation.setStartDelay(startDelay);
            alphaAnimation.setRepeatCount(ValueAnimator.INFINITE);
            alphaAnimation.setInterpolator(new DecelerateInterpolator());
            alphaAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    alphaValues[index] = (int) animation.getAnimatedValue();
                    invalidateSelf();
                }
            });

            animators.add(scaleAnimator);
            animators.add(alphaAnimation);
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        for (int i = 0; i < circleCount; i++) {
            paint.setAlpha(alphaValues[i]);
            canvas.drawCircle(centerX, centerY, radiusValues[i], paint);
        }
    }
}

package io.github.vejei.progressindicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.TypedValue;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class LineStretchDrawable extends ProgressIndicatorDrawable {
    private static final int LINE_COUNT = 4;
    private static final @Dimension(unit = Dimension.DP) float LINE_WIDTH = 3;
    private static final long ANIMATION_DURATION = 400;

    private final float maxLineHeight;
    private final float lineWidth;
    private final float linesSpacing;
    private Rect linesBound = new Rect();

    private List<Line> lines = new ArrayList<>();
    private List<Animator> animators = new ArrayList<>();

    public LineStretchDrawable(Context context) {
        lineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, LINE_WIDTH,
                context.getResources().getDisplayMetrics());
        maxLineHeight = lineWidth * 9f;
        linesSpacing = lineWidth * 1.5f;

        paint.setColor(Color.WHITE);
    }

    @Override
    public void start() {
        if (animators != null && !animators.isEmpty()) {
            for (Animator animator : animators) {
                animator.start();
            }
        }
    }

    @Override
    public void stop() {
        if (animators != null) {
            for (Animator animator : animators) {
                animator.cancel();
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
        return super.isRunning();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        int boundsCenterX = bounds.centerX();
        int boundsCenterY = bounds.centerY();

        float startX = boundsCenterX - linesSpacing * 1.5f - 2f * lineWidth;
        float endX = boundsCenterX + linesSpacing * 1.5f - 2f * lineWidth;
        float top = boundsCenterY - maxLineHeight / 2f;
        float bottom = boundsCenterY + maxLineHeight / 2f;

        if (linesBound == null) {
            linesBound = new Rect();
        }
        linesBound.set((int) Math.ceil(startX), (int) Math.ceil(top), (int) Math.ceil(endX),
                (int) Math.ceil(bottom));

        if (lines == null) {
            lines = new ArrayList<>();
        }
        lines.clear();
        for (int i = 0; i < LINE_COUNT; i++) {
            float left = startX + i * lineWidth + i * linesSpacing;
            Line strip = new Line();
            strip.bounds = new RectF(left, 0, left + lineWidth, bottom);

            computeHeightExtremums(strip, i);
            lines.add(strip);
        }

        if (animators == null) {
            animators = new ArrayList<>();
        }
        animators.clear();
        for (final Line strip : lines) {
            ValueAnimator heightAnimator = ValueAnimator.ofFloat(strip.startHeight, strip.endHeight);
            heightAnimator.setDuration(ANIMATION_DURATION);
            heightAnimator.setRepeatCount(ValueAnimator.INFINITE);
            heightAnimator.setRepeatMode(ValueAnimator.REVERSE);
            heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    strip.bounds.top = strip.bounds.bottom - (float) animation.getAnimatedValue();
                    invalidateSelf();
                }
            });
            animators.add(heightAnimator);
        }

        invalidateSelf();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        for (Line strip : lines) {
            canvas.drawRect(strip.bounds, paint);
        }
    }

    private void computeHeightExtremums(Line line, int index) {
        switch (index) {
            case 0:
                line.startHeight = maxLineHeight * 0.5f;
                line.endHeight = maxLineHeight * 0.85f;
                break;
            case 1:
                line.startHeight = maxLineHeight;
                line.endHeight = maxLineHeight * 0.5f;
                break;
            case 2:
                line.startHeight = maxLineHeight * 0.3f;
                line.endHeight = maxLineHeight * 0.9f;
                break;
            case 3:
                line.startHeight = maxLineHeight * 0.6f;
                line.endHeight = maxLineHeight * 0.3f;
                break;
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return !linesBound.isEmpty() ? linesBound.width() : -1;
    }

    @Override
    public int getIntrinsicHeight() {
        return !linesBound.isEmpty() ? linesBound.height() : -1;
    }

    private static class Line {
        RectF bounds = new RectF();
        float startHeight;
        float endHeight;
    }
}

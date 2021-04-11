package io.github.vejei.progressindicator;

import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

public abstract class ProgressIndicatorDrawable extends Drawable implements Animatable {
//    protected static final int NO_COLOR = -1;

    protected final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /*protected Context context;
    protected @ColorInt int primaryColor = NO_COLOR;
    protected @ColorInt int secondaryColor = NO_COLOR;*/

    /*public ProgressIndicatorDrawable(Context context) {
        this.context = context;

        createColors();
    }*/

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    public boolean isIndeterminate() {
        return true;
    }

    /*private void createColors() {
        Resources.Theme theme = context.getTheme();
        TypedValue value = new TypedValue();
        if (theme.resolveAttribute(R.attr.colorPrimary, value, true)) {
            primaryColor = value.data;
        }
        if (theme.resolveAttribute(R.attr.colorSecondary, value, true)
                || theme.resolveAttribute(R.attr.colorSecondary, value, true)) {
            secondaryColor = value.data;
        }

        if (primaryColor == NO_COLOR) {
            primaryColor = Color.WHITE;
        }

        if (secondaryColor == NO_COLOR) {
            secondaryColor = Color.BLACK;
        }
    }*/
}

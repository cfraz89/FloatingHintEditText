package com.trogdor.widgets;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Region;
import android.util.TypedValue;
import com.trogdor.floatinghintedittext.R;

/**
 * Created by chrisfraser on 8/04/15.
 */
public class ErrorTextHandler {
    final Paint paint = new Paint();
    final float errorScale;
    final int errorPadding;
    final int animationSteps;
    int animationFrame;
    Animation animation;
    String error;

    public ErrorTextHandler(MaterialEditText editText)
    {
        TypedValue errorScaleTyped = new TypedValue();
        editText.getResources().getValue(R.dimen.error_scale, errorScaleTyped, true);
        errorScale = errorScaleTyped.getFloat();
        errorPadding = (int) editText.getResources().getDimension(R.dimen.error_padding);
        animationSteps = editText.getResources().getInteger(R.dimen.animation_steps);
    }

    public void onDraw(Canvas canvas, MaterialEditText editText) {
        final boolean isAnimating = animation != Animation.NONE;

        if (error != null) {
            paint.set(editText.getPaint());
            paint.setColor(editText.getResources().getColor(android.R.color.holo_red_light));

            final float posX = editText.getCompoundPaddingLeft();
            final float topPosY = editText.getBottom() + errorPadding;
            final float bottomPosY = topPosY + 20.0f;
            final float fontSize = editText.getTextSize();
            final float errorSize = fontSize * errorScale;

            paint.setTextSize(errorSize);
            canvas.clipRect(posX,editText.getBottom(), editText.getMeasuredWidth(), getBottomSpace(editText) + 200.0f, Region.Op.UNION);
            if (!isAnimating) {
                canvas.drawText(error, posX, topPosY, paint);
                return;
            }
            if (animation == Animation.SHOW) {
                drawAnimationFrame(canvas, editText,  errorSize, posX, bottomPosY, topPosY, 0, 255);
            } else {
                drawAnimationFrame(canvas, editText, errorSize, posX, topPosY, bottomPosY, 255, 0);
            }

            animationFrame++;
            if (animationFrame == animationSteps) {
                if (animation == Animation.HIDE)
                    error = null;
                animation = Animation.NONE;
                animationFrame = 0;
            }

            editText.invalidate();
        }
    }

    private void drawAnimationFrame(Canvas canvas, MaterialEditText editText, float errorSize, float posX, float startPosY, float endPosY, int startAlpha, int endAlpha) {
        float dist = startPosY - endPosY;
        float alphaDiff = startAlpha - endAlpha;
        int framesLeft = animationSteps - animationFrame;
        float posY = endPosY + (dist/(float)animationSteps) * framesLeft;
        int alpha = endAlpha + (int)(alphaDiff/animationSteps) * framesLeft;
        paint.setAlpha(alpha);
        canvas.drawText(error, posX, posY, paint);
        paint.setAlpha(255);
    }

    public int getBottomSpace(MaterialEditText editText) {
        final Paint.FontMetricsInt metrics = editText.getPaint().getFontMetricsInt();
        return (int) ((metrics.bottom - metrics.top) * errorScale) + errorPadding;
    }

    public void setError(CharSequence error) {
        if (error != null) {
            this.error = error.toString();
            animation = Animation.SHOW;
        }
        else if (this.error != null) {
            animation = Animation.HIDE;
        }
    }

    public static enum Animation {NONE, SHOW, HIDE}
}

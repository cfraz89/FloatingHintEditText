package com.trogdor.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.trogdor.floatinghintedittext.R;

public class FloatingHintHandler {
    final Paint paint = new Paint();

    ColorStateList hintColors;
    final float hintScale;
    final int animationSteps;
    boolean wasEmpty;
    int animationFrame;
    Animation animation;

    public float getHintScale() {
        return hintScale;
    }

    public FloatingHintHandler(MaterialEditText editText, AttributeSet attrs) {
        this.animation = Animation.NONE;

        TypedValue typedValue = new TypedValue();
        editText.getContext().getResources().getValue(R.dimen.floatinghintedittext_hint_scale, typedValue, true);
        hintScale = typedValue.getFloat();
        animationSteps = editText.getResources().getInteger(R.dimen.animation_steps);

        hintColors = editText.getHintTextColors();
        wasEmpty = TextUtils.isEmpty(editText.getText());
    }

    protected void onDraw(Canvas canvas, MaterialEditText editText) {
        final String hint = editText.getHint().toString();
        if (TextUtils.isEmpty(hint)) {
            return;
        }

        final boolean isAnimating = animation != Animation.NONE;

        // The large hint is drawn by Android, so do nothing.
        if (!isAnimating && TextUtils.isEmpty(editText.getText())) {
            return;
        }

        paint.set(editText.getPaint());
        paint.setColor(
                hintColors.getColorForState(editText.getDrawableState(), hintColors.getDefaultColor()));

        final float hintPosX = editText.getCompoundPaddingLeft() + editText.getScrollX();
        final float normalHintPosY = editText.getBaseline();
        final float floatingHintPosY = normalHintPosY + editText.getPaint().getFontMetricsInt().top + editText.getScrollY();
        final float normalHintSize = editText.getTextSize();
        final float floatingHintSize = normalHintSize * hintScale;

        // If we're not animating, we're showing the floating hint, so draw it and bail.
        if (!isAnimating) {
            paint.setTextSize(floatingHintSize);
            canvas.drawText(hint, hintPosX, floatingHintPosY, paint);
            return;
        }

        if (animation == Animation.SHRINK) {
            drawAnimationFrame(canvas, hint, normalHintSize, floatingHintSize,
                    hintPosX, normalHintPosY, floatingHintPosY);
        } else {
            drawAnimationFrame(canvas, hint, floatingHintSize, normalHintSize,
                    hintPosX, floatingHintPosY, normalHintPosY);
        }

        animationFrame++;

        if (animationFrame == animationSteps) {
            if (animation == Animation.GROW) {
                editText.setHintTextColor(hintColors);
            }
            animation = Animation.NONE;
            animationFrame = 0;
        }

        editText.invalidate();
    }

    private void drawAnimationFrame(Canvas canvas,String hint, float fromSize, float toSize,
                                    float hintPosX, float fromY, float toY) {
        final float textSize = lerp(fromSize, toSize);
        final float hintPosY = lerp(fromY, toY);
        paint.setTextSize(textSize);
        canvas.drawText(hint, hintPosX, hintPosY, paint);
    }

    private float lerp(float from, float to) {
        final float alpha = (float) animationFrame / (animationSteps - 1);
        return from * (1 - alpha) + to * alpha;
    }

    public void onTextChanged(CharSequence text, MaterialEditText editText) {
        final boolean isEmpty = TextUtils.isEmpty(text);

        // The empty state hasn't changed, so the hint stays the same.
        if (wasEmpty == isEmpty) {
            return;
        }

        wasEmpty = isEmpty;

        if (isEmpty) {
            animation = Animation.GROW;
            editText.setHintTextColor(Color.TRANSPARENT);
        } else {
           animation = FloatingHintHandler.Animation.SHRINK;
        }
    }

    public int getFloatingHintHeight(MaterialEditText editText)
    {
        final Paint.FontMetricsInt metrics = editText.getPaint().getFontMetricsInt();
        return (int) ((metrics.bottom - metrics.top) * hintScale);

    }

    public static enum Animation {NONE, SHRINK, GROW}
}
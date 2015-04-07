package com.thebnich.floatinghintedittext;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.TypedValue;

public class FloatingHintHandler {
    final Paint mFloatingHintPaint = new Paint();

    ColorStateList mHintColors;
    float mHintScale;
    int mAnimationSteps;
    boolean mWasEmpty;
    int mAnimationFrame;
    Animation mAnimation;

    public float getHintScale() {
        return mHintScale;
    }

    public FloatingHintHandler(FloatingHintEditText floatingHintEditText) {
        this.mAnimation = Animation.NONE;

        TypedValue typedValue = new TypedValue();
        floatingHintEditText.getResources().getValue(R.dimen.floatinghintedittext_hint_scale, typedValue, true);
        mHintScale = typedValue.getFloat();
        mAnimationSteps = floatingHintEditText.getResources().getInteger(R.dimen.floatinghintedittext_animation_steps);

        mHintColors = floatingHintEditText.getHintTextColors();
        mWasEmpty = TextUtils.isEmpty(floatingHintEditText.getText());
    }

    protected void onDraw(Canvas canvas, FloatingHintEditText editText) {
        final String hint = editText.getHint().toString();
        if (TextUtils.isEmpty(hint)) {
            return;
        }

        final boolean isAnimating = mAnimation != Animation.NONE;

        // The large hint is drawn by Android, so do nothing.
        if (!isAnimating && TextUtils.isEmpty(editText.getText())) {
            return;
        }

        mFloatingHintPaint.set(editText.getPaint());
        mFloatingHintPaint.setColor(
                mHintColors.getColorForState(editText.getDrawableState(), mHintColors.getDefaultColor()));

        final float hintPosX = editText.getCompoundPaddingLeft() + editText.getScrollX();
        final float normalHintPosY = editText.getBaseline();
        final float floatingHintPosY = normalHintPosY + editText.getPaint().getFontMetricsInt().top + editText.getScrollY();
        final float normalHintSize = editText.getTextSize();
        final float floatingHintSize = normalHintSize * mHintScale;

        // If we're not animating, we're showing the floating hint, so draw it and bail.
        if (!isAnimating) {
            mFloatingHintPaint.setTextSize(floatingHintSize);
            canvas.drawText(hint, hintPosX, floatingHintPosY, mFloatingHintPaint);
            return;
        }

        if (mAnimation == Animation.SHRINK) {
            drawAnimationFrame(canvas, hint, normalHintSize, floatingHintSize,
                    hintPosX, normalHintPosY, floatingHintPosY);
        } else {
            drawAnimationFrame(canvas, hint, floatingHintSize, normalHintSize,
                    hintPosX, floatingHintPosY, normalHintPosY);
        }

        mAnimationFrame++;

        if (mAnimationFrame == mAnimationSteps) {
            if (mAnimation == Animation.GROW) {
                editText.setHintTextColor(mHintColors);
            }
            mAnimation = Animation.NONE;
            mAnimationFrame = 0;
        }

        editText.invalidate();
    }

    private void drawAnimationFrame(Canvas canvas,String hint, float fromSize, float toSize,
                                    float hintPosX, float fromY, float toY) {
        final float textSize = lerp(fromSize, toSize);
        final float hintPosY = lerp(fromY, toY);
        mFloatingHintPaint.setTextSize(textSize);
        canvas.drawText(hint, hintPosX, hintPosY, mFloatingHintPaint);
    }

    private float lerp(float from, float to) {
        final float alpha = (float) mAnimationFrame / (mAnimationSteps - 1);
        return from * (1 - alpha) + to * alpha;
    }

    public void onTextChanged(CharSequence text, FloatingHintEditText editText) {
        final boolean isEmpty = TextUtils.isEmpty(text);

        // The empty state hasn't changed, so the hint stays the same.
        if (mWasEmpty == isEmpty) {
            return;
        }

        mWasEmpty = isEmpty;

        if (isEmpty) {
            mAnimation = Animation.GROW;
            editText.setHintTextColor(Color.TRANSPARENT);
        } else {
           mAnimation = FloatingHintHandler.Animation.SHRINK;
        }
    }

    public int getFloatingHintHeight(FloatingHintEditText editText)
    {
        final Paint.FontMetricsInt metrics = editText.getPaint().getFontMetricsInt();
        return (int) ((metrics.bottom - metrics.top) * mHintScale);

    }

    public static enum Animation {NONE, SHRINK, GROW}
}
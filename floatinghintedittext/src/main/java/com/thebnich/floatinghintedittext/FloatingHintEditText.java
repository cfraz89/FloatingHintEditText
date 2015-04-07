package com.thebnich.floatinghintedittext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.FontMetricsInt;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;

public class FloatingHintEditText extends EditText {
    private final FloatingHintHandler floatingHintHandler;

    public FloatingHintEditText(Context context) {
        this(context, null);
    }

    public FloatingHintEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        floatingHintHandler = new FloatingHintHandler(this);
    }


    @Override
    public int getCompoundPaddingTop() {
        return super.getCompoundPaddingTop() + floatingHintHandler.getFloatingHintHeight(this);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (isShown()) {
            floatingHintHandler.onTextChanged(text, this);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        floatingHintHandler.onDraw(canvas, this);
    }

    @Override
    public void setError(CharSequence error) {
        super.setError(error);
        if (error != null && error.length() > 0)
            getBackground().setTint(getResources().getColor(android.R.color.holo_red_light));
        else
            getBackground().setTint(getResources().getColor(android.R.color.darker_gray));

    }




}

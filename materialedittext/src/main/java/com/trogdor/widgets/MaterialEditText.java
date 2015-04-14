package com.trogdor.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;

import com.trogdor.floatinghintedittext.R;

public class MaterialEditText extends EditText {
    private final FloatingHintHandler floatingHintHandler;
    private final ErrorTextHandler errorTextHandler;
    private final int errorColor;
    private CharSequence error;

    public MaterialEditText(Context context) {
        this(context, null);
    }

    public MaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        errorColor = ColorExtractor.getErrorColor(context, attrs);
        //if (errorColor == 0)
        //   errorColor =
        floatingHintHandler = new FloatingHintHandler(this, attrs);
        errorTextHandler = new ErrorTextHandler(this, attrs);
        error = null;
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
    public int getTotalPaddingTop() {
        return super.getTotalPaddingTop();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        floatingHintHandler.onDraw(canvas, this);
        errorTextHandler.onDraw(canvas, this);
    }

    public void setError(CharSequence error) {
        this.error = error;
        errorTextHandler.setError(error);
        if (error != null && error.length() > 0)
            getBackground().setColorFilter(errorColor, PorterDuff.Mode.SRC_IN);
        else
            getBackground().clearColorFilter();

    }

    @Override
    public CharSequence getError() {
        return error;
    }
}

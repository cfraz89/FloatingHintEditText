package com.trogdor.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.EditText;

public class MaterialEditText extends EditText {
    private final FloatingHintHandler floatingHintHandler;
    private final ErrorTextHandler errorTextHandler;
    private CharSequence error;

    public MaterialEditText(Context context) {
        this(context, null);
    }

    public MaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        floatingHintHandler = new FloatingHintHandler(this);
        errorTextHandler = new ErrorTextHandler(this);
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
            getBackground().setTint(getResources().getColor(android.R.color.holo_red_light));
        else
            getBackground().setTint(getResources().getColor(android.R.color.darker_gray));

    }

    @Override
    public CharSequence getError() {
        return error;
    }
}

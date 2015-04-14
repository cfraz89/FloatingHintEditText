package com.trogdor.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.trogdor.floatinghintedittext.R;

/**
 * Created by chrisfraser on 14/04/15.
 */
public class ColorExtractor {
    public static int getErrorColor(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText);
        int defaultErrorColor = context.getResources().getColor(R.color.material_error);
        return a.getColor(R.styleable.MaterialEditText_errorColor, defaultErrorColor);
    }
}

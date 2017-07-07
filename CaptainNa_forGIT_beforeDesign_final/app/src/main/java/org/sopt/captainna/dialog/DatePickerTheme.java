package org.sopt.captainna.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import org.sopt.captainna.R;

import java.lang.reflect.Field;

/**
 * Created by minjeong on 2017-06-30.
 */

public class DatePickerTheme extends DatePicker {
    public DatePickerTheme(Context context) {
        super(context);
        Create(context, null);
    }

    public DatePickerTheme(Context context, AttributeSet attrs) {
        super(context, attrs);
        Create(context, attrs);
    }

    public DatePickerTheme(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Create(context, attrs);
    }

    private void Create(Context clsContext, AttributeSet attrs) {
        try {
            Class<?> clsParent = Class.forName("com.android.internal.R$id");
            NumberPicker clsMonth = (NumberPicker) findViewById(clsParent.getField("month").getInt(null));
            NumberPicker clsDay = (NumberPicker) findViewById(clsParent.getField("day").getInt(null));
            NumberPicker clsYear = (NumberPicker) findViewById(clsParent.getField("year").getInt(null));
            Class<?> clsNumberPicker = Class.forName("android.widget.NumberPicker");
            Field clsSelectionDivider = clsNumberPicker.getDeclaredField("mSelectionDivider");

            clsSelectionDivider.setAccessible(true);
            clsSelectionDivider.set(clsMonth, getResources().getDrawable(R.drawable.popup_day_box1));
            clsSelectionDivider.set(clsDay, getResources().getDrawable(R.drawable.popup_day_box1));
            clsSelectionDivider.set(clsYear, getResources().getDrawable(R.drawable.popup_day_box1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
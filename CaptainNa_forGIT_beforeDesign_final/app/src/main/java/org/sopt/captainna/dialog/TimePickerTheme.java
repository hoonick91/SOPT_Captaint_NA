package org.sopt.captainna.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import org.sopt.captainna.R;

import java.lang.reflect.Field;

/**
 * Created by minjeong on 2017-06-30.
 */

public class TimePickerTheme extends TimePicker {
    public TimePickerTheme(Context context) {
        super(context);
        Create(context, null);
    }

    public TimePickerTheme(Context context, AttributeSet attrs) {
        super(context, attrs);
        Create(context, attrs);
    }

    public TimePickerTheme(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Create(context, attrs);
    }

    private void Create(Context clsContext, AttributeSet attrs) {
        try {
            Class<?> clsParent = Class.forName("com.android.internal.R$id");
            NumberPicker clshour = (NumberPicker) findViewById(clsParent.getField("hour").getInt(null));
            NumberPicker clsmin = (NumberPicker) findViewById(clsParent.getField("minute").getInt(null));
            Class<?> clsNumberPicker = Class.forName("android.widget.NumberPicker");
            Field clsSelectionDivider = clsNumberPicker.getDeclaredField("mSelectionDivider");

            clsSelectionDivider.setAccessible(true);
            clsSelectionDivider.set(clshour, getResources().getDrawable(R.drawable.popup_day_box1));
            clsSelectionDivider.set(clsmin, getResources().getDrawable(R.drawable.popup_day_box1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package org.sopt.captainna.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import org.sopt.captainna.R;

import java.util.Calendar;

import static org.sopt.captainna.activity.CommonVariable.chooseTime;
import static org.sopt.captainna.activity.CommonVariable.chooseTime2;
import static org.sopt.captainna.activity.CommonVariable.chooseday;
import static org.sopt.captainna.activity.CommonVariable.chooseday2;
import static org.sopt.captainna.activity.CommonVariable.isFrombutton;

/**
 * Created by minjeong on 2017-06-30.
 */

public class DateDialog extends Dialog implements DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {

    private TextView fromday;
    private TextView today;
    private TextView fromtime;
    private TextView totime;
    private RelativeLayout canceldate;
    private RelativeLayout submitdate;
    private Calendar cal;

    public TimePickerTheme timePickerTheme;
    public DatePickerTheme datePickerTheme;
    public int year_from;
    public int month_from;
    public int day_from;
    public int hour_from;
    public int min_from;

    public int year_to;
    public int month_to;
    public int day_to;
    public int hour_to;
    public int min_to;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.7f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_date);

        setLayout(); // id연결
        clickListener();

    }

    public DateDialog(Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
    }

    private void clickListener(){
        canceldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        submitdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isFrombutton){
                    chooseday = fromday.getText().toString();
                    chooseTime = fromtime.getText().toString();

                }
                else{
                    chooseday2 = today.getText().toString();
                    chooseTime2 = totime.getText().toString();
                }
                dismiss();
            }
        });


    }
    private void setLayout(){
        canceldate = (RelativeLayout) findViewById(R.id.canceldate);
        submitdate = (RelativeLayout) findViewById(R.id.submitdate);
        timePickerTheme = (TimePickerTheme)findViewById(R.id.time);
        datePickerTheme = (DatePickerTheme)findViewById(R.id.date);
        fromday = (TextView) findViewById(R.id.fromday);
        today = (TextView) findViewById(R.id.today);
        fromtime = (TextView) findViewById(R.id.fromtime);
        totime = (TextView) findViewById(R.id.totime);


       cal = Calendar.getInstance();
        year_from = cal.get(Calendar.YEAR);
        year_to = cal.get(Calendar.YEAR);
        month_from = cal.get(Calendar.MONTH);
        month_to =  cal.get(Calendar.MONTH);
        day_from = cal.get(Calendar.DAY_OF_MONTH);
        day_to = cal.get(Calendar.DAY_OF_MONTH);
        hour_from = cal.get(Calendar.HOUR_OF_DAY);
        hour_to = cal.get(Calendar.HOUR_OF_DAY);
        min_from  = cal.get(Calendar.MINUTE);
        min_to  = cal.get(Calendar.MINUTE);

        fromday.setText(chooseday);
        fromtime.setText(chooseTime);
        today.setText(chooseday2);
        totime.setText(chooseTime2);


        timePickerTheme.setIs24HourView(true);
        timePickerTheme.setOnTimeChangedListener(this);

        datePickerTheme.init(cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH)), cal.get(Calendar.DAY_OF_MONTH), this);




    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
        if(isFrombutton) {
            fromday.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
            year_from= year;
            month_from = monthOfYear;
            day_from = dayOfMonth;
        }
        else{
            today.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
            year_to= year;
            month_to = monthOfYear;
            day_to = dayOfMonth;
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        String afternoon = "오전 ";

        if (hourOfDay>=12) { //pm일때
            afternoon = "오후 ";
           if(hourOfDay > 12) //13시~23시
            hourOfDay -= 12;
        }

        if(isFrombutton) {
            fromtime.setText(afternoon + hourOfDay + "시 " + minute + "분");
            hour_from = hourOfDay;
            min_from = minute;
        }
        else{
            totime.setText(afternoon + hourOfDay + "시 " + minute + "분");
            hour_to = hourOfDay;
            min_to = minute;
        }
    }



}
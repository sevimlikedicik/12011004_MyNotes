package com.murad.mobileproject.edit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.murad.mobileproject.R;
import com.murad.mobileproject.models.Reminder;

import java.util.Calendar;

public class AddReminderDialog extends Dialog implements View.OnClickListener{

    private ReminderListener listener;
    private Reminder reminder ;
    private ImageView mImageViewCancel;
    private Button mButtonAddReminder;
    private RadioGroup mRadioGroup;
    private TextView mTextViewDate;
    private TextView mTextViewTime;
    private int period = 0;

    private Context context;
    public AddReminderDialog(Context context, Reminder reminder, ReminderListener listener) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_reminder);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        this.listener = listener;
        this.reminder = reminder;
        this.context = context;

        if (reminder == null){
            this.reminder = new Reminder();
        }

        mImageViewCancel = findViewById(R.id.iv_reminder_cancel);
        mButtonAddReminder = findViewById(R.id.btn_add_reminder);
        mRadioGroup = findViewById(R.id.radiogroup);
        mTextViewDate = findViewById(R.id.tv_picker_date);
        mTextViewTime = findViewById(R.id.tv_picker_time);
        mImageViewCancel.setOnClickListener(this);
        mButtonAddReminder.setOnClickListener(this);
        mTextViewDate.setOnClickListener(this);
        mTextViewTime.setOnClickListener(this);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.daily) {
                    period = 1;
                } else if(checkedId == R.id.weekly) {
                    period = 7;
                } else  if(checkedId == R.id.monthly){
                    period = 30;
                }
            }
        });

    }

    private void selectDate(){
            // Get Current Date
           final Calendar c = Calendar.getInstance();
           int mYear = c.get(Calendar.YEAR);
           int mMonth = c.get(Calendar.MONTH);
           int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            String day = "" + dayOfMonth;
                            if (dayOfMonth < 10)
                                day = "0" + dayOfMonth;
                            String month = "";
                            if (++monthOfYear < 10)
                                month = "0" + monthOfYear;
                            else {
                                month = "" + monthOfYear;
                            }

                            reminder.setDate(day + "-" + month + "-" + year);
                            mTextViewDate.setText(reminder.getDate());

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

    }

    private void selectTime(){
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String hour = "" + hourOfDay;
                        if (hourOfDay < 10)
                            hour = "0" + hourOfDay;
                        String minuteString = "";
                        if (minute < 10)
                            minuteString = "0" + minute;
                        else {
                            minuteString = "" + minute;
                        }
                        reminder.setTime(hour + ":" + minuteString);
                        mTextViewTime.setText(reminder.getTime());
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_reminder :
                reminder.setPeriod(period);
                listener.onReminderInfo(reminder);
                dismiss();
                break;
            case R.id.iv_reminder_cancel :
                dismiss();
                break;
            case R.id.tv_picker_date :
                selectDate();
                break;
            case R.id.tv_picker_time :
                selectTime();
                break;
        }
    }


}

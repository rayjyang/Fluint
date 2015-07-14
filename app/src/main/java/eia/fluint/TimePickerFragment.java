package eia.fluint;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class TimePickerFragment extends DialogFragment {

    public static final String EXTRA_TIME = "TimePickerFragment";

    private Date date;
    private int hour, min;

    public static TimePickerFragment newInstance(Date date) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TIME, date);
        fragment.setArguments(args);
        return fragment;
    }

    public TimePickerFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        date = (Date) getArguments().getSerializable(EXTRA_TIME);

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        View v = getActivity().getLayoutInflater().inflate(R.layout.custom_time_picker, null);
        TimePicker tp = (TimePicker) v.findViewById(R.id.dialog_time_timePicker);
        tp.setCurrentHour(hour);
        tp.setCurrentMinute(min);

        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                final Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int year = cal.get(Calendar.YEAR);
                int monthOfYear = cal.get(Calendar.MONTH);
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

                date = new GregorianCalendar(year, monthOfYear, dayOfMonth, hourOfDay, minute).getTime();

                // Update argument to preserve selected value on rotation
                getArguments().putSerializable(EXTRA_TIME, date);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();


    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent i = new Intent();
        i.putExtra(EXTRA_TIME, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }
}

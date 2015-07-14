package eia.fluint;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "DatePickerFragment";
    private Date date;
    private int year, month, day;

    public DatePickerFragment() {
        // Required empty public constructor
    }

    public static DatePickerFragment newInstance(Date date) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);
        datePickerFragment.setArguments(args);
        return datePickerFragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        date = (Date) getArguments().getSerializable(EXTRA_DATE);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        View dialog = getActivity().getLayoutInflater().inflate(R.layout.custom_date_picker, null);
        initializeDatePicker(c, dialog);

        return new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setView(dialog)
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

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);

    }


    private void initializeDatePicker(Calendar calendar, View view) {
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.dialog_date_picker);
        datePicker.init(year, month, day,
                new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker picker, int year, int month, int day) {
                        date = new GregorianCalendar(year, month, day).getTime();
                        getArguments().putSerializable(EXTRA_DATE, date);
                    }
                }
        );
    }

}

package eia.fluint;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TransactionAirportFragment extends Fragment {

    public static final String TAG_FRAGMENT = "TransactionAirportFragment";

    private Date currentDate;
    private SimpleDateFormat dateFormat;
    private String year;
    private String month;
    private String day;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TransactionAirportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionAirportFragment newInstance() {
        TransactionAirportFragment fragment = new TransactionAirportFragment();
        return fragment;
    }

    public TransactionAirportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_airport, container, false);

        Calendar mCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        };

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    ChooseLocationFragment fragment = new ChooseLocationFragment();

                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                    ft.setCustomAnimations(R.animator.slide_in_from_left, R.animator.slide_out_to_right);

                    ft.replace(R.id.addPostFragmentContainer, fragment);
                    ft.addToBackStack(null);
                    ft.commit();

                    return true;
                }
                return false;
            }
        });

        return view;
    }




}

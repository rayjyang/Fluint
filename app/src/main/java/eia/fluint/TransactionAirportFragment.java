package eia.fluint;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialripple.MaterialRippleLayout;

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

    private LinearLayout currencyChoiceContainer;
    private LinearLayout amountChoiceETContainer;
    private LinearLayout arrivalDateChoiceContainer;
    private LinearLayout arrivalTimeChoiceContainer;
    private LinearLayout flightNoChoiceContainer;
    private LinearLayout airportChoiceContainer;

    private AppCompatButton submitNewPost;

    private TextView tvChangeCurrency;
    private TextView tvChangeAmount;
    private TextView tvChangeArrivalDate;
    private TextView tvChangeArrivalTime;
    private TextView tvChangeFlightNo;
    private TextView tvChangeAirport;

    private String curDate;

    private int currencyWhich = 0;
    private String currencyCodeChoice;



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

        // Get current date and set as Arrival Date TextView
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        curDate = sdf.format(c.getTime());

        // TODO: set OnClickListeners on all the LinearLayouts, wrap in ripple effect

        setUpView(view);
        setUpOnClicks(view);

        currencyChoiceContainer = (LinearLayout) view.findViewById(R.id.currencyChoiceContainer);
        currencyChoiceContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
                builder.title("Choose your currency");
                builder.items(R.array.CurrencyCodesValues);
                builder.itemsCallbackSingleChoice(currencyWhich, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        currencyWhich = i;
                        currencyCodeChoice = charSequence.toString();


                        Toast.makeText(getActivity(), currencyCodeChoice, Toast.LENGTH_SHORT).show();

                        tvChangeCurrency.setText(charSequence);
                        materialDialog.dismiss();
                        Toast.makeText(getActivity(), "Got past dismiss", Toast.LENGTH_SHORT).show();

                        return true;
                    }
                });
                builder.callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                });
                builder.positiveText("CANCEL");
                builder.show();


            }
        });

        return view;
    }

    private void setUpOnClicks(View view) {
        // TODO: Set up OnClicks for all LinearLayouts


    }

    private void setUpView(View view) {
        tvChangeCurrency = (TextView) view.findViewById(R.id.tvChangeCurrency);
        tvChangeAmount = (TextView) view.findViewById(R.id.tvChangeAmount);
        tvChangeArrivalDate = (TextView) view.findViewById(R.id.tvChangeArrivalDate);
        tvChangeArrivalTime = (TextView) view.findViewById(R.id.tvChangeArrivalTime);
        tvChangeFlightNo = (TextView) view.findViewById(R.id.tvChangeFlightNo);
        tvChangeAirport = (TextView) view.findViewById(R.id.tvChangeAirport);

    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    ChooseLocationFragment fragment = new ChooseLocationFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();

//                    ft.setCustomAnimations(R.animator.slide_in_from_left, R.animator.slide_out_to_right);

                    ft.replace(R.id.addPostFragmentContainer, fragment, "ChooseLocationFragment");
                    ft.addToBackStack(null);
                    ft.commit();

                    // TODO: Change toolbar title


                    // TODO: Fix animations

                    return true;
                }
                return false;
            }
        });


    }
}

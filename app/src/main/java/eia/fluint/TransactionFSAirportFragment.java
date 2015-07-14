package eia.fluint;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialripple.MaterialRippleLayout;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TransactionFSAirportFragment extends Fragment {

    public static final String TAG_FRAGMENT = "TransactionFSAirportFragment";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_TIME = "time";

    private Date currentDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
    private int year;
    private int month;
    private int day;

    private LinearLayout currencyChoiceContainer;
    private LinearLayout amountChoiceContainer;
    private LinearLayout arrivalDateContainer;
    private LinearLayout arrivalTimeContainer;
    private LinearLayout flightNumberContainer;
    private LinearLayout airportNameContainer;

    // References to the buy/sell tabs
    private CardView cardViewBuyToggleIndex;
    private CardView cardViewSellToggleIndex;
    private TextView buyToggleIndex;
    private TextView sellToggleIndex;

    private AppCompatButton submitNewPost;

    private TextView tvChangeCurrency;
    private TextView tvChangeAmount;
    private TextView tvChangeArrivalDate;
    private TextView tvChangeArrivalTime;
    private TextView tvChangeFlightNo;
    private TextView tvChangeAirport;

    private String curDate;

    private int currencyWhich = 0;

    // Data to collect from user input
    private String transactionChoice;
    private String currencyCodeChoice;
    private int amountChoice;
    private String userSelectedStringDate;
    private String userSelectedStringTime;
    private String flightNo;
    private String airportName;
    private Location airport;



    // Data to collect in int format -- for speedier database queries
    private int currencyIntChoice;
    private Date userSelectedDate;
    private Date userSelectedTime;

    private Transaction transaction;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TransactionFSAirportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionFSAirportFragment newInstance() {
        TransactionFSAirportFragment fragment = new TransactionFSAirportFragment();
        return fragment;
    }

    public TransactionFSAirportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transaction = new Transaction();
        transactionChoice = "sell";
        transaction.setTransactionType(transactionChoice);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_airport, container, false);

        // Get current date and set as Arrival Date TextView
        Calendar c = Calendar.getInstance();
        curDate = sdf.format(c.getTime());
        currentDate = c.getTime();
        userSelectedDate = currentDate;
        userSelectedTime = currentDate;

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // TODO: set OnClickListeners on all the LinearLayouts, wrap in ripple effect

        setUpView(view);
        setUpOnClicks(view);

        return view;
    }

    private void setUpOnClicks(View view) {
        // TODO: Set up OnClicks for all LinearLayouts

        cardViewBuyToggleIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewBuyToggleIndex.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                buyToggleIndex.setTextColor(getResources().getColor(R.color.whiteColor));
                cardViewSellToggleIndex.setBackgroundColor(getResources().getColor(R.color.whiteColor));
                sellToggleIndex.setTextColor(getResources().getColor(R.color.primaryColor));

                transactionChoice = "buy";

                transaction.setTransactionType(transactionChoice);
            }
        });

        cardViewSellToggleIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewSellToggleIndex.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                sellToggleIndex.setTextColor(getResources().getColor(R.color.whiteColor));
                cardViewBuyToggleIndex.setBackgroundColor(getResources().getColor(R.color.whiteColor));
                buyToggleIndex.setTextColor(getResources().getColor(R.color.primaryColor));

                transactionChoice = "sell";

                transaction.setTransactionType(transactionChoice);
            }
        });


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
                        tvChangeCurrency.setText(charSequence);
                        transaction.setCurrency(currencyCodeChoice);
                        return true;
                    }
                });
                builder.positiveText("OK");
                builder.show();


            }
        });

        amountChoiceContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(getActivity())
                        .title("Choose amount")
                        .customView(R.layout.custom_number_picker, false)
                        .positiveText("OK")
                        .show();
            }
        });

        arrivalDateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Show DatePicker dialog
                FragmentManager fm = getActivity().getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(userSelectedDate);
                dialog.setTargetFragment(TransactionFSAirportFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        arrivalTimeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm1 = getActivity().getFragmentManager();
                TimePickerFragment tpDialog = TimePickerFragment.newInstance(userSelectedTime);
                tpDialog.setTargetFragment(TransactionFSAirportFragment.this, REQUEST_TIME);
                tpDialog.show(fm1, DIALOG_TIME);
            }
        });

        flightNumberContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        airportNameContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        submitNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: Show either ProgressDialog or use Material Progress Button


                // TODO: Check if fields are null. If so, show user Error


                // Buy post will end up in Sell page
                // Sell post will end up in Buy page

                // TODO: throwing a NPE. Check if .getTransactionType() returns null
                if (transaction.getTransactionType().equals("buy")) {
                    ParseObject buyPost = new ParseObject("BuyPost");
                    buyPost.put("transactionObject", transaction);
                    buyPost.put("transactionType", transaction.getTransactionType());
                    buyPost.put("currency", transaction.getCurrency());
                    buyPost.put("amount", transaction.getAmount());
                    buyPost.put("arrival", transaction.getArrival());
                    buyPost.put("location", transaction.getLocation());
                    buyPost.put("flightNo", transaction);
                    buyPost.put("airport", transaction.getAirport());

                    buyPost.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // Save successful. Finish AddForSalePostActivity and refresh Buy feed

                            } else {
                                // Save did not occur successfully. Ask user to try again later

                            }
                        }
                    });
                } else {
                    ParseObject sellPost = new ParseObject("SellPost");
//                    sellPost.put("");

                }

            }
        });


    }

    private void setUpView(View view) {
        tvChangeCurrency = (TextView) view.findViewById(R.id.tvChangeCurrency);
        tvChangeAmount = (TextView) view.findViewById(R.id.tvChangeAmount);
        tvChangeArrivalDate = (TextView) view.findViewById(R.id.tvChangeArrivalDate);
        tvChangeArrivalTime = (TextView) view.findViewById(R.id.tvChangeArrivalTime);
        tvChangeFlightNo = (TextView) view.findViewById(R.id.tvChangeFlightNo);
        tvChangeAirport = (TextView) view.findViewById(R.id.tvChangeAirport);

        currencyChoiceContainer = (LinearLayout) view.findViewById(R.id.currencyChoiceContainer);
        amountChoiceContainer = (LinearLayout) view.findViewById(R.id.amountChoiceContainer);
        arrivalDateContainer = (LinearLayout) view.findViewById(R.id.arrivalDateContainer);
        arrivalTimeContainer = (LinearLayout) view.findViewById(R.id.arrivalTimeContainer);
        flightNumberContainer = (LinearLayout) view.findViewById(R.id.flightNumberContainer);
        airportNameContainer = (LinearLayout) view.findViewById(R.id.airportNameContainer);

        cardViewBuyToggleIndex = (CardView) view.findViewById(R.id.cardViewBuyToggleIndex);
        cardViewSellToggleIndex = (CardView) view.findViewById(R.id.cardViewSellToggleIndex);
        buyToggleIndex = (TextView) view.findViewById(R.id.buyToggleIndex);
        sellToggleIndex = (TextView) view.findViewById(R.id.sellToggleIndex);

        submitNewPost = (AppCompatButton) view.findViewById(R.id.submitNewPost);

        MaterialRippleLayout.on(cardViewBuyToggleIndex).rippleColor(Color.BLACK).create();
        MaterialRippleLayout.on(cardViewSellToggleIndex).rippleColor(Color.BLACK).create();

        tvChangeArrivalDate.setText(curDate);

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

                    ft.replace(R.id.addForSalePostFragmentContainer, fragment, "ChooseLocationFragment");
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            userSelectedDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            // TODO: Set the user-specified date in our Transaction object

            // TODO: Update tvChangeArrivalDate
            userSelectedStringDate = sdf.format(userSelectedDate);
            tvChangeArrivalDate.setText(userSelectedStringDate);

        } else if (requestCode == REQUEST_TIME) {
            userSelectedTime = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            // TODO: Set the user-specified time in our Transaction object

            // TODO: Update tvChangeArrivalTime
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            userSelectedStringTime = sdf1.format(userSelectedTime);
            tvChangeArrivalTime.setText(userSelectedStringTime);
        }
    }
}

package eia.fluint;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;


public class ForSaleFeedFragment extends Fragment implements FeedAdapter.ClickListener {

    private static final String TAG = "ForSaleFeedFragment";
    private static final int RAD_PREF = 25;
    private static final int MIN_BUY_PREF = 1;
    private static final int MAX_BUY_FREF = 1000;

    private RecyclerView recyclerView;
    private FeedAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout feedSwipeRefresh;

    private Set<String> defaultBuyPreference;
    private Set<String> buyPreference;
    private int minBuyPreference;
    private int maxBuyPreference;
    private int radiusPreference;

    private double latitude = 0.0;
    private double longitude = 0.0;
    private ParseGeoPoint currentLocation;

    private SharedPreferences globalUserSettings;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ForSaleFeedFragment.
     */
    public static ForSaleFeedFragment newInstance() {
        ForSaleFeedFragment fragment = new ForSaleFeedFragment();
        return fragment;
    }

    public ForSaleFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globalUserSettings = getActivity().getSharedPreferences("eia.fluint.USER_SETTINGS", Context.MODE_PRIVATE);

        defaultBuyPreference = new HashSet<>();
        defaultBuyPreference.add("USD");
        defaultBuyPreference.add("EUR");
        defaultBuyPreference.add("JPY");
        defaultBuyPreference.add("GBP");
        defaultBuyPreference.add("CNY");
        defaultBuyPreference.add("AUD");
        defaultBuyPreference.add("CAD");

        buyPreference = globalUserSettings.getStringSet("buyPreference", defaultBuyPreference);
        minBuyPreference = globalUserSettings.getInt("minBuyPreference", MIN_BUY_PREF);
        maxBuyPreference = globalUserSettings.getInt("maxBuyPreference", MAX_BUY_FREF);
        radiusPreference = globalUserSettings.getInt("radiusPreference", RAD_PREF);

        Log.d("BUY_PREF", buyPreference.toString());
        Log.d("MIN_BUY_PREF", minBuyPreference + "");
        Log.d("MAX_BUY_PREF", maxBuyPreference + "");
        Log.d("RAD_PREF", radiusPreference + "");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_for_sale_feed, container, false);
        view.setTag(TAG);

        // TODO: Get references to the layout views

        feedSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.feedSwipeRefresh);
        feedSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO: refresh content and update location
                boolean hasLocation = getUserLocation();
                if (!hasLocation) {
                    // TODO: display a snackbar saying we could not find location at this time
                    // use a saved location?
                    mAdapter = new FeedAdapter(getActivity(), getPreferredPosts());
                    recyclerView.setAdapter(mAdapter);
//                    getPreferredPosts();
                } else {
                    mAdapter = new FeedAdapter(getActivity(), getPreferredPosts());
                    recyclerView.setAdapter(mAdapter);
//                    getPreferredPosts();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        feedSwipeRefresh.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        feedSwipeRefresh.setColorSchemeResources(R.color.primaryColor);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new FeedAdapter(getActivity(), getPreferredPosts());
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);


        // mAdapter = new FeedAdapter(myDataset);
        // recyclerView.setAdapter(mAdapter);


        // TODO: XML: wrap FAB in a CoordinatorLayout

        // TODO: Change color of fab with setBackgroundTintList(ColorStateList)

        // TODO: Display icon in fab with setImageDrawable(Drawable)

        return view;
    }

//    private List<Transaction> fakeData() {
//        ArrayList<Transaction> data = new ArrayList<>();
//        for (int i = 0; i < 40; i++) {
//            Transaction obj = new Transaction();
//            obj.setPosterName("Raymond Yang");
//            obj.setAmountA((i + 4) * 3 * i + 15);
//            obj.setCurrencyA("EUR");
//            obj.setAmountB((int) (((i + 4) * 3 * i + 15) * 1.08300));
//            obj.setCurrencyB("USD");
//            data.add(obj);
//        }
//
//        return data;
//    }

    // TODO: do parse methods and run them in background off the UI thread


    @Override
    public void onResume() {
        super.onResume();

        // Get all user preferences

        Toast.makeText(getActivity(), "FS onResume", Toast.LENGTH_SHORT).show();

        // Gets the currency that the user is interested in seeing on the FOR SALE PAGE
        buyPreference = globalUserSettings.getStringSet("buyPreference", defaultBuyPreference);
        minBuyPreference = globalUserSettings.getInt("minBuyPreference", MIN_BUY_PREF);
        maxBuyPreference = globalUserSettings.getInt("maxBuyPreference", MAX_BUY_FREF);
        radiusPreference = globalUserSettings.getInt("radiusPreference", RAD_PREF);

        Log.d("BUY_PREF", buyPreference.toString());
        Log.d("MIN_BUY_PREF", minBuyPreference + "");
        Log.d("MAX_BUY_PREF", maxBuyPreference + "");
        Log.d("RAD_PREF", radiusPreference + "");
        boolean hasLocation = getUserLocation();
        if (!hasLocation) {
            // TODO: display a snackbar indicating we could not get their location at this time

        }



        mAdapter = new FeedAdapter(getActivity(), getPreferredPosts());
        recyclerView.setAdapter(mAdapter);
    }



    public List<Transaction> getPreferredPosts() {

        final ArrayList<Transaction> transactions = new ArrayList<>();
        Calendar c = Calendar.getInstance();


        // query1 is for when the seller used his current location
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("ForSalePost");
        query1.whereEqualTo("resolved", false);
        query1.whereEqualTo("transactionType", "sell");
        query1.whereEqualTo("where", "CL");
        query1.whereContainedIn("currencyA", buyPreference);
        query1.whereLessThanOrEqualTo("amountA", maxBuyPreference);
        query1.whereGreaterThanOrEqualTo("amountA", minBuyPreference);
        query1.whereEqualTo("locationType", "curr");
//        query1.whereWithinKilometers("currentLoc", currentLocation, 100);

        Date currentDate = new Date();
        c.setTime(currentDate);
        c.add(Calendar.HOUR, -6);
        Date sixHoursAgo = c.getTime();
        Log.d("SIXHOURAGO", sixHoursAgo.toString());
        query1.whereGreaterThanOrEqualTo("createdAt", sixHoursAgo);

        query1.setLimit(200);
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject trans : list) {

                    }
                }
            }
        });


        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("ForSalePost");
        query2.whereEqualTo("resolved", false);
        query2.whereEqualTo("transactionType", "sell");
        query2.whereEqualTo("where", "PL");
        query2.whereContainedIn("currencyA", buyPreference);
        query2.whereLessThanOrEqualTo("amountA", maxBuyPreference);
        query2.whereGreaterThanOrEqualTo("amountB", minBuyPreference);
        query2.whereEqualTo("locationType", "pick");
//        query2.whereWithinKilometers("location", currentLocation, 100);

        c.setTime(currentDate);
        c.add(Calendar.DATE, -30);
        Date thirtyDaysAgo = c.getTime();
        Log.d("THIRTYDAYSAGO", thirtyDaysAgo.toString());
        query2.whereGreaterThanOrEqualTo("createdAt", thirtyDaysAgo);

        query2.setLimit(200);
        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject trans : list) {
                        Log.d(TAG, "findInBackground in ok");
                        Transaction t = new Transaction();
                        t.setTransactionType(trans.getString("transactionType"));
                        t.setWhere(trans.getString("where"));
                        t.setAmountA(trans.getInt("amountA"));
                        t.setCurrencyA(trans.getString("currencyA"));
                        t.setAmountB(trans.getInt("amountB"));
                        t.setCurrencyB(trans.getString("currencyB"));
                        t.setLocationPoint(trans.getParseGeoPoint("location"));
                        t.setLocationType(trans.getString("locationType"));
                        t.setRadius(trans.getInt("radius"));
                        t.setResolved(trans.getBoolean("resolved"));
                        t.setPosterName(trans.getString("posterName"));
                        t.setPosterId(trans.getString("posterId"));
                        t.setOpUsername(trans.getString("username"));
                        t.setCreatedAt(trans.getCreatedAt());

                        transactions.add(t);

                    }
                } else {
                    // TODO: display a cute fragment indicating we could not retrieve all posts at this time
                    Log.d(TAG, "Could not retrieve posts at this time.");
                }
            }
        });
        // TODO: research if it is possible to split up a query into different queries
        // since we are placing the same first few restrictions




        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("ForSalePost");
        query3.whereEqualTo("resolved", false);
        query3.whereEqualTo("transactionType", "sell");
        query3.whereEqualTo("where", "AP");

        return transactions;
    }

    private boolean getUserLocation() {
        GPSTracker gps = ((MainFeedActivity) getActivity()).getGps();
        try {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            currentLocation = new ParseGeoPoint(latitude, longitude);
            Toast.makeText(getActivity(), "" + latitude + "  ####  " + longitude, Toast.LENGTH_LONG).show();


        } catch (NullPointerException e) {
            Log.d("USER_LOC_NULL", e.getMessage());
        }

        // Latitude and longitude might be null
        if (latitude != 0.0 && longitude != 0.0) {
            // currentLocation will contain the correct user's location
            return true;
        } else {
            return false;
        }


    }


    @Override
    public void itemClicked(View view, int position) {
        Toast.makeText(getActivity(), position + "", Toast.LENGTH_SHORT).show();

    }


}

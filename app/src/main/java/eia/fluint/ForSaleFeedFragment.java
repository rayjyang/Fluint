package eia.fluint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class ForSaleFeedFragment extends Fragment {

    private static final String TAG = "ForSaleFeedFragment";
    private static final String FS_POST_TAG = "FS_POSTS";
    private static final int RAD_PREF = 25;
    private static final int MIN_BUY_PREF = 1;
    private static final int MAX_BUY_FREF = 1000;
    private static final String DIST_PREF = "km";

    private RecyclerView recyclerView;
    private FeedAdapterFS mAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout feedSwipeRefresh;

    private Set<String> defaultBuyPreference;
    private Set<String> buyPreference;
    private int minBuyPreference;
    private int maxBuyPreference;
    private int radiusPreference;
    private String distancePreference;

    private double latitude = 0.0;
    private double longitude = 0.0;
    private ParseGeoPoint currentLocation;

    private SharedPreferences globalUserSettings;

    private ArrayList<Transaction> intentTransactions;

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
        distancePreference = globalUserSettings.getString("distancePreference", DIST_PREF);

        Log.d("BUY_PREF", buyPreference.toString());
        Log.d("MIN_BUY_PREF", minBuyPreference + "");
        Log.d("MAX_BUY_PREF", maxBuyPreference + "");
        Log.d("RAD_PREF", radiusPreference + "");
        Log.d("DIST_PREF", distancePreference);


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
                    mAdapter = new FeedAdapterFS(getActivity(), getPreferredPosts());
                    mAdapter.notifyDataSetChanged();
//                    getPreferredPosts();
                } else {
                    mAdapter = new FeedAdapterFS(getActivity(), getPreferredPosts());
                    mAdapter.notifyDataSetChanged();
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
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    Transaction details = intentTransactions.get(position);
                } catch (Exception e) {
                    Log.d("EmptyArr", "Transactions size: " + intentTransactions.size());
                }
                Intent intent = new Intent(getActivity(), PostDetailsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new FeedAdapterFS(getActivity(), getPreferredPosts());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();



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
        distancePreference = globalUserSettings.getString("distancePreference", DIST_PREF);

        Log.d("BUY_PREF", buyPreference.toString());
        Log.d("MIN_BUY_PREF", minBuyPreference + "");
        Log.d("MAX_BUY_PREF", maxBuyPreference + "");
        Log.d("RAD_PREF", radiusPreference + "");
        Log.d("DIST_PREF", distancePreference);
        boolean hasLocation = getUserLocation();
        if (!hasLocation) {
            // TODO: display a snackbar indicating we could not get their location at this time

        }



        mAdapter = new FeedAdapterFS(getActivity(), getPreferredPosts());
        mAdapter.notifyDataSetChanged();
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
//        query2.orderByDescending("createdAt");
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
                    String parsedName, parsedDetails, parsedA, parsedB, parsedDistance;

                    int amountA, amountB;
                    String currencyA, currencyB, fullName, posterId;
                    ParseGeoPoint location;


                    PostParser pp = new PostParser(getActivity());
                    for (ParseObject trans : list) {
                        final Transaction t = new Transaction();
                        amountA = trans.getInt("amountA");
                        amountB = trans.getInt("amountB");
                        currencyA = trans.getString("currencyA");
                        currencyB = trans.getString("currencyB");
                        fullName = trans.getString("posterName");
                        location = trans.getParseGeoPoint("location");
                        posterId = trans.getString("posterId");

                        // TODO: find user in background and get rating (2 columns)
                        ParseQuery<ParseUser> queryForUser = ParseUser.getQuery();
                        queryForUser.getInBackground(posterId, new GetCallback<ParseUser>() {
                            @Override
                            public void done(ParseUser parseUser, ParseException e) {
                                if (e == null) {
                                    double rating = parseUser.getInt("rating");
                                    int numRatings = parseUser.getInt("numRatings");

                                    double average = rating / numRatings;

                                    t.setParsedRating(average + "");

                                } else {
                                    t.setParsedRating(4.5 + "");
                                }
                            }
                        });


                        Log.d(TAG, "findInBackground in ok");
                        t.setTransactionType(trans.getString("transactionType"));
                        t.setWhere(trans.getString("where"));
                        t.setAmountA(amountA);
                        t.setCurrencyA(currencyA);
                        t.setAmountB(amountB);
                        t.setCurrencyB(currencyB);
                        t.setLocationPoint(location);
                        t.setLocationType(trans.getString("locationType"));
                        t.setRadius(trans.getInt("radius"));
                        t.setResolved(trans.getBoolean("resolved"));
                        t.setPosterName(fullName);
                        t.setPosterId(posterId);
                        t.setOpUsername(trans.getString("username"));
                        t.setCreatedAt(trans.getCreatedAt());

                        // used to calculate location distances on the cardview
                        t.setCurrentPoint(currentLocation);

                        parsedName = pp.parseName(fullName);
                        parsedA = pp.parseA(amountA + "", currencyA);
                        parsedB = pp.parseB(amountB + "", currencyB);
                        parsedDistance = pp.parseDistance(t);


                        t.setParsedName(parsedName);
                        t.setParsedA(parsedA);
                        t.setParsedB(parsedB);
                        t.setParsedDistance(parsedDistance);


                        transactions.add(t);

                    }

                    ParseObject.unpinAllInBackground(FS_POST_TAG, list, new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d(FS_POST_TAG, "WHEEEEEE");
                            } else {
                                Log.e(FS_POST_TAG, "UHOHH", e);
                                return;
                            }
                        }
                    });

                    ParseObject.pinAllInBackground(FS_POST_TAG, list);
                } else {
                    // TODO: display a cute fragment indicating we could not retrieve all posts at this time
                    Log.d(TAG, "Could not retrieve posts at this time.");
                    return;
                }
            }
        });
        // TODO: research if it is possible to split up a query into different queries
        // since we are placing the same first few restrictions




        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("ForSalePost");
        query3.whereEqualTo("resolved", false);
        query3.whereEqualTo("transactionType", "sell");
        query3.whereEqualTo("where", "AP");

        intentTransactions = transactions;

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


}

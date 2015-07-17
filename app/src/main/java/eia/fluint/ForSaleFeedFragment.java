package eia.fluint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;




public class ForSaleFeedFragment extends Fragment implements FeedAdapter.ClickListener {

    private static final String TAG = "ForSaleFeedFragment";

    private RecyclerView recyclerView;
    private FeedAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout feedSwipeRefresh;

    private String buyPreference = "None";

    private ParseGeoPoint currentLocation;
    private int radiusWithin;

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
                getUserLocation();
                getLatestPostsLocSorted(buyPreference);
            }
        });

        // TODO: set color scheme of the SwipeRefreshLayout
        feedSwipeRefresh.setColorSchemeResources(R.color.primaryColor);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new FeedAdapter(getActivity(), fakeData());
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);


        // mAdapter = new FeedAdapter(myDataset);
        // recyclerView.setAdapter(mAdapter);


        // TODO: XML: wrap FAB in a CoordinatorLayout

        // TODO: Change color of fab with setBackgroundTintList(ColorStateList)

        // TODO: Display icon in fab with setImageDrawable(Drawable)

        return view;
    }

    private List<Transaction> fakeData() {
        ArrayList<Transaction> data = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            Transaction obj = new Transaction();
            obj.setPosterName("Raymond Yang");
            obj.setAmountA((i + 4) * 3 * i + 15);
            obj.setCurrencyA("EUR");
            obj.setAmountB((i + 5) * 3 * i + 15);
            obj.setCurrencyB("USD");
            data.add(obj);
        }

        return data;
    }


    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        buyPreference = sp.getString(getResources().getString(R.string.buy_preference), "None");

        Toast.makeText(getActivity(), buyPreference, Toast.LENGTH_SHORT).show();

        getUserLocation();
        getLatestPostsLocSorted(buyPreference);

    }



    public void getLatestPostsLocSorted(String pref) {
        // TODO: get latest posts

//        SharedPreferences sp

        // TODO: add user's country to settings and if they're from America use miles



        if (pref.equals("None")) {
            // User has no preference to what currency he wants to buy
            ParseQuery<ParseObject> query = ParseQuery.getQuery("ForSalePost");
            query.whereEqualTo("resolved", false);

            query.whereWithinKilometers("pickedLoc", currentLocation, radiusWithin);

            Date currentDate = new Date();

            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);
            c.add(Calendar.DATE, -30);
            Date thirtyDaysAgo = c.getTime();

            query.whereLessThanOrEqualTo("createdAt", thirtyDaysAgo);



        } else {


        }


    }

    private void getUserLocation() {
        // TODO: get user's location
        GPSTracker gps = ((MainFeedActivity) getActivity()).getGps();
        try {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            ParseGeoPoint userPoint = new ParseGeoPoint(latitude, longitude);
            Toast.makeText(getActivity(), "" + latitude + "  ####  " + longitude, Toast.LENGTH_LONG).show();
            // Latitude and longitude are 0.0 and 0.0 for some reason

        } catch (NullPointerException e) {

        }


    }


    @Override
    public void itemClicked(View view, int position) {
        Toast.makeText(getActivity(), position + "", Toast.LENGTH_SHORT).show();
    }


}

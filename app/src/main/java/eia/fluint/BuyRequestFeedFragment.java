package eia.fluint;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;


public class BuyRequestFeedFragment extends Fragment {

    private static final String TAG = "BuyRequestFeedFragment";
    private static final int RAD_PREF = 25;
    private static final int MIN_SELL_PREF = 1;
    private static final int MAX_SELL_FREF = 1000;

    private RecyclerView recyclerViewSell;
    private FeedAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshSell;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private ParseGeoPoint currentLocation;

    private SharedPreferences globalUserSettings;
    private Set<String> defaultSellPreference;
    private Set<String> sellPreference;
    private int minSellPreference;
    private int maxSellPreference;
    private int radiusPreference;



    public static BuyRequestFeedFragment newInstance() {
        BuyRequestFeedFragment fragment = new BuyRequestFeedFragment();
        return fragment;
    }

    public BuyRequestFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globalUserSettings = getActivity().getSharedPreferences("eia.fluint.USER_SETTINGS", Context.MODE_PRIVATE);

        defaultSellPreference = new HashSet<>();
        defaultSellPreference.add("USD");
        defaultSellPreference.add("EUR");
        defaultSellPreference.add("JPY");
        defaultSellPreference.add("GBP");
        defaultSellPreference.add("CNY");
        defaultSellPreference.add("AUD");
        defaultSellPreference.add("CAD");

        sellPreference = globalUserSettings.getStringSet("sellPreference", defaultSellPreference);
        minSellPreference = globalUserSettings.getInt("minSellPreference", MIN_SELL_PREF);
        maxSellPreference = globalUserSettings.getInt("maxSellPreference", MAX_SELL_FREF);
        radiusPreference = globalUserSettings.getInt("radiusPreference", RAD_PREF);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_buy_requests_feed, container, false);
        layout.setTag(TAG);

        swipeRefreshSell = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshSell);
        swipeRefreshSell.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshSell.setBackgroundColor(getResources().getColor(R.color.lightColor));
                boolean hasLocation = getUserLocation();
                if (!hasLocation) {
                    // TODO: display a snackbar saying we could not get their location at this time

                }
                getPreferredPosts();
                return;
            }
        });
        swipeRefreshSell.setColorSchemeResources(R.color.primaryColor);

        recyclerViewSell = (RecyclerView) layout.findViewById(R.id.recyclerViewSell);
        recyclerViewSell.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewSell.setLayoutManager(mLayoutManager);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        Toast.makeText(getActivity(), "BR onResume", Toast.LENGTH_SHORT).show();

        // TODO: get user preferences

        boolean hasLocation = getUserLocation();
        if (!hasLocation) {

        }

    }

    private boolean getUserLocation() {

        GPSTracker gps = ((MainFeedActivity) getActivity()).getGps();
        try {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            currentLocation = new ParseGeoPoint(latitude, longitude);
            Toast.makeText(getActivity(), "" + latitude + " ***** " + longitude, Toast.LENGTH_LONG).show();


        } catch (NullPointerException e) {
            Log.d("GPS_NULL", e.getMessage());
        }

        // Latitude and longitude might be null
        if (latitude != 0.0 && longitude != 0.0) {
            // currentLocation will contain the correct user's location
            return true;
        } else {
            return false;
        }
    }

    private void getPreferredPosts() {

        Calendar c = Calendar.getInstance();

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("BuyRequestPost");
        query1.whereEqualTo("transactionType", "buy");
        query1.whereEqualTo("resolved", false);
        query1.whereEqualTo("where", "CurrentLocation");



    }

}

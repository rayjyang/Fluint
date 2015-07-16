package eia.fluint;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.app.Fragment;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;
import java.util.Map;

public class MainFeedActivity extends AppCompatActivity {

    private static final String NAV_ITEM_ID = "navItemId";

    private Toolbar toolbar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mNavItemId;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private GPSTracker gps;

    private FloatingActionButton fabMainFeed;

    private Map<Integer, Fragment> mPageReferenceMap = new HashMap<>();

    private boolean whichView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        gps = new GPSTracker(this);
        Location loc = gps.getLocation();

        if (loc != null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainFeedActivity.this);

        }


        toolbar = (Toolbar) findViewById(R.id.toolBarFeed);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            mNavItemId = R.id.browse;
        } else {
            mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }

        int navValue = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            navValue = extras.getInt("nav", 0);
        }

        mPager = (ViewPager) findViewById(R.id.loginPagerMain);
        mPager.setAdapter(new MainPagerAdapter(getFragmentManager()));
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    // Hide the keyboard.
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(mPager.getWindowToken(), 0);

                }
            }
        });

        mTabs = (SlidingTabLayout) findViewById(R.id.loginTabsMain);
        mTabs.setCustomTabView(R.layout.custom_tab_view_main, R.id.mainTabsText);
        mTabs.setDistributeEvenly(true);
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.whiteColor);
            }
        });
        mTabs.setViewPager(mPager);


        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);

                mNavItemId = menuItem.getItemId();
                drawerLayout.closeDrawers();

                Fragment fragment;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                switch (mNavItemId) {
                    case R.id.navProfile:
                        toolbar.setTitle("Profile");
                        mTabs.setVisibility(View.GONE);
                        mPager.setVisibility(View.GONE);
                        fabMainFeed.setVisibility(View.GONE);
                        mPager.setActivated(false);

                        toolbar.setTitleTextColor(getResources().getColor(R.color.whiteColor));

                        fragment = new ProfileFragment();
                        fragmentTransaction.replace(R.id.feedFragmentContainer, fragment);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.navSettings:
                        toolbar.setTitle("Settings");
                        mTabs.setVisibility(View.GONE);
                        mPager.setVisibility(View.GONE);
                        fabMainFeed.setVisibility(View.GONE);
                        mPager.setActivated(false);

                        toolbar.setTitleTextColor(getResources().getColor(R.color.whiteColor));

                        fragment = new SettingsFragment();
                        fragmentTransaction.replace(R.id.feedFragmentContainer, fragment);
                        fragmentTransaction.commit();
                        return true;
                    default:
                        toolbar.setTitle("Browse");
                        toolbar.setTitleTextColor(getResources().getColor(R.color.whiteColor));
                        mTabs.setVisibility(View.VISIBLE);
                        mPager.setVisibility(View.VISIBLE);
                        fabMainFeed.setVisibility(View.VISIBLE);
                        mPager.setActivated(true);
                        mPager.setCurrentItem(0, true);
                        fragment = new ForSaleFeedFragment();
                        fragmentTransaction.replace(R.id.feedFragmentContainer, fragment);
                        fragmentTransaction.commit();
                        return true;
                }
            }
        });

        fabMainFeed = (FloatingActionButton) findViewById(R.id.fabMainFeed);
        fabMainFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment f = getFragmentManager().findFragmentById(R.id.feedFragmentContainer);
//                if (f instanceof ForSaleFeedFragment) {
//                    Intent intent = new Intent(MainFeedActivity.this, AddForSalePostActivity.class);
//                    startActivity(intent);
//                } else if (f instanceof BuyRequestFeedFragment) {
//                    Toast.makeText(MainFeedActivity.this, "LOOOOOOOOONG", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(MainFeedActivity.this, AddForSalePostActivity.class);
//                    startActivity(intent);
//                } else {
//
//                    // TODO: f is null
//                    Toast.makeText(MainFeedActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
//                }

                if (!whichView) {
                    Toast.makeText(MainFeedActivity.this, "SECOND INTENT", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainFeedActivity.this, AddForSalePostActivity.class);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(MainFeedActivity.this, AddForSalePostActivity.class);
                    startActivity(intent);
                }
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.setDrawerListener(mDrawerToggle);

//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        switch (navValue) {
//            case 1:
//                toolbar.setTitle("Requests");
//                BuyRequestFeedFragment buyRequestFeedFragment = new BuyRequestFeedFragment();
//                ft.replace(R.id.feedFragmentContainer, buyRequestFeedFragment).commit();
//                break;
//            default:
//                toolbar.setTitle("For Sale");
//                ForSaleFeedFragment forSaleFeedFragment = new ForSaleFeedFragment();
//                ft.replace(R.id.feedFragmentContainer, forSaleFeedFragment).commit();
//                break;
//        }



        toolbar.setTitle("Browse");
        toolbar.setTitleTextColor(getResources().getColor(R.color.whiteColor));
        mTabs.setVisibility(View.VISIBLE);
        mPager.setVisibility(View.VISIBLE);
        mPager.setActivated(true);
        mPager.setCurrentItem(0, true);
        ForSaleFeedFragment forSaleFeedFragment = new ForSaleFeedFragment();
        getFragmentManager().beginTransaction().replace(R.id.feedFragmentContainer, forSaleFeedFragment);
        getFragmentManager().beginTransaction().commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public ViewPager getmPager() {
        return mPager;
    }

    public SlidingTabLayout getmTabs() {
        return mTabs;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    public GPSTracker getGps() {
        return gps;
    }

    public FloatingActionButton getFabMainFeed() {
        return fabMainFeed;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(NAV_ITEM_ID, mNavItemId);
    }



    @Override
    protected void onPause() {
        gps.stopUsingGPS();
        super.onPause();
    }

    class MainPagerAdapter extends FragmentPagerAdapter {

        String[] tabs = {"FOR SALE", "REQUESTS"};

        public MainPagerAdapter(android.app.FragmentManager fm) {
            super(fm);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public android.app.Fragment getItem(int position) {

            switch (position) {
                case 1:
                    BuyRequestFeedFragment brfFragment = new BuyRequestFeedFragment();
                    whichView = true;
                    return brfFragment;
                default:
                    ForSaleFeedFragment fsfFragment = new ForSaleFeedFragment();
                    whichView = false;
                    return fsfFragment;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
        }

        @Override
        public int getCount() {
            return tabs.length;
        }
    }

}

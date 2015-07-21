package eia.fluint;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;


public class MainFeedActivity extends AppCompatActivity {

    private static final String NAV_ITEM_ID = "navItemId";

    private Toolbar toolbar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mNavItemId;

    private GPSTracker gps;

    private FloatingActionButton fabMainFeed;

    private FloatingActionMenu fabMenu;


    private boolean whichView = false;

    int pivotX, pivotY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        gps = new GPSTracker(this);



        toolbar = (Toolbar) findViewById(R.id.toolBarFeed);
        setSupportActionBar(toolbar);

        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        int maxX = mdispSize.x;
        int maxY = mdispSize.y;

        float dpPadding = convertDpToPixel(20, MainFeedActivity.this);

        pivotX = maxX - (int) dpPadding;
        pivotY = maxY - (int) dpPadding;


        mPager = (ViewPager) findViewById(R.id.loginPagerMain);
        mPager.setAdapter(new MainPagerAdapter(getFragmentManager()));
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // TODO: scale Floating Action Button size
                Log.d("POSITION_OFFSET", positionOffset + "");
                Log.d("POS_OFFSET_VIEW", whichView + "");
                Log.d("JUST_POS", position + "");


                if (positionOffset < 0.5) {
                    if (whichView) {
//                        fabMainFeed.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primaryColorDark)));
                    } else {
//                        fabMainFeed.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.accentColor)));
                    }
//                    fabMainFeed.setScaleX((1 - 2 * positionOffset));
//                    fabMainFeed.setScaleY((1 - 2 * positionOffset));

                } else {

//                    fabMainFeed.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primaryColorDark)));
//                    fabMainFeed.setScaleX((2 * positionOffset - 1));
//                    fabMainFeed.setScaleY((2 * positionOffset - 1));
                }
            }

            @Override
            public void onPageSelected(int position) {
                whichView = !whichView;
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
                return getResources().getColor(R.color.primaryColorLight);
            }
        });
        mTabs.setViewPager(mPager);


        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                menuItem.setChecked(false);

                mNavItemId = menuItem.getItemId();
                drawerLayout.closeDrawers();


                switch (mNavItemId) {
                    case R.id.navProfile:
                        Intent profIntent = new Intent(MainFeedActivity.this, ProfileActivity.class);
                        startActivity(profIntent);
                        return true;
                    case R.id.navSettings:
                        Intent settingsIntent = new Intent(MainFeedActivity.this, SettingsActivity.class);
                        startActivity(settingsIntent);
                        return true;


                }
                return false;
            }
        });

        fabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);

//        fabMainFeed = (FloatingActionButton) findViewById(R.id.fabMainFeed);
//        fabMainFeed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // TODO: expand the FAB to give three options, each launching a different intent
//                // Can use whichView boolean value to launch the right intent by assigning
//                // OnClickListener to the 3 options and check the whichView boolean in each
//
//                if (whichView) {
//                    Toast.makeText(MainFeedActivity.this, "SECOND INTENT", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(MainFeedActivity.this, AddForSalePostActivity.class);
//                    startActivity(intent);
//
//                } else {
//                    Toast.makeText(MainFeedActivity.this, "FIRST INTENT", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(MainFeedActivity.this, AddForSalePostActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.setDrawerListener(mDrawerToggle);



        toolbar.setTitle("Browse");
        toolbar.setTitleTextColor(getResources().getColor(R.color.whiteColor));
        mPager.setCurrentItem(0, true);
        ForSaleFeedFragment forSaleFeedFragment = new ForSaleFeedFragment();
        getFragmentManager().beginTransaction().replace(R.id.feedFragmentContainer, forSaleFeedFragment);
        getFragmentManager().beginTransaction().commit();

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

    public GPSTracker getGps() {
        return gps;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
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
                    return brfFragment;
                default:
                    ForSaleFeedFragment fsfFragment = new ForSaleFeedFragment();
                    return fsfFragment;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return tabs.length;
        }
    }

}

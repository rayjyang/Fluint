package eia.fluint;

import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainFeedActivity extends AppCompatActivity implements BuyFeedFragment.OnFragmentInteractionListener {

    private static final String NAV_ITEM_ID = "navItemId";

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mNavItemId;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        toolbar = (Toolbar) findViewById(R.id.toolBarFeed);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            mNavItemId = R.id.navBuy;
        } else {
            mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }

        mTitle = getTitle().toString();
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }

                mNavItemId = menuItem.getItemId();
                drawerLayout.closeDrawers();

                switch (mNavItemId) {
                    case R.id.navSell:
                        Toast.makeText(getApplicationContext(), "Sell selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navProfile:
                        Toast.makeText(getApplicationContext(), "Profile selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navSettings:
                        Toast.makeText(getApplicationContext(), "Settings selected", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        BuyFeedFragment fragment = new BuyFeedFragment();
                        FragmentTransaction fragmentTransaction =
                                getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.feedFragmentContainer, fragment);
                        fragmentTransaction.commit();
                        return true;
                }
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.setDrawerListener(mDrawerToggle);

        BuyFeedFragment buyFeedFragment = new BuyFeedFragment();
        getFragmentManager().beginTransaction().add(R.id.feedFragmentContainer, buyFeedFragment).commit();



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
    public void onFragmentInteraction(Uri uri) {

    }
}

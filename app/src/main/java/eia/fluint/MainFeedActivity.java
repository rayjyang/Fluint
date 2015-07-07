package eia.fluint;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainFeedActivity extends AppCompatActivity implements BuyFeedFragment.OnFragmentInteractionListener {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        toolbar = (Toolbar) findViewById(R.id.toolBarFeed);
        setSupportActionBar(toolbar);

        BuyFeedFragment buyFeedFragment = new BuyFeedFragment();
        getFragmentManager().beginTransaction().add(R.id.feedFragmentContainer, buyFeedFragment).commit();


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

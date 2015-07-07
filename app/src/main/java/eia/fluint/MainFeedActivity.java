package eia.fluint;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

public class MainFeedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar toolbar;
    private SwipeRefreshLayout feedSwipeRefresh;
    private List<Transaction> mRefreshData;

    private final int TIME_INVERVAL_UPDATE = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        toolbar = (Toolbar) findViewById(R.id.toolBarFeed);
        setSupportActionBar(toolbar);

        feedSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.feedSwipeRefresh);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        // TODO: call Parse server, get recent transaction posts in background.
        // store in a List<Transaction> dataset, mRefreshData

        // TODO: specify an Adapter
        // Create a new instance of FeedAdapter passing in a dataset

        // TODO: setClickListener on the adapter

        // TODO: recyclerView.setAdapter(mAdapter);

        // TODO: Set onRefreshListener on SwipeRefreshLayout
        feedSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO: refresh content and update location

            }
        });

        // TODO: set color scheme of the SwipeRefreshLayout

        // TODO: Get user's location. Will be used to show how far away
        // from others he is (like in Yelp)


    }

}

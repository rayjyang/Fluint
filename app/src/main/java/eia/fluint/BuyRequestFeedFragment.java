package eia.fluint;

import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


public class BuyRequestFeedFragment extends Fragment {

    private static final String TAG = "BuyRequestFeedFragment";

    private RecyclerView recyclerViewSell;
    private FeedAdapter feedAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshSell;


    public static BuyRequestFeedFragment newInstance() {
        BuyRequestFeedFragment fragment = new BuyRequestFeedFragment();
        return fragment;
    }

    public BuyRequestFeedFragment() {
        // Required empty public constructor
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
                getLocation();
                getLatestPosts();
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


    private void getLocation() {

    }

    private void getLatestPosts() {

    }

}

package eia.fluint;

import android.app.Activity;
import android.net.Uri;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SellFeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SellFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellFeedFragment extends Fragment {

    private static final String TAG = "SellFeedFragment";

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerViewSell;
    private FeedAdapter feedAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshSell;
    private ProgressBar sellFeedProgressBar;
    private FloatingActionButton fabSellFeed;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SellFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellFeedFragment newInstance() {
        SellFeedFragment fragment = new SellFeedFragment();
        return fragment;
    }

    public SellFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_sell_feed, container, false);
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

        sellFeedProgressBar = (ProgressBar) layout.findViewById(R.id.sellFeedProgressBar);
        fabSellFeed = (FloatingActionButton) layout.findViewById(R.id.fabSellFeed);

        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void getLocation() {

    }

    private void getLatestPosts() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

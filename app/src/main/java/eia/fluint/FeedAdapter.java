package eia.fluint;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Raymond on 6/29/2015.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.DataViewHolder> {

    private String[] mDataset;
    private LayoutInflater inflater;

    public static class DataViewHolder extends RecyclerView.ViewHolder {

        public DataViewHolder(View itemView) {
            super(itemView);
        }
    }

    public FeedAdapter(String[] dataset) {
        mDataset = dataset;
    }

    public FeedAdapter(Context c, ArrayList<Integer> imgs) {
        inflater = LayoutInflater.from(c);
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_feed_recycler_row, parent, false);

        // TODO: set the view's size, margins, paddings, and layout parameters

        DataViewHolder holder = new DataViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

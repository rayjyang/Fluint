package eia.fluint;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Raymond on 6/29/2015.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.DataViewHolder> {

    private String[] mDataset;

    public static class DataViewHolder extends RecyclerView.ViewHolder {

        public DataViewHolder(View itemView) {
            super(itemView);
        }
    }

    public FeedAdapter(String[] dataset) {
        mDataset = dataset;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

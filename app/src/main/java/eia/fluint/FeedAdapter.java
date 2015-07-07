package eia.fluint;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Raymond on 6/29/2015.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.DataViewHolder> {

    private List<Transaction> transactions;
    private Context mContext;
    private ClickListener clickListener;

    /**
     * Viewholder for our cards
     */
    class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // TODO: references to views in a card

        public DataViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // TODO: Handle on item click
            if (clickListener != null) {
                clickListener.itemClicked(v, getPosition());
            }
        }
    }

    public interface ClickListener {

        // 1) Make an interface 2) implement that interface with a fragment 3) Override itemClicked method
        // in fragment 4) Make variable reference to the interface just created in the adapter class
        // 5) Define a method in the adapter class that allows you to set an object that implements
        // that interface (in this case the fragment itself, which is why mAdapter.setClickListener(this)
        // is called

        void itemClicked(View view, int position);
    }

    /**
     * Public constructor for FeedAdapter
     * @param context
     * @param list
     */
    public FeedAdapter(Context context, List<Transaction> list) {
        mContext = context;
        transactions = list;
    }

    private void initializeData() {
        // Get data about users and their posts
    }

    public void add(Transaction trans) {
        transactions.add(trans);
        notifyDataSetChanged();
    }

    public void setClickListener(ClickListener cl) {
        clickListener = cl;
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
        Transaction transaction = transactions.get(position);

        // TODO: dynamically set the views according to data in the Transaction object
        // holder.VIEW.setText()

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}

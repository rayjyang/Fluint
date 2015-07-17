package eia.fluint;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.DataViewHolder> {

    private List<Transaction> transactions;
    private Context mContext;
    private ClickListener clickListener;

    /**
     * Viewholder for our cards
     */
    class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // TODO: references to views in a card
        TextView posterNameFS;
        TextView postDetailsFS;
        TextView postAmountFS;
        TextView ratingFS;
        TextView distanceFS;

        public DataViewHolder(View itemView) {
            super(itemView);

            posterNameFS = (TextView) itemView.findViewById(R.id.posterNameFS);
            postDetailsFS = (TextView) itemView.findViewById(R.id.postDetailsFS);
            postAmountFS = (TextView) itemView.findViewById(R.id.postAmountFS);
            ratingFS = (TextView) itemView.findViewById(R.id.ratingFS);
            distanceFS = (TextView) itemView.findViewById(R.id.distanceFS);

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


        String posterName = transaction.getPosterName();
        String shortenedName = parseName(posterName);
        if (shortenedName == null || shortenedName.equals("")) {
            shortenedName = "User";
        }
        holder.posterNameFS.setText(shortenedName);

        String postDetails = parsePostDetails(transaction);
        holder.postDetailsFS.setText(postDetails);

        String postAmount = parsePostAmount(transaction);
        holder.postAmountFS.setText(postAmount);

        String rating = parseRating(transaction);
        holder.ratingFS.setText(rating);

        String distance = parseDistance(transaction);
        holder.distanceFS.setText(distance);
    }

    private String parseDistance(Transaction trans) {
        return "12 km";
    }

    private String parseRating(Transaction trans) {
        // Make a query with object id
        ParseQuery query = ParseUser.getQuery();

        // TODO: actually get the user
        ParseUser user = new ParseUser();
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {

            }

            @Override
            public void done(Object o, Throwable throwable) {

            }
        });

        String rating;
//        rating = user.get("rating") + "";
        rating = "4.3";
        return rating;

    }

    private String parsePostAmount(Transaction trans) {
        String amountA = trans.getAmountA() + " " + trans.getCurrencyA();
        return amountA;
    }

    private String parsePostDetails(Transaction trans) {
        Date date = new Date();


        return "2 hrs" + " " + Character.toString((char) 183) +  " San Francisco";
    }

    private String parseName(String posterName) {
        try {
            if (!posterName.contains(" ") || (posterName.charAt(posterName.length() - 1) == ' ')) {
                return posterName;
            } else {
                String shortenedName;
                String[] nameParts = posterName.split(" ");
                if (nameParts.length == 2) {
                    String firstName = nameParts[0];
                    String lastName = nameParts[1];
                    shortenedName = firstName + " " + lastName.charAt(0) + ".";
                    return shortenedName;
                } else {
                    String firstName = nameParts[0];
                    String lastName = nameParts[nameParts.length - 1];
                    shortenedName = firstName + " " + lastName.charAt(0) + ".";
                    return shortenedName;
                }
            }
        } catch (NullPointerException e) {
            if (posterName != null || !posterName.equals("")) {
                return posterName;
            } else {
                return "User";
            }
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}

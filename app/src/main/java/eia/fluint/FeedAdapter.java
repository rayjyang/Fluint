package eia.fluint;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.joda.time.DateTime;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        TextView exchangeFS;
        ImageView profileFS;

        public DataViewHolder(View itemView) {
            super(itemView);

            posterNameFS = (TextView) itemView.findViewById(R.id.posterNameFS);
            postDetailsFS = (TextView) itemView.findViewById(R.id.postDetailsFS);
            postAmountFS = (TextView) itemView.findViewById(R.id.postAmountFS);
            ratingFS = (TextView) itemView.findViewById(R.id.ratingFS);
            distanceFS = (TextView) itemView.findViewById(R.id.distanceFS);
            exchangeFS = (TextView) itemView.findViewById(R.id.exchangeFS);
            profileFS = (ImageView) itemView.findViewById(R.id.profileFS);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // TODO: Handle on item click
            if (clickListener != null) {
                clickListener.itemClicked(v, getAdapterPosition());
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_feed_fs_recycler_row, parent, false);

        // TODO: set the view's size, margins, paddings, and layout parameters

        DataViewHolder holder = new DataViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final DataViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        // TODO: dynamically set the views according to data in the Transaction object
        // holder.VIEW.setText()


        String posterName = transaction.getPosterName();
        String shortenedName = parseName(posterName);
        if (shortenedName == null || shortenedName.equals("")) {
            shortenedName = "User";
        }
        holder.posterNameFS.setText(shortenedName);

        final String elapsedTime;



        parsePostDetails(transaction, new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                String result;
                switch (msg.what) {
                    case 1:
                        Bundle bundle = msg.getData();
                        result = bundle.getString("city");
                        holder.postDetailsFS.setText(result);
                        return true;
                    default:
                        result = null;
                        holder.postDetailsFS.setText(result);
                        return false;
                }
            }
        }));

        String postAmount = parsePostAmount(transaction);
        holder.postAmountFS.setText(postAmount);

        String rating = parseRating(transaction);
        holder.ratingFS.setText(rating);

        String distance = parseDistance(transaction);
        holder.distanceFS.setText(distance);

        String exchange = parseExchange(transaction);
        holder.exchangeFS.setText(exchange);


    }

    private String parseExchange(Transaction trans) {
        String currencyB = trans.getCurrencyB();
        int amountB = trans.getAmountB();
        String parsed = "" + amountB + " " + currencyB;
        return parsed;
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

    private void parsePostDetails(final Transaction trans, final Handler handler) {
        Thread t = new Thread() {
            @Override
            public void run() {
                Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
                String result = null;
                ParseGeoPoint locationPoint = trans.getLocationPoint();
                try {
                    List<Address> addresses = gcd.getFromLocation(locationPoint.getLatitude(), locationPoint.getLongitude(), 1);
                    if (addresses != null && addresses.size() > 0) {
                        result = addresses.get(0).getLocality();
                    }
                } catch (IOException e) {
                    Log.e("FeedAdapterDetails", "Failed to connect to Geocoder", e);
                } finally {
                    Message msg = Message.obtain();
                    msg.setTarget(handler);
                    if (result != null) {
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("city", result);
                        msg.setData(bundle);
                    } else {
                        msg.what = 0;
                    }
                    msg.sendToTarget();
                }


            }
        };
        t.start();

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

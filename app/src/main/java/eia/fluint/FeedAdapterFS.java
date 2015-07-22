package eia.fluint;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FeedAdapterFS extends RecyclerView.Adapter<FeedAdapterFS.DataViewHolder> {

    private List<Transaction> transactions;
    private Context mContext;
    /**
     * Viewholder for our cards
     */
    class DataViewHolder extends RecyclerView.ViewHolder {

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

        }
    }


    /**
     * Public constructor for FeedAdapterFS
     */
    public FeedAdapterFS(Context context, List<Transaction> list) {
        mContext = context;
        transactions = list;
    }


    public void add(Transaction trans) {
        transactions.add(trans);
        notifyDataSetChanged();
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

//        parsePostDetails(transaction, new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                String result;
//                switch (msg.what) {
//                    case 1:
//                        Log.d("Handler", "Got city name");
//                        Bundle bundle = msg.getData();
//                        result = bundle.getString("city");
//                        holder.postDetailsFS.setText(Character.toString((char) 183) + " " + result);
//                        return true;
//                    default:
//                        Log.d("Handler", "Failed to get city name");
//                        result = "N/A";
//                        holder.postDetailsFS.setText(result);
//                        return false;
//                }
//            }
//        }));

//        new CityAsync(transaction.getLocationPoint().getLatitude(),
//                transaction.getLocationPoint().getLongitude(), holder).execute();

        if (transaction.getParsedRating() == null || transaction.getParsedRating().equals("")) {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.getInBackground(transaction.getPosterId(), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (e == null) {
                        Log.d("AVGS", "Somehow the rating was not set in ForSaleFeedFragment");
                        int rating = parseUser.getInt("rating");
                        int numRatings = parseUser.getInt("numRatings");

                        double r = (double) rating / numRatings;

                        String toSet = r + "";
                        if (toSet.length() > 3) {
                            toSet = toSet.substring(0, 3);
                        }

                        holder.ratingFS.setText(toSet);
                    }
                }
            });
        }


        holder.posterNameFS.setText(transaction.getParsedName());
        holder.postDetailsFS.setText(transaction.getParsedDetails());
        holder.postAmountFS.setText(transaction.getParsedA());
        holder.ratingFS.setText(transaction.getParsedRating());
        holder.distanceFS.setText(transaction.getParsedDistance());
        holder.exchangeFS.setText(transaction.getParsedB());

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

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    private class CityAsync extends AsyncTask<Void, Void, String> {

        double lat, lng;
        String city;
        DataViewHolder holder;

        public CityAsync(double latitude, double longitude, DataViewHolder holder) {
            lat = latitude;
            lng = longitude;
            this.holder = holder;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
                List<Address> addresses = gcd.getFromLocation(lat, lng, 1);

                if (Geocoder.isPresent() && addresses.size() > 0) {
                    city = addresses.get(0).getLocality();
                }
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            holder.postDetailsFS.setText(Character.toString((char) 183) + " " + city);
        }
    }
}

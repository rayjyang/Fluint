package eia.fluint;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.parse.ParseGeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Raymond on 7/21/2015.
 */
public class PostParser {

    private Context mContext;

    public PostParser(Context context) {
        mContext = context;
    }

    public String parseName(String name) {
        try {
            if (!name.contains(" ") || (name.charAt(name.length() - 1) == ' ')) {
                return name;
            } else {
                String shortenedName;
                String[] nameParts = name.split(" ");
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
            if (name != null || !name.equals("")) {
                return name;
            } else {
                return "User";
            }
        }
    }

    public String parseA(String amount, String currency) {
        return amount + " " + currency;
    }

    public String parseB(String amount, String currency) {
        return amount + " " + currency;
    }

    public String parseDistance(Transaction trans) {
        ParseGeoPoint geoPoint = trans.getCurrentPoint();
        SharedPreferences sp = mContext.getSharedPreferences("eia.fluint.USER_SETTINGS", Context.MODE_PRIVATE);
        String distPref = sp.getString("distancePreference", "km");
        double distance;
        if (distPref.equals("km")) {
            if (geoPoint != null) {
                distance = geoPoint.distanceInKilometersTo(trans.getLocationPoint());
            } else {
                distance = 0.0;
            }
        } else {
            if (geoPoint != null) {
                distance = geoPoint.distanceInMilesTo(trans.getLocationPoint());
            } else {
                distance = 0.0;
            }
        }

        String result = "" + (int) distance + " " + distPref;

        return result;
    }

//    public void parseDetails(final Transaction trans, final Handler handler) {
//        Thread t = new Thread() {
//            @Override
//            public void run() {
//                Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
//                String result = null;
//                ParseGeoPoint locationPoint = trans.getLocationPoint();
//                try {
//                    List<Address> addresses = gcd.getFromLocation(locationPoint.getLatitude(), locationPoint.getLongitude(), 1);
//                    if (addresses != null && addresses.size() > 0) {
//                        result = addresses.get(0).getLocality();
//                    }
//                } catch (IOException e) {
//                    Log.e("FeedAdapterDetails", "Failed to connect to Geocoder", e);
//                } finally {
//                    Message msg = Message.obtain();
//                    msg.setTarget(handler);
//                    if (result != null) {
//                        msg.what = 1;
//                        Bundle bundle = new Bundle();
//                        bundle.putString("city", result);
//                        msg.setData(bundle);
//                    } else {
//                        msg.what = 0;
//                    }
//                    msg.sendToTarget();
//                }
//
//
//            }
//        };
//        t.start();
//    }

}

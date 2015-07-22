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
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Raymond on 7/21/2015.
 */
public class PostParser {

    private static final long MILLISECONDS_IN_DAY = 86400000;

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

        SharedPreferences sp;
        sp = mContext.getSharedPreferences("eia.fluint.USER_SETTINGS", Context.MODE_PRIVATE);
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

        String dist = distance + "";

        String toReturn;

        if (distance <= 1.0) {
            toReturn = "1 " + distPref;
            return toReturn;
        } else {
            if (dist.contains(".")) {
                if (distance >= 100) {
                    int d = (int) distance;
                    dist = d + "";
                } else if (distance < 100 && distance >= 10.0) {
                    dist = dist.substring(0, 4);
                } else {
                    dist = dist.substring(0, 3);
                }
            }
        }

        toReturn = dist + " " + distPref;
        return toReturn;
    }

    public String parseTimeInterval(Date dt1, Date dt2) {

        String toReturn;

        long interval = dt2.getTime() - dt1.getTime();

        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(interval);
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(interval);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(interval);
        long diffInDays = TimeUnit.MILLISECONDS.toDays(interval);

        if (diffInHours >= 24) {
            if (diffInHours < 48) {
                toReturn = "1 day";
            } else {
                toReturn = diffInDays + " days";
            }
        } else {
            if (diffInMinutes >= 60) {
                if (diffInMinutes < 120) {
                    toReturn = "1 hr";
                } else {
                    toReturn = diffInHours + " hrs";
                }
            } else {
                if (diffInSeconds >= 60) {
                    if (diffInSeconds <= 120) {
                        toReturn = "1 min";
                    } else {
                        toReturn = diffInMinutes + " min";
                    }
                } else {
                    if (diffInSeconds < 0) {
                        diffInSeconds = 0;
                    }
                    toReturn = diffInSeconds + " sec";
                }
            }
        }
        return toReturn;
    }

}

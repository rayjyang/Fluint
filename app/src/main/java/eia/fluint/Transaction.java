package eia.fluint;

import android.location.Location;

import com.parse.ParseUser;

import java.util.Date;

public class Transaction {
    private Date arrivalTime;
    private int amount;
    private Location coordinateLocation;
    private long time;
    private ParseUser parseUser;          // e.g. currentUser
    private String buyOrSell;             // e.g. SELL
    private String flightNo;              // e.g. AF6077
    private String userName;              // e.g. John Doe
    private String userEmail;             // e.g. johndoe@gmail.com
    private String currency;              // e.g. EUR, USD, JPY
    private String arrivalLocation;       // e.g. SFO, LAX, JFK

    public static final String TAG = "Transaction";

    public Transaction() {
        time = System.currentTimeMillis();
    }

}

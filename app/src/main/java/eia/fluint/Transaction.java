package eia.fluint;

import android.location.Location;

import com.parse.ParseUser;

import java.util.Date;

public class Transaction {
    private long time;
    private ParseUser parseUser;
    private String userName;
    private String userEmail;
    private String arrivalLocation;

    private String transactionType;
    private String currency;
    private int amount;
    private Date arrival;
    private Location location;
    private String flightNo;
    private String airport;



    public static final String TAG = "Transaction";

    public Transaction() {
        time = System.currentTimeMillis();
    }

    public void setTransactionType(String type) {
        transactionType = type;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setCurrency(String which) {
        currency = which;
    }

    public String getCurrency() {
        return currency;
    }

    public void setAmount(int amt) {
        amount = amt;
    }

    public int getAmount() {
        return amount;
    }

    public void setArrival(Date arr) {
        arrival = arr;
    }

    public Date getArrival() {
        return arrival;
    }

    public void setLocation(Location loc) {
        location = loc;
    }

    public Location getLocation() {
        return location;
    }

    public void setFlightNo(String fn) {
        flightNo = fn;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setAirport(String ap) {
        airport = ap;
    }

    public String getAirport() {
        return airport;
    }



}

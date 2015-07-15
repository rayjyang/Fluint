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
    private String currencyA;
    private String currencyB;
    private int amountA;
    private int amountB;
    private Date arrival;
    private Location pickedLocation;
    private Location currentLocation;
    private int radius;
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

    public void setCurrencyA(String which) {
        currencyA = which;
    }

    public String getCurrencyA() {
        return currencyA;
    }

    public void setCurrencyB(String which) {
        currencyB = which;
    }

    public String getCurrencyB() {
        return currencyB;
    }

    public void setAmountA(int amt) {
        amountA = amt;
    }

    public int getAmountA() {
        return amountA;
    }

    public void setAmountB(int amt) {
        amountB = amt;
    }

    public int getAmountB() {
        return amountB;
    }

    public void setArrival(Date arr) {
        arrival = arr;
    }

    public Date getArrival() {
        return arrival;
    }

    public void setPickedLocation(Location loc) {
        pickedLocation = loc;
    }

    public Location getPickedLocation() {
        return pickedLocation;
    }

    public void setCurrentLocation(Location loc) {
        currentLocation = loc;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setRadius(int rad) {
        radius = rad;
    }

    public int getRadius() {
        return radius;
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

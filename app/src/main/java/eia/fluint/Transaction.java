package eia.fluint;

import android.location.Location;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.Date;

public class Transaction {
    private long time;
    private String userName;
    private String userEmail;
    private String arrivalLocation;

    private String transactionType;
    private String where;
    private String currencyA;
    private String currencyB;
    private int amountA;
    private int amountB;
    private Date arrival;
    private String locationType;
    private Location location;
    private Location currentLocation;
    private ParseGeoPoint locationPoint;
    private ParseGeoPoint currentPoint;
    private int radius;
    private String flightNo;
    private String airport;
    private ParseUser originalPoster;
    private String opUsername;
    private String posterName;
    private String posterId;
    private boolean resolved;

    // private Parse fields
    private Date createdAt;
    private String postObjectId;


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

    public void setWhere(String loc) {
        where = loc;
    }

    public String getWhere() {
        return where;
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

    public void setLocationType(String loc) {
        locationType = loc;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocation(Location loc) {
        location = loc;
    }

    public Location getLocation() {
        return location;
    }

    public void setCurrentLocation(Location loc) {
        currentLocation = loc;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setLocationPoint(ParseGeoPoint point) {
        locationPoint = point;
    }

    public ParseGeoPoint getLocationPoint() {
        return locationPoint;
    }

    public void setCurrentPoint(ParseGeoPoint point) {
        currentPoint = point;
    }

    public ParseGeoPoint getCurrentPoint() {
        return currentPoint;
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

    public void setOriginalPoster(ParseUser user) {
        originalPoster = user;
    }

    public ParseUser getOriginalPoster() {
        return originalPoster;
    }

    public void setOpUsername(String user) {
        opUsername = user;
    }

    public String getOpUsername() {
        return opUsername;
    }

    public void setPosterName(String name) {
        posterName = name;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterId(String id) {
        posterId = id;
    }

    public String getPosterId() {
        return posterId;
    }

    public void setResolved(boolean bool) {
        resolved = bool;
    }

    public boolean getResolved() {
        return resolved;
    }

    public void setCreatedAt(Date date) {
        createdAt = date;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setPostObjectId(String id) {
        postObjectId = id;
    }

    public String getPostObjectId() {
        return postObjectId;
    }

}

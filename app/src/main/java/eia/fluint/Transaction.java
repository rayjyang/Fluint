package eia.fluint;

/**
 * Created by Raymond on 6/29/2015.
 */
public class Transaction {
    private int amount;
    private int flagId;
    private double lat;
    private double lon;
    private double err;
    private long time;
    private String buyOrSell;
    private String userName;
    private String currency;
    private String country;


    public Transaction() {
        time = System.currentTimeMillis();
    }

    public int getAmount() {
        return amount;
    }

    public int getFlagId() {
        return flagId;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getErr() {
        return err;
    }

    public long getTime() {
        return time;
    }

    public String getBuyOrSell() {
        return buyOrSell;
    }

    public String getUserName() {
        return userName;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCountry() {
        return country;
    }

}

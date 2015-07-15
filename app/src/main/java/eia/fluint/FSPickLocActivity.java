package eia.fluint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class FSPickLocActivity extends AppCompatActivity implements
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    // A request to connect to Location Services
    private LocationRequest mLocationRequest;
    GoogleMap mGoogleMap;

    public static String ShopLat;
    public static String ShopPlaceId;
    public static String ShopLong;
    // Stores the current instantiation of the location client in this object
    private GoogleApiClient mGoogleApiClient;
    boolean mUpdatesRequested = false;
    private TextView markerText;
    private LatLng center;
    private LinearLayout markerLayout;
    private Geocoder geocoder;
    private List<Address> addresses = new ArrayList<>();
    private TextView addressText;
    double latitude;
    double longitude;
    private GPSTracker gps;
    private LatLng currentPoint;

    private int radius = 5;
    private String currencyA;
    private String currencyB;
    private int amountA;
    private int amountB;

    private AppCompatSpinner spinnerFSPickLocGive;
    private AppCompatEditText etFSPickLocGive;
    private AppCompatSpinner spinnerFSPickLocWant;
    private AppCompatEditText etFSPickLocWant;
    private DiscreteSeekBar fsPickLocRadius;
    private AppCompatButton fsPickLocSubmit;

    private Location mostRecentUserLocation;
    private Location setLocation;

    private Transaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_pick_loc);
        markerText = (TextView) findViewById(R.id.fsPickLocMarkerText);
        addressText = (TextView) findViewById(R.id.fsPickLocAddressText);
        markerLayout = (LinearLayout) findViewById(R.id.fsPickLocMarker);

        transaction = new Transaction();
        transaction.setTransactionType("sell");
        transaction.setRadius(radius);

        // For sure: transactionType
        // Not sure: 1) currencyA 2) currencyB 3) amountA 4) amountB
        // 5) pickedLocation 6) currentLocation

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());

        if (status != ConnectionResult.SUCCESS) { // Google Play Services are
            // not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
                    requestCode);
            dialog.show();

        } else { // Google Play Services are available

            // Getting reference to the SupportMapFragment
            // Create a new global location parameters object
            mLocationRequest = LocationRequest.create();

            /*
             * Set the update interval
             */
            mLocationRequest.setInterval(GData.UPDATE_INTERVAL_IN_MILLISECONDS);

            // Use high accuracy
            mLocationRequest
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            // Set the interval ceiling to one minute
            mLocationRequest
                    .setFastestInterval(GData.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

            // Note that location updates are off until the user turns them on
            mUpdatesRequested = false;

            /*
             * Create a new location client, using the enclosing class to handle
             * callbacks.
             */
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();

            mGoogleApiClient.connect();
        }

        spinnerFSPickLocGive = (AppCompatSpinner) findViewById(R.id.spinnerFSPickLocGive);
        etFSPickLocGive = (AppCompatEditText) findViewById(R.id.etFSPickLocGive);
        spinnerFSPickLocWant = (AppCompatSpinner) findViewById(R.id.spinnerFSPickLocWant);
        etFSPickLocWant = (AppCompatEditText) findViewById(R.id.etFSPickLocWant);
        fsPickLocRadius = (DiscreteSeekBar) findViewById(R.id.fsPickLocRadius);
        fsPickLocSubmit = (AppCompatButton) findViewById(R.id.fsPickLocSubmit);

        spinnerFSPickLocGive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyA = parent.getSelectedItem().toString();
                transaction.setCurrencyA(currencyA);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFSPickLocWant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyB = parent.getSelectedItem().toString();
                transaction.setCurrencyB(currencyB);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fsPickLocRadius.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar discreteSeekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar discreteSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar discreteSeekBar) {
                radius = discreteSeekBar.getProgress();
                transaction.setRadius(radius);
            }
        });

        fsPickLocSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pDialog = new ProgressDialog(FSPickLocActivity.this, R.style.AuthenticateDialog);
                pDialog.setIndeterminate(true);
                pDialog.setMessage("Creating your post");
                pDialog.show();

                ParseUser user = ParseUser.getCurrentUser();
                String username = user.getUsername();
                String userId = user.getObjectId();

                transaction.setOriginalPoster(user);
                transaction.setOpUsername(username);
                transaction.setPosterId(userId);

                transaction.setCurrentLocation(mostRecentUserLocation);

                String amountAText = etFSPickLocGive.getText().toString();
                String amountBText = etFSPickLocWant.getText().toString();

                if (!amountAText.equals("") && amountBText.equals("")) {
                    amountA = Integer.parseInt(amountAText);
                    amountB = Integer.parseInt(amountBText);
                }

                transaction.setAmountA(amountA);
                transaction.setAmountB(amountB);

                if (transaction.getCurrencyA() == null || transaction.getCurrencyB() == null
                        || amountAText.equals("") || amountBText.equals("")
                        || transaction.getPickedLocation() == null || transaction.getCurrentLocation() == null) {
                    if (setLocation == null) {
                        if (mostRecentUserLocation != null) {
                            setLocation = mostRecentUserLocation;
                        } else {
                            pDialog.dismiss();
                            Toast.makeText(FSPickLocActivity.this, "Please specify a location", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else if (transaction.getCurrencyA() == null || transaction.getCurrencyB() == null
                            || amountAText.equals("") || amountBText.equals("")) {
                        pDialog.dismiss();
                        Toast.makeText(FSPickLocActivity.this, "Please fill out all required fields", Toast.LENGTH_LONG).show();
                        return;
                    }

                }

                if (amountAText.contains(".") || amountBText.contains(".")) {
                    pDialog.dismiss();
                    Toast.makeText(FSPickLocActivity.this, "Whole number amounts only", Toast.LENGTH_LONG).show();
                    return;
                }

                ParseObject fsPost = new ParseObject("ForSalePost");
                fsPost.put("transactionType", transaction.getTransactionType());
                fsPost.put("currencyA", transaction.getCurrencyA());
                fsPost.put("amountA", transaction.getAmountA());
                fsPost.put("currencyB", transaction.getCurrencyB());
                fsPost.put("amountB", transaction.getAmountB());
                fsPost.put("pickedLoc", transaction.getPickedLocation());
                fsPost.put("currLoc", transaction.getCurrentLocation());
                fsPost.put("radius", transaction.getRadius());
                fsPost.put("username", transaction.getOpUsername());
                fsPost.put("userId", transaction.getPosterId());
                fsPost.put("transObj", transaction);

                fsPost.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        pDialog.dismiss();
                        if (e == null) {
                            // Submit post was successful
                            Intent intent = new Intent(FSPickLocActivity.this, MainFeedActivity.class);
                            intent.putExtra("nav", 0);
                            startActivity(intent);

                        } else {

                        }
                    }
                });





            }
        });


    }

    private void stupMap() {
        try {

            mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.fsPickLocMapFragment)).getMap();

            // Enabling MyLocation in Google Map
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            mGoogleMap.getUiSettings().setCompassEnabled(true);
            mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
            mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);

            PendingResult<Status> result = LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient, mLocationRequest,
                            new LocationListener() {

                                @Override
                                public void onLocationChanged(Location location) {

                                    mostRecentUserLocation = location;
//                                    markerText.setText("Location received: "
//                                            + location.toString());

                                }
                            });

            Log.e("Reached", "here");

            result.setResultCallback(new ResultCallback<Status>() {

                @Override
                public void onResult(Status status) {

                    if (status.isSuccess()) {

                        Log.e("Result", "success");

                    } else if (status.hasResolution()) {
                        // Google provides a way to fix the issue
                        try {
                            status.startResolutionForResult(FSPickLocActivity.this,
                                    100);
                        } catch (SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            gps = new GPSTracker(this);

            gps.canGetLocation();

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            currentPoint = new LatLng(latitude, longitude);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentPoint).zoom(19f).tilt(0).build();

            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            // Clears all the existing markers
            mGoogleMap.clear();

            mGoogleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

                @Override
                public void onCameraChange(CameraPosition arg0) {
                    // TODO Auto-generated method stub
                    center = mGoogleMap.getCameraPosition().target;

                    markerText.setText("Set Location");
                    markerText.setTextColor(getResources().getColor(R.color.textColor));
                    markerText.setBackgroundColor(getResources().getColor(R.color.whiteColor));
                    mGoogleMap.clear();
//                    markerLayout.setVisibility(View.VISIBLE);

                    try {
                        new GetLocationAsync(center.latitude, center.longitude)
                                .execute();

                    } catch (Exception e) {
                    }
                }
            });

            markerLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    try {

                        LatLng latLng1 = new LatLng(center.latitude,
                                center.longitude);

                        setLocation = new Location("");
                        setLocation.setLatitude(latLng1.latitude);
                        setLocation.setLongitude(latLng1.longitude);
                        setLocation.setTime(new Date().getTime());

                        transaction.setPickedLocation(setLocation);

                        markerText.setBackgroundColor(getResources().getColor(R.color.materialLightGreen));
                        markerText.setTextColor(getResources().getColor(R.color.whiteColor));
//                        markerLayout.setVisibility(View.GONE);
                    } catch (Exception e) {
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
        stupMap();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(ev);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + w.getLeft() - scrcoords[0];
            float y = ev.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event " + ev.getRawX() + "," + ev.getRawY() + " " + x + "," + y
                    + " rect " + w.getLeft() + "," + w.getTop() + "," + w.getRight() + "," + w.getBottom()
                    + " coords " + scrcoords[0] + "," + scrcoords[1]);
            if (ev.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    private class GetLocationAsync extends AsyncTask<String, Void, String> {

        // boolean duplicateResponse;
        double x, y;
        StringBuilder str;

        public GetLocationAsync(double latitude, double longitude) {
            // TODO Auto-generated constructor stub

            x = latitude;
            y = longitude;
        }

        @Override
        protected void onPreExecute() {
            addressText.setText("Getting location...");
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                geocoder = new Geocoder(FSPickLocActivity.this, Locale.ENGLISH);
                addresses = geocoder.getFromLocation(x, y, 1);

                Log.d("PickLocActivity", "" + addresses.size());
                str = new StringBuilder();
                if (Geocoder.isPresent() && addresses.size() > 0) {
                    Address returnAddress = addresses.get(0);

                    String localityString = returnAddress.getLocality();
                    String city = returnAddress.getCountryName();
                    String region_code = returnAddress.getCountryCode();
                    String zipcode = returnAddress.getPostalCode();

                    str.append(localityString + "");
                    str.append(city + "" + region_code + "");
                    str.append(zipcode + "");

                } else {

                }
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                addressText.setText(addresses.get(0).getAddressLine(0)
                        + addresses.get(0).getAddressLine(1) + " ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub

    }

}
package eia.fluint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.content.IntentSender.SendIntentException;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
    private LatLng curentpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_pick_loc);
        markerText = (TextView) findViewById(R.id.fsPickLocMarkerText);
        addressText = (TextView) findViewById(R.id.fsPickLocAddressText);
        markerLayout = (LinearLayout) findViewById(R.id.fsPickLocMarker);
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
                                    markerText.setText("Location received: "
                                            + location.toString());

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
            curentpoint = new LatLng(latitude, longitude);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(curentpoint).zoom(19f).tilt(70).build();

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

                    markerText.setText(" Set your Location ");
                    mGoogleMap.clear();
                    markerLayout.setVisibility(View.VISIBLE);

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

                        Marker m = mGoogleMap.addMarker(new MarkerOptions()
                                .position(latLng1)
                                .title(" Set your Location ")
                                .snippet("")
                                .icon(BitmapDescriptorFactory
                                        .fromResource(R.drawable.pin_last)));
                        m.setDraggable(true);

                        markerLayout.setVisibility(View.GONE);
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
            addressText.setText(" Getting location ");
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
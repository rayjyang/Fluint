package eia.fluint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private CardView buyPreference;
    private CardView sellPreference;
    private CardView radiusPreference;
    private CardView logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Settings");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buyPreference = (CardView) findViewById(R.id.buyPreference);
        sellPreference = (CardView) findViewById(R.id.sellPreference);
        radiusPreference = (CardView) findViewById(R.id.radiusPreference);
        logOut = (CardView) findViewById(R.id.logOut);

        buyPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sellPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        radiusPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pDialog = new ProgressDialog(SettingsActivity.this, R.style.AuthenticateDialog);
                pDialog.setIndeterminate(true);
                pDialog.setMessage("Logging out");
                pDialog.show();
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        pDialog.dismiss();
                        if (e == null) {
                            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            // TODO: show AlertDialog indicating logout was unsuccessful
                            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                            builder.setTitle("Logout unsuccessful");
                            builder.setMessage("We could not log you out at this time. Please try again later.");
                            builder.show();
                        }
                    }
                });

            }
        });

        // TODO: Get references to settings views and set OnClickListeners

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class SettingsFragment extends PreferenceFragment {

        private static final String TAG = "SettingsFragment";

        Preference logOutPreference;
        ListPreference buyListPreference;
        ListPreference sellListPreference;

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SettingsFragment.
         */
        public static SettingsFragment newInstance() {
            SettingsFragment fragment = new SettingsFragment();
            return fragment;
        }

        public SettingsFragment() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
            logOutPreference = findPreference(getString(R.string.log_out_button));
            logOutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.AuthenticateDialog);
                    pDialog.setIndeterminate(true);
                    pDialog.setMessage("Logging out");
                    pDialog.show();
                    ParseUser.logOutInBackground(new LogOutCallback() {
                        @Override
                        public void done(ParseException e) {
                            pDialog.dismiss();
                            if (e == null) {
                                Intent intent = new Intent(getActivity(), LoginSignUpActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                // TODO: show AlertDialog indicating logout was unsuccessful
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Logout unsuccessful");
                                builder.setMessage("We could not log you out at this time. Please try again later.");
                                builder.show();
                            }
                        }
                    });

                    if (ParseUser.getCurrentUser() == null) {
                        return true;
                    } else {
                        Log.d(TAG, "User is not logged out");
                        return false;
                    }
                }
            });

            buyListPreference = (ListPreference) findPreference(getString(R.string.buy_preference));
            buyListPreference.setSummary(buyListPreference.getValue());
            buyListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    preference.setSummary(newValue.toString());
                    return true;
                }
            });

            sellListPreference = (ListPreference) findPreference(getString(R.string.sell_preference));
            sellListPreference.setSummary(sellListPreference.getValue());
            sellListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    preference.setSummary(newValue.toString());
                    return true;
                }
            });



        }
    }
}

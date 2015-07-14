package eia.fluint;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class SettingsFragment extends PreferenceFragment {

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

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    ForSaleFeedFragment fragment = new ForSaleFeedFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.setCustomAnimations(R.animator.slide_in_from_left, R.animator.slide_out_to_right);
                    ft.replace(R.id.feedFragmentContainer, fragment);
                    ft.addToBackStack(null);
                    ft.commit();

                    // TODO: Set toolbar title to "Buy"

                    return true;
                }

                return false;
            }
        });

    }
}

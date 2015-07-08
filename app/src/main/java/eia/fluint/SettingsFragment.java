package eia.fluint;

import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
                ParseUser.logOut();
                Intent intent = new Intent(getActivity(), LoginSignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
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

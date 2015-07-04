package eia.fluint;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;


public class LoginOrSignUpActivity extends AppCompatActivity {

    static final String TAG = "LoginOrSignUp";

    private Toolbar toolbar;
    public static ViewPager mPager;
    private SlidingTabLayout mTabs;

    private static final String APPLICATION_ID = "ClUIw0Dh2ja21S5sH0vjTrJ6a9nL9g1vH2b9EfMg";
    private static final String CLIENT_KEY = "I9qGl0kinVIKh0Ld76aehXrA4O7EcXu10Ojnt6ze";

    protected static final String POSITION_TAG = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_sign_up);

        // TODO: Parse initialization
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);


        // TODO: Parse authentication
        // Check if user is already logged in
        // If true, send the user to the MainActivity with an intent
        // And make sure that when the back button is pressed from the MainActivity
        // that quits the application for the user; does not send back to login page

        // WARNING: THIS MAY CAUSE CRASHES
        if (false) {

            // TODO: Create an intent to send user to the MainActivity
            Intent intent = new Intent(this, MainFeedActivity.class);
            // TODO: Add necessary flags with intent.addFlags();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // TODO: startActivity(intent);
            startActivity(intent);
        }

        // TODO: get reference to the toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);

        // Tell Android we aren't using ActionBar
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // TODO: Set logo
//        getSupportActionBar().setLogo(R.drawable.fluint_android_white);

        // TODO: Adjust logo size

        mPager = (ViewPager) findViewById(R.id.loginPager);
        mPager.setAdapter(new LoginPagerAdapter(getSupportFragmentManager()));
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    // Hide the keyboard.
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(mPager.getWindowToken(), 0);

                }
            }
        });
        mTabs = (SlidingTabLayout) findViewById(R.id.loginTabs);
        mTabs.setCustomTabView(R.layout.custom_tab_view, R.id.loginTabsText);
        mTabs.setDistributeEvenly(true);
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.whiteColor);
            }
        });
        mTabs.setViewPager(mPager);


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

    class LoginPagerAdapter extends FragmentPagerAdapter {

        String[] tabs = getResources().getStringArray(R.array.tabs);

        public LoginPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public CharSequence getPageTitle(int position) {

//            Drawable drawable = getDrawable(icons[position]);
//            drawable.setBounds(0, 0, 36, 36);
//            ImageSpan imageSpan = new ImageSpan(drawable);
//            SpannableString spannableString = new SpannableString(tabs[position]);
//            spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            return tabs[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    NewUserFragment newUserFragment = NewUserFragment.getInstance(position);
                    return newUserFragment;
                case 1:
                    ExistingUserFragment existingUserFragment = ExistingUserFragment.getInstance(position);
                    return existingUserFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

    }

    public static class NewUserFragment extends Fragment {

        private static final String TAG_NEW = "NewLoginOrSignup";
        private static final String USER_DATA = "user_data";

        protected EditText etName;
        protected EditText etEmailText;
        protected EditText etPasswordText;
        protected ImageButton ibContinue;
        protected ImageButton ibFacebook;
        protected ImageButton ibGoogle;
        protected ConnectionDetector cd;
        protected boolean isInternetPresentNew;


        public static NewUserFragment getInstance(int position) {
            NewUserFragment newUserFragment = new NewUserFragment();
            return newUserFragment;
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            Log.d(TAG_NEW, "Getting references to widgets in newUserFragment's onCreateView!");

            View layout = inflater.inflate(R.layout.new_user_fragment, container, false);

            etName = (EditText) layout.findViewById(R.id.etName);
            etEmailText = (EditText) layout.findViewById(R.id.etEmailText);
            etPasswordText = (EditText) layout.findViewById(R.id.etPasswordText);
            ibContinue = (ImageButton) layout.findViewById(R.id.ibContinue);
            ibFacebook = (ImageButton) layout.findViewById(R.id.ibFacebook);
            ibGoogle = (ImageButton) layout.findViewById(R.id.ibGoogle);
            cd = new ConnectionDetector(getActivity());

            ibContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // TODO: Parse signup
                    // 1) Check if the email is already in the Parse database
                    //    Dialog: An account with that email already exists. Refer to UX forums to
                    //    see what users should be asked next to solve this issue
                    //    If so, display a Dialog, with the question: "Would you like to login?"
                    //    The answers are "No" and "Yes", with "Yes" sending the user to the other
                    //    PageView to login
                    // 2) Make sure to have users verify their email by having a boolean value
                    //    called hasVerifiedEmail to check if user has clicked on the link in their email

                    Log.d(TAG_NEW, "User wants to continue signing up!");


                    String name = etName.getText().toString();
                    String email = etEmailText.getText().toString();
                    String password = etPasswordText.getText().toString();

                    if (name.equals("") || email.equals("") || password.equals("")) {
                        if (name.equals("") && email.equals("") && password.equals("")) {
                            etName.setError("Name cannot be left blank");
                            etEmailText.setError("Email cannot be left blank");
                            etPasswordText.setError("Password cannot be left blank");
                            return;
                        } else if (name.equals("") && email.equals("")) {
                            etName.setError("Name cannot be left blank");
                            etEmailText.setError("Email cannot be left blank");
                            return;
                        } else if (email.equals("") && password.equals("")) {
                            etEmailText.setError("Email cannot be left blank");
                            etPasswordText.setError("Password cannot be left blank");
                            return;
                        } else if (name.equals("") && password.equals("")) {
                            etName.setError("Name cannot be left blank");
                            etPasswordText.setError("Password cannot be left blank");
                            return;
                        } else if (name.equals("")) {
                            etName.setError("Name cannot be left blank");
                            return;
                        } else if (email.equals("")) {
                            etEmailText.setError("Email cannot be left blank");
                            return;
                        } else if (password.equals("")) {
                            etPasswordText.setError("Password cannot be left blank");
                            return;
                        }
                    }

                    isInternetPresentNew = cd.isConnectingToInternet();
                    if (isInternetPresentNew) {
                        // TODO: Sign user up!
                        final String[] userData = new String[10];
                        userData[0] = name;
                        userData[1] = email;
                        userData[2] = password;

                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.whereEqualTo("email", email);
                        query.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> list, ParseException e) {
                                if (e == null && list.size() > 0) {
                                    // TODO: email is already taken. Show a dialog
                                    // should also check if list.size() > 0 ?
                                    android.support.v7.app.AlertDialog.Builder builder = new
                                            android.support.v7.app.AlertDialog.Builder(getActivity(),
                                            R.style.AppCompatAlertDialogStyle);
                                    builder.setTitle("Sign Up");
                                    builder.setMessage("This email is already registered. Want to login?");
                                    builder.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO: Take user to ExistingUserFragment
                                            mPager.setCurrentItem(1, true);
                                        }
                                    });
                                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO: Retrieve user password
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.show();

                                } else {
                                    // TODO: Email is not taken. Allow user to continue
                                    // Or sign user up

                                    Intent intent = new Intent(getActivity(), ContinueSignUpActivity.class);
                                    intent.putExtra(USER_DATA, userData);
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        android.support.v7.app.AlertDialog.Builder builder2 = new
                                android.support.v7.app.AlertDialog.Builder(getActivity(),
                                R.style.AppCompatAlertDialogStyle);
                        builder2.setTitle("No Internet Connection");
                        builder2.setMessage("You are not connected to the internet.\nDo you want to check your Wifi settings?");
                        builder2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            }
                        });
                        builder2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder2.show();
                    }
                }
            });

            ibFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "ibFacebook clicked!", Toast.LENGTH_SHORT).show();
                    isInternetPresentNew = cd.isConnectingToInternet();
                    if (isInternetPresentNew) {

                    } else {

                    }
                }
            });

            ibGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            return layout;
        }

        @Override
        public void onResume() {
            super.onResume();

            Log.d(TAG_NEW, "Entered new user onResume!");

        }
    }

    public static class ExistingUserFragment extends Fragment {

        private static final String TAG_EXISTING = "ExistingLoginOrSignup";

        // TODO: create class variables for our Views
        protected EditText etLoginEmail;
        protected EditText etLoginPassword;
        protected ImageButton ibLoginButton;
        protected ImageButton ibLoginFacebook;
        protected ImageButton ibLoginGoogle;
        protected TextView forgotPassword;
        protected ConnectionDetector cd;
        protected boolean isInternetPresent;

        protected int failedLoginCount;

        public static ExistingUserFragment getInstance(int position) {
            ExistingUserFragment newUserFragment = new ExistingUserFragment();
            return newUserFragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            failedLoginCount = 0;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            Log.d(TAG_EXISTING, "Getting references to widgets in ExistingUserFragment's onCreateView!");

            View layout = inflater.inflate(R.layout.existing_user_fragment, container, false);

            // TODO: get references to the different Views
            etLoginEmail = (EditText) layout.findViewById(R.id.etLoginEmail);
            etLoginPassword = (EditText) layout.findViewById(R.id.etLoginPassword);
            ibLoginButton = (ImageButton) layout.findViewById(R.id.ibLoginButton);
            ibLoginFacebook = (ImageButton) layout.findViewById(R.id.ibLoginFacebook);
            ibLoginGoogle = (ImageButton) layout.findViewById(R.id.ibLoginGoogle);
            forgotPassword = (TextView) layout.findViewById(R.id.forgotPassword);
            cd = new ConnectionDetector(getActivity());

            ibLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // TODO: set ProgressBar's visibility to View.VISIBLE

                    // Get the email and password from the user-inputted fields
                    String email = etLoginEmail.getText().toString();
                    String password = etLoginPassword.getText().toString();


                    if (email.equals("") || password.equals("")) {
                        if (email.equals("") && password.equals("")) {
                            etLoginEmail.setError("Email cannot be left blank");
                            etLoginPassword.setError("Password cannot be left blank");
                            return;
                        } else if (email.equals("")) {
                            etLoginEmail.setError("Email cannot be left blank");
                            return;
                        } else if (password.equals("")) {
                            etLoginPassword.setError("Password cannot be left blank");
                            return;
                        }
                    }
                    // TODO: verify. If Parse verified, create a new ParseUser and save to device
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        Toast.makeText(getActivity(), "Internet present", Toast.LENGTH_SHORT).show();
                        ParseUser.logInInBackground(email, password, new LogInCallback() {
                            @Override
                            public void done(ParseUser parseUser, ParseException e) {
                                if (e == null && parseUser != null) {
                                    Intent intent = new Intent(getActivity(), MainFeedActivity.class);
                                    startActivity(intent);
                                } else if (parseUser == null) {
                                    failedLoginCount++;
                                    android.support.v7.app.AlertDialog.Builder dialogBuilder = new
                                            android.support.v7.app.AlertDialog.Builder(getActivity(),
                                            R.style.AppCompatAlertDialogStyle);
                                    dialogBuilder.setTitle("Invalid Login");
                                    dialogBuilder.setMessage("The email and password do not match.");
                                    dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialogBuilder.show();
                                } else {
                                    // TODO: something else went wrong somewhere
                                    Toast.makeText(getActivity(), "An error occurred. Please try again later.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        android.support.v7.app.AlertDialog.Builder builderDialog = new
                                android.support.v7.app.AlertDialog.Builder(getActivity(),
                                R.style.AppCompatAlertDialogStyle);
                        builderDialog.setTitle("No Internet Connection");
                        builderDialog.setMessage("You are not connected to the internet.\nDo you want to check your Wifi settings?");
                        builderDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            }
                        });
                        builderDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builderDialog.show();
                    }


                }
            });

            ibLoginFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            ibLoginGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: start a new activity to ask for email?

                }
            });

            return layout;
        }

        @Override
        public void onResume() {
            super.onResume();

            Log.d(TAG_EXISTING, "Entered ExistingUserFragment's onResume!");


        }
    }

}

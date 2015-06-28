package eia.fluint;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.Toast;


public class LoginOrSignUpActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;

    protected static final String POSITION_TAG = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_sign_up);

        // TODO: Parse authentication
        toolbar = (Toolbar) findViewById(R.id.app_bar);

        // Tell Android we aren't using ActionBar
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set logo with getSupportActionBar().setLogo();

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

        protected EditText etName;
        protected EditText etEmailText;
        protected EditText etPasswordText;
        protected ImageButton ibContinue;
        protected ImageButton ibFacebook;
        protected ImageButton ibGoogle;


        public static NewUserFragment getInstance(int position) {
            NewUserFragment newUserFragment = new NewUserFragment();

            Bundle args = new Bundle();
            args.putInt(POSITION_TAG, position);
            newUserFragment.setArguments(args);
            return newUserFragment;
        }


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // TODO: Check if user is already logged in
            // If so, send user to the MainActivity with an Intent

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.new_user_fragment, container, false);

            etName = (EditText) layout.findViewById(R.id.etName);
            etEmailText = (EditText) layout.findViewById(R.id.etEmailText);
            etPasswordText = (EditText) layout.findViewById(R.id.etPasswordText);
            ibContinue = (ImageButton) layout.findViewById(R.id.ibContinue);
            ibFacebook = (ImageButton) layout.findViewById(R.id.ibFacebook);
            ibGoogle = (ImageButton) layout.findViewById(R.id.ibGoogle);

            Bundle bundle = getArguments();
            if (bundle != null) {
                // TODO: not sure
            }
            return layout;
        }

    }

    public static class ExistingUserFragment extends Fragment {

        // TODO: create class variables for our Views

        public static ExistingUserFragment getInstance(int position) {
            ExistingUserFragment newUserFragment = new ExistingUserFragment();

            Bundle args = new Bundle();
            args.putInt(POSITION_TAG, position);
            newUserFragment.setArguments(args);
            return newUserFragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.new_user_fragment, container, false);

            // TODO: get references to the different Views


            Bundle bundle = getArguments();
            if (bundle != null) {
                // TODO: don't know what to do

            }
            return layout;
        }

    }

}

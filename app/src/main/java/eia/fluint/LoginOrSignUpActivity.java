package eia.fluint;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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
        mTabs = (SlidingTabLayout) findViewById(R.id.loginTabs);
        mTabs.setCustomTabView(R.layout.custom_tab_view, R.id.loginTabsText);
        mTabs.setViewPager(mPager);

    }

    class LoginPagerAdapter extends FragmentPagerAdapter {

        private final int TAB_COUNT = 2;

        // Create an int array of icons with {R.drawable.ic_action_icon1, R.drawableic_action_icon2}
        int icons[];
        String[] tabs = getResources().getStringArray(R.array.tabs);

        public LoginPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            Drawable drawable = getDrawable(icons[position]);
            ImageSpan imageSpan = new ImageSpan(drawable);
            SpannableString spannableString = new SpannableString("");
            spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            return spannableString;
        }

        @Override
        public Fragment getItem(int position) {
            LandingFragment landingFragment = LandingFragment.getInstance(position);
            return landingFragment;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }

    public static class LandingFragment extends Fragment {

        private TextView textView;

        public static LandingFragment getInstance(int position) {
            LandingFragment landingFragment = new LandingFragment();

            Bundle args = new Bundle();
            args.putInt(POSITION_TAG, position);
            landingFragment.setArguments(args);
            return landingFragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.landing_fragment, container, false);
            textView = (TextView) layout.findViewById(R.id.fragmentPosition);
            Bundle bundle= getArguments();
            if (bundle != null) {
                textView.setText("The page selected is: " + bundle.getInt(POSITION_TAG));
            }
            return layout;
        }

    }

}

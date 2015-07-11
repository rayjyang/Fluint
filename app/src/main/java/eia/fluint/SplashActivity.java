package eia.fluint;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatButton;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class SplashActivity extends Activity {

    private long splashDelay = 800;

    private static final String APPLICATION_ID = "ClUIw0Dh2ja21S5sH0vjTrJ6a9nL9g1vH2b9EfMg";
    private static final String CLIENT_KEY = "I9qGl0kinVIKh0Ld76aehXrA4O7EcXu10Ojnt6ze";

    private ImageView fluintLogo;
    private LinearLayout parentLayout;
    private AppCompatButton splashSignIn;
    private AppCompatButton spashRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Parse initialization
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);


        ParseFacebookUtils.initialize(getApplicationContext());

        // TODO: Parse authentication
        // Check if user is already logged in
        // If true, send the user to the MainActivity with an intent
        // And make sure that when the back button is pressed from the MainActivity
        // that quits the application for the user; does not send back to login page

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (true) {

            // TODO: Create an intent to send user to the MainActivity
            Intent intent = new Intent(this, MainFeedActivity.class);
            // TODO: Add necessary flags with intent.addFlags();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // TODO: startActivity(intent);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_splash);

            fluintLogo = (ImageView) findViewById(R.id.fluintLogo);
            parentLayout = (LinearLayout) findViewById(R.id.parentLayout);

            animateInLogo();

        }


    }

    private void animateInLogo() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        ObjectAnimator animator = ObjectAnimator.ofFloat(fluintLogo, "y", -200);
        animator.setDuration(5000);
        animator.start();
    }

}

package eia.fluint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class SplashActivity extends Activity {

    private long splashDelay = 800;

    private static final String APPLICATION_ID = "ClUIw0Dh2ja21S5sH0vjTrJ6a9nL9g1vH2b9EfMg";
    private static final String CLIENT_KEY = "I9qGl0kinVIKh0Ld76aehXrA4O7EcXu10Ojnt6ze";

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
        if (currentUser != null) {

            // TODO: Create an intent to send user to the MainActivity
            Intent intent = new Intent(this, MainFeedActivity.class);
            // TODO: Add necessary flags with intent.addFlags();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // TODO: startActivity(intent);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_splash);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginSignUpActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, splashDelay);
        }


    }

}

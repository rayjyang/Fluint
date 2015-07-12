package eia.fluint;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class FluintApplication extends Application {

    private static final String APPLICATION_ID = "ClUIw0Dh2ja21S5sH0vjTrJ6a9nL9g1vH2b9EfMg";
    private static final String CLIENT_KEY = "I9qGl0kinVIKh0Ld76aehXrA4O7EcXu10Ojnt6ze";

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);

        ParseFacebookUtils.initialize(this);
    }
}

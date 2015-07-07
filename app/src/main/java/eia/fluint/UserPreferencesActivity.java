package eia.fluint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class UserPreferencesActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preferences);

        toolbar = (Toolbar) findViewById(R.id.toolBarUserPref);
        setSupportActionBar(toolbar);

        // Features to:
        // 1) Change password 2) Link and unlink fb accounts
    }


}

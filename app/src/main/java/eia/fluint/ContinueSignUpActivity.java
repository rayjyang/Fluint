package eia.fluint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


public class ContinueSignUpActivity extends AppCompatActivity {

    private static final String TAG = "CONTINUE_SIGNUP";
    protected String[] userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_sign_up);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Log.d(TAG, "Got the user's data!");
            userData = extras.getStringArray("user_data");
        } else {
            Toast.makeText(ContinueSignUpActivity.this, "No extras", Toast.LENGTH_SHORT).show();
        }




    }

}

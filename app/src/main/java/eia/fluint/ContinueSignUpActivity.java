package eia.fluint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class ContinueSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_sign_up);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String[] userData = extras.getStringArray("user_data");
        }

    }

}

package eia.fluint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class AddPostActivity extends AppCompatActivity {

    private static final String TAG = "AddPostActivity";
    private final String CLASS_KEY = "transaction";

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        toolbar = (Toolbar) findViewById(R.id.toolbarAddPost);
        setSupportActionBar(toolbar);

        ChooseLocationFragment fragment = new ChooseLocationFragment();
        getFragmentManager().beginTransaction().add(R.id.addPostFragmentContainer, fragment).commit();


    }
}

package eia.fluint;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


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
        toolbar.setTitle("Choose A Location");


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:

                Fragment f = getFragmentManager().findFragmentById(R.id.addPostFragmentContainer);
                if (f instanceof ChooseLocationFragment) {
                    finish();
                }
                ChooseLocationFragment fragment = new ChooseLocationFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                // TODO: Fix going back animations

                ft.replace(R.id.addPostFragmentContainer, fragment);
                ft.addToBackStack(null);
                ft.commit();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }




}

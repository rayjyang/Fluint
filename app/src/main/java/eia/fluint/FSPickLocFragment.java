package eia.fluint;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FSPickLocFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FSPickLocFragment extends Fragment {

    public static final String TAG_FRAGMENT = "FSPickLocFragment";


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FSPickLocFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FSPickLocFragment newInstance() {
        FSPickLocFragment fragment = new FSPickLocFragment();
        return fragment;
    }

    public FSPickLocFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fs_pick_loc, container, false);
    }


}

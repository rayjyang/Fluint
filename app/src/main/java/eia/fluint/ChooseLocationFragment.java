package eia.fluint;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ChooseLocationFragment extends Fragment {

    private LinearLayout chooseCurrentLocation;
    private LinearLayout chooseALocation;
    private LinearLayout chooseAirportLocation;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChooseLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChooseLocationFragment newInstance() {
        ChooseLocationFragment fragment = new ChooseLocationFragment();
        return fragment;
    }

    public ChooseLocationFragment() {
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
        View view = inflater.inflate(R.layout.fragment_choose_location, container, false);

        chooseCurrentLocation = (LinearLayout) view.findViewById(R.id.chooseCurrentLocation);
        chooseALocation = (LinearLayout) view.findViewById(R.id.chooseALocation);
        chooseAirportLocation = (LinearLayout) view.findViewById(R.id.chooseAirportLocation);

        chooseCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        chooseALocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        chooseAirportLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionAirportFragment fragment = new TransactionAirportFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.replace(R.id.addPostFragmentContainer, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }

}

package eia.fluint;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TransactionAirportFragment extends Fragment {


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TransactionAirportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionAirportFragment newInstance() {
        TransactionAirportFragment fragment = new TransactionAirportFragment();
        return fragment;
    }

    public TransactionAirportFragment() {
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
        View view = inflater.inflate(R.layout.fragment_transaction_airport, container, false);



        return view;
    }


}

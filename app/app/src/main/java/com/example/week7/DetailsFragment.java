package com.example.week7;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class DetailsFragment extends Fragment {
    private ViewModel viewModel;
    private Country country;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details__fragment, container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView countryDetails = view.findViewById(R.id.details);
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
        viewModel.getCurrentCountries().observe(getActivity(), new Observer<ArrayList<Country>>() {
            @Override
            public void onChanged(ArrayList<Country> countries) {
                if (country != null && !countries.contains(country)){
                    countryDetails.setText("");
                }
            }
        });
        viewModel.getCurrentPosition().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer row) {
                if (row >= 0){
                    country = viewModel.getCountry(row);
                    if (country != null)
                        countryDetails.setText(country.getDetails());
                    else
                        countryDetails.setText("");
                }
            }
        });
    }

}

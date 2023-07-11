package com.example.finalproject;

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
    private Product product;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_fragment, container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView details = view.findViewById(R.id.details);
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
        viewModel.getCurrentCountries().observe(getActivity(), new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> countries) {
                if (product != null && !countries.contains(product)){
                    details.setText("");
                }
            }
        });
        viewModel.getCurrentPosition().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer row) {
                if (row >= 0){
                    product = viewModel.getProduct(row);
                    if (product != null)
                        details.setText(product.getDetails());
                    else
                        details.setText("");
                }
            }
        });
    }

}

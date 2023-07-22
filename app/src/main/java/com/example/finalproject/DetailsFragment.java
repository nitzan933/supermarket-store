package com.example.finalproject;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class DetailsFragment extends Fragment {
    private ViewModel viewModel;
    private Product product;
    private ImageView image;
    private TextView name;
    private TextView brand;
    private TextView details;
    private TextView price;
    private Context context;
    private Button add_but;


    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
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
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
        image = view.findViewById(R.id.dimage);
        name = view.findViewById(R.id.name);
        brand = view.findViewById(R.id.brand);
        details = view.findViewById(R.id.details);
        price = view.findViewById(R.id.price);
        add_but = view.findViewById(R.id.add_but);
        add_but.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                viewModel.addProductToCart(viewModel.getPosition());
            }
        });
        viewModel.getCurrentCountries().observe(getActivity(), new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                if (product != null && !products.contains(product)){
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
                        changeDetails();
                    else delDetails();
                }
            }
        });
    }
    
    public void changeDetails(){
        int id = context.getResources().getIdentifier(product.getImage(), "drawable", context.getPackageName());
        image.setImageResource(id);
        name.setText(product.getName());
        brand.setText(product.getBrand());
        details.setText(product.getDetails());
        price.setText("" + product.getPrice());
    }
    public void delDetails(){
        int id = context.getResources().getIdentifier("loading", "drawable", context.getPackageName());
        image.setImageResource(id);
        name.setText("");
        brand.setText("");
        details.setText("");
        price.setText("");
    }

}

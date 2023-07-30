package com.example.finalproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartRecyclerFrag extends Fragment{

    private ArrayList<Product> countries = new ArrayList<>();
    private View view;

    private RecyclerView recyclerView;
    private ProductAdapterCart adapter;
    private ViewModelCart viewModel;
    private OnClickListenerFrag listenerFrag;

    @Override
    public void onAttach(@NonNull Context context) {
        try{
            this.listenerFrag = (OnClickListenerFrag) context;
        }catch(ClassCastException e){
            throw new ClassCastException("the class " +
                    context.getClass().getName() +
                    " must implements the interface ''");
        }
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cart_frag, container,false);
        recyclerView = view.findViewById(R.id.mRecyclerview2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel = new ViewModelProvider(getActivity()).get(ViewModelCart.class);
        adapter = new ProductAdapterCart(getContext(), getActivity(), viewModel, listenerFrag);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    interface OnClickListenerFrag
    {
        void clickOnProduct();
    }


}

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

public class Frag extends Fragment{

    private ArrayList<Product> countries = new ArrayList<>();
    private View view;

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ViewModel viewModel;
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
        view = inflater.inflate(R.layout.products_frag, container,false);
        recyclerView = view.findViewById(R.id.mRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel = new ViewModelProvider(getActivity()).get(ViewModel.class);
        adapter = new ProductAdapter(getContext(), getActivity(), viewModel, listenerFrag);
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

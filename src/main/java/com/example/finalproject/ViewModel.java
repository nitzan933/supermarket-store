package com.example.finalproject;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Product>> products;
    private MutableLiveData<Integer> productSelected;
    private ArrayList<Product> productsList;
    private Integer pos;
    private Context context;

    public ViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        products = new MutableLiveData<>();
        productsList = ProductXMLParser.parseProducts(context);
        products.setValue(productsList);
        productSelected = new MutableLiveData<>();
        pos = RecyclerView.NO_POSITION;
        productSelected.setValue(pos);
    }

    public int getPosition() {
        return pos;
    }

    public void setNumOfRow(int position) {
        pos = position;
        productSelected.setValue(pos);
    }

    public MutableLiveData<ArrayList<Product>> getCurrentCountries() {
        return products;
    }

    public LiveData<Integer> getCurrentPosition() {
        return productSelected;
    }

    public Product getProduct(Integer row) {
        return productsList.get(row);
    }


    public void addProductToCart(int position) {
    }
}

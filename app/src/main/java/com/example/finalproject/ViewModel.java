package com.example.finalproject;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

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
    public MutableLiveData<Integer> getItemSelected() {
        return productSelected;
    }

    public MutableLiveData<ArrayList<Product>> getCurrentProducts() {
        return products;
    }

    public LiveData<Integer> getCurrentPosition() {
        return productSelected;
    }

    public Product getProduct(Integer row) {
        return productsList.get(row);
    }

    public void removeProduct(int index) {
        ArrayList<Product> list = getCurrentProducts().getValue();
        Product removedProducts = list.remove(index);
       // writeToFile(removedCountry);
        //getCountries().setValue(list);
    }

    public void addProductToCart(int position) {
        String product = getProduct(position).getName();
        Toast.makeText(context, product + " added to cart", Toast.LENGTH_LONG).show();
        Data data = new Data.Builder().putString("product_down_of_cart", product).build();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(TimerWorker.class).setInputData(data).build();
        WorkManager.getInstance(context).enqueue(workRequest);
    }

}

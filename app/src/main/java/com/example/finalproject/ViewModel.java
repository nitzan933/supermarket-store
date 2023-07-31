package com.example.finalproject;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ViewModel extends AndroidViewModel {
    private final Application application;
    private final String fileName = "RemovedProducts.rc";
    private MutableLiveData<ArrayList<Product>> products,cart;
    private MutableLiveData<Integer> productSelected;
    private ArrayList<Product> productsList;
    private Integer pos;
    private Context context;

    private ArrayList<Product>  currentList;
    private boolean[] removed;

    private int originalSize;

    public ViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        context = application.getApplicationContext();
        products = new MutableLiveData<>();
        cart=new MutableLiveData<>();
        productsList = ProductXMLParser.parseProducts(context);
        products.setValue(productsList);
        cart.setValue(productsList);
        originalSize = productsList.size();
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
    public MutableLiveData<ArrayList<Product>> getCurrentCart() {
        return cart;
    }

    public LiveData<Integer> getCurrentPosition() {
        return productSelected;
    }

    public Product getProduct(Integer row) {
        return productsList.get(row);
    }

    public void removeProduct(int index) {

      // writeToFile(removedProducts);
      // getCurrentProducts().setValue(list);
    }

    private void writeToFile(Product removedCountry) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(application);
        for (int i = 0; i < originalSize; i++)
            if (productsList.get(i).equals(removedCountry))
                removed[i] = true;
        // SP
        if (sharedPref.getBoolean("useSP", true)) {

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(removedCountry.getName(), true);
            editor.apply();
        }
        // RAW
        else {

            try {
                FileOutputStream fos = application.openFileOutput(fileName, Context.MODE_PRIVATE);
                byte[] bb = new byte[originalSize];
                //
                for (int i = 0; i < originalSize; i++) {
                    bb[i] = (byte) (removed[i] ? 1 : 0);
                }
                Log.i("EX8", bb.toString());
                fos.write(bb);
                fos.close();
            } catch (IOException e) {
                Log.e("EX8", "Failed to save");
            }
        }
    }


}

package com.example.finalproject;

import android.app.Application;
import android.content.Context;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewModelCart extends AndroidViewModel {
    private final Application application;
    private final String fileName = "RemovedProducts.rc";
    private MutableLiveData<ArrayList<Product>> products,cart;
    private MutableLiveData<Integer> productSelected;
    private ArrayList<Product> productsList;
    private static ArrayList<Product> cartList;
    private Integer pos;
    private Context context;

    private boolean[] added;

    private Object cartArray;

    private SharedPreferences sharedPref;
    private int originalSize;

    byte[] bb;

    private int numberOfProducts;

    HashMap<String, Integer> hashMap;
    public ViewModelCart(@NonNull Application application) {
        super(application);
        this.application = application;
        context = application.getApplicationContext();
        products = new MutableLiveData<>();
        cart=new MutableLiveData<>();
        productsList = ProductXMLParser.parseProducts(context);
        cartList=new ArrayList<>();
        products.setValue(productsList);
        cart.setValue(cartList);
        originalSize = productsList.size();
        productSelected = new MutableLiveData<>();
        pos = RecyclerView.NO_POSITION;
        productSelected.setValue(pos);
        added = new boolean[originalSize];
        numberOfProducts=0;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(application);
        boolean remember = sharedPref.getBoolean("rememberCart", false);
        bb=new byte[100];

        if (remember) {
           readFile(sharedPref);
      } else {
           resetFile(sharedPref);
        }

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



    private void readFile(SharedPreferences sharedPref) {

        // SP
        if (sharedPref.getBoolean("useSP", true))
        {
            for (int i = originalSize - 1; i >= 0; i--)
            {
                if (sharedPref.getBoolean(productsList.get(i).getName(), false))
                        cartList.add(productsList.get(i));

            }

        }
        // RAW FILE
        else {
            ByteBuffer bf = readSavedRAWFile();
            numberOfProducts=sharedPref.getInt("productCount",0);

            for(int i=0;i<numberOfProducts;i++)
                  {
                      cartList.add(getProduct((int) bf.get(i)));

                    }
                }
        }


    private ByteBuffer readSavedRAWFile() {
        FileInputStream fin = null;
        try {
            fin = application.openFileInput(fileName);
            ByteBuffer bf = ByteBuffer.allocate(originalSize);
            fin.read(bf.array());
            fin.close();
            return bf;
        } catch (FileNotFoundException e) {
            Log.i("EX8", "No file found");
            return null;
        } catch (IOException e) {
            Log.i("EX8", "Couldn't read file");
            return null;
        }
    }



public static void productRemove(String Product)
{
    cartList.remove(0);
}


    private void writeToFile(Product cartProduct) {

        // SP
        if (sharedPref.getBoolean("useSP", true)) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(cartProduct.getName(), true);
            editor.commit();
            editor.apply();
        }
        // RAW
        else {

            try {
                FileOutputStream fos = application.openFileOutput(fileName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                //
                for (int i = 0; i < originalSize; i++) {
                    if (productsList.get(i).equals(cartProduct)) {
                        bb[numberOfProducts++] = (byte) (i);
                    }
                }
                editor.putInt("productCount",numberOfProducts);
                editor.commit();
                editor.apply();

                Log.i("EX8", bb.toString());
                fos.write(bb);
                fos.close();
            } catch (IOException e) {
                Log.e("EX8", "Failed to save");
            }
        }
    }

    private void resetFile(SharedPreferences sharedPref) {
        SharedPreferences.Editor editor = sharedPref.edit();
        numberOfProducts=0;
        // SP
        for (int i = productsList.size() - 1; i >= 0; i--) {
            editor.putBoolean(productsList.get(i).getName(), false);
        }
        editor.apply();
        // RAW
        try {
            FileOutputStream fos = application.openFileOutput(fileName, Context.MODE_PRIVATE);
            byte[] bb = new byte[originalSize];
            //
            for (int i = 0; i < originalSize; i++) {
                bb[i] = (byte) 0;
            }
            fos.write(bb);
            fos.close();
        } catch (IOException e) {
            Log.e("EX8", "Failed to reset");
        }
    }


    public void addProductToCart(int position) {
        String product = getProduct(position).getName();
        Toast.makeText(context, product + " added to cart", Toast.LENGTH_LONG).show();
        cartList.add(getProduct(position));
       // ArrayList<Product> list = getCurrentCart().getValue();
        //save cart to files:
        writeToFile(getProduct(position));
        Data data = new Data.Builder().putString("product_down_of_cart", product).build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(TimerWorker.class).setInputData(data).build();
        WorkManager.getInstance(context).enqueue(workRequest);
    }

}

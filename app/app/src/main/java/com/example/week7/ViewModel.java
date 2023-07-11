package com.example.week7;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;
import static android.provider.Settings.System.getString;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Country>> countries;
    private MutableLiveData<Integer> itemSelected;
    private ArrayList<Country> countryList;
    private ArrayList<Country> countryAfterRemoveList;
    private Integer item;

    private ArrayList<Integer> removedCountryList;
    private String filename = "removed_countries";

    private Context context;

    private boolean checked = false;
    private File file;

    private SharedPreferences sharedPref;

    public ViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        file = new File(context.getFilesDir(), filename);

        countries = new MutableLiveData<>();
        countryList = CountryXMLParser.parseCountries(context);
        countryAfterRemoveList = countryList;

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplication());
        checked = sharedPref.getBoolean("check_box_preference" , false);

        if(checked == true) {
            removedCountryList = new ArrayList<>();
//            try (FileInputStream fis = context.openFileInput(filename)) {
//                InputStreamReader isr = new InputStreamReader(fis);
//                BufferedReader br = new BufferedReader(isr);
//                String res = br.readLine();
//                while(res != null){
//                    removedCountryList.add(Integer.valueOf(res));
//                    res = br.readLine();
//                }
//                fis.close();
//                Log.d(TAG, "ViewModel: ");
//            } catch (IOException e) {
//                Log.d(TAG, "no ViewModel: ");
//            }

            int index;
            for (int i=0; i< countryList.size(); i++) {
                index = sharedPref.getInt(String.valueOf(i), -1);
                if(index != -1){
                    removedCountryList.add(Integer.valueOf(index));
                }
            }


            for (Integer removedCountry : removedCountryList) {
                countryAfterRemoveList.remove(countryAfterRemoveList.get(removedCountry));
            }
            countries.setValue(countryAfterRemoveList);

        }
        else countries.setValue(countryList);

        itemSelected = new MutableLiveData<>();
        item = RecyclerView.NO_POSITION;
        itemSelected.setValue(item);
    }


    public int getPosition() {
        return item;
    }

    public void setNumOfRow(int position) {
        item = position;
        itemSelected.setValue(item);
    }

    public MutableLiveData<ArrayList<Country>> getCurrentCountries() {
        return countries;
    }

    public LiveData<Integer> getCurrentPosition() {
        return itemSelected;
    }

    public Country getCountry(Integer row) {
        return countryList.get(row);
    }

    public void removeItemFromList(int position) {
//        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
//            ByteBuffer bf = ByteBuffer.allocate(8);
//            byte[] bb = bf.putInt(position).array();
//            fos.write((String.valueOf(position)+"\n").getBytes());
//
//            Log.d(TAG, "removeItemFromList: ");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(String.valueOf(position), position);
        editor.apply();

        countryList.remove(position);
        countries.setValue(countryList);
        

    }






}

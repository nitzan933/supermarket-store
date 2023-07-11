package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements Frag.OnClickListenerFrag {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DetailsFragment fragB = (DetailsFragment) getSupportFragmentManager().findFragmentByTag("FRAGB");
        if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
            if (fragB != null) {
                getSupportFragmentManager().beginTransaction()
                        .show(fragB)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentDetailContainerView, DetailsFragment.class, null, "FRAGB")
                        .commit();
            }
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    @Override
    public void clickOnProduct() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragA, DetailsFragment.class, null,"FRAGB")
                    .addToBackStack("BBB")
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }
    }
}
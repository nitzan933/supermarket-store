package com.example.week7;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder>
{
    private ArrayList<Country> countries;
    private Context context;
    private ViewModel viewModel;
    private Frag.OnClickListenerFrag listenerFrag;
    private int selected = -1;

    public CountryAdapter(Context context, FragmentActivity activity, ViewModel viewModel, Frag.OnClickListenerFrag listenerFrag)
    {
       countries = CountryXMLParser.parseCountries(context);
       this.context = context;
       this.viewModel = viewModel;
       this.listenerFrag = listenerFrag;
        this.viewModel.getCurrentCountries().observe(activity, new Observer<ArrayList<Country>>() {
            @Override
            public void onChanged(ArrayList<Country> countries) {
                setCountryList(countries);
            }
        });
    }

    public void setCountryList(ArrayList<Country> countries){
        this.countries = countries;
    }

    @NonNull
    @Override
    public CountryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //This is where you inflate the layout (Giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row,parent,false);
        return new CountryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryAdapter.MyViewHolder holder, int position)
    {
        selected = this.viewModel.getPosition();
        holder.itemView.setBackgroundColor(selected == position ? Color.WHITE : Color.TRANSPARENT);
        holder.addRowToList(position);
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        ImageView image;
        TextView name;
        TextView population;
        View row;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            row = itemView.findViewById(R.id.row);
            image = itemView.findViewById(R.id.country_image);
            name = itemView.findViewById(R.id.country_name);
            population = itemView.findViewById(R.id.country_population);

        }

        public void addRowToList(int position)
        {
            int id = context.getResources().getIdentifier(countries.get(position).getFlag(), "drawable", context.getPackageName());
            image.setImageResource(id);
            name.setText(" "+countries.get(position).getName());
            population.setText(" "+countries.get(position).getShorty());
            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    viewModel.removeItemFromList(position);
                    if (position == selected)
                        viewModel.setNumOfRow(RecyclerView.NO_POSITION);
                    else if (position < selected)
                        viewModel.setNumOfRow(selected - 1);
                    notifyDataSetChanged();
                    return true;
                }
            });
            row.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.setNumOfRow(position);
                    notifyItemChanged(position);
                    listenerFrag.clickOnCountry();
                    notifyItemChanged(position);
                }
            });
        }
    }



}

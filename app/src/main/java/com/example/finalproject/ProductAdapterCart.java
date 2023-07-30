package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdapterCart extends RecyclerView.Adapter<ProductAdapterCart.MyViewHolder>
{
    private ArrayList<Product> products;
    private Context context;
    private ViewModelCart viewModel;
    private CartRecyclerFrag.OnClickListenerFrag listenerFragCart;

    private int selected = -1;
    private int prevSelected = -1;

    public ProductAdapterCart(Context context, FragmentActivity activity, ViewModelCart viewModel, CartRecyclerFrag.OnClickListenerFrag listenerFragCart)
    {
       products = ProductXMLParser.parseProducts(context);
       this.context = context;
       this.viewModel = viewModel;
       this.listenerFragCart = listenerFragCart;
        this.viewModel.getCurrentCart().observe(activity, new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                setProductList(products);
            }
        });
    }

    public void setProductList(ArrayList<Product> products){
        this.products = products;
    }

    @NonNull
    @Override
    public ProductAdapterCart.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //This is where you inflate the layout (Giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row,parent,false);
        return new ProductAdapterCart.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapterCart.MyViewHolder holder, int position)
    {
        selected = this.viewModel.getPosition();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(prefs.getBoolean("switch_dark_theme", false)){
            holder.itemView.setBackgroundColor(selected == position ? Color.valueOf(0xFF282A3A).toArgb() : Color.TRANSPARENT);
        }
        else holder.itemView.setBackgroundColor(selected == position ? Color.valueOf(0xFFDEDFE2).toArgb() : Color.TRANSPARENT);
        holder.addRowToList(position);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        ImageView image;
        TextView name;
        TextView brand;
        TextView price;
        View row;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            row = itemView.findViewById(R.id.row);
            image = itemView.findViewById(R.id.product_image);
            name = itemView.findViewById(R.id.product_name);
            brand = itemView.findViewById(R.id.product_brand);
            price = itemView.findViewById(R.id.product_price);
        }

        public void addRowToList(int position)
        {
            int id = context.getResources().getIdentifier(products.get(position).getImage(), "drawable", context.getPackageName());
            image.setImageResource(id);
            name.setText(" "+products.get(position).getName());
            brand.setText(" "+products.get(position).getBrand());
            price.setText(" "+products.get(position).getPrice());
        }
    }



}

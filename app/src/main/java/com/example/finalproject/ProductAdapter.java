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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder>
{
    private ArrayList<Product> products;
    private Context context;
    private ViewModel viewModel;
    private ProductRecyclerFrag.OnClickListenerFrag listenerFrag;

    private CartRecyclerFrag.OnClickListenerFrag listenerFragCart;

    private int selected = -1;
    private int prevSelected = -1;

    public ProductAdapter(Context context, FragmentActivity activity, ViewModel viewModel, ProductRecyclerFrag.OnClickListenerFrag listenerFrag)
    {
       products = ProductXMLParser.parseProducts(context);
       this.context = context;
       this.viewModel = viewModel;
       this.listenerFrag = listenerFrag;
        this.viewModel.getCurrentProducts().observe(activity, new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> countries) {
                setProductList(countries);
            }
        });
    }

    public ProductAdapter(Context context, FragmentActivity activity, ViewModel viewModel, CartRecyclerFrag.OnClickListenerFrag listenerFragCart)
    {
        products = ProductXMLParser.parseProducts(context);
        this.context = context;
        this.viewModel = viewModel;
        this.listenerFrag = listenerFrag;
        this.viewModel.getCurrentProducts().observe(activity, new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> countries) {
                setProductList(countries);
            }
        });
    }

    public void setProductList(ArrayList<Product> products){
        this.products = products;
    }

    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //This is where you inflate the layout (Giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row,parent,false);
        return new ProductAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder holder, int position)
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
            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    viewModel.addProductToCart(position);
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
                    listenerFrag.clickOnProduct();
                    notifyItemChanged(position);
                    if(prevSelected!= -1)
                        notifyItemChanged(prevSelected);
                    prevSelected=position;

                }
            });
            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    viewModel.addProductToCart(position);
                    if (position == selected)
                        viewModel.setNumOfRow(RecyclerView.NO_POSITION);
                    else if (position < selected)
                        viewModel.setNumOfRow(selected - 1);
                    notifyDataSetChanged();
                    return true;
                }
            });
        }
    }



}

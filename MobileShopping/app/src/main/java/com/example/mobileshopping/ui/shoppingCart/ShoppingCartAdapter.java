package com.example.mobileshopping.ui.shoppingCart;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobileshopping.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ShoppingCartAdapter extends BaseAdapter {

    private ArrayList<CartProduct> data=new ArrayList<>();
    private Context mContext;
    SharedPreferences cart;

    public ShoppingCartAdapter(ArrayList<CartProduct> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
        cart = mContext.getSharedPreferences("shopping_cart", MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_shopping_cart, container, false);
        }

        ((TextView) convertView.findViewById(R.id.tv_name))
                .setText(data.get(position).getName());
        ((TextView) convertView.findViewById(R.id.tv_price_value))
                .setText(""+data.get(position).getPrice());
        TextView buy_number=convertView.findViewById(R.id.tv_edit_buy_number);
        buy_number.setText(""+data.get(position).getQuantity());
        TextView tv_amount=((Activity)mContext).findViewById(R.id.tv_amount);
        ((ImageView) convertView.findViewById(R.id.iv_edit_add))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(data.get(position).getQuantity()<data.get(position).getStone()) {
                            data.get(position).setQuantity(data.get(position).getQuantity()+1);
                            buy_number.setText(""+data.get(position).getQuantity());
                            cart.edit().putInt(String.valueOf(data.get(position).getId()), data.get(position).getQuantity()).apply();
                        }
                        tv_amount.setText("$"+calAmount());
                    }
                });
        ((ImageView) convertView.findViewById(R.id.iv_edit_subtract))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.get(position).setQuantity(data.get(position).getQuantity()-1);
                        if(data.get(position).getQuantity()==0) {
                            cart.edit().remove(String.valueOf(data.get(position).getId())).apply();
                            data.remove(position);
                            notifyDataSetChanged();
                        } else {
                            buy_number.setText("" + data.get(position).getQuantity());
                            cart.edit().putInt(String.valueOf(data.get(position).getId()), data.get(position).getQuantity()).apply();
                        }
                        tv_amount.setText("$"+calAmount());
                    }
                });
        return convertView;
    }

    public int calAmount() {
        int amount=0;
        for (CartProduct d:data) {
            amount+=d.getPrice()*d.getQuantity();
        }
        return amount;
    }
}
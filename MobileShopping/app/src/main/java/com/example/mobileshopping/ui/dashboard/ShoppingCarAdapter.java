package com.example.mobileshopping.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mobileshopping.R;

import java.util.ArrayList;

public class ShoppingCarAdapter extends BaseAdapter {

    private ArrayList<CartProduct> data;
    private Context mContext;

    public ShoppingCarAdapter(ArrayList<CartProduct> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
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
        Object obj=data.get(position);
        return data.indexOf(obj);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shopping_car, container, false);
        }

        ((TextView) convertView.findViewById(R.id.tv_name))
                .setText(data.get(position).getName());
        ((TextView) convertView.findViewById(R.id.tv_price))
                .setText(""+data.get(position).getPrice());
        ((TextView) convertView.findViewById(R.id.tv_edit_buy_number))
                .setText(""+data.get(position).getQuantity());
        return convertView;
    }
}
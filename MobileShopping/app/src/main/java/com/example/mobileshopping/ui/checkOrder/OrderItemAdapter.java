package com.example.mobileshopping.ui.checkOrder;

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
import com.example.mobileshopping.ui.shoppingCart.CartProduct;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class OrderItemAdapter extends BaseAdapter {

    private ArrayList<OrderItem> data=new ArrayList<>();
    private Context mContext;
    SharedPreferences cart;

    public OrderItemAdapter(ArrayList<OrderItem> data, Context mContext) {
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_order, container, false);
        }

        ((TextView) convertView.findViewById(R.id.tv_name))
                .setText(data.get(position).getName());
        ((TextView) convertView.findViewById(R.id.tv_price_value))
                .setText(""+data.get(position).getPrice());
        ((TextView) convertView.findViewById(R.id.tv_quantity))
            .setText(""+data.get(position).getQuantity());

        return convertView;
    }
}

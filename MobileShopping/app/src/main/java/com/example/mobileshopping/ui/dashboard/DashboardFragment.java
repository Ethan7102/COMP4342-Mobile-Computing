package com.example.mobileshopping.ui.dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mobileshopping.R;
import com.example.mobileshopping.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import static android.content.Context.MODE_PRIVATE;


public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private ArrayList<CartProduct> data;
    private ShoppingCarAdapter shoppingCarAdapter;
    private RequestQueue queue;
    String url="http://192.168.1.4/getCartProduct.php";
    SharedPreferences cart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        cart = getActivity().getSharedPreferences("shopping_cart", MODE_PRIVATE);
        cart.edit().putInt("1",1).putInt("2",1).apply();
        queue = VolleySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        data=new ArrayList<>();
        init();
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    public void init() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response", response);
                try {
                    JSONArray productArray = new JSONArray(response);
                    for(int i=0;i<productArray.length();i++) {
                        JSONObject product=productArray.getJSONObject(i);
                        CartProduct cartProduct=new CartProduct();
                        cartProduct.setName(product.getString("productName"));
                        cartProduct.setPrice(product.getInt("price"));
                        cartProduct.setQuantity(cart.getInt(product.getString("productID"), 0));
                        data.add(cartProduct);
                        Log.i("response", data.get(i).getName());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, (VolleyError error) -> {
            error.printStackTrace();
            Log.i("idList", error.toString());
        })
        {
            @Override
            protected Map<String,String> getParams(){
                JSONObject jsonObject=new JSONObject();
                Map<String,String> params = new HashMap<>();
                Map<String, ?> allEntries = cart.getAll();
                int i=0;
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    try {
                        jsonObject.put("id_"+i, Integer.valueOf(entry.getKey().toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
                params.put("idList", jsonObject.toString());
                Log.i("idList", jsonObject.toString());
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
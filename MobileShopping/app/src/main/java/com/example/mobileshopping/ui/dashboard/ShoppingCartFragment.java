package com.example.mobileshopping.ui.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mobileshopping.APIUrl;
import com.example.mobileshopping.R;
import com.example.mobileshopping.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import static android.content.Context.MODE_PRIVATE;


public class ShoppingCartFragment extends Fragment {

    private ShoppingCartViewModel shoppingCartViewModel;
    private ArrayList<CartProduct> data;
    private ShoppingCartAdapter shoppingCartAdapter;
    private RequestQueue queue;
    private ListView cartList;
    Button btn_order;
    TextView tv_amount;
    EditText et_email;
    String url = APIUrl.url+"/getCartProduct.php";
    SharedPreferences cart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shoppingCartViewModel =
                new ViewModelProvider(this).get(ShoppingCartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        cartList =root.findViewById(R.id.lv_1);
        cart = getActivity().getSharedPreferences("shopping_cart", MODE_PRIVATE);
        queue = VolleySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        btn_order = root.findViewById(R.id.btn_order);
        tv_amount = root.findViewById(R.id.tv_amount);
        et_email = root.findViewById(R.id.et_email);
        data=new ArrayList<>();
        initData();
        shoppingCartViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    public void initData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response", response);
                data.clear();
                try {
                    JSONArray productArray = new JSONArray(response);
                    for(int i=0;i<productArray.length();i++) {
                        JSONObject product=productArray.getJSONObject(i);
                        CartProduct cartProduct=new CartProduct();
                        cartProduct.setId(product.getInt("productID"));
                        cartProduct.setName(product.getString("productName"));
                        cartProduct.setPrice(product.getInt("price"));
                        cartProduct.setQuantity(cart.getInt(product.getString("productID"), 0));
                        cartProduct.setStone(product.getInt("quantity"));
                        data.add(cartProduct);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initActivity();
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
                return params;
            }
        };
        queue.add(stringRequest);
    }
    public void initActivity() {
        shoppingCartAdapter = new ShoppingCartAdapter(data, getActivity());
        cartList.setAdapter(shoppingCartAdapter);
        tv_amount.setText("HKD$"+ calAmount());
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_email.getText().toString().matches("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Email cannot be null", Toast.LENGTH_SHORT).show();
                } else if(cart.getAll().isEmpty()) {
                    Toast.makeText(getActivity().getBaseContext(), "No product inside the shopping cart", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent=new Intent(getActivity(), CreateOrder.class);
                    intent.putExtra("email", et_email.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
    public int calAmount() {
        int amount=0;
        for (CartProduct d:data) {
            amount+=d.getPrice()*d.getQuantity();
        }
        return amount;
    }
    @Override
    public void onResume() {
        super.onResume();
        if(shoppingCartAdapter !=null && cart.getAll().isEmpty()) {
            data.clear();
            et_email.setText("");
            tv_amount.setText("HKD$0");
            shoppingCartAdapter.notifyDataSetChanged();
        }
    }
}
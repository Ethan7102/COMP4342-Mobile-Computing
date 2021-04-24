package com.example.mobileshopping.ui.checkOrder;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import com.example.mobileshopping.ui.shoppingCart.CartProduct;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CheckOrderFragment extends Fragment {

    TextView txtEmail, txtCode, tv_amount;
    ListView lvOrderDetail;
    Button btnConfirm;
    String url = APIUrl.url+"/orderDetail.php";
    String email, code;
    private ArrayList<OrderItem> data;
    private RequestQueue queue;
    private CheckOrderViewModel checkOrderViewModel;
    double amount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        checkOrderViewModel =
                new ViewModelProvider(this).get(CheckOrderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        //final TextView textView = root.findViewById(R.id.text_notifications);
        checkOrderViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        queue = VolleySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        data = new ArrayList<>();
        txtEmail = root.findViewById(R.id.txtEmail);
        txtCode = root.findViewById(R.id.txtCode);
        tv_amount = root.findViewById(R.id.tv_amount);
        lvOrderDetail = root.findViewById(R.id.lvOrder);
        btnConfirm = root.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = txtEmail.getText().toString();
                code = txtCode.getText().toString();
                System.out.println(email+"  "+code);
                getData();
            }
        });

        return root;
    }

    public void getData() {
        data.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response", response);
                amount=0;
                try {
                    System.out.println(response);
                    JSONObject responseJSON = new JSONObject(response);
                    JSONArray orderDetail = responseJSON.getJSONArray("product");
                    for(int i = 0; i < orderDetail.length(); i++){
                        JSONObject product = orderDetail.getJSONObject(i);
                        int price = product.getInt("price");
                        int quantity = product.getInt("quantity");
                        OrderItem item = new OrderItem(product.getString("productName"), price, quantity);
                        amount+=price*quantity;
                        data.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initListView();
            }
        }, (VolleyError error) -> {
            error.printStackTrace();
            //tv_name.setText("That didn't work!");
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("email",email);
                params.put("code",code);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void initListView() {
        OrderItemAdapter adapter = new OrderItemAdapter(data, getActivity());
        lvOrderDetail.setAdapter(adapter);
        if (!data.isEmpty()) {
            tv_amount.setText("HKD$"+amount);
        } else {
            adapter.notifyDataSetChanged();
            tv_amount.setText("HKD$0");
        }
    }
}
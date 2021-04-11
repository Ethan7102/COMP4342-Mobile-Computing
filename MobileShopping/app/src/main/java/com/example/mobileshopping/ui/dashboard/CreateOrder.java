package com.example.mobileshopping.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.Map;

public class CreateOrder extends AppCompatActivity {
    TextView tv_email, tv_code, tv_status;
    private RequestQueue queue;
    String url="http://192.168.1.5/getCartProduct.php";
    String email;
    SharedPreferences cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        setContentView(R.layout.activity_create_order);
        Intent intent=getIntent();
        email=intent.getStringExtra("email");
        cart = getSharedPreferences("shopping_cart", MODE_PRIVATE);
        sendOrder();
    }
    public void sendOrder() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject inform = new JSONObject(response);
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
                        jsonObject.put(""+i, Integer.valueOf(entry.getKey().toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
                params.put("email", email);
                Log.i("response", email);
                params.put("idQuantity", jsonObject.toString());
                Log.i("response", jsonObject.toString());
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
package com.example.mobileshopping.ui.shoppingCart;

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
import com.example.mobileshopping.APIUrl;
import com.example.mobileshopping.R;
import com.example.mobileshopping.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateOrder extends AppCompatActivity {
    TextView tv_email, tv_code, tv_status;
    private RequestQueue queue;
    String url = APIUrl.url+"/sendOrder.php";
    String email;
    SharedPreferences cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        setContentView(R.layout.activity_create_order);
        tv_status=findViewById(R.id.tv_status);
        tv_email=findViewById(R.id.tv_email);
        tv_code=findViewById(R.id.tv_code);
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
                    Log.i("response", response.toString());
                    JSONObject inform = new JSONObject(response);
                    tv_email.setText(inform.getString("email"));
                    tv_code.setText(inform.getString("code"));
                    String status = inform.getString("status");
                    if(status.equals("success")) {
                        tv_status.setText("Success!\nThe order has been accepted,keep the email and confirmation code to check the order");
                        cart.edit().clear().apply();
                    } else {
                        tv_email.setText("");
                        tv_code.setText("Error");
                        tv_status.setText("Failed!\nSomething happened.Please try again later");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    tv_email.setText("");
                    tv_code.setText("Error");
                    tv_status.setText("Failed!, Something happened.\nPlease try again later");
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
                        jsonObject.put(entry.getKey(), entry.getValue());
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
        };
        queue.add(stringRequest);
    }
}
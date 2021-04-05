package com.example.mobileshopping.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.mobileshopping.R;
import com.example.mobileshopping.VolleySingleton;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductDetail extends AppCompatActivity implements View.OnClickListener {

    String url ="http://192.168.1.4/productDetail.php";
    private RequestQueue queue;
    String productName, productDescription, type;
    int id, price, quantity, quantityOfCart=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        TextView tv_name=findViewById(R.id.tv_name);
        TextView tv_description=findViewById(R.id.tv_description);
        TextView tv_price = findViewById(R.id.tv_price);
        TextView tv_quantityOfCart = findViewById(R.id.tv_quan);
        Button addCart = findViewById(R.id.btn_addCart);
        Button addQuantityOfCart = findViewById(R.id.btn_add);
        Button subQuantityOfCart = findViewById(R.id.btn_sub);
        addCart.setOnClickListener(this);
        addQuantityOfCart.setOnClickListener(this);
        subQuantityOfCart.setOnClickListener(this);
        Intent intent=getIntent();
        id=intent.getIntExtra("id", 0);
        tv_quantityOfCart.setText(quantityOfCart);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                response -> {
                    try {
                        productName = response.getString("type");
                        productDescription = response.getString("productDescription");
                        type = response.getString("type");
                        price = response.getInt("price");
                        quantity = response.getInt("quantity");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    tv_name.setText(productName);
                    tv_price.setText("$" + price);
                    tv_description.setText(productDescription);
                    if(quantity>0) {
                        addCart.setEnabled(true);
                        addCart.setText(R.string.add_to_cart);
                    } else {
                        addCart.setEnabled(false);
                        addCart.setText(R.string.no_stone);
                    }
                }, error -> {
                    error.printStackTrace();
                    tv_name.setText("That didn't work!");
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("id",String.valueOf(id));
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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addCart:
                SharedPreferences cart = getSharedPreferences("shopping_cart", MODE_PRIVATE);
                cart.edit()
                        .putInt(String.valueOf(id), quantityOfCart)
                        .apply();
                break;
            case R.id.btn_add:
                if(quantityOfCart<quantity) {
                    quantityOfCart++;
                }
                break;
            case R.id.btn_sub:
                if(quantityOfCart>0) {
                    quantityOfCart--;
                }
                break;
        }
    }
}
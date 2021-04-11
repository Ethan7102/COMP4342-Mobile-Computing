package com.example.mobileshopping.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mobileshopping.R;
import com.example.mobileshopping.VolleySingleton;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductDetail extends AppCompatActivity implements View.OnClickListener {

    //String url ="http://192.168.1.31/productDetail.php"; //Angus network
    //String url ="http://192.168.1.4/productDetail.php";
    String url ="http://192.168.1.11/webServer/COMP4342-Mobile-Computing/productDetail.php"; //Ethan network
    private RequestQueue queue;
    String productName, productDescription, type;
    int id, price, quantity, quantityOfCart=1;
    TextView tv_quantityOfCart, tv_name, tv_description, tv_price, tv_type;
    Button addCart, addQuantityOfCart, subQuantityOfCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        tv_name=findViewById(R.id.tv_name);
        tv_description=findViewById(R.id.tv_description);
        tv_price = findViewById(R.id.tv_price);
        tv_quantityOfCart = findViewById(R.id.tv_quan);
        tv_type = findViewById(R.id.tv_type);
        addCart = findViewById(R.id.btn_addCart);
        addQuantityOfCart = findViewById(R.id.btn_add);
        subQuantityOfCart = findViewById(R.id.btn_sub);
        addCart.setOnClickListener(this);
        addQuantityOfCart.setOnClickListener(this);
        subQuantityOfCart.setOnClickListener(this);
        Intent intent=getIntent();
        id=intent.getIntExtra("id", 0);
        Log.i("response", String.valueOf(id));
        tv_quantityOfCart.setText(String.valueOf(quantityOfCart));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response", response);
                try {
                    JSONObject product = new JSONObject(response);
                    productName = product.getString("productName");
                    productDescription = product.getString("productDescription");
                    type = product.getString("type");
                    price = product.getInt("price");
                    quantity = product.getInt("quantity");
                    tv_name.setText(productName);
                    tv_price.setText("$" + price);
                    tv_type.setText(type);
                    tv_description.setText(productDescription);
                    if(quantity>0) {
                        addCart.setEnabled(true);
                        addCart.setText(R.string.add_to_cart);
                    } else {
                        addCart.setEnabled(false);
                        addCart.setText(R.string.no_stone);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, (VolleyError error) -> {
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
                int productInCart=cart.getInt(String.valueOf(id), 0);
                cart.edit()
                        .putInt(String.valueOf(id), quantityOfCart+productInCart)
                        .apply();
                Log.i("Cart item", String.valueOf(quantityOfCart+productInCart));
                Toast.makeText(this, productName+" "+getResources().getString(R.string.add_to_cart), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_add:
                if(quantityOfCart<quantity) {
                    quantityOfCart++;
                }
                tv_quantityOfCart.setText(String.valueOf(quantityOfCart));
                break;
            case R.id.btn_sub:
                if(quantityOfCart>0) {
                    quantityOfCart--;
                }
                tv_quantityOfCart.setText(String.valueOf(quantityOfCart));
                break;
        }
    }
}
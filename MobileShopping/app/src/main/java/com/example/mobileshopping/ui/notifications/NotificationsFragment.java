package com.example.mobileshopping.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.net.Uri;
import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobileshopping.R;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class NotificationsFragment extends Fragment {

    TextView txtEmail;
    ListView lvOrder;
    MaterialButton btnConfirm;
    HttpURLConnection con;


    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        //final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        txtEmail = root.findViewById(R.id.txtEmail);
        lvOrder = root.findViewById(R.id.lvOrder);
        btnConfirm = root.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                System.out.println(email);

                String mailto = "mailto:" + email +
                        "?cc=" +
                        "&subject=" + Uri.encode("your subject") +
                        "&body=" + Uri.encode("your mail body");
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));

                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "Error to open email app", Toast.LENGTH_SHORT).show();
                }
            }
        });

        URL url = null;
        InputStream inputStream = null;
        String result = "";

        try{
            url = new URL("http://192.168.1.11/webServer/COMP4342-Mobile-Computing/orderDetail.php");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            Log.d("connectServer", "process start");

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            ExecutorService service = Executors.newSingleThreadExecutor();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        con.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            Future<?> f = service.submit(r);
            f.get(1, TimeUnit.SECONDS); //if the connection cannot completes with in 1 seconds, a TimeroutException will be throw.

            inputStream = con.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            Log.d("connectServer", "process complete");
            inputStream.close();


        }
        catch(Exception e){
            e.printStackTrace();
        }



        return root;
    }
}
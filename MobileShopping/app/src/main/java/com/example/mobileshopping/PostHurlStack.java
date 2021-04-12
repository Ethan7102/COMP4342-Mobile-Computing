package com.example.mobileshopping;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PostHurlStack extends HurlStack {
    @Override
    public HttpResponse executeRequest(Request<?> request,
                                       Map<String, String> additionalHeaders)
            throws IOException, AuthFailureError {
        if (additionalHeaders == null || Collections.emptyMap().equals(additionalHeaders)) {
            additionalHeaders = new HashMap<>();
        }
        additionalHeaders.put("Content-Type","application/x-www-form-urlencoded");
        return super.executeRequest(request, additionalHeaders);
    }

}

package com.simpl.pay.sample.zc_s2s.utils;

import android.util.Log;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public final class BaseApi {

    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static RequestBody createRequestBody(String jsonString) {
        Log.v("ZeroClickSDK", "JSON" + jsonString);
        return RequestBody.create(JSON, jsonString);
    }

    public static Request buildPOSTRequest(RequestBody body, String url) {
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }

    public static Request buildGETRequest(String url) {
        return new Request.Builder()
                .url(url)
                .build();
    }

    public static Call executeRequest(Request request, Callback callback) {
        client.newCall(request).enqueue(callback);
        return null;

    }
}
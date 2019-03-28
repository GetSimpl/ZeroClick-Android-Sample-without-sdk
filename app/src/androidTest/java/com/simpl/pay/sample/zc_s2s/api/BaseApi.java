package com.simpl.pay.sample.zc_s2s.api;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class BaseApi {

    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    static RequestBody createRequestBody(String jsonString) {
        return RequestBody.create(JSON, jsonString);
    }

    public static Request buildPOSTRequest(RequestBody body, String url, String authorizationHeader) {
        return new Request.Builder()
                .header("Authorization", authorizationHeader)
                .url(url)
                .post(body)
                .build();
    }

    public static Request buildGETRequest(String url) {
        return new Request.Builder()
                .url(url)
                .build();
    }

    public static Response executeRequest(Request request) throws IOException {
        return client.newCall(request).execute();
    }

}

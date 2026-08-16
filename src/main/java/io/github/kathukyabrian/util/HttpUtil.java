package io.github.kathukyabrian.util;


import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class HttpUtil {
    public static String post(String url, String body, Map<String, String> headerMap, MediaType mediaType) throws IOException {

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(body, mediaType);

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);

        for (Map.Entry<String, String> pair : headerMap.entrySet()) {
            requestBuilder.addHeader(pair.getKey(), pair.getValue());
        }

        Request request = requestBuilder.build();

        return getResponse(client, request);
    }

    public static String get(String url, Map<String, String> headerMap, MediaType mediaType) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder()
                .url(url);

        for (Map.Entry<String, String> pair : headerMap.entrySet()) {
            requestBuilder.addHeader(pair.getKey(), pair.getValue());
        }

        Request request = requestBuilder
                .get()
                .build();

        return getResponse(client, request);
    }

    private static String getResponse(OkHttpClient client, Request request) throws IOException {
        try (
                Response response = client.newCall(request).execute();
        ) {
            return response.body().string();
        }
    }
}

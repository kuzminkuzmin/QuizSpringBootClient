package com.dmitriikuzmin.quizspringbootclient.retrofit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class BasicAuthInterceptor implements Interceptor {
    private String token;

    public BasicAuthInterceptor(String token) {
        this.token = "Bearer_" + token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .header("Authorization", token).build();
        return chain.proceed(authenticatedRequest);
    }
}

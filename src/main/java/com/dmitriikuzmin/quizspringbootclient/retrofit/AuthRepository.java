package com.dmitriikuzmin.quizspringbootclient.retrofit;

import com.dmitriikuzmin.quizspringbootclient.dto.ResponseResult;
import com.dmitriikuzmin.quizspringbootclient.util.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

public class AuthRepository {
    private ObjectMapper objectMapper;

    private AuthService authService;

    public AuthRepository(String token) {
        objectMapper = new ObjectMapper();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(token)).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL + "participant/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper)).client(client)
                .build();
        this.authService = retrofit.create(AuthService.class);
    }

    public AuthRepository() {
        objectMapper = new ObjectMapper();
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL + "auth/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper)).client(client)
                .build();
        this.authService = retrofit.create(AuthService.class);
    }

    private <T> T getData(Response<ResponseResult<T>> execute) throws IOException {
        if (execute.code() != 200) {
            String message = objectMapper.readValue(execute.errorBody().string(),
                    new TypeReference<ResponseResult<T>>() {
                    }).getMessage();
            throw new IllegalArgumentException(message);
        }
        return execute.body().getData();
    }

    public String auth(String username, String password) throws IOException {
        Response<ResponseResult<String>> execute = authService.auth(username, password).execute();
        return getData(execute);
    }
}

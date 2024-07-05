package com.dmitriikuzmin.quizspringbootclient.retrofit;

import com.dmitriikuzmin.quizspringbootclient.dto.ResponseResult;
import com.dmitriikuzmin.quizspringbootclient.model.Admin;
import com.dmitriikuzmin.quizspringbootclient.util.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

public class AdminRepository {
    private ObjectMapper objectMapper;
    private final AdminService adminService;

    public AdminRepository(String token) {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new BasicAuthInterceptor(token)).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL + "admin/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper)).client(client)
                .build();
        this.adminService = retrofit.create(AdminService.class);
    }

    private <T> T getData(Response<ResponseResult<T>> execute) throws IOException {
        if (execute.code() != 200) {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String message = objectMapper.readValue(execute.errorBody().string(),
                    new TypeReference<ResponseResult<T>>() {
                    }).getMessage();
            throw new IllegalArgumentException(message);
        }
        return execute.body().getData();
    }

    public Admin get(long id) throws IOException {
        Response<ResponseResult<Admin>> execute = this.adminService.get(id).execute();
        return getData(execute);
    }

    public Admin getByName(String username) throws IOException {
        Response<ResponseResult<Admin>> execute = this.adminService.getByUsername(username).execute();
        return getData(execute);
    }
}

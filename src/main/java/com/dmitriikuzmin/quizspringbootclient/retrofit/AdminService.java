package com.dmitriikuzmin.quizspringbootclient.retrofit;

import com.dmitriikuzmin.quizspringbootclient.dto.ResponseResult;
import com.dmitriikuzmin.quizspringbootclient.model.Admin;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AdminService {
    @GET("{id}")
    Call<ResponseResult<Admin>> get(@Path("id") long id);

    @GET("byName/{username}")
    Call<ResponseResult<Admin>> getByUsername(@Path("username") String username);
}

package com.dmitriikuzmin.quizspringbootclient.retrofit;

import com.dmitriikuzmin.quizspringbootclient.dto.ResponseResult;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AuthService {
    @POST("{username}/{password}")
    Call<ResponseResult<String>> auth(@Path("username") String username, @Path("password") String password);
}

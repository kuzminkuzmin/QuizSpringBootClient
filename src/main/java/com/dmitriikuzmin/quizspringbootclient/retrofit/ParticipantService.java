package com.dmitriikuzmin.quizspringbootclient.retrofit;

import com.dmitriikuzmin.quizspringbootclient.dto.ResponseResult;
import com.dmitriikuzmin.quizspringbootclient.model.Participant;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ParticipantService {
    @POST(".")
    Call<ResponseResult<Participant>> post(@Body Participant participant);

    @GET("{id}")
    Call<ResponseResult<Participant>> get(@Path("id") long id);

    @GET("byName/{username}")
    Call<ResponseResult<Participant>> getByUsername(@Path("username") String username);

    @GET(".")
    Call<ResponseResult<List<Participant>>> getAll();

    @PUT(".")
    Call<ResponseResult<Participant>> update(@Body Participant participant);
}

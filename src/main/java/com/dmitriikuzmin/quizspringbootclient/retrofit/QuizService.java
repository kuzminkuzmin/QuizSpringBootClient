package com.dmitriikuzmin.quizspringbootclient.retrofit;

import com.dmitriikuzmin.quizspringbootclient.dto.ResponseResult;
import com.dmitriikuzmin.quizspringbootclient.model.Quiz;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface QuizService {
    @POST("{participantId}/")
    Call<ResponseResult<Quiz>> post(
            @Path("participantId") long participantId,
            @Query("amount") int settings,
            @Query("category") int category,
            @Query("difficulty") String difficulty);

    @GET("{id}")
    Call<ResponseResult<Quiz>> get(@Path("id") long id);

    @GET("byParticipant/{participantId}")
    Call<ResponseResult<List<Quiz>>> getByParticipant(@Path("participantId") long participantId);
}

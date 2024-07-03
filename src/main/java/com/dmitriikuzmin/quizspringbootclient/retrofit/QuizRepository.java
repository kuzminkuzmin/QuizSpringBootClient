package com.dmitriikuzmin.quizspringbootclient.retrofit;

import com.dmitriikuzmin.quizspringbootclient.dto.ResponseResult;
import com.dmitriikuzmin.quizspringbootclient.model.Quiz;
import com.dmitriikuzmin.quizspringbootclient.util.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

public class QuizRepository {
    private ObjectMapper objectMapper;
    private final QuizService quizService;

    public QuizRepository(String token) {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new BasicAuthInterceptor(token)).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL + "quiz/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper)).client(client)
                .build();
        this.quizService = retrofit.create(QuizService.class);
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

    public Quiz post(long participantId, String settings) throws IOException {
        Response<ResponseResult<Quiz>> execute = this.quizService.post(participantId, settings).execute();
        return getData(execute);
    }

    public Quiz get(long id) throws IOException {
        Response<ResponseResult<Quiz>> execute = this.quizService.get(id).execute();
        return getData(execute);
    }

    public List<Quiz> getByParticipant(long participantId) throws IOException {
        Response<ResponseResult<List<Quiz>>> execute = this.quizService.getByParticipant(participantId).execute();
        return getData(execute);
    }
}

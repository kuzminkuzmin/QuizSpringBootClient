package com.dmitriikuzmin.quizspringbootclient.retrofit;

import com.dmitriikuzmin.quizspringbootclient.dto.ResponseResult;
import com.dmitriikuzmin.quizspringbootclient.model.Participant;
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

public class ParticipantRepository {
    private ObjectMapper objectMapper;
    private final ParticipantService participantService;

    public ParticipantRepository(String token) {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new BasicAuthInterceptor(token)).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL + "participant/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper)).client(client)
                .build();
        this.participantService = retrofit.create(ParticipantService.class);
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

    public Participant post(Participant participant) throws IOException {
        Response<ResponseResult<Participant>> execute = this.participantService.post(participant).execute();
        return getData(execute);
    }

    public Participant get(long id) throws IOException {
        Response<ResponseResult<Participant>> execute = this.participantService.get(id).execute();
        return getData(execute);
    }

    public Participant getByUsername(String username) throws IOException {
        Response<ResponseResult<Participant>> execute = this.participantService.getByUsername(username).execute();
        return getData(execute);
    }

    public List<Participant> getAll() throws IOException {
        Response<ResponseResult<List<Participant>>> execute = this.participantService.getAll().execute();
        return getData(execute);
    }

    public Participant update(Participant participant) throws IOException {
        Response<ResponseResult<Participant>> execute = this.participantService.update(participant).execute();
        return getData(execute);
    }
}

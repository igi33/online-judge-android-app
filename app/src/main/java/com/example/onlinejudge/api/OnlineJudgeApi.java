package com.example.onlinejudge.api;

import com.example.onlinejudge.models.ComputerLanguage;
import com.example.onlinejudge.models.Submission;
import com.example.onlinejudge.models.Tag;
import com.example.onlinejudge.models.Task;
import com.example.onlinejudge.models.User;
import com.example.onlinejudge.models.UserCredentials;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface OnlineJudgeApi {
    @GET("task")
    Observable<ArrayList<Task>> getTasks(@QueryMap Map<String, Object> options);

    @GET("computerlanguage")
    Call<ArrayList<ComputerLanguage>> getComputerLanguages();

    @GET("submission")
    Observable<ArrayList<Submission>> getSubmissions(@QueryMap Map<String, Object> options);

    @GET("tag")
    Observable<ArrayList<Tag>> getTags();

    @POST("user/authenticate")
    Observable<User> authenticateUser(@Body UserCredentials userCredentials);
}

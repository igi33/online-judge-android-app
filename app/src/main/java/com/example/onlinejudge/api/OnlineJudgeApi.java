package com.example.onlinejudge.api;

import com.example.onlinejudge.models.ComputerLanguage;
import com.example.onlinejudge.models.Submission;
import com.example.onlinejudge.models.Task;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface OnlineJudgeApi {
    @GET("task")
    Observable<ArrayList<Task>> getTasks(@QueryMap Map<String, Object> options);

    @GET("computerlanguage")
    Call<ArrayList<ComputerLanguage>> getComputerLanguages();

    @GET("submission")
    Observable<ArrayList<Submission>> getSubmissions(@QueryMap Map<String, Object> options);
}

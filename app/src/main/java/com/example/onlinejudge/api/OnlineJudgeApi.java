package com.example.onlinejudge.api;

import com.example.onlinejudge.models.ComputerLanguage;
import com.example.onlinejudge.models.Submission;
import com.example.onlinejudge.models.SubmissionSkeleton;
import com.example.onlinejudge.models.Tag;
import com.example.onlinejudge.models.Task;
import com.example.onlinejudge.models.User;
import com.example.onlinejudge.models.UserCredentials;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface OnlineJudgeApi {
    // tasks
    @GET("task")
    Observable<ArrayList<Task>> getTasks(@QueryMap Map<String, Object> options);
    @GET("task/solvedby/{userId}")
    Observable<ArrayList<Task>> getSolvedTasksByUser(@Path("userId") int userId, @QueryMap Map<String, Object> options);
    @GET("task/{id}")
    Observable<Task> getTask(@Path("id") int id);
    @POST("task")
    Observable<Task> postTask(@Body Task task);
    @PUT("task/{id}")
    Observable<Response<Void>> putTask(@Path("id") int id, @Body Task task);
    @DELETE("task/{id}")
    Observable<Response<Void>> deleteTask(@Path("id") int id);

    // computer languages
    @GET("computerlanguage")
    Observable<ArrayList<ComputerLanguage>> getComputerLanguages();

    // submissions
    @GET("submission")
    Observable<ArrayList<Submission>> getSubmissions(@QueryMap Map<String, Object> options);
    @GET("submission/{id}")
    Observable<Submission> getSubmission(@Path("id") int id);
    @GET("submission/task/{taskId}/best")
    Observable<ArrayList<Submission>> getBestSubmissionsOfTask(@Path("taskId") int taskId, @QueryMap Map<String, Object> options);
    @POST("submission/task/{taskId}")
    Observable<Submission> postSubmission(@Path("taskId") int taskId, @Body SubmissionSkeleton submissionSkeleton);

    // tags
    @GET("tag")
    Observable<ArrayList<Tag>> getTags();
    @GET("tag/{id}")
    Observable<Tag> getTag(@Path("id") int id);

    // users
    @GET("user")
    Observable<ArrayList<User>> getUsers();
    @GET("user/{id}")
    Observable<User> getUser(@Path("id") int id);
    @POST("user/authenticate")
    Observable<User> authenticateUser(@Body UserCredentials userCredentials);
    @POST("user")
    Observable<User> postUser(@Body UserCredentials userCredentials);
    @PUT("user/{id}")
    Observable<Response<Void>> putUser(@Path("id") int id, @Body User user);
    @DELETE("user/{id}")
    Observable<Response<Void>> deleteUser(@Path("id") int id);
}

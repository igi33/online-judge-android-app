package com.example.onlinejudge.repositories;

import androidx.lifecycle.LiveData;

import com.example.onlinejudge.api.OnlineJudgeApi;
import com.example.onlinejudge.db.TaskDao;
import com.example.onlinejudge.models.ComputerLanguage;
import com.example.onlinejudge.models.Submission;
import com.example.onlinejudge.models.SubmissionSkeleton;
import com.example.onlinejudge.models.Tag;
import com.example.onlinejudge.models.Task;
import com.example.onlinejudge.models.User;
import com.example.onlinejudge.models.UserCredentials;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;

import javax.inject.Inject;

public class OnlineJudgeRepository {
    private TaskDao taskDao;
    private OnlineJudgeApi apiService;

    @Inject
    public OnlineJudgeRepository(TaskDao taskDao, OnlineJudgeApi apiService) {
        this.taskDao = taskDao;
        this.apiService = apiService;
    }

    // api tasks
    public Observable<ArrayList<Task>> getTasks(Map<String, Object> options) {
        return apiService.getTasks(options);
    }
    public Observable<Task> getTask(int id) {
        return apiService.getTask(id);
    }
    public Observable<Task> postTask(Task task) {
        return apiService.postTask(task);
    }
    public Observable<Response<Void>> putTask(int id, Task task) {
        return apiService.putTask(id, task);
    }
    public Observable<Response<Void>> deleteTask(int id) {
        return apiService.deleteTask(id);
    }

    // api submissions
    public Observable<ArrayList<Submission>> getSubmissions(Map<String, Object> options) {
        return apiService.getSubmissions(options);
    }
    public Observable<ArrayList<Submission>> getBestSubmissionsOfTask(int taskId, Map<String, Object> options) {
        return apiService.getBestSubmissionsOfTask(taskId, options);
    }
    public Observable<Submission> postSubmission(int taskId, SubmissionSkeleton submissionSkeleton) {
        return apiService.postSubmission(taskId, submissionSkeleton);
    }

    // api computer languages
    public Observable<ArrayList<ComputerLanguage>> getComputerLanguages() {
        return apiService.getComputerLanguages();
    }

    // api tags
    public Observable<ArrayList<Tag>> getTags() {
        return apiService.getTags();
    }

    // api users
    public Observable<User> authenticateUser(UserCredentials userCredentials) {
        return apiService.authenticateUser(userCredentials);
    }
    public Observable<User> postUser(UserCredentials userCredentials) {
        return apiService.postUser(userCredentials);
    }

    // room tasks
    public LiveData<List<Task>> getSavedTasks() {
        return taskDao.getAll();
    }
    public void insertSavedTask(Task task) {
        taskDao.insert(task);
    }
    public void deleteSavedTask(int taskId) {
        taskDao.delete(taskId);
    }
}

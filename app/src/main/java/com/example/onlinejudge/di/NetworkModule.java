package com.example.onlinejudge.di;

import com.example.onlinejudge.api.OnlineJudgeApi;
import com.example.onlinejudge.auth.SessionManager;
import com.example.onlinejudge.helpers.JwtTokenInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(ApplicationComponent.class)
public class NetworkModule {
    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient(JwtTokenInterceptor jwtTokenInterceptor) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(jwtTokenInterceptor)
                .build();
    }

    @Provides
    @Singleton
    public static OnlineJudgeApi provideOnlineJudgeApi(OkHttpClient httpClient) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:4000/api/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(OnlineJudgeApi.class);
    }
}

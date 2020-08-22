package com.example.onlinejudge.di;

import android.app.Application;

import androidx.room.Room;

import com.example.onlinejudge.db.OnlineJudgeDb;
import com.example.onlinejudge.db.TaskDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public class DatabaseModule {
    @Provides
    @Singleton
    public static OnlineJudgeDb provideOnlineJudgeDb(Application application) {
        return Room.databaseBuilder(application, OnlineJudgeDb.class,"ojdb")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    public static TaskDao provideTaskDao(OnlineJudgeDb ojdb) {
        return ojdb.taskDao();
    }
}

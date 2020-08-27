package com.example.onlinejudge.di;

import android.app.Application;

import com.example.onlinejudge.auth.SessionManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public class AuthModule {
    @Provides
    @Singleton
    public static SessionManager provideSessionManager(Application application) {
        return new SessionManager(application);
    }
}

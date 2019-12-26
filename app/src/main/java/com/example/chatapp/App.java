package com.example.chatapp;

import android.app.Application;
import android.util.Log;

import timber.log.Timber;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());

        } else {
            Timber.plant(new ReleaseTree());
        }

    }
    public class ReleaseTree extends Timber.Tree
    {
        @Override
        protected void log(int priority, String tag, String message, Throwable t)
        {
            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO)
            {
                return;
            }
        }
    }
}

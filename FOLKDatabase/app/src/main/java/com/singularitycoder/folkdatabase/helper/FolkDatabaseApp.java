package com.singularitycoder.folkdatabase.helper;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;

public class FolkDatabaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initializing Fresco
        Fresco.initialize(this);

        // Initisalizing Leaky Canary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}

package com.singularitycoder.folkdatabase.helper;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;

public class FolkDatabaseApp extends Application {

    private static FolkDatabaseApp instance;

    @Override
    public void onCreate() {
        super.onCreate();

        if(instance == null) {
            instance = this;
        }

        // Initializing Fresco
        Fresco.initialize(this);

        // Initisalizing Leaky Canary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    public static FolkDatabaseApp getInstance() {
        return instance;
    }

    public static boolean hasInternet() {
        return instance.isInternetConnected();
    }

    private boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}

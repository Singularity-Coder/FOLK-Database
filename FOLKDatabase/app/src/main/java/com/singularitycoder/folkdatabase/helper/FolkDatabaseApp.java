package com.singularitycoder.folkdatabase.helper;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.evernote.android.job.JobRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;

import org.acra.ACRA;
import org.acra.BuildConfig;
import org.acra.annotation.AcraCore;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.HttpSenderConfigurationBuilder;
import org.acra.config.LimiterConfigurationBuilder;
import org.acra.config.SchedulerConfigurationBuilder;
import org.acra.data.StringFormat;
import org.acra.sender.HttpSender;

@AcraCore(buildConfigClass = BuildConfig.class)
public class FolkDatabaseApp extends Application {

    private static final String TAG = "FolkDatabaseApp";

    private static FolkDatabaseApp instance;

    @Override
    public void onCreate() {
        super.onCreate();

        if(instance == null) {
            instance = this;
        }

        // Initializing Fresco
        Fresco.initialize(this);

        // Initializing Leaky Canary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // Initializing ACRA
//        CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this)
//                .setBuildConfigClass(BuildConfig.class)
//                .setReportFormat(StringFormat.JSON)
//                .setAlsoReportToAndroidFramework(true);
//        builder.getPluginConfigurationBuilder(HttpSenderConfigurationBuilder.class)
//                .setUri("https://yourdomain.com/acra/report")
//                .setHttpMethod(HttpSender.Method.POST)
//                .setBasicAuthLogin("*****")
//                .setBasicAuthPassword("*****")
//                .setEnabled(true);
//        builder.getPluginConfigurationBuilder(SchedulerConfigurationBuilder.class)
//                .setRequiresNetworkType(JobRequest.NetworkType.UNMETERED)
//                .setRequiresBatteryNotLow(true)
//                .setEnabled(true);
//        builder.getPluginConfigurationBuilder(LimiterConfigurationBuilder.class)
//                .setEnabled(true);
//        ACRA.init(this);
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

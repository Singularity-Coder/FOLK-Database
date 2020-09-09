package com.singularitycoder.folkdatabase;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;

import org.acra.BuildConfig;
import org.acra.annotation.AcraCore;

@AcraCore(buildConfigClass = BuildConfig.class)
public class BaseApplication extends Application {

    private static final String TAG = "FolkDatabaseApp";

    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        if (null == instance) instance = this;

        // Initializing Fresco
        Fresco.initialize(this);

        // Initializing Leaky Canary
        if (LeakCanary.isInAnalyzerProcess(this)) return;
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

    public static synchronized BaseApplication getInstance() {
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

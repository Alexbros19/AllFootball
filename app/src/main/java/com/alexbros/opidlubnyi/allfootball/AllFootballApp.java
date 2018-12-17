package com.alexbros.opidlubnyi.allfootball;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.alexbros.opidlubnyi.allfootball.config.ConfigManager;
import com.alexbros.opidlubnyi.allfootball.helpers.MemoryCacheCleaner;

public class AllFootballApp extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "AllFootballApp";

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        checkAppReplacingState();

//        Crashlytics kit = new Crashlytics.Builder()
//                .core(new CrashlyticsCore.Builder().disabled(false).build())
//                .build();
//        Fabric.with(this, kit, new Crashlytics());
//        FirebaseApp.initializeApp(this);
//
//        PreferenceHelper.wipeAllIfNeeded(this);

        ConfigManager configManager = ConfigManager.getInstance();
        configManager.init(this);
        registerActivityLifecycleCallbacks(this);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationHelper.createNotificationChannels(getApplicationContext());
//        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        MemoryCacheCleaner.clean();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void checkAppReplacingState() {
        if (getResources() == null) {
            Log.w(TAG, "app is replacing... kill");
//            Process.killProcess(Process.myPid());
            System.exit(0);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
//        ModelData.getInstance().wasAtLeastOneActivityResumed.set(true);
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}

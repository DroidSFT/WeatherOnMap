package ua.droidsft.weatheronmap;

import android.app.Application;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;

/**
 * Application class with JobManager.
 * Created by Vlad on 19.05.2016.
 */
public class App extends Application {
    private static App sApp;
    private JobManager mJobManager;

    public App() {
        sApp = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        configJobManager();
    }

    private void configJobManager() {
        Configuration config = new Configuration.Builder(this)
                .minConsumerCount(1)
                .maxConsumerCount(3)
                .consumerKeepAlive(30)
                .build();

        mJobManager = new JobManager(this, config);
    }

    public static App getApp() {
        return sApp;
    }

    public JobManager getJobManager() {
        return mJobManager;
    }
}

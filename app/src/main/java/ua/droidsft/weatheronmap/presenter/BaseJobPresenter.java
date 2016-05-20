package ua.droidsft.weatheronmap.presenter;

import com.path.android.jobqueue.JobManager;

import ua.droidsft.weatheronmap.App;

/**
 * Base presenter with JobManager.
 * Created by Vlad on 19.05.2016.
 */
abstract class BaseJobPresenter {
    final JobManager mJobManager;

    BaseJobPresenter() {
        mJobManager = App.getApp().getJobManager();
    }
}

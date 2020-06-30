package com.kirsh.pythonanywhere.work;

import android.content.Context;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class FlowManager {
    private WorkManager mWorkManager;
    private Context mContext;
    private static FlowManager mInstance = null;

    private FlowManager(Context context){
        mContext = context;
        mWorkManager = WorkManager.getInstance(context);
        mInstance = this;
    }

    public static FlowManager getInstance(Context context) {
        if (mInstance == null){
            mInstance = new FlowManager(context);
        }
        return mInstance;
    }

    public void reloadServerUserInfo(){
        WorkRequest request = new OneTimeWorkRequest.Builder(GetUserDataWorker.class).build();
        mWorkManager.enqueue(request);
        // request user info from worker
        // add loading-image till received
        // init mUser
    }

    public void getNewToken(){
        WorkRequest request = new OneTimeWorkRequest.Builder(GetTokenWorker.class).build();
        mWorkManager.enqueue(request);
    }

}

package com.lscarp4.lscarpl4assessments;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.lscarp4.lscarpl4assessments.StudentFiles.Splash_Screen;
import com.lscarp4.lscarpl4assessments.StudentFiles.Test_Main;

/**
 * Service to handle callbacks from the JobScheduler. Requests scheduled with the JobScheduler
 * ultimately land on this service's "onStartJob" method.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkSchedulerService extends JobService implements
        ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = NetworkSchedulerService.class.getSimpleName();

    private ConnectivityReceiver mConnectivityReceiver;
    Test_Main test_main;
    String getOfflineValue;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "Service created");
        mConnectivityReceiver = new ConnectivityReceiver(this);
        test_main = new Test_Main();
        getOfflineValue = Splash_Screen.sh.getString("OfflineValue", null);
    }
    /**
     * When the app's MainActivity is created, it starts this service. This is so that the
     * activity and this service can communicate back and forth. See "setUiCallback()"
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        return START_STICKY;
    }


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(TAG, "onStartJob" + mConnectivityReceiver);
        registerReceiver(mConnectivityReceiver, new IntentFilter(Constants.CONNECTIVITY_ACTION));
        Log.e("REGISTER RECEIVER", ">>>>>>>>>>>.." + mConnectivityReceiver);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e(TAG, "onStopJob" +mConnectivityReceiver);
        try {
            unregisterReceiver(mConnectivityReceiver);
            Log.e("UNREGISTER RECEIVER", ">>>>>>>>>>>>>" + mConnectivityReceiver);

        } catch (IllegalArgumentException ill) {
            ill.printStackTrace();
        }


        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Service destroyed");
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if (!MyApplication.isInterestingActivityVisible()) {
            String message = isConnected ? "Good! Connected to Internet" : "Sorry! Not connected to internet";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

            if(message.contains("Good! Connected to Internet")){

                Log.e("TestSch TRUE CASE", ">>>>>>>>>>>>>>>" + isConnected);
                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();

                test_main.callFromService(getApplicationContext());


            }else if(message.contains("Sorry! Not connected to internet")){

                Log.e("TestSch FALSE CASE", ">>>>>>>>>>>>>>>" + isConnected);
                Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_LONG).show();
            }

            Log.e("NETWORK MESSAGE", ">>>>>>>>>>>>>>>>>>>>>>" + message);
        }
    }
}

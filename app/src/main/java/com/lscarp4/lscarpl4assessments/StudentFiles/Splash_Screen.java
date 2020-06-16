package com.lscarp4.lscarpl4assessments.StudentFiles;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.lscarp4.lscarpl4assessments.AssessorFiles.AssessorDashboard;
import com.lscarp4.lscarpl4assessments.R;

public class Splash_Screen extends Activity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    static final int LOCATION_REQUEST = 1;

    private Handler handler;
    private long startTime, currentTime, finishedTime = 0L;
    private int duration = 20000 / 4;// 1 character is equal to 1 second. if want to
    // reduce. can use as divide
    // by 2,4,8
    private int endTime = 0;
    public static String str_login_test,ass_login_test;

    public static SharedPreferences sh;
    public static SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        sh = getSharedPreferences("myprefe", 0);
        editor = sh.edit();

        // check here if user is login or not
        str_login_test = sh.getString("loginTest", null);
        ass_login_test = sh.getString("loginTestAssessor", null);

        Log.e("LOGIN TEST", ">>>>" + str_login_test + ass_login_test);

      //  nextactivity(1);

        if (!runtime_permissions())
            location_pemission();

    }


    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Alert boz to show user what permissions has to be granted
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("App Permissions");
            alertDialogBuilder.setMessage(R.string.ALLinone);
            alertDialogBuilder.setCancelable(false);
            //If user allow ask permission
            alertDialogBuilder.setPositiveButton("Allow",
                    new DialogInterface.OnClickListener() {

                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                    android.Manifest.permission.INTERNET,
                                    android.Manifest.permission.ACCESS_WIFI_STATE,
                                    android.Manifest.permission.ACCESS_NETWORK_STATE,
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION,}, REQUEST_ID_MULTIPLE_PERMISSIONS);
                        }
                    });

            alertDialogBuilder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //If user deny then close app
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


            return true;
        }
        return false;
    }




    public void nextactivity(final int requestCode) {

        if (requestCode == LOCATION_REQUEST) {
            handler = new Handler();
            startTime = Long.valueOf(System.currentTimeMillis());
            currentTime = startTime;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    currentTime = Long.valueOf(System.currentTimeMillis());
                    finishedTime = Long.valueOf(currentTime)
                            - Long.valueOf(startTime);

                    if (finishedTime >= duration + 30) {
                        //
                    } else {
                        endTime = (int) (finishedTime / 250);// divide this by

                        handler.postDelayed(this, 10);
                    }
                }
            }, 10);


//            flipit(image);
            /****** Create Thread that will sleep for 5 seconds *************/
            Thread background = new Thread() {
                public void run() {

                    try {
                        // Thread will sleep for 5 seconds
                        sleep(5 * 1000);

                        if(str_login_test == null && ass_login_test == null){
                            Log.e("BOTH VALUES NULL", ">>>>>>>>>>" + str_login_test + ass_login_test);
                            Intent i = new Intent(getApplicationContext(), Login.class);
                            startActivityForResult(i, requestCode);
                        }else  if ((str_login_test != null && !str_login_test.toString().trim().equals(""))) {

                            Log.e("STDNT TRUE", ">>>>>>>>>>>>>." + str_login_test + ass_login_test);
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivityForResult(i, requestCode);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else  if ((ass_login_test != null && !ass_login_test.toString().trim().equals(""))) {

                            Log.e("ASS TRUE", ">>>>>>>>>>>>>." + ass_login_test + str_login_test);

                            Intent i = new Intent(getApplicationContext(), AssessorDashboard.class);
                            startActivityForResult(i, requestCode);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                        //Remove activity
                        finish();

                    } catch (Exception e) {

                    }
                }
            };

            // start thread
            background.start();


        }

    }


    public void location_pemission() {

        final String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (locationProviders == null || locationProviders.equals("")) {
            //Alert dialog box to request location from user
            new AlertDialog.Builder(Splash_Screen.this)
                    .setTitle("Use Location?")
                    .setMessage("This app wants to enable your GPS for location.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                            nextactivity(1);
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();

        } else {
            nextactivity(1);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                location_pemission();

            } else

            {
                runtime_permissions();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}

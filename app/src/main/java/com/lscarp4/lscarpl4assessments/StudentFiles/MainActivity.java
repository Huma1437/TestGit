package com.lscarp4.lscarpl4assessments.StudentFiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lscarp4.lscarpl4assessments.R;

public class MainActivity extends Activity {

    TextView logout,enrolnum,stdname;
    ImageView profile,reading,exam,contact;

    AlertDialog alertDialog = null;
    NetworkChangeReceiver br;
    String batchName,jobRole,tb_nsdcID,enrol_num,std_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        br = new NetworkChangeReceiver();

        if (haveNetworkConnection()) {

        } else {
            Toast.makeText(MainActivity.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();
        }

        logout = (TextView)findViewById(R.id.logout);
        profile = (ImageView)findViewById(R.id.profile);
        reading = (ImageView)findViewById(R.id.reading);
        exam = (ImageView)findViewById(R.id.exam);
        contact = (ImageView)findViewById(R.id.contact);
        enrolnum = (TextView) findViewById(R.id.enrolnum);
        stdname = (TextView) findViewById(R.id.stdname);

        batchName = Splash_Screen.sh.getString("tb_name", null);
        jobRole = Splash_Screen.sh.getString("trade_title", null);
        tb_nsdcID = Splash_Screen.sh.getString("tb_nsdc_id", null);
        enrol_num = Splash_Screen.sh.getString("SDMS_enrolment_number", null);
        std_name = Splash_Screen.sh.getString("get_stdname", null);

        enrolnum.setText("SDMS ENROLLMENT NO. : "  + enrol_num);
        stdname.setText("STUDENT NAME : " + std_name);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "You have successfully logout",
                        Toast.LENGTH_LONG).show();
                Splash_Screen.editor.remove("loginTest");
                Splash_Screen.editor.commit();

                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Profile.class);
                startActivity(intent);

            }
        });

        reading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ReadingMaterial.class);
                startActivity(intent);

            }
        });


        exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Main_Exam.class);
                startActivity(intent);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Contact.class);
                startActivity(intent);
            }
        });

    }


    public void dialogBoxForInternet() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("No Internet Connection.");
        alertDialogBuilder
                .setMessage("Go to Settings to enable Internet Connectivity")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivityForResult(
                                        new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (haveNetworkConnection()) {
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }

            } else {

                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }

                dialogBoxForInternet();
            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
}

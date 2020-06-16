package com.lscarp4.lscarpl4assessments.AssessorFiles;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lscarp4.lscarpl4assessments.R;
import com.lscarp4.lscarpl4assessments.StudentFiles.Splash_Screen;

public class Assessor_Contact extends Activity {

    AlertDialog alertDialog = null;
    NetworkChangeReceiver br;
    TextView logout,header;
    Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessor_contact);
        br = new NetworkChangeReceiver();

        if (haveNetworkConnection()) {

        } else {
            Toast.makeText(Assessor_Contact.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();
        }

        header = (TextView)findViewById(R.id.header);
        header.setText("Contact Details");

        back = (Button) findViewById(R.id.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Assessor_Contact.this,AssessorDashboard.class);
                startActivity(intent);
            }
        });

        logout = (TextView)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "You have successfully logout",
                        Toast.LENGTH_LONG).show();
                Splash_Screen.editor.remove("loginTestAssessor");
                Splash_Screen.editor.commit();

                Intent intent = new Intent(Assessor_Contact.this, Assessor_Login.class);
                startActivity(intent);
            }
        });


    }

    public void dialogBoxForInternet() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Assessor_Contact.this);
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
        Intent intent = new Intent(Assessor_Contact.this,AssessorDashboard.class);
        startActivity(intent);

    }
}

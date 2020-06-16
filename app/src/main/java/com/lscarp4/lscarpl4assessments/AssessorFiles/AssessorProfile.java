package com.lscarp4.lscarpl4assessments.AssessorFiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lscarp4.lscarpl4assessments.R;
import com.lscarp4.lscarpl4assessments.StudentFiles.Splash_Screen;

public class AssessorProfile extends Activity {

    AlertDialog alertDialog = null;
    NetworkChangeReceiver br;
    TextView logout,header;
    Button back;
    TextView t_code,t_fname,t_lname,t_gender,t_a1,t_a2,t_city,t_pin,t_state,t_dist,t_phone,t_mob,t_mob2,t_email,t_aemail,t_adhar,t_qua,t_exp,t_resume;
    String username,pwd;
    ImageView photo;
    String s_code,s_fname,s_lname,s_gender,s_a1,s_a2,s_city,s_pin,s_state,s_dist,s_phone,s_mob,s_mob2,s_email,s_aemail,
            s_pic,s_adhar,s_qua,s_exp,s_resume,ass_pic_name,ass_resume_name;

    String ass_photo_link,ass_resume_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessor_profile);

        br = new NetworkChangeReceiver();

        header = (TextView)findViewById(R.id.header);
        header.setText("Assessor Profile");

        username = Splash_Screen.sh.getString("Ass_uname", null);
        pwd = Splash_Screen.sh.getString("Ass_pwd", null);

        t_code = (TextView) findViewById(R.id.code);
        t_fname = (TextView) findViewById(R.id.fname);
        t_lname = (TextView) findViewById(R.id.lname);
        t_gender = (TextView) findViewById(R.id.gender);
        t_a1 = (TextView) findViewById(R.id.a1);
        t_a2 = (TextView) findViewById(R.id.a2);
        t_city = (TextView) findViewById(R.id.city);
        t_pin = (TextView) findViewById(R.id.pin);
        t_state = (TextView) findViewById(R.id.state);
        t_dist = (TextView) findViewById(R.id.dist);
        t_phone = (TextView) findViewById(R.id.phone);
        t_mob = (TextView) findViewById(R.id.mobile);
        t_mob2 = (TextView) findViewById(R.id.mobile2);
        t_email = (TextView) findViewById(R.id.email);
        t_aemail = (TextView) findViewById(R.id.a_email);
        photo = (ImageView) findViewById(R.id.photo);
        t_adhar = (TextView) findViewById(R.id.aadhar);
        t_qua = (TextView) findViewById(R.id.qua);
        t_exp = (TextView) findViewById(R.id.exp);
        t_resume = (TextView) findViewById(R.id.resume);

        s_code = Splash_Screen.sh.getString("s_code", null);
        s_fname = Splash_Screen.sh.getString("s_fname", null);
        s_lname = Splash_Screen.sh.getString("s_lname", null);
        s_gender = Splash_Screen.sh.getString("s_gender", null);
        s_a1 = Splash_Screen.sh.getString("s_a1", null);
        s_a2 = Splash_Screen.sh.getString("s_a2", null);
        s_city = Splash_Screen.sh.getString("s_city", null);
        s_pin = Splash_Screen.sh.getString("s_pin", null);
        s_state = Splash_Screen.sh.getString("s_state", null);
        s_dist = Splash_Screen.sh.getString("s_dist", null);
        s_phone = Splash_Screen.sh.getString("s_phone", null);
        s_mob = Splash_Screen.sh.getString("s_mob", null);
        s_mob2 = Splash_Screen.sh.getString("s_mob2", null);
        s_email = Splash_Screen.sh.getString("s_email", null);
        s_aemail = Splash_Screen.sh.getString("s_aemail", null);
        s_pic = Splash_Screen.sh.getString("s_pic", null);
        s_adhar = Splash_Screen.sh.getString("s_adhar", null);
        s_qua = Splash_Screen.sh.getString("s_qua", null);
        s_exp = Splash_Screen.sh.getString("s_exp", null);
        s_resume = Splash_Screen.sh.getString("s_resume", null);
        ass_pic_name = Splash_Screen.sh.getString("ass_photo", null);
        ass_resume_name = Splash_Screen.sh.getString("ass_resume", null);

        Log.e("Ass pic&res profile", ">>>>>>>>>>>>" + ass_pic_name + " " + ass_resume_name);

        ass_photo_link = s_pic+"/"+ass_pic_name;
        ass_resume_link = s_resume+"/"+ass_resume_name;

        Log.e("ASS_PHOTO_LINK", ">>>>>>>>>>>>>" + ass_photo_link);
        Log.e("ASS_RESUME_LINK", ">>>>>>>>>>>>>" + ass_resume_link);


        if(s_code.equals("null")){
            t_code.setText("");
        }else{
            t_code.setText(s_code);
        }

        if(s_fname.equals("null")){
            t_fname.setText("");
        }else{
            t_fname.setText(s_fname);
        }

        if(s_lname.equals("null")){
            t_lname.setText("");
        }else{
            t_lname.setText(s_lname);
        }

        if(s_gender.equals("null")){
            t_gender.setText("");
        }else{
            t_gender.setText(s_gender);
        }

        if(s_a1.equals("null")){
            t_a1.setText("");
        }else{
            t_a1.setText(s_a1);
        }

        if(s_a2.equals("null")){
            t_a2.setText("");
        }else{
            t_a2.setText(s_a2);
        }

        if(s_city.equals("null")){
            t_city.setText("");
        }else{
            t_city.setText(s_city);
        }

        if(s_pin.equals("null")){
            t_pin.setText("");
        }else{
            t_pin.setText(s_pin);
        }

        if(s_state.equals("null")){
            t_state.setText("");
        }else{
            t_state.setText(s_state);
        }

        if(s_dist.equals("null")){
            t_dist.setText("");
        }else{
            t_dist.setText(s_dist);
        }

        if(s_phone.equals("null")){
            t_phone.setText("");
        }else{
            t_phone.setText(s_phone);
        }

        if(s_mob.equals("null")){
            t_mob.setText("");
        }else{
            t_mob.setText(s_mob);
        }

        if(s_mob2.equals("null")){
            t_mob2.setText("");
        }else{
            t_mob2.setText(s_mob2);
        }

        if(s_email.equals("null")){
            t_email.setText("");
        }else{
            t_email.setText(s_email);
        }

        if(s_aemail.equals("null")){
            t_aemail.setText("");
        }else{
            t_aemail.setText(s_aemail);
        }

        if(s_adhar.equals("null")){
            t_adhar.setText("");
        }else{
            t_adhar.setText(s_adhar);
        }

        if(s_qua.equals("null")){
            t_qua.setText("");
        }else{
            t_qua.setText(s_qua);
        }


        if(s_exp.equals("null")){
            t_exp.setText("");
        }else{
            t_exp.setText(s_exp);
        }


        if(ass_resume_link.equals("null")){
            t_resume.setText("");
        }else{
            t_resume.setText(ass_resume_link);
        }

        if(ass_pic_name.equals("null")){
            photo.setImageResource(R.drawable.no_image);
        }

        if(!ass_pic_name.equals("null")){
            Glide.with(AssessorProfile.this).load(ass_photo_link).into(photo);

        }


        t_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = ass_resume_link;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });


        back = (Button) findViewById(R.id.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AssessorProfile.this,AssessorDashboard.class);
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

                Intent intent = new Intent(AssessorProfile.this, Assessor_Login.class);
                startActivity(intent);
            }
        });


        if (haveNetworkConnection()) {

        } else {
            Toast.makeText(AssessorProfile.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();
        }



    }

    public void dialogBoxForInternet() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AssessorProfile.this);
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
      Intent intent = new Intent(AssessorProfile.this,AssessorDashboard.class);
      startActivity(intent);

    }
}

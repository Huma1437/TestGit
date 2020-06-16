package com.lscarp4.lscarpl4assessments.StudentFiles;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lscarp4.lscarpl4assessments.R;

public class Profile extends Activity {

    TextView header;
    ImageView desc,logout;
    Button back;
    String anum,stdname,dob,address,city,dist,state,pincode,mob,email;
    TextView ed_anum,ed_stdname,ed_dob,ed_address,ed_city,ed_dist,ed_state,ed_pincode,ed_mob,ed_email;
    Dialog dialog;
    String b_ID,b_name,j_role,enrol_num;
    TextView s_name,e_num,batch_id,bname,jobRole;
    TextView tname,tmob,temail,tadhar,tdob,taddress,tstate,tcity,tdist,tpin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        header = (TextView)findViewById(R.id.header);
        back = (Button) findViewById(R.id.backbtn);
        desc = (ImageView) findViewById(R.id.desc);

        anum = Splash_Screen.sh.getString("aadhaarNum", null);
        stdname = Splash_Screen.sh.getString("get_stdname",null);
        dob = Splash_Screen.sh.getString("dob",null);
        address = Splash_Screen.sh.getString("address",null);
        city = Splash_Screen.sh.getString("city",null);
        dist = Splash_Screen.sh.getString("dist_id",null);
        state = Splash_Screen.sh.getString("state_id",null);
        pincode = Splash_Screen.sh.getString("pincode",null);
        mob = Splash_Screen.sh.getString("StdMobile", null);
        email = Splash_Screen.sh.getString("StdEmail", null);

        b_name = Splash_Screen.sh.getString("tb_name", null);
        j_role = Splash_Screen.sh.getString("trade_title", null);
        b_ID = Splash_Screen.sh.getString("tb_nsdc_id", null);
        enrol_num = Splash_Screen.sh.getString("SDMS_enrolment_number", null);

        Log.e("DESC DETAILS", "????????????????" + b_name + j_role + " " + b_ID  );


        ed_anum = (TextView)findViewById(R.id.anum);
        ed_stdname = (TextView) findViewById(R.id.stdname);
        ed_dob = (TextView)findViewById(R.id.dob);
        ed_address = (TextView) findViewById(R.id.address);
        ed_city = (TextView) findViewById(R.id.city);
        ed_dist = (TextView) findViewById(R.id.dist);
        ed_state = (TextView) findViewById(R.id.state);
        ed_pincode = (TextView) findViewById(R.id.pin);
        ed_mob = (TextView) findViewById(R.id.mob);
        ed_email = (TextView) findViewById(R.id.email);

        tname = (TextView)findViewById(R.id.tsn);
        tmob = (TextView)findViewById(R.id.tsm);
        temail = (TextView)findViewById(R.id.tse);
        tadhar = (TextView)findViewById(R.id.tadhar);
        tdob = (TextView)findViewById(R.id.tdob);
        taddress = (TextView)findViewById(R.id.tadd);
        tstate = (TextView)findViewById(R.id.tstate);
        tcity = (TextView)findViewById(R.id.tcity);
        tdist = (TextView)findViewById(R.id.tdist);
        tpin = (TextView)findViewById(R.id.tpin);

        Log.e(" SP VALUES", "ADHAR NUM" + anum);
        Log.e(" SP VALUES", "stUDENT NAME" + stdname);
        Log.e(" SP VALUES", "DOB" + dob);
        Log.e(" SP VALUES", "ADDRESSS" + address);
        Log.e(" SP VALUES", "CITY" + city);
        Log.e(" SP VALUES", "DIST" + dist);
        Log.e(" SP VALUES", "STATE" + state);
        Log.e(" SP VALUES", "PINCODE" + pincode);
        Log.e(" SP VALUES", "MOBILE" + mob);
        Log.e(" SP VALUES", "EMAIL" + email);


        if(!anum.isEmpty()){
            ed_anum.setText(anum);
            tadhar.setVisibility(View.VISIBLE);
        }
        if(anum.equals("0") || anum.equals("null") || anum.equals("")) {
            ed_anum.setText("");
            tadhar.setVisibility(View.GONE);

        }

        if(!stdname.isEmpty()){
            ed_stdname.setText(stdname);
            tname.setVisibility(View.VISIBLE);
        }
        if(stdname.equals("null") || stdname.equals("")){
            ed_stdname.setText("");
            tname.setVisibility(View.GONE);

        }

        if(!dob.isEmpty() ){
            ed_dob.setText(dob);
            tdob.setVisibility(View.VISIBLE);
        }
        if(dob.equals("0") || dob.equals("") || dob.matches("0")){
            ed_dob.setText("");
            tdob.setVisibility(View.GONE);

        }

        if(!address.isEmpty()){
            ed_address.setText(address);
            taddress.setVisibility(View.VISIBLE);
        }
        if(address.equals("") || address.equals("null")){
            ed_address.setText("");
            taddress.setVisibility(View.GONE);

        }

        if(!city.isEmpty()){
            ed_city.setText(city);
            tcity.setVisibility(View.VISIBLE);
        }
        if(city.equals("0") || city.equals("null") || city.equals("")){
            ed_city.setText("");
            tcity.setVisibility(View.GONE);

        }

        if(!dist.isEmpty()){
            ed_dist.setText(dist);
            tdist.setVisibility(View.VISIBLE);
        }
        if(dist.equals("0") || dist.equals("") || dist.equals("null")){
            ed_dist.setText("");
            tdist.setVisibility(View.GONE);

        }

        if(!state.isEmpty()){
            ed_state.setText(state);
            tstate.setVisibility(View.VISIBLE);
        }
        if(state.equals("0") || state.equals("") || state.equals("null")){
            ed_state.setText("");
            tstate.setVisibility(View.GONE);

        }

        if(!pincode.isEmpty()){
            ed_pincode.setText(pincode);
            tpin.setVisibility(View.VISIBLE);
        }
        if(pincode.equals("0") || pincode.equals(null) || pincode.equals("null")){
            ed_pincode.setText("");
            tpin.setVisibility(View.GONE);

        }

        if(!mob.isEmpty()){
            ed_mob.setText(mob);
            tmob.setVisibility(View.VISIBLE);
        }
        if(mob.equals("null")){
            ed_mob.setText("");
            tmob.setVisibility(View.GONE);
        }

        if(mob.equals("0")){
            ed_mob.setText("");
            tmob.setVisibility(View.GONE);
        }

        if(!email.isEmpty()){
            ed_email.setText(email);
            temail.setVisibility(View.VISIBLE);
        }
        if(email.equals("null")){
            ed_email.setText("");
            temail.setVisibility(View.GONE);

        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Profile.this,MainActivity.class);
                startActivity(intent);
            }
        });

        header.setText("Student Profile");

        logout = (ImageView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "You have successfully logout",
                        Toast.LENGTH_LONG).show();
                Splash_Screen.editor.remove("loginTest");
                Splash_Screen.editor.commit();

                Intent intent = new Intent(Profile.this,Login.class);
                startActivity(intent);
            }
        });

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(Profile.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialogue_desc);
                dialog.show();

                 s_name = (TextView) dialog .findViewById(R.id.stdname);
                 e_num = (TextView) dialog.findViewById(R.id.e_num);
                 batch_id = (TextView) dialog .findViewById(R.id.batchId);
                 bname = (TextView) dialog.findViewById(R.id.batchN);
                 jobRole = (TextView) dialog.findViewById(R.id.jobrole);

                if(b_ID.equals("0") || b_ID.equals("null") || b_ID.equals("")){
                    batch_id.setText("");
                }

                if(j_role.equals("0") || j_role.equals("null") || j_role.equals("")){
                    jobRole.setText("");
                }

                if(b_name.equals("0") || b_name.equals("null") || b_name.equals("")){
                    bname.setText("");
                }

                s_name.setText(stdname);
                e_num.setText(enrol_num);
                batch_id.setText(b_ID);
                bname.setText(b_name);
                jobRole.setText(j_role);

                Button close = (Button)dialog.findViewById(R.id.close);


                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }
}

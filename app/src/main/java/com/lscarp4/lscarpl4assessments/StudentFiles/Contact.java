package com.lscarp4.lscarpl4assessments.StudentFiles;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lscarp4.lscarpl4assessments.R;

public class Contact extends Activity {

    TextView header;
    ImageView logout,desc;
    Button back;
    Dialog dialog;
    String stdname,b_ID,b_name,j_role,enrol_num;
    TextView s_name,e_num,batch_id,bname,jobRole;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        header = (TextView)findViewById(R.id.header);

        back = (Button) findViewById(R.id.backbtn);
        desc = (ImageView) findViewById(R.id.desc);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Contact.this,MainActivity.class);
                startActivity(intent);
            }
        });

        header.setText("Contact");

        stdname = Splash_Screen.sh.getString("get_stdname",null);
        b_name = Splash_Screen.sh.getString("tb_name", null);
        j_role = Splash_Screen.sh.getString("trade_title", null);
        b_ID = Splash_Screen.sh.getString("tb_nsdc_id", null);
        enrol_num = Splash_Screen.sh.getString("SDMS_enrolment_number", null);

        logout = (ImageView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "You have successfully logout",
                        Toast.LENGTH_LONG).show();
                Splash_Screen.editor.remove("loginTest");
                Splash_Screen.editor.commit();

                Intent intent = new Intent(Contact.this,Login.class);
                startActivity(intent);
            }
        });

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(Contact.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialogue_desc);
                dialog.show();

                s_name = (TextView) dialog .findViewById(R.id.stdname);
                e_num = (TextView) dialog.findViewById(R.id.e_num);
                batch_id = (TextView) dialog .findViewById(R.id.batchId);
                bname = (TextView) dialog.findViewById(R.id.batchN);
                jobRole = (TextView) dialog.findViewById(R.id.jobrole);

                if(b_ID.equals("null")){
                    batch_id.setText("");
                }

                if(j_role.equals("null")){
                    jobRole.setText("");
                }

                if(b_name.equals("null")){
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

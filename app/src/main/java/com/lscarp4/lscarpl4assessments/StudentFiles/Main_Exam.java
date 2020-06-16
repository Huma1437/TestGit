package com.lscarp4.lscarpl4assessments.StudentFiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lscarp4.lscarpl4assessments.CloseDialogueTimerTask;
import com.lscarp4.lscarpl4assessments.Database.DBHelper;
import com.lscarp4.lscarpl4assessments.ItemClickListener;
import com.lscarp4.lscarpl4assessments.NetworkSchedulerService;
import com.lscarp4.lscarpl4assessments.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;

public class Main_Exam extends Activity implements ItemClickListener {

    TextView header,mEmptyView;
    ImageView logout,desc;
    Button back;
    ArrayList<MexamsPojo> eId;
    MainExamAdapter adapter;
    RecyclerView rv;
    String get_examID,get_stdID,get_stdImage,get_assImage,get_sscID,get_tradeID,get_tbID,get_snap;
    final static String get_exam_status = "http://lscmis.com/arpl4/student/student_api/check_student_exam_status_api";
    AlertDialog alertDialog = null;
    NetworkChangeReceiver br;
    private static final String API_KEY = "YIA0Uub5QLhH9hxXRL1A6tXDwFWzbaUL";
    String s_stdid,s_estatus,s_examName,s_esdate,s_eedate,s_eresponse,s_duration,s_enrolRequired,s_enrolment,std_feedback,feedback_status;
    private ProgressDialog pDialog;
    Dialog dialog;
    String stdname,b_ID,b_name,j_role,enrol_num,get_DBstdID;
    TextView s_name,e_num,batch_id,bname,jobRole;
    DBHelper mydb;
    ArrayList<String> Image_Path;
    ArrayList<String> new_List;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_exam);

        br = new NetworkChangeReceiver();
        mydb = new DBHelper(Main_Exam.this);
        mEmptyView = (TextView) findViewById(R.id.emptyElement);


        header = (TextView)findViewById(R.id.header);
        desc = (ImageView) findViewById(R.id.desc);
        back = (Button) findViewById(R.id.backbtn);
        Image_Path = new ArrayList<>();
        new_List = new ArrayList<>();

        get_examID = Splash_Screen.sh.getString("exam_id", null);
        get_stdID = Splash_Screen.sh.getString("student_id", null);
        get_stdImage = Splash_Screen.sh.getString("student_image", null);
        get_assImage = Splash_Screen.sh.getString("assessor_image", null);
        get_sscID = Splash_Screen.sh.getString("ssc_id", null);
        get_tradeID = Splash_Screen.sh.getString("trade_id", null);
        get_tbID = Splash_Screen.sh.getString("tb_id", null);
        get_snap = Splash_Screen.sh.getString("Snap", null);

        stdname = Splash_Screen.sh.getString("get_stdname",null);
        b_name = Splash_Screen.sh.getString("tb_name", null);
        j_role = Splash_Screen.sh.getString("trade_title", null);
        b_ID = Splash_Screen.sh.getString("tb_nsdc_id", null);
        enrol_num = Splash_Screen.sh.getString("SDMS_enrolment_number", null);

        get_DBstdID = Splash_Screen.sh.getString("student_id", null);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Main_Exam.this,MainActivity.class);
                startActivity(intent);
            }
        });

        header.setText("Main Exam");

        logout = (ImageView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "You have successfully logout",
                        Toast.LENGTH_LONG).show();
                Splash_Screen.editor.remove("loginTest");
                Splash_Screen.editor.commit();

                Intent intent = new Intent(Main_Exam.this,Login.class);
                startActivity(intent);
            }
        });

        Log.e("STUDENT ID", ">>>>>>>>>>>" + get_stdID);

            desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(Main_Exam.this);
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


        rv = (RecyclerView) findViewById(R.id.recyclerView);
        // Set layout manager to position the items
        rv.setLayoutManager(new LinearLayoutManager(this));

        rv.setItemAnimator(new DefaultItemAnimator());


        if (haveNetworkConnection()) {

            new SendPostRequest().execute();

        } else {

            scheduleJob();
            Toast.makeText(Main_Exam.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();

        }

        database();
    }

    @Override
    public void onClick(View view, int position) {

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleJob() {
        JobInfo myJob = new JobInfo.Builder(0, new ComponentName(this, NetworkSchedulerService.class))
                .setRequiresCharging(false)
                .setMinimumLatency(1000)
                .setOverrideDeadline(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(myJob);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start service and provide it a way to communicate with this class.
        Intent startServiceIntent = new Intent(this, NetworkSchedulerService.class);
        startService(startServiceIntent);
    }

    @Override
    protected void onStop() {
        // A service can be "started" and/or "bound". In this case, it's "started" by this Activity
        // and "bound" to the JobScheduler (also called "Scheduled" by the JobScheduler). This call
        // to stopService() won't prevent scheduled jobs to be processed. However, failing
        // to call stopService() would keep it alive indefinitely.
        stopService(new Intent(this, NetworkSchedulerService.class));
        super.onStop();
    }


    //get the data from the database
    private void database() {
        mydb = new DBHelper(this);
        Cursor data = mydb.getAnswerData(mydb);

        if(data.getCount() > 0 && data.moveToFirst())
            do{

                String apikey = data.getString(1);
                String exam_id = data.getString(2);
                String std_id = data.getString(3);
                String std_name = data.getString(4);
                String examdate = data.getString(5);
                String ssc_id = data.getString(6);
                String trade_id = data.getString(7);
                String tb_id = data.getString(8);
                String starttime = data.getString(9);
                String endtime = data.getString(10);
                String IP_address = data.getString(11);
                String browser = data.getString(12);
                String question_ids = data.getString(13);
                String snapshot_image_name = data.getString(14);
                String snapshot_image_date = data.getString(15);
                String selected_ans = data.getString(16);
                String snaps = data.getString(17);


                Log.e("ANSWERS DBVALUESMAIN", ">>>>>>>>>>" +  apikey + " " + "" + exam_id+ " " +  std_id +
                        " " + std_name +" " +examdate+" " +ssc_id + " " + trade_id + " " + tb_id + " " + starttime + " "
                        + endtime + " " + IP_address + " " + browser + " " + question_ids + " " + snapshot_image_name + " "
                        + snapshot_image_date + " " + selected_ans + " " + snaps);

            }while(data.moveToNext());

    }

    public void dialogBoxForInternet() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Main_Exam.this);
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

    public class SendPostRequest extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

            // Showing progress dialog
            pDialog = new ProgressDialog(Main_Exam.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

            //timer is used to displays progress dialogue for 5 seconds
            Timer t = new Timer();
            t.schedule(new CloseDialogueTimerTask(pDialog), 5000);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            eId = new ArrayList<>();

            try {

                MexamsPojo exm;

                if (jsonObject != null && jsonObject.getString("status_message") != null) {

                    String message = jsonObject.getString("status_message");  // Message

                    Log.e("MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.equals("Success")) {

                        s_stdid = jsonObject.getString("student_id");
                        s_estatus = jsonObject.getString("exam_status");
                        s_examName = jsonObject.getString("exam_name");
                        s_esdate = jsonObject.getString("exam_start_date");
                        s_eedate = jsonObject.getString("exam_end_date");
                        s_eresponse = jsonObject.getString("exam_response");
                        s_duration = jsonObject.getString("exam_duration");
                        s_enrolRequired = jsonObject.getString("enrollment_form_required");
                        s_enrolment = jsonObject.getString("enrollment_form_status");
                        std_feedback = jsonObject.getString("student_feedback");
                        feedback_status = jsonObject.getString("feedback_form_status");
                        Log.e("SUCCESS RES MAIN", ">>>>>>>" + "Exam_name" + s_examName);

                        exm = new MexamsPojo(s_stdid,s_estatus,s_examName,s_esdate,s_eedate,s_eresponse,s_duration,s_enrolRequired,s_enrolment,std_feedback,feedback_status);
                        eId.add(exm);

                        // Create adapter passing in the sample user data
                        adapter = new MainExamAdapter(eId, Main_Exam.this);
                        // Attach the adapter to the recyclerview to populate itemsce
                        rv.setAdapter(adapter);

                        adapter.setItemClickListener(Main_Exam.this);// BIND THE LISTENER

                        pDialog.dismiss();


                    } else if (message.equals("Access Denied ! Authentication Failed")) {


                        Toast.makeText(getApplicationContext(),
                                "Access Denied", Toast.LENGTH_LONG).show();

                    }else if (message.equals("Not Found")) {


                        Toast.makeText(getApplicationContext(),
                                "Student Data Not Found", Toast.LENGTH_LONG).show();

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(eId.isEmpty()){
                mEmptyView.setVisibility(View.VISIBLE);

            }else{
                mEmptyView.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
            }
        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObject(get_exam_status, makingJson());

        }
    }


    private JSONObject makingJson() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", API_KEY);
            postDataParams.put("student_id", get_stdID);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postDataParams;
    }

    public JSONObject postJsonObject(String url, JSONObject loginJobj) {
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            System.out.println(url);
            String json = "";

            // 4. convert JSONObject to JSON to String
            json = loginJobj.toString();

            System.out.println(json);
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);
            httpPost.setEntity(new StringEntity(loginJobj.toString(), "UTF-8"));

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else
                result = "Unable to retrieve any data from server";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        JSONObject json = null;
        try {

            json = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 11. return result

        return json;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(this,MainActivity.class);
        finish();
        startActivity(a);
    }
}

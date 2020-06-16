package com.lscarp4.lscarpl4assessments.AssessorFiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lscarp4.lscarpl4assessments.CloseDialogueTimerTask;
import com.lscarp4.lscarpl4assessments.R;
import com.lscarp4.lscarpl4assessments.StudentFiles.Splash_Screen;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;

public class View_Details extends Activity {

    TextView batch_ID,bname,job_role,start_date,end_date,stdnum,mEmptyView;
    String s_batchid,s_bname,s_jobrole,s_sdate,s_edate,s_stdnum,tb_id;
    AlertDialog alertDialog = null;
    NetworkChangeReceiver br;
    TextView logout,header;
    Button back;
    RecyclerView rv;
    ArrayList<View_batches_pojo> batches_view;
    View_Batches_Adapter view_batches_adapter;
    private ProgressDialog pDialog;
    String stdfname,stdlname,adharnum,mobile,email,uname,pwd,exam_otp,theory_status;

    final static String view_batches = "http://lscmis.com/arpl4/assessor/assessor_api/get_students_details";

    private static final String API_KEY = "XDwFWzbaULYIA0Uub5QLhH9hxXRL1A6t";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        br = new NetworkChangeReceiver();

        header = (TextView)findViewById(R.id.header);
        header.setText("Batches to be Assessed - View Details");
        header.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mEmptyView = (TextView) findViewById(R.id.emptyElement);

        back = (Button) findViewById(R.id.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(View_Details.this,Batches_Assessed.class);
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

                Intent intent = new Intent(View_Details.this, Assessor_Login.class);
                startActivity(intent);
            }
        });


        batch_ID = (TextView) findViewById(R.id.batch_id);
        bname = (TextView) findViewById(R.id.batch_name);
        job_role = (TextView) findViewById(R.id.jrole);
        start_date = (TextView)findViewById(R.id.sdate);
        end_date = (TextView) findViewById(R.id.edate);
        stdnum = (TextView) findViewById(R.id.stdnum);

        s_batchid = getIntent().getStringExtra("batchid");
        s_bname = getIntent().getStringExtra("batchname");
        s_jobrole = getIntent().getStringExtra("jobrole");
        s_sdate = getIntent().getStringExtra("startDate");
        s_edate = getIntent().getStringExtra("endDate");
        s_stdnum = getIntent().getStringExtra("stdnum");
        tb_id = getIntent().getStringExtra("tb_id");

        batch_ID.setText("Batch ID: " + s_batchid);
        bname.setText("Batch Name: " + s_bname);
        job_role.setText("Job Role: " + s_jobrole);
        start_date.setText("Start Date: " + s_sdate);
        end_date.setText("End Date: " + s_edate);
        stdnum.setText("No. of Students: " + s_stdnum);

        Log.e("TB_ID", "????????????????" + tb_id + " ");


        Splash_Screen.editor.putString("batch_id", s_batchid);
        Splash_Screen.editor.putString("batch_name", s_bname);
        Splash_Screen.editor.commit();

        if (haveNetworkConnection()) {
            new SendPostRequest().execute();
        } else {
            Toast.makeText(View_Details.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();
        }

        rv = (RecyclerView) findViewById(R.id.recyclerView);
        // Set layout manager to position the items
        rv.setLayoutManager(new LinearLayoutManager(this));

        rv.setItemAnimator(new DefaultItemAnimator());

    }

    public void dialogBoxForInternet() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(View_Details.this);
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
            pDialog = new ProgressDialog(View_Details.this);
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

            batches_view = new ArrayList<>();

            try {

                View_batches_pojo viewB;

                if (jsonObject != null && jsonObject.getString("status_message") != null) {

                    String message = jsonObject.getString("status_message");  // Message

                    Log.e("MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.equals("Success")) {


                        String tb_ID = jsonObject.getString("tb_id");

                        // Creating JSONArray from JSONObject
                        JSONArray batches_assigned = jsonObject.getJSONArray("student_details");

                        for (int y = 0; y < batches_assigned.length(); y++) {

                            JSONObject jObj = batches_assigned.getJSONObject(y);

                            stdfname = jObj.getString("first_name");
                            stdlname = jObj.getString("last_name");
                            adharnum = jObj.getString("aadhaar_number");
                            mobile = jObj.getString("student_mobile");
                            email = jObj.getString("student_email");
                            uname = jObj.getString("SDMS_enrolment_number");
                            pwd = jObj.getString("password");
                            exam_otp = jObj.getString("exam_otp");
                            theory_status = jObj.getString("exam_status");
                            String student_id = jObj.getString("student_id");
                            String student_result = jObj.getString("student_result");

                            viewB = new View_batches_pojo(stdfname,stdlname,adharnum,mobile,email,uname,pwd,exam_otp,theory_status,student_id,student_result);

                            batches_view.add(viewB);

                            // Create adapter passing in the sample user data
                            view_batches_adapter = new View_Batches_Adapter(batches_view, View_Details.this);
                            // Attach the adapter to the recyclerview to populate itemsce
                            rv.setAdapter(view_batches_adapter);

                         //   view_batches_adapter.setItemClickListener(View_Details.this);// BIND THE LISTENER

                            pDialog.dismiss();


                        }

                    } else if (message.equals("Access Denied ! Authentication Failed")) {


                    }else if (message.equals("Not Found")) {


                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(batches_view.isEmpty()){
                mEmptyView.setVisibility(View.VISIBLE);

            }else{
                mEmptyView.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
            }
        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObject(view_batches, makingJson());

        }
    }


    private JSONObject makingJson() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", API_KEY);
            postDataParams.put("tb_id", tb_id);

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
        Intent intent = new Intent(View_Details.this,Batches_Assessed.class);
        startActivity(intent);

    }
}

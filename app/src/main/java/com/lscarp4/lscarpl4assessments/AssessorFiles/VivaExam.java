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
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;

public class VivaExam extends Activity {

    String batch_id,bname,name,sdms,tb_id,trade_id,ssc_id,exam_id,std_id;
    final static String viva_questions = "http://lscmis.com/arpl4/assessor/assessor_api/get_viva_questions";

    final static String viva_submission = "http://lscmis.com/arpl4/assessor/assessor_api/submit_vivamarks";

    private static final String API_KEY = "XDwFWzbaULYIA0Uub5QLhH9hxXRL1A6t";
    AlertDialog alertDialog = null;
    NetworkChangeReceiver br;
    TextView logout,header,mEmptyView;
    Button back;
    RecyclerView rv;
    ArrayList<Viva_Ques_pojo> viva_ques_pojos;
    Viva_Ques_Adapter viva_ques_adapter;
    private ProgressDialog pDialog;
    TextView bid,b_name,sdms_num, std_name;
    Button submit;
    String start_time,ip;
    ArrayList<String> viva_ques_id;
    ArrayList<String> max_marks;
    ArrayList<String> nosID;
    String marksTaken,Viva_id_string;
    ArrayList<String> getValueAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viva_exam);

        br = new NetworkChangeReceiver();

        viva_ques_id = new ArrayList<>();
        max_marks = new ArrayList<>();
        getValueAdap = new ArrayList<>();
        nosID = new ArrayList<>();

        std_id = getIntent().getStringExtra("student_id");
        sdms = getIntent().getStringExtra("sdms");
        name = getIntent().getStringExtra("name");
        batch_id = Splash_Screen.sh.getString("batch_id",null);
        bname = Splash_Screen.sh.getString("batch_name", null);
        tb_id = Splash_Screen.sh.getString("tb_id", null);
        trade_id = Splash_Screen.sh.getString("trade_id", null);
        ssc_id = Splash_Screen.sh.getString("sscid", null);
        exam_id = Splash_Screen.sh.getString("exam_id", null);

        Log.e("VIVA EXAM STDID", ">>>>>>>>>>>>>>" + std_id);

        bid = (TextView) findViewById(R.id.batch_id);
        b_name = (TextView) findViewById(R.id.batch_name);
        sdms_num = (TextView) findViewById(R.id.sdms);
        std_name = (TextView) findViewById(R.id.name);
        submit = (Button) findViewById(R.id.submit);

        bid.setText("Batch ID: " +batch_id);
        b_name.setText("Batch Name: " + bname);
        std_name.setText("Name: " + name);
        sdms_num.setText("SDMS Enrollment No: " + sdms);

        header = (TextView)findViewById(R.id.header);
        header.setText("Viva Exam");
        mEmptyView = (TextView) findViewById(R.id.emptyElement);

        //getting the ip address of the device/mobile
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());


        back = (Button) findViewById(R.id.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VivaExam.this,Batches_Assessed.class);
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

                Intent intent = new Intent(VivaExam.this, Assessor_Login.class);
                startActivity(intent);
            }
        });

        if (haveNetworkConnection()) {
            new SendPostRequest().execute();
        } else {
            Toast.makeText(VivaExam.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();
        }

        rv = (RecyclerView) findViewById(R.id.recyclerView);
        // Set layout manager to position the items
        rv.setLayoutManager(new LinearLayoutManager(this));

        rv.setItemAnimator(new DefaultItemAnimator());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SubmitVivaAnswers().execute();

                Log.e("MARKS TAKEN", ">>>>>>>>>>>>>>>>>>" + getValueAdap);
            }
        });
    }


    public void dialogBoxForInternet() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(VivaExam.this);
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

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public class SendPostRequest extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

            // Showing progress dialog
            pDialog = new ProgressDialog(VivaExam.this);
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

            viva_ques_pojos = new ArrayList<>();

            try {

                Viva_Ques_pojo viva;

                if (jsonObject != null && jsonObject.getString("status_message") != null) {

                    String message = jsonObject.getString("status_message");  // Message

                    Log.e("MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.equals("Success")) {

                        // Creating JSONArray from JSONObject
                        JSONArray viva_ques = jsonObject.getJSONArray("viva_questions");

                        for (int y = 0; y < viva_ques.length(); y++) {

                            JSONObject jObj = viva_ques.getJSONObject(y);

                            String q_id = jObj.getString("question_id");
                            String question = jObj.getString("question");
                            String qn_max_marks = jObj.getString("qn_max_marks");
                            String nos_id = jObj.getString("nos_id");
                            String nos_code = jObj.getString("nos_code");
                            String nos_title = jObj.getString("nos_title");

                            viva_ques_id.add(q_id);
                            max_marks.add(qn_max_marks);
                            nosID.add(nos_id);

                            Log.e("NOS ID", ">>>>>>>>>>>>>>>>" + nos_id + "   " + nosID);
                            Log.e("VIVA QUES_ID", "???????????" + q_id + " " + viva_ques_id);
                            Log.e("MAxMARKS", ">>>>>>>>>>>>" + qn_max_marks + "  " + max_marks);

                            Log.e("Question ID Arraylist ", ">>>>>>>>>>>>>>>" + viva_ques_id);

                            viva = new Viva_Ques_pojo(q_id,question,qn_max_marks,nos_id,nos_code,nos_title);
                            viva_ques_pojos.add(viva);

                            // Create adapter passing in the sample user data
                            viva_ques_adapter = new Viva_Ques_Adapter(viva_ques_pojos, VivaExam.this);
                            // Attach the adapter to the recyclerview to populate itemsce
                            rv.setAdapter(viva_ques_adapter);
                            pDialog.dismiss();

                            start_time = getDateTime();

                            Log.e("STTART TIME", ">>>>>>>>>>." + start_time);

                        }

                    } else if (message.equals("Access Denied ! Authentication Failed")) {

                        Toast.makeText(getApplicationContext(),
                                "Access Denied", Toast.LENGTH_LONG).show();

                    }else if (message.equals("Error - Incorrect Exam ID")) {

                        Toast.makeText(getApplicationContext(),
                                message, Toast.LENGTH_LONG).show();

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if(viva_ques_pojos.isEmpty()){
                mEmptyView.setVisibility(View.VISIBLE);

            }else{
                mEmptyView.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
            }
        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObject(viva_questions, makingJson());

        }
    }


    private JSONObject makingJson() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", API_KEY);
            postDataParams.put("tb_id", tb_id);
            postDataParams.put("ssc_id", ssc_id);
            postDataParams.put("trade_id", trade_id);
            postDataParams.put("student_id", std_id);
            postDataParams.put("exam_id", exam_id);

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
        Intent intent = new Intent(VivaExam.this,Batches_Assessed.class);
        startActivity(intent);
    }

    public class SubmitVivaAnswers extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

            // Showing progress dialog
            pDialog = new ProgressDialog(VivaExam.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            try {

                if (jsonObject != null && jsonObject.getString("status_message") != null) {

                    String message = jsonObject.getString("status_message");  // Message

                    Log.e("MESSAGE VIVA SUBMIT", ">>>>>>>>>>>>>>>>" + message);

                    if (message.equals("Success")) {
                        Toast.makeText(VivaExam.this, "Submit Successful", Toast.LENGTH_LONG).show();

                        pDialog.dismiss();
                        Intent intent = new Intent(VivaExam.this,Batches_Assessed.class);
                        startActivity(intent);


                    } else if (message.equals("Access Denied ! Authentication Failed")) {

                        Toast.makeText(VivaExam.this, "Access Denied ! Authentication Failed", Toast.LENGTH_LONG).show();


                    }else if (message.equals("Error while submitting Viva marks. Incorrect parameters. Please contact Admin")) {

                        Toast.makeText(VivaExam.this, message, Toast.LENGTH_LONG).show();

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected JSONObject doInBackground(Void... params) {


            for(int i = 0 ; i < viva_ques_id.size() ; i++){
                marksTaken = viva_ques_adapter.getValue(i);
                getValueAdap.add(marksTaken);
            }

            Viva_id_string = TextUtils.join(", ", viva_ques_id);


            return postJsonObject1(viva_submission, makingJson1());

        }
    }


    private JSONObject makingJson1() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", API_KEY);
            postDataParams.put("tb_id", tb_id);
            postDataParams.put("ssc_id", ssc_id);
            postDataParams.put("trade_id", trade_id);
            postDataParams.put("exam_id", exam_id);
            postDataParams.put("student_id", std_id);
            postDataParams.put("start_time", start_time);
            postDataParams.put("viva_question_ids", Viva_id_string);

            //submitting given marks for each questions
            for(int i=0;i<viva_ques_id.size();i++)
            {

                postDataParams.put("marks_"+viva_ques_id.get(i),getValueAdap.get(i));

                Log.e("MARKS TAKEN POST FOR",">>>>>>>>>>." + "marks_"+viva_ques_id.get(i)+ getValueAdap.get(i));
            }
            HashMap<String, String> params1=new HashMap<String, String>();
            params1.put("params",postDataParams.toString());
            Log.e("INTERNET", ">>>>>>>>>>>."+ params1);

            //submitting max marks for each questions

            for(int i=0;i<max_marks.size();i++)
            {
                postDataParams.put("qn_max_marks_"+viva_ques_id.get(i),max_marks.get(i));

                Log.e("MAX MARKS FOR LOOP",">>>>>>>>>>." + "qn_max_marks_"+viva_ques_id.get(i) + "         " + max_marks.get(i));
            }


            HashMap<String, String> params2=new HashMap<String, String>();
            params2.put("params",postDataParams.toString());

            Log.e("QN_MAX_MARKS PARAM2", ">>>>>>>>>>>."+ params2);

            //Submitting nos id for each questions

            for(int m  =0; m < nosID.size(); m++)
            {
                postDataParams.put("nos_id_"+viva_ques_id.get(m),nosID.get(m));
                Log.e("NOS ID FOR LOOP",">>>>>>>>>>." + "nos_id_"+viva_ques_id.get(m) + "   "+ nosID.get(m));
            }


            HashMap<String, String> params3=new HashMap<String, String>();
            params3.put("params",postDataParams.toString());

            Log.e("NOS IDS PARAM3", ">>>>>>>>>>>."+ params3);

            postDataParams.put("IP_address", ip);
            postDataParams.put("browser ", "mobile_app");

            Log.e("ALL PARAM", "??????????????" + API_KEY + " " + tb_id + " " + trade_id + " " + ssc_id + " "+
                    std_id + " " + exam_id + " " + ip +" "+ String.valueOf(viva_ques_id) + " " + start_time + " " + params1 + " " + params2 + " "+ params3);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postDataParams;
    }

    public JSONObject postJsonObject1(String url, JSONObject loginJobj1) {
        InputStream inputStream1 = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            System.out.println(url);
            String json = "";

            // 4. convert JSONObject to JSON to String
            json = loginJobj1.toString();

            System.out.println(json);
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);
            httpPost.setEntity(new StringEntity(loginJobj1.toString(), "UTF-8"));

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream1 = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream1 != null) {
                result = convertInputStreamToString1(inputStream1);
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

    private String convertInputStreamToString1(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

}

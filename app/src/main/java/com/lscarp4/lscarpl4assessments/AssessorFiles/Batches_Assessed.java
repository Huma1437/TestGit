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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lscarp4.lscarpl4assessments.CloseDialogueTimerTask;
import com.lscarp4.lscarpl4assessments.ItemClickListener;
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

public class Batches_Assessed extends Activity implements ItemClickListener {

    AlertDialog alertDialog = null;
    NetworkChangeReceiver br;
    TextView logout,header,mEmptyView;
    Button back;
    RecyclerView rv;
    ArrayList<Batches_Assigned_Pojo> batches_assigned_pojos;
    Batches_Ass_Adapter batches_ass_adapter;
    private ProgressDialog pDialog;

    String get_username,get_password;

    final static String Login_url = "http://lscmis.com/arpl4/assessor/assessor_api/validate_assessor_login";

    private static final String API_KEY = "XDwFWzbaULYIA0Uub5QLhH9hxXRL1A6t";

    String tc_name,tb_name,batch_name,tb_start_date_time,tb_end_date_time,tb_assessment_status,tc_id,tb_exam_type,
            tb_id,tb_target,e_id,sscid,trade_id,tp_id,qp_shuffling;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batches_assessed);

        br = new NetworkChangeReceiver();


        header = (TextView)findViewById(R.id.header);
        header.setText("Batches to be Assessed");
        mEmptyView = (TextView) findViewById(R.id.emptyElement);

        get_username = Splash_Screen.sh.getString("Ass_uname", null);
        get_password = Splash_Screen.sh.getString("Ass_pwd", null);

        if (haveNetworkConnection()) {

            if(!get_username.isEmpty() && !get_password.isEmpty()){

                new SendPostRequest().execute();

            }else{


            }


        } else {
            Toast.makeText(Batches_Assessed.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();
        }


        back = (Button) findViewById(R.id.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Batches_Assessed.this,AssessorDashboard.class);
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

                Intent intent = new Intent(Batches_Assessed.this, Assessor_Login.class);
                startActivity(intent);
            }
        });

        rv = (RecyclerView) findViewById(R.id.recyclerView);
        // Set layout manager to position the items
        rv.setLayoutManager(new LinearLayoutManager(this));

        rv.setItemAnimator(new DefaultItemAnimator());

    }

    public void dialogBoxForInternet() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Batches_Assessed.this);
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
    public void onClick(View view, int position) {

    }


    public class SendPostRequest extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

            // Showing progress dialog
            pDialog = new ProgressDialog(Batches_Assessed.this);
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

            batches_assigned_pojos = new ArrayList<>();

            try {

                Batches_Assigned_Pojo bap;

                if (jsonObject != null && jsonObject.getString("status_message") != null) {

                    String message = jsonObject.getString("status_message");  // Message

                    Log.e("MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.equals("Success")) {


                        JSONObject jObj_1 = jsonObject.getJSONObject("logged_in");

                        String auid = jObj_1.getString("auid");
                        String ut_id = jObj_1.getString("ut_id");
                        String username = jObj_1.getString("username");
                        String user_type = jObj_1.getString("user_type");

                        // Creating JSONArray from JSONObject
                        JSONArray batches_assigned = jsonObject.getJSONArray("assessor_batch_assign");

                        for (int y = 0; y < batches_assigned.length(); y++) {

                            JSONObject jObj = batches_assigned.getJSONObject(y);

                            tb_assessment_status = jObj.getString("tb_assessment_status");
                            tc_name = jObj.getString("tc_name");
                            tb_name = jObj.getString("tb_name");
                            batch_name = jObj.getString("batch_name");
                            tb_start_date_time = jObj.getString("tb_start_date_time");
                            tb_end_date_time = jObj.getString("tb_end_date_time");
                            tc_id = jObj.getString("tc_id");
                            tb_exam_type = jObj.getString("tb_exam_type");
                            tb_id = jObj.getString("tb_id");
                            tb_target = jObj.getString("tb_target");
                            e_id = jObj.getString("e_id");
                            sscid = jObj.getString("sscid");
                            trade_id = jObj.getString("trade_id");
                            tp_id = jObj.getString("tp_id");
                            qp_shuffling = jObj.getString("qp_shuffling");
                            String trade_title = jObj.getString("trade_title");

                            Splash_Screen.editor.putString("tb_id", tb_id);
                            Splash_Screen.editor.putString("sscid", sscid);
                            Splash_Screen.editor.putString("trade_id", trade_id);
                            Splash_Screen.editor.putString("exam_id", e_id);
                            Splash_Screen.editor.commit();

                            bap = new Batches_Assigned_Pojo(tc_name,tb_name,batch_name,tb_start_date_time,tb_end_date_time,
                                    tb_assessment_status,tc_id,tb_exam_type,tb_id,tb_target,e_id,sscid,trade_id,tb_id,qp_shuffling,trade_title);

                            batches_assigned_pojos.add(bap);

                            // Create adapter passing in the sample user data
                            batches_ass_adapter = new Batches_Ass_Adapter(batches_assigned_pojos, Batches_Assessed.this);
                            // Attach the adapter to the recyclerview to populate itemsce
                            rv.setAdapter(batches_ass_adapter);

                            batches_ass_adapter.setItemClickListener(Batches_Assessed.this);// BIND THE LISTENER

                            pDialog.dismiss();
                        }

                    } else if (message.equals("Access Denied ! Authentication Failed")) {


                    }else if (message.equals("Not Found")) {


                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(batches_assigned_pojos.isEmpty()){
                mEmptyView.setVisibility(View.VISIBLE);

            }else{
                mEmptyView.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
            }
        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObject(Login_url, makingJson());

        }
    }


    private JSONObject makingJson() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", API_KEY);
            postDataParams.put("username", get_username);
            postDataParams.put("password", get_password);

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
        Intent intent = new Intent(Batches_Assessed.this,AssessorDashboard.class);
        startActivity(intent);

    }
}

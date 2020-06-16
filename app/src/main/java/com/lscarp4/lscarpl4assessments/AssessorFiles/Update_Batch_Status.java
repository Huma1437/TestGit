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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lscarp4.lscarpl4assessments.CloseDialogueTimerTask;
import com.lscarp4.lscarpl4assessments.R;
import com.lscarp4.lscarpl4assessments.StudentFiles.Splash_Screen;

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
import java.util.Timer;

public class Update_Batch_Status extends Activity implements AdapterView.OnItemSelectedListener {

    Spinner status;
    EditText comment;
    Button update;
    String tb_id, selected_status, comment_msg;
    AlertDialog alertDialog = null;
    NetworkChangeReceiver br;
    TextView logout,header;
    Button back;
    private ProgressDialog pDialog;
    private static final String[] s_values = {"Pending", "Completed"};

    final static String update_batch_status = "http://lscmis.com/arpl4/assessor/assessor_api/update_assessment_status";

    final static String check_status = "http://lscmis.com/arpl4/assessor/assessor_api/get_tb_assessment_status";

    private static final String API_KEY = "XDwFWzbaULYIA0Uub5QLhH9hxXRL1A6t";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_batch_status);

        br = new NetworkChangeReceiver();

        header = (TextView)findViewById(R.id.header);
        header.setText("Update Batch Status");

        back = (Button) findViewById(R.id.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Update_Batch_Status.this,Batches_Assessed.class);
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

                Intent intent = new Intent(Update_Batch_Status.this, Assessor_Login.class);
                startActivity(intent);
            }
        });

        comment = (EditText)findViewById(R.id.comment);
        update = (Button) findViewById(R.id.submit);
        status = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Update_Batch_Status.this,
                R.layout.spinner,s_values);

        adapter.setDropDownViewResource(R.layout.spinner);
        status.setAdapter(adapter);
        status.setOnItemSelectedListener(this);

        tb_id = getIntent().getStringExtra("tb_id");

        if (haveNetworkConnection()) {

            new checkExamStatus().execute();

        } else {
            Toast.makeText(Update_Batch_Status.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                comment_msg = comment.getText().toString().trim();

                if(selected_status.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Select Status",
                            Toast.LENGTH_LONG).show();
                }else if(comment_msg.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter your comment",
                            Toast.LENGTH_LONG).show();
                }

                if(!selected_status.isEmpty() && !comment_msg.isEmpty()){
                    new SendPostRequest().execute();
                }

                Log.e("CLICKED", "????????????" + tb_id + " " + selected_status + " " + comment_msg);

            }
        });

    }


    public void dialogBoxForInternet() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Update_Batch_Status.this);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                selected_status = parent.getItemAtPosition(position).toString();
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                selected_status = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    //Updating the status of the batch
    public class SendPostRequest extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

            // Showing progress dialog
            pDialog = new ProgressDialog(Update_Batch_Status.this);
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

                    Log.e("MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.equals("Success")) {

                        Toast.makeText(Update_Batch_Status.this,
                                "Updated Successfully", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();

                        Intent intent = new Intent(Update_Batch_Status.this, Batches_Assessed.class);
                        startActivity(intent);

                    } else if (message.equals("Access Denied ! Authentication Failed")) {

                        pDialog.dismiss();
                        Toast.makeText(Update_Batch_Status.this,
                                "Access Denied ! Authentication Failed", Toast.LENGTH_LONG).show();

                    }else if (message.contains("No Action Taken")) {

                        pDialog.dismiss();
                        Toast.makeText(Update_Batch_Status.this,
                                "Please Select the Status", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObject(update_batch_status, makingJson());

        }
    }


    private JSONObject makingJson() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", API_KEY);
            postDataParams.put("tb_id", tb_id);
            postDataParams.put("tb_assessment_status", selected_status);
            postDataParams.put("assessor_comments", comment_msg);


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
        Intent intent = new Intent(Update_Batch_Status.this,Batches_Assessed.class);
        startActivity(intent);

    }

    //Checking the status of the exam of all the students
    public class checkExamStatus extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

            // Showing progress dialog
            pDialog = new ProgressDialog(Update_Batch_Status.this);
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

            try {

                if (jsonObject != null && jsonObject.getString("status_message") != null) {

                    String message = jsonObject.getString("status_message");  // Message

                    Log.e("MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.equals("Error")) {

                        Toast.makeText(Update_Batch_Status.this,
                                "Theory/Viva marks for all students are not entered. Please enter before updating batch status.", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();

                        Intent intent = new Intent(Update_Batch_Status.this, Batches_Assessed.class);
                        startActivity(intent);

                    } else if (message.equals("Access Denied ! Authentication Failed")) {

                        pDialog.dismiss();
                        Toast.makeText(Update_Batch_Status.this,
                                "Access Denied ! Authentication Failed", Toast.LENGTH_LONG).show();

                    }else if (message.contains("Success")) {

                        pDialog.dismiss();

                        Toast.makeText(Update_Batch_Status.this,
                                "You can update the Batch Status", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObject1(check_status, makingJson1());

        }
    }


    private JSONObject makingJson1() {

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

    public JSONObject postJsonObject1(String url, JSONObject loginJobj) {
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
                result = convertInputStreamToString1(inputStream);
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

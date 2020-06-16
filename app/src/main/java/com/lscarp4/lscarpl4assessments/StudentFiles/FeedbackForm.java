package com.lscarp4.lscarpl4assessments.StudentFiles;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lscarp4.lscarpl4assessments.AssessorFiles.AssessorDashboard;
import com.lscarp4.lscarpl4assessments.AssessorFiles.Batches_Ass_Adapter;
import com.lscarp4.lscarpl4assessments.AssessorFiles.Batches_Assessed;
import com.lscarp4.lscarpl4assessments.AssessorFiles.Batches_Assigned_Pojo;
import com.lscarp4.lscarpl4assessments.CloseDialogueTimerTask;
import com.lscarp4.lscarpl4assessments.Database.DBHelper;
import com.lscarp4.lscarpl4assessments.R;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;

public class FeedbackForm extends AppCompatActivity {

    TextView header,mEmptyView;
    Button submit,comment,close;
    EditText commentBox;
    RecyclerView rv;
    String getComment;
    String question,option_1,option_2,option_3,option_4,option_5;
    Integer qnum;
    ImageView logout,desc;
    Button back;
    Dialog dialog;
    String stdname,b_ID,b_name,j_role,enrol_num,get_stdID;
    TextView s_name,e_num,batch_id,bname,jobRole;
    LinearLayout linearLayout;

    AlertDialog alertDialog = null;
    NetworkChangeReceiver br;

    ArrayList<Feedback_Pojo> feedbackPojoArrayList;
    FeedbackAdapter feedbackAdapter;
    private ProgressDialog pDialog;
    ArrayList<Integer> Ques_Ids;
    ArrayList<String> SelectedAnswers;
    DBHelper mydb;
    String strQnum,strSelopt,DbSelectedOpt;

    final static String feedback_url_question = "https://lscmis.com/arpl4/student/feedback_api/questions";
    final static String feedback_url_submission = "https://lscmis.com/arpl4/student/feedback_api/submit_feedback";
    private static final String API_KEY = "YIA0Uub5QLhH9hxXRL1A6tXDwFWzbaUL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);

        br = new NetworkChangeReceiver();
        header = (TextView) findViewById(R.id.header);
        desc = (ImageView) findViewById(R.id.desc);
        back = (Button) findViewById(R.id.backbtn);
        mydb = new DBHelper(this);
        SelectedAnswers = new ArrayList<>();

        mEmptyView = (TextView) findViewById(R.id.emptyElement);
        submit = (Button) findViewById(R.id.submit);
        comment = (Button)findViewById(R.id.commentbtn);
        close = (Button) findViewById(R.id.close);
        commentBox = (EditText) findViewById(R.id.comment);
        linearLayout = (LinearLayout) findViewById(R.id.cmntBoxLayout);
        linearLayout.setVisibility(View.GONE);
        close.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        comment.setVisibility(View.GONE);

        header.setText("Training Feedback Form");

        get_stdID = Splash_Screen.sh.getString("student_id", null);
        stdname = Splash_Screen.sh.getString("get_stdname",null);
        b_name = Splash_Screen.sh.getString("tb_name", null);
        j_role = Splash_Screen.sh.getString("trade_title", null);
        b_ID = Splash_Screen.sh.getString("tb_nsdc_id", null);
        enrol_num = Splash_Screen.sh.getString("SDMS_enrolment_number", null);


        Log.e("STUDENT ID FEEDBACK", ">>>>>>>>>>>>>>> " + get_stdID);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FeedbackForm.this,MainActivity.class);
                startActivity(intent);
            }
        });


        logout = (ImageView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "You have successfully logout",
                        Toast.LENGTH_LONG).show();
                Splash_Screen.editor.remove("loginTest");
                Splash_Screen.editor.commit();

                Intent intent = new Intent(FeedbackForm.this,Login.class);
                startActivity(intent);
            }
        });

        Log.e("STUDENT ID", ">>>>>>>>>>>" + get_stdID);

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(FeedbackForm.this);
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

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearLayout.setVisibility(View.VISIBLE);
                close.setVisibility(View.VISIBLE);
                comment.setVisibility(View.GONE);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearLayout.setVisibility(View.GONE);
                comment.setVisibility(View.VISIBLE);
                close.setVisibility(View.GONE);
            }
        });

        rv = (RecyclerView) findViewById(R.id.recyclerView);
        // Set layout manager to position the items
        rv.setLayoutManager(new LinearLayoutManager(this));

        rv.setItemAnimator(new DefaultItemAnimator());

        if(haveNetworkConnection()){

            new GetFeedBackQuestion().execute();
        }else{
            Toast.makeText(FeedbackForm.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();

        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getComment = commentBox.getText().toString().trim();

                if(getComment.isEmpty()){

                    Toast.makeText(FeedbackForm.this,"Please Enter your Comment",Toast.LENGTH_LONG).show();
                }

                if(haveNetworkConnection()){

                    if(!getComment.isEmpty()){

                     new SubmitFeedBack().execute();

                    }

                }else{


                    Toast.makeText(FeedbackForm.this,"No Internet Connection",Toast.LENGTH_LONG).show();

                }


                Log.e("SUBMIT FEEDBACK", ">>>>>>>>>.." + getComment + " " +get_stdID + " " + Ques_Ids + " " + SelectedAnswers );

            }
        });

        getDBDATA();

    }

    public void getDBDATA(){
        Cursor data = mydb.getfeedbackContent();

        if(data != null) {
            while (data.moveToNext()) {
                String Dbq_id = data.getString(1);
                DbSelectedOpt = data.getString(2);
                SelectedAnswers.add(DbSelectedOpt);
                Log.e("SELECTEDANS","INSIDE METHOD" +SelectedAnswers);
            }


        }else{

            Log.e("NULLDB",">>>>>>>>>>>>>>>>" +"" );
        }
    }


    public void dialogBoxForInternet() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FeedbackForm.this);
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

    public class GetFeedBackQuestion extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

            // Showing progress dialog
            pDialog = new ProgressDialog(FeedbackForm.this);
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

            Ques_Ids = new ArrayList<>();
            feedbackPojoArrayList = new ArrayList<>();

            try {

                Feedback_Pojo feedback_pojo;

                if (jsonObject != null && jsonObject.getString("status_message") != null) {

                    String message = jsonObject.getString("status_message");  // Message

                    Log.e("MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.equals("Success")) {

                        // Creating JSONArray from JSONObject
                        JSONArray feedback = jsonObject.getJSONArray("questions");

                        for (int y = 0; y < feedback.length(); y++) {

                            JSONObject jObj = feedback.getJSONObject(y);

                            qnum = jObj.getInt("Qnumber");
                            question = jObj.getString("question");
                            option_1 = jObj.getString("option_1");
                            option_2 = jObj.getString("option_2");
                            option_3 = jObj.getString("option_3");
                            option_4 = jObj.getString("option_4");
                            option_5 = jObj.getString("option_5");

                            String getqnum = String.valueOf(qnum);

                            feedback_pojo = new Feedback_Pojo(qnum,question,option_1,option_2,option_3,option_4,option_5);

                            feedbackPojoArrayList.add(feedback_pojo);

                            Ques_Ids.add(qnum);

                            Log.e("QUESTION IDS","???????//" + Ques_Ids);

                            boolean recordExists= mydb.checkFeedbackExists(mydb.feedback_table ,mydb.feed_col_1 ,getqnum);
                            if(recordExists)
                            {


                            }else{

                                mydb.addFeedback(getqnum,null);

                            }




                            Log.e("FEEDBACK QUES", " ??????????????? " + qnum + " " + question + " " + option_1 + " " + option_2 +
                                    " " + option_3 + " " + option_4 + " " + option_5);

                            // Create adapter passing in the sample user data
                            feedbackAdapter = new FeedbackAdapter(feedbackPojoArrayList, FeedbackForm.this, get_stdID);
                            // Attach the adapter to the recyclerview to populate itemsce
                            rv.setAdapter(feedbackAdapter);

                            pDialog.dismiss();
                        }

                    } else if (message.equals("Access Denied ! Authentication Failed")) {


                    }else if (message.equals("Not Found")) {


                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(feedbackPojoArrayList.isEmpty()){
                mEmptyView.setVisibility(View.VISIBLE);

            }else{
                mEmptyView.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                comment.setVisibility(View.VISIBLE);
            }
        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObject(feedback_url_question, makingJson());

        }
    }


    private JSONObject makingJson() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", API_KEY);

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


    public class SubmitFeedBack extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

            // Showing progress dialog
            pDialog = new ProgressDialog(FeedbackForm.this);
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

                    if (message.equals("Success")) {

                        mydb.deleteFeedback();
                        Toast.makeText(FeedbackForm.this,"Feedback Submited Successfully",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(FeedbackForm.this,Enrollment_Form.class);
                        startActivity(intent);
                        pDialog.dismiss();

                    } else if (message.equals("Access Denied ! Authentication Failed")) {

                        mydb.deleteFeedback();
                        Toast.makeText(FeedbackForm.this,message,Toast.LENGTH_LONG).show();
                        pDialog.dismiss();

                    }else if (message.equals("Invalid Input Details")) {

                        mydb.deleteFeedback();
                        Toast.makeText(FeedbackForm.this,message,Toast.LENGTH_LONG).show();
                        pDialog.dismiss();

                    }else if (message.equals("Feedback already Submitted")) {

                        mydb.deleteFeedback();
                        Toast.makeText(FeedbackForm.this,message,Toast.LENGTH_LONG).show();
                        pDialog.dismiss();

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        protected JSONObject doInBackground(Void... params) {

            Cursor data = mydb.getfeedbackContent();

            if(data != null) {
                while (data.moveToNext()) {
                    String Dbq_id = data.getString(1);
                    DbSelectedOpt = data.getString(2);
                    SelectedAnswers.add(DbSelectedOpt);
                    Log.e("SELECTEDANS","INSIDE METHOD" +SelectedAnswers);
                }


            }else{

                Log.e("NULLDB",">>>>>>>>>>>>>>>>" +"" );
            }

            strQnum = Arrays.toString(Ques_Ids.toArray()).replace("[", "").replace("]", "");
            strSelopt = Arrays.toString(SelectedAnswers.toArray()).replace("[", "").replace("]", "");

            return postJsonObject1(feedback_url_submission, makingJson1());

        }
    }


    private JSONObject makingJson1() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", API_KEY);
            postDataParams.put("student_id", get_stdID);
            postDataParams.put("questions", strQnum);
            postDataParams.put("answers", strSelopt);
            postDataParams.put("comments", getComment);

            HashMap<String, String> params=new HashMap<String, String>();
            params.put("params",postDataParams.toString());

            Log.e("SUBMIT FEEDBACK", "ON POST"+ params);

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FeedbackForm.this, MainActivity.class);
        startActivity(intent);

    }
}

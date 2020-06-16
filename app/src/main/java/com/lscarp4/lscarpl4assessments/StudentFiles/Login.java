package com.lscarp4.lscarpl4assessments.StudentFiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lscarp4.lscarpl4assessments.AssessorFiles.Assessor_Login;
import com.lscarp4.lscarpl4assessments.Database.DBHelper;
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

public class Login extends Activity {


    EditText uname,pwd;
    String get_username,get_password;
    TextView login,assl;
    CheckBox show_pwd;

    AlertDialog alertDialog = null;
    NetworkChangeReceiver br;

    final static String Login_url = "http://lscmis.com/arpl4/student/student_api/validate_student_login";

    private static final String API_KEY = "YIA0Uub5QLhH9hxXRL1A6tXDwFWzbaUL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        br = new NetworkChangeReceiver();

        uname = (EditText)findViewById(R.id.uname);
        pwd = (EditText)findViewById(R.id.pwd);
        login = (TextView)findViewById(R.id.login);
        show_pwd=(CheckBox)findViewById(R.id.checkBox1);
        assl = (TextView) findViewById(R.id.assL);

        assl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, Assessor_Login.class);
                startActivity(intent);
            }
        });


        show_pwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //show password
                    pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }else{
                    //hide password
                    pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get_username = uname.getText().toString().trim();
                get_password = pwd.getText().toString().trim();

                if(get_username.isEmpty()){
                    uname.setError("Enter Username");
                }else if(get_password.isEmpty()){
                    pwd.setError("Enter Password");
                }

                if (haveNetworkConnection()) {

                    if(!get_username.isEmpty() && !get_password.isEmpty()){

                        new SendPostRequest().execute();

                        Toast.makeText(getApplicationContext(),
                                "Clicked", Toast.LENGTH_LONG).show();


                    }else{
                        Toast.makeText(getApplicationContext(),
                                "Provide Credentials to Login", Toast.LENGTH_LONG).show();
                    }


                } else {

                    Toast.makeText(Login.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();

                }

            }
        });

    }


    public void dialogBoxForInternet() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
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
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            try {
                if (jsonObject != null && jsonObject.getString("status_message") != null) {

                    String message = jsonObject.getString("status_message");  // Message

                    Log.e("MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.equals("Success")) {


                        JSONObject jObj_1 = jsonObject.getJSONObject("logged_in_student");

                        String get_stdID = jObj_1.getString("student_id");
                        String get_uname = jObj_1.getString("username");


                        String get_stdname = jsonObject.getString("student_name");
                        String enrol = jsonObject.getString("SDMS_enrolment_number");
                        String adhaar = jsonObject.getString("aadhaar_number");
                        String std_pic = jsonObject.getString("student_photo");
                        String g_name = jsonObject.getString("guardian_name");
                        String dob = jsonObject.getString("date_of_birth");
                        String mobile = jsonObject.getString("student_mobile");
                        String email = jsonObject.getString("student_email");
                        String address = jsonObject.getString("address");
                        String city = jsonObject.getString("city");
                        String pincode = jsonObject.getString("pincode");
                        String state_id = jsonObject.getString("state_id");
                        String dist_id = jsonObject.getString("dist_id");
                        String ssc_id = jsonObject.getString("ssc_id");
                        String trade_id = jsonObject.getString("trade_id");
                        String tb_id = jsonObject.getString("tb_id");
                        String tb_nsdc_id = jsonObject.getString("tb_nsdc_id");
                        String tb_name = jsonObject.getString("tb_name");
                        String trade_code = jsonObject.getString("trade_code");
                        String trade_title = jsonObject.getString("trade_title");
                        String student_id = jsonObject.getString("student_id");
                        String practice_exam = jsonObject.getString("practice_exam");
                        String practice_exam_no = jsonObject.getString("practice_exam_no");
                        String exam_id = jsonObject.getString("exam_id");
                        String exam_response = jsonObject.getString("exam_response");
                        String exam_otp = jsonObject.getString("exam_otp");
                        String student_image_path = jsonObject.getString("student_image_path");
                        String assessor_image_path = jsonObject.getString("assessor_image_path");
                        String take_snapshots = jsonObject.getString("take_snapshots");

                        String snapshots_file_path = jsonObject.getString("snapshots_file_path");
                        String snapshots_thumbs_file_path = jsonObject.getString("snapshots_thumbs_file_path");

                        //For feedback and enrollment form visibility
                        String enrollment_form = jsonObject.getString("enrollment_form");
                        String enrollment_form_status = jsonObject.getString("enrollment_form_status");
                        String student_feedback = jsonObject.getString("student_feedback");
                        String feedback_form_status = jsonObject.getString("feedback_form_status");

                        //for sending images to server using ftp
                        String ftp_snapshot_host = jsonObject.getString("ftp_snapshot_host");
                        String ftp_snapshot_username = jsonObject.getString("ftp_snapshot_username");
                        String ftp_snapshot_password = jsonObject.getString("ftp_snapshot_password");


                        Log.e("LOGGED IN", ">>>>>.... " + enrollment_form + " " + enrollment_form_status + " " + student_feedback + " " + feedback_form_status
                        + " " + ftp_snapshot_host + " " + ftp_snapshot_username + " " + ftp_snapshot_password);


                    /*    Log.e("LOGIN",">>>>>>> " + enrol +"\n" + adhaar +"\n" +  std_pic  +"\n" + g_name +"\n"
                                + dob +"\n" + email +"\n" + mobile +"\n" + address+"\n" +  city +"\n"+pincode +"\n"+ state_id +"\n"+ dist_id + get_stdID + get_uname +
                                get_stdname + "SSCID: "+ssc_id + "TradeID : " +  trade_id  +  "TBID" +tb_id + "Student id:" + student_id +
                                practice_exam  + practice_exam_no + "Exam Id: " +exam_id + exam_response + "Exam_OTP" + " " + exam_otp + "Student image:" +student_image_path
                        + "Assessor_image: " + assessor_image_path + "Take_snaps: " + take_snapshots + "enrollment_form" + enrollment_form + "\n" + snapshots_file_path + snapshots_thumbs_file_path);*/


                        Splash_Screen.editor.putString("exam_id", exam_id);
                        Splash_Screen.editor.putString("get_stdname",get_stdname);
                        Splash_Screen.editor.putString("SDMS_enrolment_number", enrol);
                        Splash_Screen.editor.putString("aadhaarNum", adhaar);
                        Splash_Screen.editor.putString("StdMobile", mobile);
                        Splash_Screen.editor.putString("StdEmail", email);
                        Splash_Screen.editor.putString("Gname", g_name);
                        Splash_Screen.editor.putString("student_id", student_id);
                        Splash_Screen.editor.putString("student_image", student_image_path);
                        Splash_Screen.editor.putString("assessor_image", assessor_image_path);
                        Splash_Screen.editor.putString("ssc_id", ssc_id);
                        Splash_Screen.editor.putString("trade_id", trade_id);
                        Splash_Screen.editor.putString("tb_id",tb_id);
                        Splash_Screen.editor.putString("tb_nsdc_id",tb_nsdc_id);
                        Splash_Screen.editor.putString("tb_name",tb_name);
                        Splash_Screen.editor.putString("trade_code",trade_code);
                        Splash_Screen.editor.putString("trade_title",trade_title);
                        Splash_Screen.editor.putString("Snap", take_snapshots);
                        Splash_Screen.editor.putString("exam_otp" , exam_otp);
                        Splash_Screen.editor.putString("dob", dob);
                        Splash_Screen.editor.putString("address", address);
                        Splash_Screen.editor.putString("state_id", state_id);
                        Splash_Screen.editor.putString("city",city);
                        Splash_Screen.editor.putString("dist_id",dist_id);
                        Splash_Screen.editor.putString("pincode",pincode);
                        Splash_Screen.editor.putString("ftp_snapshot_host", ftp_snapshot_host);
                        Splash_Screen.editor.putString("ftp_snapshot_username",ftp_snapshot_username);
                        Splash_Screen.editor.putString("ftp_snapshot_password",ftp_snapshot_password);
                        Splash_Screen.editor.commit();

                        Splash_Screen.editor.putString("loginTest", "true");
                        Splash_Screen.editor.commit();

                        Intent intent = new Intent(Login.this,MainActivity.class);
                        Toast.makeText(getApplicationContext(),
                                "You have successfully login", Toast.LENGTH_LONG).show();
                        startActivity(intent);


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
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

}

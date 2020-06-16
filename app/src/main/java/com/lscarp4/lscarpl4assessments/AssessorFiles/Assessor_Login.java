package com.lscarp4.lscarpl4assessments.AssessorFiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.lscarp4.lscarpl4assessments.Database.DBHelper;
import com.lscarp4.lscarpl4assessments.R;
import com.lscarp4.lscarpl4assessments.StudentFiles.Login;
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

public class Assessor_Login extends Activity {


    EditText uname,pwd;
    String get_username,get_password;
    TextView login,stdL;
    CheckBox show_pwd;
    DBHelper mydb;

    AlertDialog alertDialog = null;
    NetworkChangeReceiver br;
    String s_code,s_fname,s_lname,s_gender,s_a1,s_a2,s_city,s_pin,s_state,s_dist,s_phone,s_mob,s_mob2,s_email,s_aemail,
            s_pic,s_adhar,s_qua,s_exp,s_resume;

    final static String Login_url = "http://lscmis.com/arpl4/assessor/assessor_api/validate_assessor_login";

    private static final String API_KEY = "XDwFWzbaULYIA0Uub5QLhH9hxXRL1A6t";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessor_login);

        br = new NetworkChangeReceiver();
        mydb = new DBHelper(this);

        if (haveNetworkConnection()) {

        } else {
            Toast.makeText(Assessor_Login.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();
        }


        uname = (EditText)findViewById(R.id.uname);
        pwd = (EditText)findViewById(R.id.pwd);
        login = (TextView)findViewById(R.id.login);
        stdL =(TextView)findViewById(R.id.stdL);
        show_pwd=(CheckBox)findViewById(R.id.checkBox1);

        stdL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Assessor_Login.this,Login.class);
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
                    Toast.makeText(Assessor_Login.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();
                }



            }
        });

    }


    public void dialogBoxForInternet() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Assessor_Login.this);
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

                        JSONObject jObj_1 = jsonObject.getJSONObject("logged_in");

                        String auid = jObj_1.getString("auid");
                        String ut_id = jObj_1.getString("ut_id");
                        String username = jObj_1.getString("username");
                        String user_type = jObj_1.getString("user_type");

                        JSONObject jObj_2 = jsonObject.getJSONObject("arr_assessor_profile_details");

                        s_code = jObj_2.getString("assessor_code");
                        s_fname = jObj_2.getString("assessor_firstname");
                        s_lname = jObj_2.getString("assessor_lastname");
                        s_gender = jObj_2.getString("assessor_gender");
                        s_a1 = jObj_2.getString("assessor_address1");
                        s_a2 = jObj_2.getString("assessor_address2");
                        s_city = jObj_2.getString("assessor_city");
                        s_pin = jObj_2.getString("pin_code");
                        s_state = jObj_2.getString("state_id");
                        s_dist = jObj_2.getString("dist_id");
                        s_phone = jObj_2.getString("assessor_phone");
                        s_mob = jObj_2.getString("assessor_mobile");
                        s_mob2 = jObj_2.getString("assessor_mobile2");
                        s_email = jObj_2.getString("assessor_email");
                        s_aemail = jObj_2.getString("assessor_email2");
                        s_pic = jObj_2.getString("assessor_photo_path");
                        s_adhar = jObj_2.getString("assessor_aadhaar");
                        s_qua = jObj_2.getString("assessor_qualification");
                        s_exp = jObj_2.getString("assessor_year_experience");
                        s_resume = jObj_2.getString("assessor_resume_path");
                        String ass_photo = jObj_2.getString("assessor_photo");
                        String ass_resume = jObj_2.getString("assessor_resume");

                        Splash_Screen.editor.putString("s_code", s_code);
                        Splash_Screen.editor.putString("s_fname", s_fname);
                        Splash_Screen.editor.putString("s_lname", s_lname);
                        Splash_Screen.editor.putString("s_gender", s_gender);
                        Splash_Screen.editor.putString("s_a1", s_a1);
                        Splash_Screen.editor.putString("s_a2", s_a2);
                        Splash_Screen.editor.putString("s_city", s_city);
                        Splash_Screen.editor.putString("s_pin", s_pin);
                        Splash_Screen.editor.putString("s_state", s_state);
                        Splash_Screen.editor.putString("s_dist", s_dist);
                        Splash_Screen.editor.putString("s_phone", s_phone);
                        Splash_Screen.editor.putString("s_mob", s_mob);
                        Splash_Screen.editor.putString("s_mob2", s_mob2);
                        Splash_Screen.editor.putString("s_email", s_email);
                        Splash_Screen.editor.putString("s_aemail", s_aemail);
                        Splash_Screen.editor.putString("s_pic", s_pic);
                        Splash_Screen.editor.putString("s_adhar", s_adhar);
                        Splash_Screen.editor.putString("s_qua", s_qua);
                        Splash_Screen.editor.putString("s_exp", s_exp);
                        Splash_Screen.editor.putString("s_resume", s_resume);
                        Splash_Screen.editor.putString("ass_photo", ass_photo);
                        Splash_Screen.editor.putString("ass_resume", ass_resume);

                        Log.e("Ass pic&res Login", ">>>>>>>>>>>>" + ass_photo + " " + ass_resume);

                        Splash_Screen.editor.putString("Ass_uname", get_username);
                        Splash_Screen.editor.putString("Ass_pwd", get_password);
                        Splash_Screen.editor.putString("ut_id", ut_id);
                        Splash_Screen.editor.commit();

                        Splash_Screen.editor.putString("loginTestAssessor", "true");
                        Splash_Screen.editor.commit();

                        Intent intent = new Intent(Assessor_Login.this, AssessorDashboard.class);
                        Toast.makeText(getApplicationContext(),
                                "You have successfully login", Toast.LENGTH_LONG).show();
                        startActivity(intent);


                    } else if (message.equals("Access Denied ! Authentication Failed")) {


                        Toast.makeText(getApplicationContext(),
                                "Access Denied", Toast.LENGTH_LONG).show();

                    }else if (message.equals("Not Found")) {


                        Toast.makeText(getApplicationContext(),
                                "Assessor Data Not Found", Toast.LENGTH_LONG).show();

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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}

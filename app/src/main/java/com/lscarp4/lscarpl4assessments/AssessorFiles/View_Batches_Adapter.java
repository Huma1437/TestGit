package com.lscarp4.lscarpl4assessments.AssessorFiles;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.lscarp4.lscarpl4assessments.AnimationUtil;
import com.lscarp4.lscarpl4assessments.ItemClickListener;
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
import java.util.ArrayList;

public class View_Batches_Adapter extends RecyclerView.Adapter<View_Batches_Adapter.ViewHolder>  {

    ArrayList<View_batches_pojo> batches_view;
    private ItemClickListener clickListener;
    private Context mContext;
    int previousPosition = 0;
    String student_id,name,get_TBID;
    final static String not_attended = "http://lscmis.com/arpl4/assessor/assessor_api/update_absent_student_status";
    private static final String API_KEY = "XDwFWzbaULYIA0Uub5QLhH9hxXRL1A6t";
    String check_status;
    boolean isChecked;

    AlertDialog alertDialog = null;
    NetworkChangeReceiver br;
    private ProgressDialog pDialog;
    // Pass in the country array into the constructor
    public View_Batches_Adapter(ArrayList<View_batches_pojo> batches_view, Context context) {
        this.batches_view = batches_view;
        this.mContext = context;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public View_Batches_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewdetails_card, parent, false);
        br = new NetworkChangeReceiver();
        get_TBID = Splash_Screen.sh.getString("TBID", null);
        return new View_Batches_Adapter.ViewHolder(view);
    }

    // Involves populating data into the item through holder
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(View_Batches_Adapter.ViewHolder holder, final int position) {

        String fn = batches_view.get(position).getFname();
        String ln = batches_view.get(position).getLname();

        name = batches_view.get(position).getFname() + batches_view.get(position).getLname();

        if(ln.equals("null")){
            holder.stdname.setText(fn);
            name = fn;
        }else if(fn.equals("null")){
            holder.stdname.setText(ln);
            name = ln;
        }else if(!fn.equals("null") && !ln.equals("null")){
            holder.stdname.setText(fn + ln);
            name = fn + ln;
        }


        holder.adhar.setText("Aadhar No.: " + batches_view.get(position).getAdhar());
        holder.mobile.setText("Mobile: " + batches_view.get(position).getMobile());
        holder.email.setText("Email: " + batches_view.get(position).getEmail());
        holder.sdms.setText("SDMS Enrollment No.: "+ batches_view.get(position).getUname());
        holder.pwd.setText("Password: " + batches_view.get(position).getPwd());
        holder.otp.setText("Exam OTP: " + batches_view.get(position).getOtp());
        student_id = batches_view.get(position).getStd_id();

        String exam_status = batches_view.get(position).getTheory();
        final String student_reslt = batches_view.get(position).getStd_result();

        Log.e("EXAM_STATUS", ">>>>>>>>>>." + exam_status +" " +student_reslt + student_id);

        holder.not_attended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(((CompoundButton) v).isChecked()){
                    System.out.println("Checked");

                    student_id = batches_view.get(position).getStd_id();
                    check_status = "checked";
                    new SendPostRequest().execute();

                    Log.e("CHECKED","????????"  + " " + student_id + isChecked + check_status);


                } else {
                    System.out.println("Un-Checked");

                    student_id = batches_view.get(position).getStd_id();
                    check_status = "unchecked";

                    new SendPostRequest().execute();

                    Log.e("UnChecked:" ,"????????????"  + " " + student_id + isChecked + check_status);
                }
            }
        });

        if(exam_status.equals("Pending") && student_reslt.equals("None")){

            holder.take_viva.setVisibility(View.GONE);
            holder.viva.setVisibility(View.VISIBLE);
            holder.viva.setText("Exam Status: "  + "Pending");
            holder.viva.setTextColor(Color.BLACK);
            holder.theory.setText("Theory Exam: " + "Pending");
            holder.theory.setTextColor(Color.BLACK);
            holder.not_attended.setEnabled(true);
            boolean checked = false;
            holder.not_attended.setChecked(checked);

        }else if(exam_status.equals("Viva") && student_reslt.equals("None")) {

            holder.take_viva.setVisibility(View.VISIBLE);
            holder.viva.setVisibility(View.GONE);
            holder.theory.setText("Theory Exam: " + "Completed");
            holder.theory.setTextColor(mContext.getResources().getColor(R.color.darkgreen));
            holder.not_attended.setEnabled(false);
            boolean checked = false;
            holder.not_attended.setChecked(checked);

        }else if(exam_status.equals("Completed") && student_reslt.equals("Not Attended")){

            holder.take_viva.setVisibility(View.GONE);
            holder.viva.setVisibility(View.VISIBLE);
            holder.viva.setText("Viva Exam: "  + "Not Attended");
            holder.viva.setTextColor(Color.RED);
            holder.theory.setText("Theory Exam: " + "Not Attended");
            holder.theory.setTextColor(Color.RED);
            holder.not_attended.setEnabled(true);
            boolean checked = true;
            holder.not_attended.setChecked(checked);

        } else if(exam_status.equals("Completed") && student_reslt.equals("None")){

            holder.take_viva.setVisibility(View.GONE);
            holder.viva.setVisibility(View.VISIBLE);
            holder.viva.setText("Viva Exam: "  + "Completed");
            holder.viva.setTextColor(mContext.getResources().getColor(R.color.darkgreen));
            holder.theory.setText("Theory Exam: " + "Completed");
            holder.theory.setTextColor(mContext.getResources().getColor(R.color.darkgreen));
            holder.not_attended.setEnabled(false);
            boolean checked = false;
            holder.not_attended.setChecked(checked);
        }


        holder.take_viva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fn = batches_view.get(position).getFname();
                String ln = batches_view.get(position).getLname();

                name = batches_view.get(position).getFname() + batches_view.get(position).getLname();

                if(ln.equals("null")){
                    name = fn;
                }else if(fn.equals("null")){

                    name = ln;
                }else if(!fn.equals("null") && !ln.equals("null")){

                    name = fn + ln;
                }

                Intent intent = new Intent(mContext, VivaExam.class);
                intent.putExtra("student_id", batches_view.get(position).getStd_id());
                intent.putExtra("sdms", batches_view.get(position).getUname());
                intent.putExtra("name", name);
                mContext.startActivity(intent);

            }
        });


        //Getting the position of items in recyclerview
        if(position > previousPosition){ //we are scrolling DOWN

            AnimationUtil.animate(holder, true);

        }else{  //we are scrolling UP

            AnimationUtil.animate(holder, false);
        }

        previousPosition = position;
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return batches_view == null ? 0 : batches_view.size();
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public View_batches_pojo view_batch_pojo(int position) {
        return batches_view.get(position);
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* Your holder should contain a member variable
     for any view that will be set as you render a row */
        TextView stdname,adhar,mobile,email,sdms,pwd,otp,theory,viva;
        Button take_viva;
        CheckBox not_attended;
        LinearLayout hideL;


        /*constructor that accepts the entire item row
             and does the view lookups to find each subview */
        public ViewHolder(View itemView) {

            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            stdname = (TextView) itemView.findViewById(R.id.stdname);
            adhar = (TextView) itemView.findViewById(R.id.adharNum);
            mobile = (TextView) itemView.findViewById(R.id.mob);
            email = (TextView) itemView.findViewById(R.id.email);
            sdms = (TextView) itemView.findViewById(R.id.sdms);
            pwd = (TextView) itemView.findViewById(R.id.pwd);
            otp = (TextView) itemView.findViewById(R.id.exam_otp);
            theory = (TextView) itemView.findViewById(R.id.exam_status);
            viva = (TextView) itemView.findViewById(R.id.viva_status);
            hideL = (LinearLayout) itemView.findViewById(R.id.hideL);

            take_viva = (Button) itemView.findViewById(R.id.viva);

            not_attended = (CheckBox)itemView.findViewById(R.id.not);
            itemView.setTag(itemView);

        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
            // call the onClick in the OnItemClickListener

        }

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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

                Toast.makeText(mContext, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();

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

                    if (message.equals("Success")) {

                        String tb_ID = jsonObject.getString("tb_id");
                        String student_id = jsonObject.getString("student_id");
                        String student_result = jsonObject.getString("student_result");
                        String exam_status = jsonObject.getString("exam_status");
                        String assessment_status = jsonObject.getString("assessment_status");

                        Log.e("GET STDRESLT", ">>>>>>>>>>>" + student_result + exam_status + student_id);

                        Intent intent = new Intent(mContext,Batches_Assessed.class);
                        mContext.startActivity(intent);

                        Log.e("msg = success", ">>>>>>>>>>>>>>>>" + message);


                    } else if (message.equals("Access Denied ! Authentication Failed")) {

                        Toast.makeText(mContext,
                                message, Toast.LENGTH_LONG).show();

                    }else if (message.equals("No Action Taken")) {

                        Toast.makeText(mContext,
                                message, Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObject(not_attended, makingJson());

        }
    }


    private JSONObject makingJson() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", API_KEY);
            postDataParams.put("tb_id", get_TBID);
            postDataParams.put("student_id", student_id);
            postDataParams.put("checked_status", check_status);

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
}

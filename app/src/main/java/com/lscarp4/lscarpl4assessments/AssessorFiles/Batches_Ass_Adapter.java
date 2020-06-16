package com.lscarp4.lscarpl4assessments.AssessorFiles;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.lscarp4.lscarpl4assessments.AnimationUtil;
import com.lscarp4.lscarpl4assessments.Database.DBHelper;
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
import java.util.Iterator;

public class Batches_Ass_Adapter extends RecyclerView.Adapter<Batches_Ass_Adapter.ViewHolder>  {

    ArrayList<Batches_Assigned_Pojo> batches_assigned_pojos;
    private ItemClickListener clickListener;
    private Context mContext;
    int previousPosition = 0;
    String tc_name,tb_name,batch_name,tb_start_date_time,tb_end_date_time,tb_assessment_status,tc_id,tb_exam_type,
            tb_id,tb_target,e_id,sscid,trade_id,tp_id,qp_shuffling;
    AlertDialog alertDialog = null;
    NetworkChangeReceiver br;
    private ProgressDialog pDialog;
    // Progress dialog type (0 - for Horizontal progress bar)
    String get_TBID;
    DBHelper mydb;
    ArrayList<String> studdent_IDS;

    final static String download_info = "http://lscmis.com/arpl4/assessor/assessor_api/get_download_information";

    private static final String API_KEY = "XDwFWzbaULYIA0Uub5QLhH9hxXRL1A6t";
    String std_id,sdms,fname,lname,pwd,mob,email,adhar,dob,address,city,state,district,pincode,
            examStatus,enrol_status,otp,trade_code,trade_title,exam_duration,image_dir_source,image_dir,
            exam_name,total_question,exam_id,tb_nsdc_id,TBNAME;

    // Pass in the country array into the constructor
    public Batches_Ass_Adapter(ArrayList<Batches_Assigned_Pojo> batches_assigned_pojos, Context context) {
        this.batches_assigned_pojos = batches_assigned_pojos;
        this.mContext = context;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public Batches_Ass_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.batches_assessed_card, parent, false);

        br = new NetworkChangeReceiver();
        mydb = new DBHelper(mContext);

        return new ViewHolder(view);
    }

    // Involves populating data into the item through holder
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(Batches_Ass_Adapter.ViewHolder holder, final int position) {

        holder.batch_ID.setText("Batch ID: " + batches_assigned_pojos.get(position).getTb_name());
        holder.bname.setText("Batch Name: " + batches_assigned_pojos.get(position).getBatch_name());
        holder.job_role.setText("Job Role Name: " + batches_assigned_pojos.get(position).getTrade_title());
        holder.start_date.setText("Start Date: " +batches_assigned_pojos.get(position).getTb_start_date_time());
        holder.end_date.setText("End Date: " + batches_assigned_pojos.get(position).getTb_end_date_time());
        holder.stdnum.setText("No. of Students: "+ batches_assigned_pojos.get(position).getTb_target());

        String TBID =  batches_assigned_pojos.get(position).getTb_id();

        holder.d_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (haveNetworkConnection()) {

                    get_TBID = batches_assigned_pojos.get(position).getTb_id();

                    Log.e("TB CLICK",">>>>>>>>" + get_TBID);

              //     new SendPostRequest().execute();

                } else {
                    Toast.makeText(mContext, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();
                }

                Toast.makeText(mContext, "DINFO " + batches_assigned_pojos.get(position).getTb_name(), Toast.LENGTH_LONG).show();
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,View_Details.class);
                intent.putExtra("batchid", batches_assigned_pojos.get(position).getTb_name());
                intent.putExtra("batchname", batches_assigned_pojos.get(position).getBatch_name());
                intent.putExtra("jobrole",  batches_assigned_pojos.get(position).getTrade_title());
                intent.putExtra("startDate", batches_assigned_pojos.get(position).getTb_start_date_time());
                intent.putExtra("endDate", batches_assigned_pojos.get(position).getTb_end_date_time());
                intent.putExtra("stdnum", batches_assigned_pojos.get(position).getTb_target());
                intent.putExtra("tb_id", batches_assigned_pojos.get(position).getTb_id());

                Splash_Screen.editor.putString("TBID",  batches_assigned_pojos.get(position).getTb_id());
                Splash_Screen.editor.commit();

                mContext.startActivity(intent);
                Toast.makeText(mContext, "VIEW " + batches_assigned_pojos.get(position).getTb_name(), Toast.LENGTH_LONG).show();
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,Update_Batch_Status.class);
                intent.putExtra("tb_id", batches_assigned_pojos.get(position).getTb_id());
                mContext.startActivity(intent);
                Toast.makeText(mContext, "UPDATE " + batches_assigned_pojos.get(position).getTb_name(), Toast.LENGTH_LONG).show();
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
        return batches_assigned_pojos == null ? 0 : batches_assigned_pojos.size();
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public Batches_Assigned_Pojo batches_pojo(int position) {
        return batches_assigned_pojos.get(position);
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


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* Your holder should contain a member variable
     for any view that will be set as you render a row */
        TextView batch_ID,bname,job_role,start_date,end_date,stdnum,downloaded;
        Button d_info,view,update;


        /*constructor that accepts the entire item row
             and does the view lookups to find each subview */
        public ViewHolder(View itemView) {

            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            batch_ID = (TextView) itemView.findViewById(R.id.batch_id);
            bname = (TextView) itemView.findViewById(R.id.batch_name);
            job_role = (TextView) itemView.findViewById(R.id.jrole);
            start_date = (TextView) itemView.findViewById(R.id.sdate);
            end_date = (TextView) itemView.findViewById(R.id.edate);
            stdnum = (TextView) itemView.findViewById(R.id.stdnum);
            downloaded = (TextView) itemView.findViewById(R.id.downloaded);

            d_info = (Button) itemView.findViewById(R.id.dinfo);
            view = (Button) itemView.findViewById(R.id.view);
            update = (Button) itemView.findViewById(R.id.update);
            itemView.setTag(itemView);

            d_info.setOnClickListener(this);// bind the listener
            view.setOnClickListener(this);// bind the listener
            update.setOnClickListener(this);// bind the listener


        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
            // call the onClick in the OnItemClickListener

            tc_name = batches_pojo(getAdapterPosition()).getTc_name();
            tb_name = batches_pojo(getAdapterPosition()).getTb_name();
            batch_name = batches_pojo(getAdapterPosition()).getBatch_name();
            tb_start_date_time = batches_pojo(getAdapterPosition()).getTb_start_date_time();
            tb_end_date_time = batches_pojo(getAdapterPosition()).getTb_end_date_time();
            tb_assessment_status = batches_pojo(getAdapterPosition()).getTb_assessment_status();
            tc_id = batches_pojo(getAdapterPosition()).getTc_id();
            tb_exam_type = batches_pojo(getAdapterPosition()).getTb_exam_type();
            tb_id = batches_pojo(getAdapterPosition()).getTb_id();
            tb_target = batches_pojo(getAdapterPosition()).getTb_target();
            e_id = batches_pojo(getAdapterPosition()).getE_id();
            sscid =batches_pojo(getAdapterPosition()).getSscid();
            trade_id = batches_pojo(getAdapterPosition()).getTrade_id();
            tp_id = batches_pojo(getAdapterPosition()).getTp_id();
            qp_shuffling = batches_pojo(getAdapterPosition()).getQp_shuffling();

        }

    }

    public class SendPostRequest extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

            // Showing progress dialog
            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage("Downloading File...Please wait");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            studdent_IDS = new ArrayList<>();
            try {


                if (jsonObject != null && jsonObject.getString("status_message") != null) {

                    String message = jsonObject.getString("status_message");  // Message

                    Log.e("ADAPTER MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.equals("Success")) {

                        Log.e("msg = success", ">>>>>>>>>>>>>>>>" + message);

                         exam_id = jsonObject.getString("exam_id");
                         tb_nsdc_id = jsonObject.getString("tb_nsdc_id");
                         TBNAME = jsonObject.getString("tb_name");
                         String take_snaps = jsonObject.getString("take_snapshots");

                         trade_title = jsonObject.getString("trade_title");
                         exam_duration = jsonObject.getString("exam_duration");
                         image_dir_source = jsonObject.getString("image_dir_source");
                         image_dir = jsonObject.getString("image_dir");
                         exam_name = jsonObject.getString("exam_name");
                         total_question = jsonObject.getString("total_question");
                         trade_code = jsonObject.getString("trade_code");

                        JSONArray std_details = jsonObject.getJSONArray("student_details");

                        for (int y = 0; y < std_details.length(); y++) {

                            JSONObject jObj = std_details.getJSONObject(y);

                             std_id = jObj.getString("student_id");
                             sdms = jObj.getString("SDMS_enrolment_number");
                             fname = jObj.getString("first_name");
                             lname = jObj.getString("last_name");
                             pwd = jObj.getString("password");
                             mob = jObj.getString("student_mobile");
                             email = jObj.getString("student_email");
                             adhar = jObj.getString("aadhaar_number");
                             dob = jObj.getString("date_of_birth");
                             address = jObj.getString("address");
                             city = jObj.getString("city");
                             state = jObj.getString("state_id");
                             district = jObj.getString("dist_id");
                             pincode = jObj.getString("pincode");
                             examStatus = jObj.getString("exam_status");
                             enrol_status = jObj.getString("enrollment_form_status");
                             otp = jObj.getString("exam_otp");
                             String gname = jObj.getString("guardian_name");
                             String sscid = jObj.getString("sscid");
                             String tradeID = jObj.getString("trade_id");
                             String tbID = jObj.getString("tb_id");

                             studdent_IDS.add(std_id);
                             Log.e("STUDENT ID", ">............" + studdent_IDS);


                            Log.e("DOWNLOADED", ">>>>>>>>>>>>>..."  + " " + trade_title + " "+
                                    exam_duration + " " + image_dir + " " + image_dir_source + " " + exam_name + " "+
                                    total_question + " " + std_id + " " + sdms + " "+ fname + " " + lname + " "+
                                    pwd + " " + mob + " " + email+ " " + adhar + " "+ dob + " "+ address + " "+ city + " "+
                                    state + " "+ district + " "+ pincode + " "+ examStatus + " " + enrol_status + " "+ otp + " " + tb_nsdc_id + " " + tb_name);

                        }

                        JSONObject jObject = new JSONObject(jsonObject.toString()).getJSONObject("questions");

                        Log.e("Json", "onCreate: " + jObject.toString());

                        Iterator<String> keys = jObject.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            Log.e("Json", "**********");
                            Log.e("Json key", key);

                            JSONArray ja_data = jObject.getJSONArray(key);
                            int length = jObject.length();

                            for(int i=0;i<ja_data.length();i++){
                                JSONObject json = ja_data.getJSONObject(i);
                                String nos_id = json.getString("nos_id");
                                String nos_code =json.getString("nos_code");
                                String nos_title = json.getString("nos_title");
                                String q_id = json.getString("q_id");
                                String question = json.getString("question");
                                String option_a = json.getString("option_a");
                                String option_b = json.getString("option_b");
                                String option_c = json.getString("option_c");
                                String option_d = json.getString("option_d");
                                String lang_question = json.getString("lang_question");
                                String lang_option_a = json.getString("lang_option_a");
                                String lang_option_b = json.getString("lang_option_b");
                                String lang_option_c = json.getString("lang_option_c");
                                String lang_option_d = json.getString("lang_option_d");

                                Log.e("Json question", "nos_id: "+ nos_id + "****** nos_code: " +nos_code  + " " + nos_title +
                                        " " + q_id + " " + question + " " + option_a + " " + option_b + " " + option_c + " " + option_d +
                                        " " + lang_question + " " + lang_option_a + " " + lang_option_b + " " + lang_option_c + " " + lang_option_d);
                            }

                        }

                        pDialog.dismiss();
                        Intent intent = new Intent(mContext,AssessorDashboard.class);
                        mContext.startActivity(intent);

                        Toast.makeText(mContext, "Information Downloaded Successfully", Toast.LENGTH_LONG).show();


                    } else if (message.equals("Access Denied ! Authentication Failed")) {


                    }else if (message.equals("Not Found")) {


                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObject(download_info, makingJson());

        }
    }


    private JSONObject makingJson() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", API_KEY);
            postDataParams.put("tb_id", get_TBID);

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



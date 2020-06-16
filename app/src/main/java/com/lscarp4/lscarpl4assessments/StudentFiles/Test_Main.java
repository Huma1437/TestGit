package com.lscarp4.lscarpl4assessments.StudentFiles;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.lscarp4.lscarpl4assessments.Database.DBHelper;
import com.lscarp4.lscarpl4assessments.GPSTracker;
import com.lscarp4.lscarpl4assessments.NetworkSchedulerService;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.lscarp4.lscarpl4assessments.StudentFiles.Splash_Screen.editor;


public class Test_Main extends AppCompatActivity implements View.OnClickListener{

    TextView header,duration,time_left,question,op1,op2,op3,op4,clear,ans,not_ans,not_visited,review_later;
    TextView hindi_question,hindi_op1,hindi_op2,hindi_op3,hindi_op4;
    Button back,previous,next,review,finish,view;
    ImageView logout,desc;

    AlertDialog alertDialog=null;
    NetworkChangeReceiver br;
    private ProgressDialog pDialog;
    int id=0, position=0;
    JSONObject reader;
    DBHelper mydb;
    String exam_StartTym,exam_EndTym,ip,exam_date;

    Dialog dialog;
    String b_ID,b_name,j_role,enrol_num;
    TextView s_name,e_num,batch_id,bname,jobRole;

    String get_examID,get_stdname,get_stdID,get_stdImage,get_assImage,get_sscID,get_tradeID,get_tbID,get_snap,get_exmName,get_tradeCode,get_tradeTitle,get_enrollment_form;
    String get_img_dir_src,get_img_dir, get_duration,nos_id,nos_code,nos_title,q_id,Question,typeofquestion,option_a,option_b,option_c,option_d;
    String get_hques,get_hop1,get_hop2,get_hop3,get_hop4;
    String path1,path2,path3;
    ArrayList<String> CompletePath;
    static final int LOCATION_REQUEST = 1;


    int assign_Qnum = 1;
    String get_asqn;
    int get_totalqns;
    final static String Questions_url = "http://lscmis.com/arpl4/student/student_api/get_exam_questions";
    final static String Answers_url = "http://lscmis.com/arpl4/student/student_api/main_result";
    String base64;
    private static final String API_KEY = "YIA0Uub5QLhH9hxXRL1A6tXDwFWzbaUL";

    String completedTime,time,get_selectedOpt;
    int numofQue,dynamicTimer;
    Handler handler;
    Long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    int Hours, Seconds, Minutes, MilliSeconds ;
    private static final String FORMAT = "%02d:%02d:%02d";
    private CountDownTimer mCountDown;
    LinearLayout l1,l2,l3,l4;
    ImageView quesImage,op1_image,op2_image,op3_image,op4_image;
    private static final int PERMISSION_REQUEST_CODE = 200;
    String Image_url,E_name;
    String DbQnum,Dbq_id,DbSelectedOpt,m_review,notans,notvisit,qns,opa,opb,opc,opd,hqns,hopa,hopb,hopc,hopd;
    String option_Name,get_OPname,get_OP;
    int notVisited,not_answered,answered,reviewLater;
    ArrayList<String> QuestionNumbers = new ArrayList<>();
    public final static String DEBUG_TAG = "Test_Main";
    public Camera camera;
    public int cameraId = 0;
    ViewPaletteFragment viewPaletteFragment;
    String fileOutput,get_snames,get_sdate,img_name_date;
    ArrayList<String> SnapsNames;
    ArrayList<String> QuestionsID;
    ArrayList<String> SelectedAnswers;
    ArrayList<String> SnapsDate;
    ArrayList<String> Image_Path;
    ScrollView scrollView;
    ArrayList<String> newQues;
    File fileDirectory;
    ArrayList<String> Arr_StudentIDS;
    String get;
    String a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,aoptions,getb64,getQid,get_Snapshots;
    ArrayList<String> getExactQnum;
    ArrayList<String> getDbsltAns;
    String text1,text2;
    double latitude,longitude;
    String address;
    Geocoder geocoder;
    private GPSTracker gpsTracker;
    List<Address> addresses;
    Context context;
    NetworkSchedulerService schedulerService;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);

        br = new NetworkChangeReceiver();
        mydb = new DBHelper(this);
        newQues = new ArrayList<>();
        schedulerService = new NetworkSchedulerService();

        //Arraylist for storing selected Answers and snapshots date
        SelectedAnswers = new ArrayList<>();
        SnapsDate = new ArrayList<>();
        Image_Path = new ArrayList<>();
        CompletePath = new ArrayList<>();

        geocoder = new Geocoder(this, Locale.getDefault());

        get_examID = Splash_Screen.sh.getString("exam_id", null);
        get_stdname = Splash_Screen.sh.getString("get_stdname", null);
        get_stdID = Splash_Screen.sh.getString("student_id", null);
        get_stdImage = Splash_Screen.sh.getString("student_image", null);
        get_assImage = Splash_Screen.sh.getString("assessor_image", null);
        get_sscID = Splash_Screen.sh.getString("ssc_id", null);
        get_tradeID = Splash_Screen.sh.getString("trade_id", null);
        get_tbID = Splash_Screen.sh.getString("tb_id", null);
        E_name = Splash_Screen.sh.getString("Exam_name", null);

        b_name = Splash_Screen.sh.getString("tb_name", null);
        j_role = Splash_Screen.sh.getString("trade_title", null);
        b_ID = Splash_Screen.sh.getString("tb_nsdc_id", null);
        enrol_num = Splash_Screen.sh.getString("SDMS_enrolment_number", null);

        Splash_Screen.editor.putString("get_tbID", get_tbID);
        editor.commit();

        get_snap = Splash_Screen.sh.getString("Snap", null);

        Log.e("SNAPSHOT", ">>>>>>>>>>>>>>>>>>>.." + get_snap);

        header = (TextView)findViewById(R.id.header);
        duration = (TextView) findViewById(R.id.duration);
        time_left = (TextView) findViewById(R.id.time_left);
        question = (TextView)findViewById(R.id.ques);
        op1 = (TextView) findViewById(R.id.op1);
        op2 = (TextView) findViewById(R.id.op2);
        op3 = (TextView) findViewById(R.id.op3);
        op4 = (TextView) findViewById(R.id.op4);

        //for displaying questions and ans in hindi
        hindi_question = (TextView)findViewById(R.id.hindi_ques);
        hindi_op1 = (TextView) findViewById(R.id.hindi_op1);
        hindi_op2 = (TextView) findViewById(R.id.hindi_op2);
        hindi_op3 = (TextView) findViewById(R.id.hindi_op3);
        hindi_op4 = (TextView) findViewById(R.id.hindi_op4);


        clear = (TextView) findViewById(R.id.clear);
        ans = (TextView) findViewById(R.id.cir1);
        not_visited = (TextView) findViewById(R.id.cir2);
        not_ans = (TextView) findViewById(R.id.cir3);
        review_later = (TextView)findViewById(R.id.cir4);


        previous = (Button) findViewById(R.id.pre);
        next = (Button)findViewById(R.id.next);
        review = (Button) findViewById(R.id.review);
        finish = (Button) findViewById(R.id.finish);
        view = (Button) findViewById(R.id.palatte);

        l1 = (LinearLayout)findViewById(R.id.op1Layout);
        l2 = (LinearLayout)findViewById(R.id.op2Layout);
        l3 = (LinearLayout) findViewById(R.id.op3Layout);
        l4 = (LinearLayout) findViewById(R.id.op4Layout);

        quesImage = (ImageView) findViewById(R.id.quesImg);
        op1_image = (ImageView) findViewById(R.id.optImg1);
        op2_image = (ImageView) findViewById(R.id.optImg2);
        op3_image = (ImageView) findViewById(R.id.optImg3);
        op4_image = (ImageView) findViewById(R.id.optImg4);

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        //clear button click listener... clears selected option for that question.
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearAll();
            }
        });

        //click listener of each option
        l1.setOnClickListener(this);
        l2.setOnClickListener(this);
        l3.setOnClickListener(this);
        l4.setOnClickListener(this);


        // Get connect mangaer
        final ConnectivityManager connMgr = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // check for wifi
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        // check for mobile data
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if( wifi.isAvailable() ) {
            //getting the ip address of the device/mobile
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

            Log.e("WIFI IPADDRESSS",">>>>>>>>>>" +ip);

        } else if( mobile.isAvailable() ) {

            //Get the ip address of the device when connected to cellular network
            ip = getMobileIPAddress();

            Log.e("MOBILE DATA IPADDRESSS",">>>>>>>>>>" +ip);

        } else {
            //   networkStatus = "noNetwork";
        }


        //getting the value of question number from view pallette.
        if(get_asqn != null){
            //assigning the value of question number from pallette to qnum.
            assign_Qnum = Integer.parseInt(get_asqn);
            Log.e("GET_SELECTED_NUM",">>>>>>>>>>>>" +get_asqn);
        }


        //mark for review click listener
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //check if student selected any option for that question...
                    //Allow to mark the question only if option is selected for it else not.
                    if(option_Name == null){

                        Toast.makeText(getApplicationContext(), "Select the option please", Toast.LENGTH_SHORT).show();
                    }else{
                        boolean recordExists= mydb.checkIfRecordExist(mydb.TABLE_NAME ,mydb.COLUMN_1 ,Dbq_id);
                        if(recordExists)
                        {
                            //do your stuff
                            selectUpdateReview(Dbq_id,0,1);
                            Log.e("SELECTED", "??????????" + String.valueOf(1));

                            Log.e("RECORDEXIST",">>>>>>>>>.." + recordExists);

                        }else{


                        }

                        getUpdateOnAllClicks();

                        Toast.makeText(getApplicationContext(), "MARK CLICKED", Toast.LENGTH_SHORT).show();
                    }
            }
        });


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //if any of the question is not answered don't allow the student to submit the answers.
                    if(not_answered == 0){
                        submitDialogBox();
                    }else{

                        Toast.makeText(getApplicationContext(), "Please Answer all the Questions", Toast.LENGTH_SHORT).show();
                    }

            }
        });

        handler = new Handler();
        MillisecondTime = 0L;
        StartTime = 0L;
        TimeBuff = 0L;
        UpdateTime = 0L;
        Seconds = 0;
        Hours = 0;
        Minutes = 0;
        MilliSeconds = 0;


        //click listener to view the pallette
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showEditDialog();
                Log.e("VIEWPALETTECLICK", ">>>>>>>>>>>>>>>> " +"");
            }
        });


        back = (Button) findViewById(R.id.backbtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Test_Main.this,Main_Exam.class);
                startActivity(intent);
            }
        });


        // setting the header for action bar.
        header.setText(E_name);

        desc = (ImageView) findViewById(R.id.desc);

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(Test_Main.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialogue_desc);
                dialog.show();

                s_name = (TextView) dialog .findViewById(R.id.stdname);
                e_num = (TextView) dialog.findViewById(R.id.e_num);
                batch_id = (TextView) dialog .findViewById(R.id.batchId);
                bname = (TextView) dialog.findViewById(R.id.batchN);
                jobRole = (TextView) dialog.findViewById(R.id.jobrole);

                s_name.setText(get_stdname);
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

        next.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UnsupportedChromeOsCameraSystemFeature")
            @Override
            public void onClick(View v) {

             //   if(haveNetworkConnection()){

                    //increament the position and the question number
                    position++;
                    assign_Qnum++;

                    Log.e("GETPOS",">>>>>>>>>>>>>." + position + assign_Qnum);

                    //get the next question from the database
                    getData();

                    //calculate the middle number of the total questions
                    int getHalf = (1 + (get_totalqns)/2);

                    QuesOption();
                    //making scrollview to focus to top when going to next question
                    scrollView.fullScroll(ScrollView.FOCUS_UP);

                    if(get_snap.equals("Yes")){


                        if(assign_Qnum == getHalf) {
                            // do we have a camera?
                            if (!getPackageManager()
                                    .hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                                Toast.makeText(Test_Main.this, "No camera on this device", Toast.LENGTH_LONG)
                                        .show();

                                Log.e("No camera",">>>>>>>>>>>>>" + cameraId);
                            } else {
                                cameraId = findFrontFacingCamera();

                                Log.e("findFrontFacingCamera",">>>>>>>>>>>>>" + cameraId);
                                if (cameraId < 0) {
                                    Toast.makeText(Test_Main.this, "No front facing camera found.",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    camera = Camera.open(cameraId);

                                    try {
                                        camera.setPreviewTexture(new SurfaceTexture(10));
                                    } catch (IOException e1) {
                                        Log.e("VERSION", e1.getMessage());
                                    }


                                    camera.startPreview();
                                    camera.takePicture(null, null,
                                            new PhotoHandler(getApplicationContext(), get_stdID,get_tbID,latitude,longitude));


                                    Log.e("camera > 0",">>>>>>>>>>>>>" + cameraId);
                                }
                            }
                        }


                    }else if(get_snap.equals("No")){

                        //if take_snapshot: "No" pass null values for snapshots while submitting answers
                        base64 = "null";
                        get_snames = "null";
                        get_sdate = "null";

                    }


                    if (assign_Qnum == get_totalqns) {

                        next.setVisibility(View.GONE);
                    }else{
                        next.setEnabled(true);
                    }
                    previous.setEnabled(true);


                    if(get_snap.equals("Yes")){

                        if(assign_Qnum == get_totalqns){
                            // do we have a camera?
                            if (!getPackageManager()
                                    .hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                                Toast.makeText(Test_Main.this, "No camera on this device", Toast.LENGTH_LONG)
                                        .show();

                                Log.e("No camera",">>>>>>>>>>>>>" + cameraId);
                            } else {
                                cameraId = findFrontFacingCamera();

                                Log.e("findFrontFacingCamera",">>>>>>>>>>>>>" + cameraId);
                                if (cameraId < 0) {
                                    Toast.makeText(Test_Main.this, "No front facing camera found.",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    camera = Camera.open(cameraId);

                                    try {
                                        camera.setPreviewTexture(new SurfaceTexture(10));
                                    } catch (IOException e1) {
                                        Log.e("VERSION", e1.getMessage());
                                    }


                                    camera.startPreview();
                                    camera.takePicture(null, null,
                                            new PhotoHandler(getApplicationContext(), get_stdID,get_tbID,latitude,longitude));
                                    Log.e("camera > 0",">>>>>>>>>>>>>" + cameraId);
                                }
                            }
                        }

                    }else if(get_snap.equals("No")){

                        //if take_snapshot: "No" pass null values for snapshots while submitting answers
                        base64 = "null";
                        get_snames = "null";
                        get_sdate = "null";

                    }

              /*  }else{

                    Toast.makeText(Test_Main.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();

                }*/

            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   if(haveNetworkConnection()){

                    position--;
                    assign_Qnum--;

                    Log.e("GETPOS",">>>>>>>>>>>>>." + position + assign_Qnum);

                    getData();
                    QuesOption();
                    scrollView.fullScroll(ScrollView.FOCUS_UP);

                    if(assign_Qnum == (get_totalqns-1)){
                        next.setVisibility(View.VISIBLE);
                    }else if(assign_Qnum == 1){
                        previous.setEnabled(false);
                        scrollView.fullScroll(ScrollView.FOCUS_UP);

                    }else{
                        previous.setEnabled(true);
                    }

              /*  }else{

                    Toast.makeText(Test_Main.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();

                }*/
            }
        });

        logout = (ImageView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "You have successfully logout",
                        Toast.LENGTH_LONG).show();
                editor.remove("loginTest");
                editor.commit();

                Intent intent = new Intent(Test_Main.this,Login.class);
                startActivity(intent);

            }
        });


        if (!checkPermission()) {

            mainMethod();
        } else {
            if (checkPermission()) {
                requestPermissionAndContinue();
            } else {
                mainMethod();
            }
        }

        database();

        if(haveNetworkConnection()){

            final String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if (locationProviders == null || locationProviders.equals("")) {
                //Alert dialog box to request location from user
                new AlertDialog.Builder(Test_Main.this)
                        .setTitle("Use Location?")
                        .setMessage("This app wants to enable your GPS for location.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                                nextactivity(1);
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert).show();

            } else {

                gpsTracker = new GPSTracker(Test_Main.this);
                Log.e("HAVE CONN", ">>>>>>>>>" + "");

                Log.e("GPS ENABLED", "???????????" + "");

                if (gpsTracker.canGetLocation()) {
                    latitude = gpsTracker.getLatitude();
                    longitude = gpsTracker.getLongitude();

                    Log.e("LAT & LONG", ">>>>>>>>>>>>>>>>." + latitude + " " + longitude);

                    try {
                        Log.e("latitude", "inside latitude--" + latitude);
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (addresses != null && addresses.size() > 0) {
                            address = addresses.get(0).getAddressLine(0);

                            Log.e("GET LOCATION", ">>>>>>" + latitude + " " + longitude + " " + address);

                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }else{

            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        }

    }

    public void nextactivity(final int requestCode) {

        if (requestCode == LOCATION_REQUEST) {
            handler = new Handler();

//            flipit(image);
            /****** Create Thread that will sleep for 5 seconds *************/
            Thread background = new Thread() {
                public void run() {

                    try {
                        // Thread will sleep for 5 seconds
                        sleep(5 * 1000);

                        Intent i = new Intent(getApplicationContext(), OtpScreen.class);
                        startActivityForResult(i, requestCode);

                    } catch (Exception e) {

                    }
                }
            };
            // start thread
            background.start();

        }

    }

    public static String getMobileIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {

                        Log.e("DATA IP ADDRESS", "?????????????" + addr.getHostAddress());

                        return  addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

    //Get the question number from palette and display the question
    public void setDatafromFragment(String position){

        if(haveNetworkConnection()){

            Toast.makeText(this, ""+position, Toast.LENGTH_SHORT).show();

            get_asqn = position;
            assign_Qnum = Integer.parseInt(get_asqn);

            if(assign_Qnum != get_totalqns){
                next.setVisibility(View.VISIBLE);
            }else{
                next.setVisibility(View.GONE);
            }

            Log.e("GETASQN",">>>>>>>>>>>>>>" +get_asqn);

            getData();
            QuesOption();
            //making scrollview to focus to top when going to next question
            scrollView.fullScroll(ScrollView.FOCUS_UP);
            viewPaletteFragment.dismiss();


        }else{

            Toast.makeText(Test_Main.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();

        }


    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d(DEBUG_TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    @Override
    protected void onPause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onPause();
    }

    public void submitDialogBox() {

        SnapsNames = new ArrayList<>();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Test_Main.this);
        alertDialogBuilder.setTitle("Alert!!");
        alertDialogBuilder
                .setMessage(
                        "Do you want to submit your answers?")
                .setCancelable(false)
                .setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            public void onClick(DialogInterface dialog, int id) {

                                String get_end_tym = getDateTime();
                                byte[] data1;

                                String date = get_end_tym.toString().split(" ")[0];
                                exam_EndTym = get_end_tym.toString().split(" ")[1];
                                Log.e("EXAM_END_TYNM",">>>>>>>>>>>" +exam_EndTym);


                                if(get_snap.equals("Yes")){
                                    // gets the files in the directory
                                    fileDirectory = new File(Environment.getExternalStorageDirectory().getPath() +"/.lscarpl4/"+get_stdID+"/My_images");
                                    // lists all the files into an array
                                    File[] dirFiles = fileDirectory.listFiles();

                                    if (dirFiles.length != 0){
                                        // loops through the array of files, outputing the name to console
                                        for (int ii = 0; ii <= 2; ii++) {
                                            fileOutput = dirFiles[ii].toString(); // Complete path of the pics

                                            path1 = dirFiles[0].toString(); // path of first image
                                            path2 = dirFiles[1].toString(); // path of Second image
                                            path3 = dirFiles[2].toString(); // path of third image

                                            Image_Path.add(dirFiles[ii].getName()); //Arraylist of images with timestamp
                                            img_name_date = TextUtils.join(",", Image_Path); //String of images [name with timestamp]
                                            Log.e("FILEOUTPUT", ">>>>>>>>>>>." + Image_Path + " " + img_name_date );


                                            fileOutput = fileOutput.substring(fileOutput.lastIndexOf("/")+1);
                                            SnapsNames.add(fileOutput);  // Arraylist of names

                                            fileOutput = fileOutput.substring(fileOutput.lastIndexOf("_")+1);

                                            int index = fileOutput.indexOf (".");
                                            String str = fileOutput.substring (0, index);
                                            SnapsDate.add(str);
                                            get_sdate = TextUtils.join(",", SnapsDate); // timestamp of each images [String with comma separator]
                                            Log.e("JOINED PICSDATE",">>>>>>>>>>>>>>>" +get_sdate);

                                        }


                                        get_snames = TextUtils.join(",",SnapsNames); // pics name with comma
                                        editor.putString("uploadImgName", get_snames);
                                        editor.commit();

                                        Log.e("JOINED STRING",">>>>>>>>>>>>>>." + get_snames);

                                    }


                                }else if(get_snap.equals("No")){

                                    get_snames = "null";
                                    get_sdate = "null";
                                }



                                    Cursor data = mydb.getListContents();

                                    if(data != null) {
                                        while (data.moveToNext()) {
                                            Dbq_id = data.getString(1);
                                            DbSelectedOpt = data.getString(2);

                                            SelectedAnswers.add(DbSelectedOpt);

                                            Log.e("SELECTEDANS",">>>>>>>>>>>>>>>>" +SelectedAnswers);
                                        }


                                    }else{

                                        Log.e("NULLDB",">>>>>>>>>>>>>>>>" + DbQnum);
                                    }

                                if(haveNetworkConnection()){
                                    //If connection available directly pass the values to server
                                    new AnswersPostRequest().execute();

                                    dialog.cancel();


                                }else{

                                    String qid_list_string = Arrays.toString(QuestionsID.toArray()).replace("[", "").replace("]", "");
                                    String selectedans_lisstr = Arrays.toString(SelectedAnswers.toArray()).replace("[", "").replace("]", "");


                                    //Adding details to answer table
                                    mydb.addAnswers(API_KEY,get_examID,get_stdID,get_stdname,exam_date,get_sscID,
                                            get_tradeID,get_tbID,exam_StartTym,exam_EndTym,ip,"mobile_app",qid_list_string,
                                            get_snames,get_sdate,selectedans_lisstr,get_snap);

                                    Log.e("ADD ANSWERS", ">>>>>>>>>>>>>." + API_KEY + " " + get_examID + " " + get_stdID + " "+
                                            get_stdname+ " " + exam_date + " " + get_sscID + " " + get_tradeID + " " + get_tbID + " " +exam_StartTym+
                                            " " + exam_EndTym + " " + ip + " " + qid_list_string + " " + get_snames + "  "  + get_sdate + " " + get_snap  +  SelectedAnswers.toString());

                                    scheduleJob();
                                    Log.e("NOINTERNET", ">>>>>>>" + "SUBMIT BUTTON");


                                    Intent intent = new Intent(Test_Main.this, Main_Exam.class);
                                    startActivity(intent);

                                    Toast.makeText(Test_Main.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();

                                }
                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
    protected void onStop() {
        // A service can be "started" and/or "bound". In this case, it's "started" by this Activity
        // and "bound" to the JobScheduler (also called "Scheduled" by the JobScheduler). This call
        // to stopService() won't prevent scheduled jobs to be processed. However, failing
        // to call stopService() would keep it alive indefinitely.
        stopService(new Intent(this, NetworkSchedulerService.class));
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start service and provide it a way to communicate with this class.
        Intent startServiceIntent = new Intent(this, NetworkSchedulerService.class);
        startService(startServiceIntent);
    }



    //get the current date and time
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void clearAll(){

            l1.setBackgroundColor(Color.WHITE);
            l2.setBackgroundColor(Color.WHITE);
            l3.setBackgroundColor(Color.WHITE);
            l4.setBackgroundColor(Color.WHITE);
            get_selectedOpt = "";
            option_Name = null;

            Log.e("CLEARED", ">>>>>>>>>." + get_selectedOpt);
            Log.e("OPTCLEARED", ">>>>>>>>>." + option_Name);

            boolean recordExists= mydb.checkIfRecordExist(mydb.TABLE_NAME ,mydb.COLUMN_1 ,Dbq_id);
            if(recordExists)
            {

                if(option_Name == null){

                    selectUpdateOption(Dbq_id,DbSelectedOpt,null);

                    if(notans.equals("1")){

                        Log.e("DBQID",">>>>>>>>" + Dbq_id + notans);
                        selectUpdateNotAns(Dbq_id,1,0);
                    }

                }

            }else{

            }

            getUpdateOnAllClicks();

    }


    //get the data from the database
    private void database() {
        mydb = new DBHelper(this);
        Cursor data = mydb.getListContents();

        if(data != null) {
            while (data.moveToNext()) {
                Dbq_id = data.getString(1);
                DbSelectedOpt = data.getString(2);
                m_review = data.getString(3);
                notans = data.getString(4);
                notvisit = data.getString(5);
                qns = data.getString(6);
                opa = data.getString(7);
                opb = data.getString(8);
                opc = data.getString(9);
                opd = data.getString(10);
                DbQnum = data.getString(11);
                hqns = data.getString(12);
                hopa = data.getString(13);
                hopb = data.getString(14);
                hopc = data.getString(15);
                hopd = data.getString(16);

                Log.e("DBVALUESMAIN", ">>>>>>>>>>" +  DbQnum + " " + "" + Dbq_id+ " " +  DbSelectedOpt +
                        " " + m_review +" " +notans+" " +notvisit + " " + qns + " " + opa + " " + opb + " " + opc + " " + opd +
                        " " + hqns + " " + hopa + " " + hopb + " " + hopc + " " + hopd);
            }
        }else{

            Log.e("NULLDB",">>>>>>>>>>>>>>>>" + DbQnum);
        }
    }

    //for updating the selected options for the question in database
    public void selectUpdateOption(String qid, String old_option, String new_option) {

        ContentValues contentValues = new ContentValues();

        final String whereClause = mydb.COLUMN_1 + " =?";
        final String[] whereArgs = {
                qid
        };


        contentValues.put(mydb.COLUMN_2, option_Name);

        SQLiteDatabase sqLiteDatabase = mydb.getWritableDatabase();
        sqLiteDatabase.update(mydb.TABLE_NAME, contentValues,
                whereClause, whereArgs);
    }

    //for updating the mark for review in database
    public void selectUpdateReview(String qid, Integer old_option, Integer new_option) {

        ContentValues contentValues = new ContentValues();

        final String whereClause = mydb.COLUMN_1 + " =?";
        final String[] whereArgs = {
                qid
        };


        contentValues.put(mydb.COLUMN_3, 1);

        SQLiteDatabase sqLiteDatabase = mydb.getWritableDatabase();
        sqLiteDatabase.update(mydb.TABLE_NAME, contentValues,
                whereClause, whereArgs);
    }

    //for updating not answered field in database
    public void selectUpdateNotAns(String qid, Integer old_option, Integer new_option) {

        ContentValues contentValues = new ContentValues();

        final String whereClause = mydb.COLUMN_1 + " =?";
        final String[] whereArgs = {
                qid
        };


        contentValues.put(mydb.COLUMN_4, new_option);

        SQLiteDatabase sqLiteDatabase = mydb.getWritableDatabase();
        sqLiteDatabase.update(mydb.TABLE_NAME, contentValues,
                whereClause, whereArgs);
    }

    //for updating not visited field in database
    public void selectNotVisited(String qid, Integer old_option, Integer new_option) {

        ContentValues contentValues = new ContentValues();

        final String whereClause = mydb.COLUMN_1 + " =?";
        final String[] whereArgs = {
                qid
        };


        contentValues.put(mydb.COLUMN_5, 1);

        SQLiteDatabase sqLiteDatabase = mydb.getWritableDatabase();
        sqLiteDatabase.update(mydb.TABLE_NAME, contentValues,
                whereClause, whereArgs);
    }

    private boolean checkPermission() {

        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,CAMERA) != PackageManager.PERMISSION_GRANTED
                ;
    }

    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle(getString(R.string.permission_necessary));
                alertBuilder.setMessage(R.string.storage_permission_is_encessary_to_write_event);
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(Test_Main.this, new String[]{WRITE_EXTERNAL_STORAGE
                                , READ_EXTERNAL_STORAGE , CAMERA}, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(Test_Main.this, new String[]{WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);
            }
        } else {
            mainMethod();
        }
    }

    // When you have the request results
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalFile = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if(cameraPermission && readExternalFile && writeExternalFile)
                    {
                        // write your logic here
                        mainMethod();
                    } else {
                        Snackbar.make(this.findViewById(android.R.id.content),
                                "Please Grant Permissions",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions(Test_Main.this, new String[]{WRITE_EXTERNAL_STORAGE
                                                , READ_EXTERNAL_STORAGE , CAMERA}, PERMISSION_REQUEST_CODE);
                                    }
                                }).show();
                    }
                }
                break;
        }

    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Hours = Minutes / 60;

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 100);

            completedTime = ""
                    /* +String.format("%02d", Hours)*/
                    +":" + String.format("%02d", Minutes)  + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%02d", MilliSeconds);
            // Log.e("actualtime","" +completedTime);

            handler.postDelayed(this, 0);
        }

    };

    //for starting the timer when exam gets started
    public void myCounter(){
        numofQue = 0;
        dynamicTimer = 0;
        String get_offTime = Splash_Screen.sh.getString("dur", null);
        Log.e("OFF_TIME", ">>>>>>>>>>>" + get_offTime);

        if(haveNetworkConnection()){
            dynamicTimer= (Integer.parseInt(get_duration)*60000);

        }else{

            Toast.makeText(Test_Main.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();

        }

        Log.e("dynamictimer",""+dynamicTimer);

        mCountDown = new CountDownTimer(dynamicTimer, 1000) {

            @Override
            public void onFinish() {
                isAlertDialogShowing(alertDialog);
                Log.e("dynamictimer1",""+dynamicTimer);
                timeUp();
            }

            private void timeUp() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Test_Main.this);
                builder.setTitle("Times up!")
                        //.setMessage("Game over")
                        .setCancelable(false)
                        .setNeutralButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        handler.removeCallbacks(runnable);

                                        submitDialogBox();

                                    }
                                });
                AlertDialog alert = builder.create();
                if (!isFinishing()) {
                    alert.show();
                }

            }

            @Override
            public void onTick(long millisUntilFinished) {
                time= ""+ String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                time_left.setText("Time Left: " + time);

            }
        }.start();

    }


    public void isAlertDialogShowing(AlertDialog alertDialog) {
        if (alertDialog != null)
            alertDialog.hide();
        return;
    }

    //open up the view palette fragment
    private void showEditDialog() {

            FragmentManager fm = getSupportFragmentManager();
            viewPaletteFragment = ViewPaletteFragment.newInstance("Some Title");
            viewPaletteFragment.show(fm, "fragment_edit_name");

            viewPaletteFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);

            Bundle data = new Bundle();//Use bundle to pass data
            data.putString("Answered", String.valueOf(answered)); //put string, int, etc in bundle with a key value
            data.putString("NotAnswered", String.valueOf(not_answered) );
            data.putString("NotVisited", String.valueOf(notVisited));
            data.putString("ReviewLater", String.valueOf(reviewLater));
            data.putStringArrayList("Numbers", QuestionNumbers);

            Log.e("NOT_ANS",">>>>>>>>>>>>" + String.valueOf(not_answered));
            Log.e("MARK",">>>>>>>>>>>>" + String.valueOf(reviewLater));
            Log.e("VISIT",">>>>>>>>>>>>" + String.valueOf(notVisited));


            viewPaletteFragment.setArguments(data);//Finally set argument bundle to fragment
    }

    public void dialogBoxForInternet() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Test_Main.this);
        alertDialogBuilder.setTitle("No Internet Connection.");
        alertDialogBuilder
                .setMessage(
                        "Go to Settings to enable Internet Connecivity.")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivityForResult(
                                        new Intent(android.provider.Settings.ACTION_SETTINGS),0);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @SuppressLint("UnsupportedChromeOsCameraSystemFeature")
    public void mainMethod() {

        if (haveNetworkConnection()) {

            new QuestionsPostRequest().execute();

        } else {
            Toast.makeText(Test_Main.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();

        }

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

    //onclick of each options
    @Override
    public void onClick(View v) {

        if ((v.getId() == R.id.op1Layout)) {
            //corresponding button logic should below here
            get_selectedOpt = op1.getText().toString();

            Log.e("SELECTEDOPT1", ">>>>>>>>>>" + get_selectedOpt);

            // Initialize a new GradientDrawable instance
            GradientDrawable gd = new GradientDrawable();
// Set the gradient drawable background to transparent
            gd.setColor(Color.parseColor("#FFFFFF"));

// Set a border for the gradient drawable
            gd.setStroke(5, Color.GREEN);

// Finally, apply the gradient drawable to the edit text background
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                l1.setBackground(gd);
                l2.setBackgroundColor(Color.WHITE);
                l3.setBackgroundColor(Color.WHITE);
                l4.setBackgroundColor(Color.WHITE);

            }

            option_Name = "A";

        } else if ((v.getId() == R.id.op2Layout)) {
            //corresponding button logic should below here

            get_selectedOpt = op2.getText().toString();
            Log.e("SELECTEDOPT2", ">>>>>>>>>>" + get_selectedOpt);


            // Initialize a new GradientDrawable instance
            GradientDrawable gd = new GradientDrawable();
// Set the gradient drawable background to transparent
            gd.setColor(Color.parseColor("#FFFFFF"));

// Set a border for the gradient drawable
            gd.setStroke(5, Color.GREEN);

// Finally, apply the gradient drawable to the edit text background
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                l2.setBackground(gd);
                l1.setBackgroundColor(Color.WHITE);
                l3.setBackgroundColor(Color.WHITE);
                l4.setBackgroundColor(Color.WHITE);

            }

            option_Name = "B";


        } else if ((v.getId() == R.id.op3Layout)) {
            //corresponding button logic should below here
            get_selectedOpt = op3.getText().toString();

            Log.e("SELECTEDOPT3", ">>>>>>>>>>" + get_selectedOpt);


            // Initialize a new GradientDrawable instance
            GradientDrawable gd = new GradientDrawable();
// Set the gradient drawable background to transparent
            gd.setColor(Color.parseColor("#FFFFFF"));

// Set a border for the gradient drawable
            gd.setStroke(5, Color.GREEN);

// Finally, apply the gradient drawable to the edit text background
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                l3.setBackground(gd);
                l2.setBackgroundColor(Color.WHITE);
                l1.setBackgroundColor(Color.WHITE);
                l4.setBackgroundColor(Color.WHITE);

            }

            option_Name = "C";


        } else if ((v.getId() == R.id.op4Layout)) {
            //corresponding button logic should below here
            get_selectedOpt = op4.getText().toString();

            Log.e("SELECTEDOPT4", ">>>>>>>>>>" + get_selectedOpt);

            // Initialize a new GradientDrawable instance
            GradientDrawable gd = new GradientDrawable();
// Set the gradient drawable background to transparent
            gd.setColor(Color.parseColor("#FFFFFF"));

// Set a border for the gradient drawable
            gd.setStroke(5, Color.GREEN);

// Finally, apply the gradient drawable to the edit text background
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                l4.setBackground(gd);
                l2.setBackgroundColor(Color.WHITE);
                l3.setBackgroundColor(Color.WHITE);
                l1.setBackgroundColor(Color.WHITE);


            }

            option_Name = "D";

        }


        get_OPname = get_selectedOpt;
        get_OP = option_Name;

        boolean recordExists = mydb.checkIfRecordExist(mydb.TABLE_NAME, mydb.COLUMN_1, Dbq_id);
        if (recordExists) {
            //do your stuff
            if (get_OP == null) {


            } else {

                selectUpdateOption(Dbq_id, null, get_OP);
                Log.e("SELECTED", "??????????" + get_OP);
                selectUpdateNotAns(Dbq_id, 0, 1);

            }

        } else {

        }

        getUpdateOnAllClicks();

        Log.e("FinalValueText", ">>>>>>>>>>." + get_OPname + get_OP);

    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {

            if (haveNetworkConnection()) {
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
                Toast.makeText(context, "Connection established ", Toast.LENGTH_SHORT).show();
                /* new QuestionsPostRequest().execute();*/
            } else {

                dialogBoxForInternet();
            }
        }
    }


    // for getting the questions from server
    private class QuestionsPostRequest extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

            // Showing progress dialog
            pDialog = new ProgressDialog(Test_Main.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @SuppressLint("UnsupportedChromeOsCameraSystemFeature")
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            QuestionsID = new ArrayList<>();

            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                if (jsonObject != null && jsonObject.getString("status_message") != null) {

                    mydb.deleteRecord();
                    String message = jsonObject.getString("status_message");  // Message

                    reader = jsonObject;
                    Log.e("MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.equals("Success")) {

                        get_img_dir_src = jsonObject.getString("image_dir_source");
                        get_img_dir = jsonObject.getString("image_dir");
                        get_duration = jsonObject.getString("exam_duration");
                        get_exmName = jsonObject.getString("exam_name");
                        get_tradeCode = jsonObject.getString("trade_code");
                        get_tradeTitle = jsonObject.getString("trade_title");
                        get_totalqns = jsonObject.getInt("total_question");

                        duration.setText("Duration: " + get_duration + " Min");

                        Image_url = get_img_dir.replace("\\", "");
                        Log.e("IMageURL",">>>>>>>>>" +Image_url);

                        // Creating JSONArray from JSONObject
                        JSONArray userdetails = jsonObject.getJSONArray("questions");

                        for (int y = 0; y < userdetails.length(); y++) {

                            JSONObject jObj = userdetails.getJSONObject(y);

                            nos_id = jObj.getString("nos_id");
                            nos_code = jObj.getString("nos_code");
                            nos_title = jObj.getString("nos_title");

                            q_id = jObj.getString("q_id");
                            Question = jObj.getString("question");
                            typeofquestion = jObj.getString("typeofquestion");
                            option_a = jObj.getString("option_a");
                            option_b = jObj.getString("option_b");
                            option_c = jObj.getString("option_c");
                            option_d = jObj.getString("option_d");

                            get_hques = jObj.getString("lang_question");
                            get_hop1 = jObj.getString("lang_option_a");
                            get_hop2 = jObj.getString("lang_option_b");
                            get_hop3 = jObj.getString("lang_option_c");
                            get_hop4 = jObj.getString("lang_option_d");


                            QuestionsID.add(q_id);
                            Log.e("QUESTIONS_ID","??????????????" +QuestionsID);

                            QuestionNumbers.add(String.valueOf(y+1));
                            Log.e("QUES_NUMBER",">>>>>>>>>>.." + QuestionNumbers);

                            boolean recordExists= mydb.checkIfRecordExist(mydb.TABLE_NAME ,mydb.COLUMN_1 ,q_id);
                            if(recordExists)
                            {


                            }else{

                                mydb.addData(q_id,null,0,0,0,Question,option_a,option_b,option_c,option_d,(y+1),get_hques,get_hop1,get_hop2,get_hop3,get_hop4);

                            }

                        }

                    } else if (message.equals("Access Denied ! Authentication Failed")) {


                        Toast.makeText(getApplicationContext(),
                                "Access Denied", Toast.LENGTH_LONG).show();

                    }else if (message.equals("Not Found")) {


                        Toast.makeText(getApplicationContext(),
                                "Student Data Not Found", Toast.LENGTH_LONG).show();

                    }else if (message.equals("Error")) {


                        Toast.makeText(getApplicationContext(),
                                "Error Found", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(Test_Main.this,Main_Exam.class);
                        startActivity(intent);

                    }
                }else{

                    Toast.makeText(getApplicationContext(),
                            "Cannot Get Values from server", Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            getData();
            QuesOption();

            myCounter();
            StartTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);
            pDialog.dismiss();

            String get_start_tym = getDateTime();

            exam_date = get_start_tym.toString().split(" ")[0];
            exam_StartTym = get_start_tym.toString().split(" ")[1];
            Log.e("EXAM_START_TYM",">>>>>>>>>>>>" +exam_StartTym + " " +exam_date);

            if(get_snap.equals("Yes")){

                if(DbQnum.equals("1")){
                    // do we have a camera?
                    if (!getPackageManager()
                            .hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                        Toast.makeText(Test_Main.this, "No camera on this device", Toast.LENGTH_LONG)
                                .show();

                        Log.e("No camera",">>>>>>>>>>>>>" + cameraId);

                    } else {
                        cameraId = findFrontFacingCamera();

                        Log.e("findFrontFacingCamera",">>>>>>>>>>>>>" + cameraId);
                        if (cameraId < 0) {
                            Toast.makeText(Test_Main.this, "No front facing camera found.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            camera = Camera.open(cameraId);

                            try {
                                camera.setPreviewTexture(new SurfaceTexture(10));
                            } catch (IOException e1) {
                                Log.e("VERSION", e1.getMessage());
                            }


                            camera.startPreview();
                            camera.takePicture(null, null,
                                    new PhotoHandler(getApplicationContext(), get_stdID,get_tbID,latitude,longitude));
                            Log.e("camera > 0",">>>>>>>>>>>>>" + cameraId);
                        }
                    }
                }

            }else if(get_snap.equals("No")){

                //if take_snapshot: "No" pass null values for snapshots while submitting answers
                base64 = "null";
                get_snames = "null";
                get_sdate = "null";

            }

        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObject(Questions_url, makingJson());

        }
    }

    private JSONObject makingJson() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", API_KEY);
            postDataParams.put("exam_id", get_examID);
            postDataParams.put("student_id", get_stdID);
            postDataParams.put("ssc_id", get_sscID);
            postDataParams.put("trade_id", get_tradeID);
            postDataParams.put("tb_id", get_tbID);

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



    public void getData(){

        mydb = new DBHelper(Test_Main.this);
        Cursor data = mydb.getSingleContents(String.valueOf(assign_Qnum));

        if(data != null) {
            while (data.moveToNext()) {
                Dbq_id = data.getString(1);
                DbSelectedOpt = data.getString(2);
                m_review = data.getString(3);
                notans = data.getString(4);
                notvisit = data.getString(5);
                qns = data.getString(6);
                opa = data.getString(7);
                opb = data.getString(8);
                opc = data.getString(9);
                opd = data.getString(10);
                DbQnum = data.getString(11);
                hqns = data.getString(12);
                hopa = data.getString(13);
                hopb = data.getString(14);
                hopc = data.getString(15);
                hopd = data.getString(16);


                Log.e("JSONDBVALUES", ">>>>>>>>>>" +  DbQnum + " " + "" + Dbq_id+ " " +  DbSelectedOpt +
                        " " + m_review +" " +notans+" " +notvisit + " " + qns + " " + opa + " " + opb + " " + opc + " " + opd +
                        " " + hqns + " " + hopa + " " + hopb + " " + hopc + " " + hopd);

                notVisited = mydb.notVisitedCount();
                mydb.close();
                Log.e("NOT_VISIT_COUNT",">>>>>>>>>>>" + notVisited);

                not_answered = mydb.notAnsweredCount();
                mydb.close();
                Log.e("NOT_Answered_COUNT",">>>>>>>>>>>" + not_answered);

                answered = mydb.answeredCount();
                mydb.close();
                Log.e("Answered_COUNT",">>>>>>>>>>>" + answered);

                reviewLater = mydb.reviewCount();
                mydb.close();
                Log.e("Review_COUNT",">>>>>>>>>>>" + reviewLater);

                ans.setText(String.valueOf(answered));
                not_ans.setText(String.valueOf(not_answered));
                not_visited.setText(String.valueOf(notVisited));
                review_later.setText(String.valueOf(reviewLater));


                boolean recordExists= mydb.checkIfRecordExist(mydb.TABLE_NAME ,mydb.COLUMN_1 ,Dbq_id);
                if(recordExists)
                {
                    //do your stuff
                    if(notvisit.equals("0")){
                        Log.e("DBQID",">>>>>>>>" + Dbq_id + notvisit);
                        selectNotVisited(Dbq_id,0,1);
                    }else{

                    }

                    if(DbSelectedOpt == null){

                        clearAll();

                        if(notans.equals("1")){

                            Log.e("DBQID",">>>>>>>>" + Dbq_id + notans);
                            selectUpdateNotAns(Dbq_id,1,0);
                        }

                    }else{

                        if(notans.equals("0")){

                            Log.e("DBQID",">>>>>>>>" + Dbq_id + notans);
                            selectUpdateNotAns(Dbq_id,0,1);
                        }

                        Log.e("DBSELECXTIO",">>>>>>>>>>>> " + DbSelectedOpt);

                        if(DbSelectedOpt.equals("A")){
                            // Initialize a new GradientDrawable instance
                            GradientDrawable gd = new GradientDrawable();
// Set the gradient drawable background to transparent
                            gd.setColor(Color.parseColor("#FFFFFF"));

// Set a border for the gradient drawable
                            gd.setStroke(5, Color.GREEN);

// Finally, apply the gradient drawable to the edit text background
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                l1.setBackground(gd);
                                l2.setBackgroundColor(Color.WHITE);
                                l3.setBackgroundColor(Color.WHITE);
                                l4.setBackgroundColor(Color.WHITE);

                            }
                            Log.e("CLICK A",">>>>>>>>>>>> " + DbSelectedOpt);
                        }else if(DbSelectedOpt.equals("B")){
                            // Initialize a new GradientDrawable instance
                            GradientDrawable gd = new GradientDrawable();
// Set the gradient drawable background to transparent
                            gd.setColor(Color.parseColor("#FFFFFF"));

// Set a border for the gradient drawable
                            gd.setStroke(5, Color.GREEN);

// Finally, apply the gradient drawable to the edit text background
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                l2.setBackground(gd);
                                l1.setBackgroundColor(Color.WHITE);
                                l3.setBackgroundColor(Color.WHITE);
                                l4.setBackgroundColor(Color.WHITE);

                            }
                            Log.e("CLICK B",">>>>>>>>>>>> " + DbSelectedOpt);
                        }else if(DbSelectedOpt.equals("C")){
                            // Initialize a new GradientDrawable instance
                            GradientDrawable gd = new GradientDrawable();
// Set the gradient drawable background to transparent
                            gd.setColor(Color.parseColor("#FFFFFF"));

// Set a border for the gradient drawable
                            gd.setStroke(5, Color.GREEN);

// Finally, apply the gradient drawable to the edit text background
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                l3.setBackground(gd);
                                l2.setBackgroundColor(Color.WHITE);
                                l1.setBackgroundColor(Color.WHITE);
                                l4.setBackgroundColor(Color.WHITE);

                            }
                            Log.e("CLICK C",">>>>>>>>>>>> " + DbSelectedOpt);
                        }else if(DbSelectedOpt.equals("D")){
                            // Initialize a new GradientDrawable instance
                            GradientDrawable gd = new GradientDrawable();
// Set the gradient drawable background to transparent
                            gd.setColor(Color.parseColor("#FFFFFF"));

// Set a border for the gradient drawable
                            gd.setStroke(5, Color.GREEN);

// Finally, apply the gradient drawable to the edit text background
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                l4.setBackground(gd);
                                l2.setBackgroundColor(Color.WHITE);
                                l3.setBackgroundColor(Color.WHITE);
                                l1.setBackgroundColor(Color.WHITE);

                            }
                            Log.e("CLICK D",">>>>>>>>>>>> " + DbSelectedOpt);
                        }
                    }

                }else{

                }
            }
        }else{
            Log.e("NULLDB",">>>>>>>>>>>>>>>>" + DbQnum);
        }
    }


    public void getUpdateOnAllClicks(){

        mydb = new DBHelper(Test_Main.this);
        Cursor data = mydb.getSingleContents(String.valueOf(assign_Qnum));

        if(data != null) {
            while (data.moveToNext()) {
                Dbq_id = data.getString(1);
                DbSelectedOpt = data.getString(2);
                m_review = data.getString(3);
                notans = data.getString(4);
                notvisit = data.getString(5);
                qns = data.getString(6);
                opa = data.getString(7);
                opb = data.getString(8);
                opc = data.getString(9);
                opd = data.getString(10);
                DbQnum = data.getString(11);
                hqns = data.getString(12);
                hopa = data.getString(13);
                hopb = data.getString(14);
                hopc = data.getString(15);
                hopd = data.getString(16);


                Log.e("JSONDBVALUES", ">>>>>>>>>>" + DbQnum + " " + "" + Dbq_id + " " + DbSelectedOpt +
                        " " + m_review + " " + notans + " " + notvisit + " " + qns + " " + opa + " " + opb + " " + opc + " " + opd +
                        " " + hqns + " " + hopa + " " + hopb + " " + hopc + " " + hopd);

                notVisited = mydb.notVisitedCount();
                mydb.close();
                Log.e("NOT_VISIT_COUNT", ">>>>>>>>>>>" + notVisited);

                not_answered = mydb.notAnsweredCount();
                mydb.close();
                Log.e("NOT_Answered_COUNT", ">>>>>>>>>>>" + not_answered);

                answered = mydb.answeredCount();
                mydb.close();
                Log.e("Answered_COUNT", ">>>>>>>>>>>" + answered);

                reviewLater = mydb.reviewCount();
                mydb.close();
                Log.e("Review_COUNT", ">>>>>>>>>>>" + reviewLater);

                ans.setText(String.valueOf(answered));
                not_ans.setText(String.valueOf(not_answered));
                not_visited.setText(String.valueOf(notVisited));
                review_later.setText(String.valueOf(reviewLater));

            }
        }

    }

    public void QuesOption(){

        if(qns.contains("|")){
            quesImage.setVisibility(View.VISIBLE);
            String[] str1 = qns.trim().split("[|]");
            String spt1 = str1[0];
            String spt2 = str1[1];

            String QuesImage = spt2.replaceAll("[%]", "");

            Log.e("splitQ1", ">>>>>>>>>>" +spt1);
            Log.e("splitQ2", ">>>>>>>>>>" +spt2);
            Log.e("QuesImg", ">>>>>>>>>>>" +QuesImage);

            Glide.with(Test_Main.this).load(Image_url +"/" + QuesImage).into(quesImage);

            question.setText(DbQnum + ") "  + Html.fromHtml(spt1).toString());

            //  hindi_question.setText(assign_Qnum + ") " + get_hques);
            if(hqns.equals("null")){
                hindi_question.setVisibility(View.GONE);
            }else{
                hindi_question.setVisibility(View.VISIBLE);
                hindi_question.setText(Html.fromHtml(hqns).toString());
            }


        }else{

            question.setText(DbQnum + ") " +  Html.fromHtml(qns).toString());
            quesImage.setVisibility(View.GONE);

            // hindi_question.setText(assign_Qnum + ") " + get_hques);
            //  hindi_question.setText(assign_Qnum + ") " + get_hques);
            if(hqns.equals("null")){
                hindi_question.setVisibility(View.GONE);
            }else{
                hindi_question.setVisibility(View.VISIBLE);
                hindi_question.setText(Html.fromHtml(hqns).toString());
            }

        }

        if(opa.contains("|")){
            op1_image.setVisibility(View.VISIBLE);

            String[] op1str1 = opa.trim().split("[|]");
            String op1spt1 = op1str1[0];
            String op1spt2 = op1str1[1];

            String Image = op1spt2.replaceAll("[%]", "");

            Log.e("ImageNameOp1",">>>>>>>>>." +Image);

            Glide.with(Test_Main.this).load(Image_url +"/" + Image).into(op1_image);


            Log.e("splitOPTIONA.1", ">>>>>>>>>>" +op1spt1);
            Log.e("splitOPTIONA.2", ">>>>>>>>>>" +op1spt2);
            Log.e("optionAImg", ">>>>>>>>>>>" +Image);



            op1.setText("A) " + Html.fromHtml(op1spt1).toString());
            //  hindi_op1.setText("A) " + get_hop1);

            if(hopa.equals("null")){
                hindi_op1.setVisibility(View.GONE);
            }else{
                hindi_op1.setVisibility(View.VISIBLE);
                hindi_op1.setText(Html.fromHtml(hopa).toString());
            }


        }else{

            if(opa.equals("NA")){
                l1.setVisibility(View.GONE);
                op1_image.setVisibility(View.GONE);
            }else{
                l1.setVisibility(View.VISIBLE);
                op1.setText("A) " + Html.fromHtml(opa).toString());
                op1_image.setVisibility(View.GONE);
            }

            // hindi_op1.setText("A) " + get_hop1);
            if(hopa.equals("null")){
                hindi_op1.setVisibility(View.GONE);
            }else{
                hindi_op1.setVisibility(View.VISIBLE);
                hindi_op1.setText(Html.fromHtml(hopa).toString());
            }
        }

        if(opb.contains("|")){
            op2_image.setVisibility(View.VISIBLE);
            String[] op2str1 = opb.trim().split("[|]");
            String op2spt1 = op2str1[0];
            String op2spt2 = op2str1[1];

            String Image = op2spt2.replaceAll("[%]", "");

            Log.e("ImageNameOp2",">>>>>>>>>." +Image);

            Glide.with(Test_Main.this).load(Image_url +"/" + Image).into(op2_image);
            Log.e("splitOPTIONB.1", ">>>>>>>>>>" +op2spt1);
            Log.e("splitOPTIONB.2", ">>>>>>>>>>" +op2spt2);
            Log.e("optionBImg", ">>>>>>>>>>>" +Image);

            op2.setText("B) " + Html.fromHtml(op2spt1).toString());
            //  hindi_op2.setText("B) " + get_hop2);

            if(hopb.equals("null")){
                hindi_op2.setVisibility(View.GONE);
            }else{
                hindi_op2.setVisibility(View.VISIBLE);
                hindi_op2.setText(Html.fromHtml(hopb).toString());
            }

        }else{

            if(opb.equals("NA")){
                l2.setVisibility(View.GONE);
                op2_image.setVisibility(View.GONE);
            }else{
                l2.setVisibility(View.VISIBLE);
                op2.setText("B) " + Html.fromHtml(opb).toString());
                op2_image.setVisibility(View.GONE);
            }


            // hindi_op2.setText("B) " + get_hop2);
            if(hopb.equals("null")){
                hindi_op2.setVisibility(View.GONE);
            }else{
                hindi_op2.setVisibility(View.VISIBLE);
                hindi_op2.setText(Html.fromHtml(hopb).toString());
            }
        }

        if(opc.contains("|")){
            op3_image.setVisibility(View.VISIBLE);
            String[] op3str1 = opc.trim().split("[|]");
            String op3spt1 = op3str1[0];
            String op3spt2 = op3str1[1];

            String Image = op3spt2.replaceAll("[%]", "");

            Log.e("ImageNameOp3",">>>>>>>>>." +Image);

            Glide.with(Test_Main.this).load(Image_url +"/" + Image).into(op3_image);

            Log.e("splitOPTIONC.1", ">>>>>>>>>>" +op3spt1);
            Log.e("splitOPTIONC.2", ">>>>>>>>>>" +op3spt2);
            Log.e("optionCImg", ">>>>>>>>>>>" +Image);

            op3.setText("C) " + Html.fromHtml(op3spt1).toString());
            // hindi_op3.setText("C) " + get_hop3);

            if(hopc.equals("null")){
                hindi_op3.setVisibility(View.GONE);
            }else{
                hindi_op3.setVisibility(View.VISIBLE);
                hindi_op3.setText(Html.fromHtml(hopc).toString());
            }

        }else{

            if(opc.equals("NA")){
                l3.setVisibility(View.GONE);
                op3_image.setVisibility(View.GONE);
            }else{
                l3.setVisibility(View.VISIBLE);
                op3.setText("C) " + Html.fromHtml(opc).toString());
                op3_image.setVisibility(View.GONE);
            }


            // hindi_op3.setText("C) " + get_hop3);
            if(hopc.equals("null")){
                hindi_op3.setVisibility(View.GONE);
            }else{
                hindi_op3.setVisibility(View.VISIBLE);
                hindi_op3.setText(Html.fromHtml(hopc).toString());
            }
        }

        if(opd.contains("|")){
            op4_image.setVisibility(View.VISIBLE);
            String[] op4str1 = opd.trim().split("[|]");
            String op4spt1 = op4str1[0];
            String op4spt2 = op4str1[1];

            String Image = op4spt2.replaceAll("[%]", "");

            Log.e("ImageNameOp4",">>>>>>>>>." +Image);

            Glide.with(Test_Main.this).load(Image_url +"/" + Image).into(op4_image);

            Log.e("splitOPTIOND.1", ">>>>>>>>>>" +op4spt1);
            Log.e("splitOPTIOND.2", ">>>>>>>>>>" +op4spt2);
            Log.e("optionDImg", ">>>>>>>>>>>" +Image);

            op4.setText("D) " + Html.fromHtml(op4spt1).toString());
            // hindi_op4.setText("D) " + get_hop4);

            if(hopd.equals("null")){
                hindi_op4.setVisibility(View.GONE);
            }else{
                hindi_op4.setVisibility(View.VISIBLE);
                hindi_op4.setText(Html.fromHtml(hopd).toString());
            }


        }else{

            if(opd.equals("NA")){
                l4.setVisibility(View.GONE);
                op4_image.setVisibility(View.GONE);
            }else{
                l4.setVisibility(View.VISIBLE);
                op4.setText("D) " + Html.fromHtml(opd).toString());
                op4_image.setVisibility(View.GONE);
            }


            //hindi_op4.setText("D) " + get_hop4);

            if(hopd.equals("null")){
                hindi_op4.setVisibility(View.GONE);
            }else{
                hindi_op4.setVisibility(View.VISIBLE);
                hindi_op4.setText(Html.fromHtml(hopd).toString());
            }
        }
    }

    @Override
    public void onBackPressed() {

        if(haveNetworkConnection()){
            if(not_answered == 0){
                submitDialogBox();
            }else{

                Toast.makeText(getApplicationContext(), "Please Answer all the Questions", Toast.LENGTH_SHORT).show();
            }
        }else{

            Toast.makeText(Test_Main.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();

        }
    }

    //calling method from networkSchedulerService class
    public void callFromService(Context context){
        mydb = new DBHelper(context);
        Cursor data = mydb.getAnswerData(mydb);

        if(data.getCount()>0 && data.moveToFirst()){

            Arr_StudentIDS = new ArrayList<>();
            do{

                String studnt_id = data.getString(3);
                Arr_StudentIDS.add(studnt_id);

                Log.e("ARRAY SIZE TEST", ">>>>>>>>>>>"  + Arr_StudentIDS.size() + " " + studnt_id);

                for(int i = 0 ; i < Arr_StudentIDS.size() ; i++){

                    get = Arr_StudentIDS.get(i);

                }

                Log.e("GET STDID TEST", ">>>>>>>>>>>"  + get);
                getOnlyOneRow(context,get);
                try {
                    Thread.sleep(2000);
                    Log.e("THREAD","????????????????/" );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }while (data.moveToNext());

        }else{

            Log.e("No Value in DB", "Test Main" + "Call From Service Method");

        }

    }

    public void getOnlyOneRow(Context context,String val){
        getExactQnum = new ArrayList<>();
        getDbsltAns = new ArrayList<>();

        mydb = new DBHelper(context);
        Cursor data = mydb.getAnswerSingleVal(mydb,val);

        Log.e("SINGLE VAL TEST","???STUDENT ID??" + val);

        if(data.getCount()>0 && data.moveToFirst())
            do{

                a1 = data.getString(1); //api key
                a2 = data.getString(2); //exam_id
                a3 = data.getString(3); //std_id
                a4 = data.getString(4); //std_name
                a5 = data.getString(5); //exam_date
                a6 = data.getString(6); //ssc_id
                a7 = data.getString(7); //trade_id
                a8 = data.getString(8); //tb_id
                a9 = data.getString(9); //strttime
                a10 = data.getString(10); //endtime
                a11 = data.getString(11); //ipaddress
                a12 = data.getString(12); //browser
                a13 = data.getString(13); // question ids
                a14 = data.getString(14); //snapshot image name
                a15 = data.getString(15); //snapshot image date
                aoptions = data.getString(16); //selected_ans
                get_Snapshots = data.getString(17); // Snaps Taken [Yes/No]

                Log.e("Getonly1row", "TestMain Class" + a13 + " " + aoptions);

                text1 = a13.replace("[", "").replace("]", "").replace(" ","");
                text2 = aoptions.replace("[", "").replace("]", "").replace(" ","");

                Log.e("DO WHILE", "TEST STDID" + a3 + " " + a4);

                Log.e("Removed braces", ">>>>>> " + text1 + "  " + text2);

                //   Log.e("DB ARRAYLIST", ">>>>>>>>>>>>>" + " " +text1 + " " + text2);

          /*      Log.e("Single DB value TEST", ">>>>>>>>>>>>"
                        + a1 + a2 +a3 + a4 + a5 + a6 +a7 +a8 +a9 + a10 +a11 +
                        "Browser: "+ a12 +
                        "Question IDS: "+ a13 +
                        "Snapshot Image: " + a14 +
                        "Snapshot image date: " + a15 +
                        "Selected Ans: " + aoptions  +
                        "Encrypted Image: " + getb64 +
                        "Exact Qnum: " + getExactQnum);

                Log.e("ARRAYLIST", ">>>>>>>>>>>." + getExactQnum + " " + getDbsltAns + " " + "New Array" );*/

                final OfflineAnswersPostRequest fDownload = new OfflineAnswersPostRequest();
                fDownload.execute();

            }while (data.moveToNext());
    }

    // for submitting answers in offline mode
    public class OfflineAnswersPostRequest extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            try {
                if (jsonObject != null && jsonObject.getString("status_message") != null) {

                    String message = jsonObject.getString("status_message");  // Message

                    reader = jsonObject;
                    Log.e("SERVER MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    Log.e("GET SNAP","?????????/" + get_snap);

                    if (message.contains("Success Answer submitted successfully.")) {

                        Log.e("EXAM Offline Success", ">>>>>>>>>>>>>>>" +a3+ " " +message);

                        Log.e("GET SNAP SUCCESS","?????????/" + get_Snapshots);

                        if(get_Snapshots.equals("Yes")){

                            mydb.deleteSingleAnswerData(a3);

                        }else if(get_Snapshots.equals("No")){

                            mydb.deleteSingleAnswerData(a3);
                            Log.e("GET SNAP SUCCESS","NO/" + get_Snapshots);
                            Log.e("DBVAL dele anw" , "No Snapshots" + a3+ " ");

                        }
                    } else if (message.equals("Access Denied ! Authentication Failed")) {


                        Log.e("msg 2 offline", ">>>>>>>>>>>>>>>" + message);

                    }else if(message.equals("This exam has already been taken!")){

                        Log.e("msg 3 offline", ">>>>>>>>>>>>>>>" + message);
                    }
                }else{


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        protected JSONObject doInBackground(Void... params) {

            // step one : converting comma separate String to array of String
            String[] elements = text1.split(",");

// step two : convert String array to list of String
            List<String> fixedLenghtList = Arrays.asList(elements);

// step three : copy fixed list to an ArrayList
            getExactQnum = new ArrayList<String>(fixedLenghtList);

            //        Log.e("list from Exact:",">>>>>>>>>>>." +getExactQnum);

            // step one : converting comma separate String to array of String
            String[] elements2 = text2.split(",");

// step two : convert String array to list of String
            List<String> fixedLenghtList2 = Arrays.asList(elements2);

// step three : copy fixed list to an ArrayList
            getDbsltAns = new ArrayList<String>(fixedLenghtList2);

            //    Log.e("list from Answers:",">>>>>>>>>>>." + getDbsltAns);

            return postJsonObjectOffline(Answers_url, makingJsonOffline());

        }
    }

    private JSONObject makingJsonOffline() {

        JSONObject postDataParams = new JSONObject();

        try {

//
            //following parameters to the API
            postDataParams.put("key", a1);
            postDataParams.put("exam_id", a2);
            postDataParams.put("student_id", a3);
            postDataParams.put("student_name", a4);
            postDataParams.put("examdate", a5);
            postDataParams.put("ssc_id", a6);
            postDataParams.put("trade_id", a7);
            postDataParams.put("tb_id", a8);
            postDataParams.put("starttime", a9);
            postDataParams.put("endtime", a10);
            postDataParams.put("IP_address", a11);
            postDataParams.put("browser", a12);
            postDataParams.put("question_ids", text1);
            postDataParams.put("snapshot_image_name", a14);
            postDataParams.put("snapshot_image_date", a15);

            Log.e("AYSNTASK POST", "STD ID TEST" + a3);


            for(int i=0;i<getDbsltAns.size();i++)
            {
                postDataParams.put("radio_"+getExactQnum.get(i),getDbsltAns.get(i));

                Log.e("MARKS TAKEN POST FOR",">>>>>>>>>>." + "radio_"+getExactQnum.get(i)+ getDbsltAns.get(i));

            }

            HashMap<String, String> params=new HashMap<String, String>();
            params.put("params",postDataParams.toString());

              Log.e("Answer Submitted 1", ">>>>>>>>>>>."+ params);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postDataParams;
    }



    public JSONObject postJsonObjectOffline(String url, JSONObject loginJobjOffline) {
        InputStream inputStreamOffline = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            System.out.println(url);
            String json = "";

            // 4. convert JSONObject to JSON to String
            json = loginJobjOffline.toString();

            System.out.println(json);
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);
            httpPost.setEntity(new StringEntity(loginJobjOffline.toString(), "UTF-8"));

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStreamOffline = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStreamOffline != null) {
                result = convertInputStreamToStringOffline(inputStreamOffline);

                Log.e("RESULT RESPONSE", ">>>>>>>>>>>>>>>>>>." + result);
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

    private String convertInputStreamToStringOffline(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        return result;
    }


    // for getting the questions from server
    public class AnswersPostRequest extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

            // Showing progress dialog
            pDialog = new ProgressDialog(Test_Main.this);
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

                    reader = jsonObject;
                    Log.e("MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.contains("Success Answer submitted successfully.")) {

                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Test_Main.this,Main_Exam.class);
                        startActivity(intent);

                        if(get_snap.equals("Yes")){
                            deleteRecursive(fileDirectory);
                        }else if(get_snap.equals("No")){

                        }


                    } else if (message.equals("Access Denied ! Authentication Failed")) {


                        Toast.makeText(getApplicationContext(),
                                "Access Denied", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();

                    }else if(message.equals("This exam has already been taken!")){

                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }else if(message.equals("Incorrect Parameters Passed! Error while saving the results. Please contact Admin")){

                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }else if(message.equals("Incorrect Parameters Passed! Error while saving the results. Please contact Admin")){

                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }else if(message.equals("Error while saving the results. Please contact Admin")){

                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }


                }else{


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObject1(Answers_url, makingJson1());

        }
    }

    private JSONObject makingJson1() {

        String strQnum = Arrays.toString(QuestionsID.toArray()).replace("[", "").replace("]", "");

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", API_KEY);
            postDataParams.put("exam_id", get_examID);
            postDataParams.put("student_id", get_stdID);
            postDataParams.put("student_name", get_stdname);
            postDataParams.put("examdate", exam_date);
            postDataParams.put("ssc_id", get_sscID);
            postDataParams.put("trade_id", get_tradeID);
            postDataParams.put("tb_id", get_tbID);
            postDataParams.put("starttime", exam_StartTym);
            postDataParams.put("endtime", exam_EndTym);
            postDataParams.put("IP_address", ip);
            postDataParams.put("browser", "mobile_app");
            postDataParams.put("question_ids", strQnum);
            postDataParams.put("snapshot_image_name", get_snames);
            postDataParams.put("snapshot_image_date", get_sdate);

            Log.e("CONNECT_SUBMITSERVER", ">>>>>>> " + API_KEY + " " + get_examID + " " + get_stdID + " " + get_stdname + " " + exam_date +
                    " " + get_sscID + " " + get_tradeID + " " + get_tbID + " " + exam_StartTym + " " + exam_EndTym + " " +
                    ip + " " + " mobile_app " + " " + String.valueOf(QuestionsID) + " " + get_snames + " " + get_sdate);



            for(int i=0;i<QuestionsID.size();i++)
            {

                postDataParams.put("radio_"+QuestionsID.get(i),SelectedAnswers.get(i));

                Log.e("Connectoptions",">>>>>>>>>>." + "radio_"+QuestionsID.get(i) + SelectedAnswers.get(i));
            }

            HashMap<String, String> params=new HashMap<String, String>();
            params.put("params",postDataParams.toString());

            Log.e("INTERNET", ">>>>>>>>>>>."+ params);

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
                Log.e("RESULT RESPONSE", ">>>>>>>>>>>>>>>>>>." + result);
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

    public void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }

}

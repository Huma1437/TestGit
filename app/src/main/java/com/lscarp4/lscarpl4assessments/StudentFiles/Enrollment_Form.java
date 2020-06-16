package com.lscarp4.lscarpl4assessments.StudentFiles;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.lscarp4.lscarpl4assessments.Database.DBHelper;
import com.lscarp4.lscarpl4assessments.District;
import com.lscarp4.lscarpl4assessments.GPSTracker;
import com.lscarp4.lscarpl4assessments.R;
import com.lscarp4.lscarpl4assessments.State;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Enrollment_Form extends AppCompatActivity {

    TextView header;
    ImageView logout,desc;
    EditText fname,lname,adname,dob,age,mobile,landline,email,adharnum,doornum,streetName,village,dist,state,pin;
    EditText pdoornum,pstreetName,pvillage,ppin,fathersName,mname,foccu,moccu,fmobile,fincome;
    EditText gname,relationshp,goccu,gnum,caste,qualify,poy,inst,marks,wrkexp,currEmployer,empAddress,accntNum,BankName,branch,ifsc;
    Button uploadpic,save,back;
    CheckBox sameAddress;
    private ImageView imageview;
    private static final String IMAGE_DIRECTORY = "/lscmis_enrollment";
    private int GALLERY = 1, CAMERA = 2;


    String Gender = "";
    RadioGroup radioGroup1;
    private RadioButton rbmale,rbfmale,btn;
    String textgender,base64,ageS;
    String buttonnIDgender,get_stdname,get_stdID,getAadhar,getStdMob,getGuardianN,getStdemail;

    AlertDialog alertDialog=null;
    NetworkChangeReceiver br;
    private ProgressDialog pDialog;

    Dialog dialog;
    String b_ID,b_name,j_role,enrol_num;
    TextView s_name,e_num,batch_id,bname,jobRole;


    final static String Enroll_Form_url = "http://lscmis.com/arpl4/student/student_api/update_enrollment_form";
    private static final String New_API_KEY = "YIA0Uub5QLhH9hxXRL1A6tXDwFWzbaUL";
    static final int DATE_DIALOG_ID = 0;
    DBHelper mydb;
    Context context;

    final static String m_status_api = "http://lscmis.com/arpl4/student/student_api/get_marital_status_options";
    final static String get_religion_api = "http://lscmis.com/arpl4/student/student_api/get_religion";
    final static String get_category = "http://lscmis.com/arpl4/student/student_api/get_category";
    final static String get_state = "http://lscmis.com/arpl4/student/student_api/get_state";
    final static String get_district = "http://lscmis.com/arpl4/student/student_api/get_district";
    Spinner m_Status_spinner,reg,categ,state1,dist1,pstate1,pdist1;
    ArrayList<String> Marital_Status,Religion,Cat;
    String m_status,getfreligion,getcat,getstate,getdist,getpdist,getpstate;
    ArrayList<District> Dist1,pDist1;
    ArrayList<State> State1,pState1;
    ArrayAdapter distAdapter,stateAdapter,pdistAdapter,pstateAdapter;
    private boolean selected_dist,selected_state,selected_pstate,selected_pdist;
    EditText ed_state,ed_dist;
    private LayoutInflater mInflator;
    TextView errorText;
    int s1,d1;
    double latitude,longitude;
    String address;
    Geocoder geocoder;
    private GPSTracker gpsTracker;
    List<Address> addresses;

    String getfname,getlname,getadname,getdob,getage,getmobile,getlandline,getEmail,getadhar,get_adhar_picname,getdnum,getstrtname,getvillage,getpin;
    String getpdnum,getpstrtn,getpvillage,getppin,getfathern,getmname,getfoccu,getmoccu,getFmobile,getfincome;
    String getgname,getrship,getgoccu,getgnum,getcaste,getqua,getpoy,getinst,getmarks,getwrk,getcurrEmp,getempAddr,getacntNum,getbname,getbranch,getcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment_form);

        br = new NetworkChangeReceiver();
        mydb = new DBHelper(this);

        String requireEnroll = Splash_Screen.sh.getString("enrollment_form", null);
        String getStatus = Splash_Screen.sh.getString("enrollment_form_status", null);

        Log.e("Enrollment Status", " Enrollment Page" + getStatus + " " + requireEnroll);

        if(getStatus.contains("1") || requireEnroll.contains("No")){

            Intent intent = new Intent(this,OtpScreen.class);
            startActivity(intent);
        }


        header = (TextView)findViewById(R.id.header);
        header.setText("Enrollment Form");

        requestMultiplePermissions();
        gpsTracker = new GPSTracker(Enrollment_Form.this);
        geocoder = new Geocoder(this, Locale.getDefault());

        mInflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        m_Status_spinner = (Spinner)findViewById(R.id.m_status);
        reg = (Spinner)findViewById(R.id.reg);
        categ = (Spinner)findViewById(R.id.cat);
        state1 = (Spinner)findViewById(R.id.state);
        dist1 = (Spinner)findViewById(R.id.dis);
        pstate1 = (Spinner)findViewById(R.id.statep);
        pdist1 = (Spinner)findViewById(R.id.disp);
        Religion = new ArrayList<>();
        Marital_Status = new ArrayList<>();
        Cat = new ArrayList<>();
        State1 = new ArrayList<>();
        Dist1 = new ArrayList<>();
        pState1 = new ArrayList<>();
        pDist1 = new ArrayList<>();

        get_stdname = Splash_Screen.sh.getString("get_stdname", null);
        get_stdID = Splash_Screen.sh.getString("student_id", null);
        getAadhar = Splash_Screen.sh.getString("aadhaarNum", null);
        getStdMob = Splash_Screen.sh.getString("StdMobile", null);
        getGuardianN = Splash_Screen.sh.getString("Gname", null);
        getStdemail = Splash_Screen.sh.getString("StdEmail", null);

        b_name = Splash_Screen.sh.getString("tb_name", null);
        j_role = Splash_Screen.sh.getString("trade_title", null);
        b_ID = Splash_Screen.sh.getString("tb_nsdc_id", null);
        enrol_num = Splash_Screen.sh.getString("SDMS_enrolment_number", null);



        logout = (ImageView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "You have successfully logout",
                        Toast.LENGTH_LONG).show();
                Splash_Screen.editor.remove("loginTest");
                Splash_Screen.editor.commit();

                Intent intent = new Intent(Enrollment_Form.this,Login.class);
                startActivity(intent);
            }
        });

        desc = (ImageView) findViewById(R.id.desc);

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(Enrollment_Form.this);
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


       // image_name = (TextView) findViewById(R.id.image_name);
        fname = (EditText)findViewById(R.id.edit_fname);
        lname =(EditText) findViewById(R.id.ed_lname);
        adname = (EditText) findViewById(R.id.ed_adname);
        dob = (EditText) findViewById(R.id.edt_bday);
        age = (EditText) findViewById(R.id.ed_age);
        mobile = (EditText) findViewById(R.id.ed_mobile);
        landline = (EditText) findViewById(R.id.ed_land);
        email = (EditText) findViewById(R.id.edt_email);
        adharnum = (EditText) findViewById(R.id.aadhar);
        doornum = (EditText) findViewById(R.id.dnum);
        streetName = (EditText) findViewById(R.id.strName);
        village = (EditText) findViewById(R.id.village);
        pin = (EditText) findViewById(R.id.pin);
        pdoornum = (EditText) findViewById(R.id.dnump);
        pstreetName = (EditText) findViewById(R.id.strNamep);
        pvillage = (EditText) findViewById(R.id.villagep);
        ppin = (EditText) findViewById(R.id.pinp);
        fathersName = (EditText) findViewById(R.id.FatherName);
        mname = (EditText) findViewById(R.id.mname);
        foccu = (EditText) findViewById(R.id.Foccu);
        moccu = (EditText) findViewById(R.id.moccu);
        fmobile = (EditText) findViewById(R.id.fatherMobile);
        fincome = (EditText) findViewById(R.id.fincome);
        ed_state = (EditText) findViewById(R.id.statepEdit);
        ed_dist = (EditText) findViewById(R.id.dispEdit);

        ed_state.setVisibility(View.GONE);
        ed_dist.setVisibility(View.GONE);

        gname = (EditText) findViewById(R.id.gname);
        relationshp = (EditText) findViewById(R.id.relation);
        goccu = (EditText) findViewById(R.id.occu);
        gnum = (EditText) findViewById(R.id.Gcont);
        caste = (EditText)findViewById(R.id.caste);
        qualify = (EditText)findViewById(R.id.qua);
        poy = (EditText) findViewById(R.id.poy);
        inst = (EditText) findViewById(R.id.inst);
        marks = (EditText) findViewById(R.id.marks);
        wrkexp = (EditText) findViewById(R.id.year);
        currEmployer = (EditText) findViewById(R.id.cemploy);
        empAddress = (EditText) findViewById(R.id.empAddress);
        accntNum = (EditText) findViewById(R.id.accntNum);
        BankName = (EditText) findViewById(R.id.edbank);
        branch = (EditText) findViewById(R.id.edBranch);
        ifsc = (EditText) findViewById(R.id.edcode);

        uploadpic = (Button) findViewById(R.id.pic_upload);
        imageview = (ImageView) findViewById(R.id.adharImg);
        imageview.setVisibility(View.GONE);
        save = (Button) findViewById(R.id.save);
        back = (Button) findViewById(R.id.backbtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Enrollment_Form.this,MainActivity.class);
                startActivity(intent);
            }
        });

        uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPictureDialog();
            }
        });


        new GetMaritalStatus().execute();
        new GetReligionStatus().execute();
        new GetCategory().execute();
        new GetState().execute();
        new PermanentGetState().execute();

        //Spinner for marital status
        m_Status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);
                Log.e("SPINNER CLICK", ">>>>>>>>>>>" + ">>>>>>..");
                m_status =   m_Status_spinner.getItemAtPosition(m_Status_spinner.getSelectedItemPosition()).toString();
                Log.e("Marital Status selectd", ">>>>>>>>>>>>" + m_status);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner for Religion
        reg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);
                getfreligion =   reg.getItemAtPosition(reg.getSelectedItemPosition()).toString();
                Log.e("Religion selectd", ">>>>>>>>>>>>" + getfreligion);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner for Category
        categ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);
                getcat =   categ.getItemAtPosition(categ.getSelectedItemPosition()).toString();
                Log.e("Category selectd", ">>>>>>>>>>>>" + getcat);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner for States [Current Address Details]
        state1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final State s = (State) state1.getItemAtPosition(position);

                getstate = s.getName();
                ((TextView)parent.getChildAt(0)).setText(getstate);
                ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);


                Log.e("Selected_State","???????????????/" + getstate);

                if(distAdapter != null){
                    distAdapter.clear();
                    distAdapter.notifyDataSetChanged();
                }

                new GetDistrict().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner for Districts [Current Address Details]
        dist1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final District d = (District) dist1.getItemAtPosition(position);

                getdist = d.getName();
                ((TextView)parent.getChildAt(0)).setText(getdist);
                ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);


                Log.e("Selected_Dist","???????????????/" + getdist);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner for State [Permanent Address Details]
        pstate1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final State s = (State) pstate1.getItemAtPosition(position);

                getpstate = s.getName();
                ((TextView)parent.getChildAt(0)).setText(getpstate);
                ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);


                Log.e("State_sel_Perm","????????/" + getpstate);

                if(pdistAdapter != null){
                    pdistAdapter.clear();
                    pdistAdapter.notifyDataSetChanged();
                }

                new PermanentGetDistrict().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner for Districts [Permanent Address Details]
        pdist1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final District d = (District) pdist1.getItemAtPosition(position);

                getpdist = d.getName();
                ((TextView)parent.getChildAt(0)).setText(getpdist);
                ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);


                Log.e("Sel_dist_perm","???????????????/" + getpdist);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sameAddress=(CheckBox)findViewById(R.id.checkBox1);

        radioGroup1 = (RadioGroup) findViewById(R.id.rdg1);
        rbmale = (RadioButton) findViewById(R.id.rbmale);
        rbfmale = (RadioButton) findViewById(R.id.rbfmale);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < radioGroup1.getChildCount(); i++) {
                    btn = (RadioButton) radioGroup1.getChildAt(i);
                    if (btn.getId() == checkedId) {
                        textgender = (String) btn.getText();
                        buttonnIDgender = Integer.toString(btn.getId());
                        Log.e("RADIOGRP1", " >>>>>>>>>>>>>>>" + textgender);

                        if(textgender.equals("Male")){
                            Gender = "M";

                            Log.e("Selected Gender", " >>>>>>>>>>>>>>>" + Gender);

                        }else if(textgender.equals("Female")){
                            Gender = "F";

                            Log.e("Selected Gender", " >>>>>>>>>>>>>>>" + Gender);
                        }

                        return;
                    } else {

                    }
                }
            }
        });


        dob.setInputType(InputType.TYPE_NULL);

        dob.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                if(v == dob)
                    showDialog(DATE_DIALOG_ID);
                return false;
            }
        });

        sameAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()) {

                    getdnum = doornum.getText().toString().trim();
                    getstrtname = streetName.getText().toString().trim();
                    getvillage = village.getText().toString().trim();
                    getpin = pin.getText().toString().trim();
                    s1 = stateAdapter.getPosition(getstate);
                    d1 = distAdapter.getPosition(getdist);

                    if(getdnum.isEmpty()){
                        doornum.requestFocus();
                        doornum.setError("Please Enter Door Number");
                    }else if(getstrtname.isEmpty()){
                        streetName.requestFocus();
                        streetName.setError("Enter Street Name");
                    }else if(getvillage.isEmpty()){
                        village.requestFocus();
                        village.setError("Enter Village Name");
                    }else if(getstate.isEmpty()){
                        state1.setFocusable(true);
                        state1.setFocusableInTouchMode(true);
                        state1.requestFocus();
                        Toast.makeText(Enrollment_Form.this, "Select State from List", Toast.LENGTH_LONG).show();
                    } else if(getdist.isEmpty()){
                        dist1.setFocusable(true);
                        dist1.setFocusableInTouchMode(true);
                        dist1.requestFocus();
                        Toast.makeText(Enrollment_Form.this, "Select District from List", Toast.LENGTH_LONG).show();
                    }else if(getpin.isEmpty()){
                        pin.requestFocus();
                        pin.setError("Enter Pincode");
                    }

                    // checked
                    if(!getdnum.isEmpty() || !getstrtname.isEmpty() || !getvillage.isEmpty() || !getdist.isEmpty() || !getstate.isEmpty() || !getpin.isEmpty()){

                        pdoornum.setText(getdnum);
                        pstreetName.setText(getstrtname);
                        pvillage.setText(getvillage);

                        ed_state.setVisibility(View.VISIBLE);
                        ed_state.setText(getstate);
                        getpstate = ed_state.getText().toString();
                        pstate1.setVisibility(View.GONE);

                        ed_dist.setVisibility(View.VISIBLE);
                        ed_dist.setText(getdist);
                        getpdist = ed_dist.getText().toString();
                        pdist1.setVisibility(View.GONE);

                        ppin.setText(getpin);

                        Log.e("CHECKBOX CHECKED", " " + getdnum + "  "+ getstrtname + " "+ getvillage+ " " + getdist +
                                " " + getstate + " " + getpin);

                    }else{

                        Log.e("SOME THINGS MISSING", "CHECKBOX CHECKED " + getdnum + "  "+ getstrtname + " "+ getvillage+ " " + getdist +
                                " " + getstate + " " + getpin);

                    }
                }
                else
                {
                    // not checked
                    pdoornum.setText("");
                    pstreetName.setText("");
                    pvillage.setText("");
                    ed_state.setVisibility(View.GONE);
                    ed_dist.setVisibility(View.GONE);
                    pstate1.setVisibility(View.VISIBLE);
                    pdist1.setVisibility(View.VISIBLE);
                    ppin.setText("");
                    Log.e("CHECKBOX NOT CHECKED", ">>>>>>>>>>." + buttonView);
                }
            }
        });

        if(!get_stdname.isEmpty() || !getAadhar.isEmpty() || !getStdMob.isEmpty() || !getGuardianN.isEmpty() || !getStdemail.isEmpty()){
            fname.setText(get_stdname);
            adharnum.setText(getAadhar);
            mobile.setText(getStdMob);
            gname.setText(getGuardianN);
            email.setText(getStdemail);
        }

        if(getGuardianN.equals("null")){
            gname.setText("");
        }
        if(getAadhar.equals("0")){
            adharnum.setText("");
        }
        if(getStdMob.equals("0")){
            mobile.setText("");
        }

        save.setOnClickListener(listener);

        if(haveNetworkConnection()){

            gpsTracker = new GPSTracker(Enrollment_Form.this);
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

        }else{

            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        }

    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        byte[] data1;
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);

                    imageview.setVisibility(View.VISIBLE);
                    Toast.makeText(Enrollment_Form.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageview.setImageBitmap(bitmap);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,10,baos);
                    byte[] b = baos.toByteArray();
                    base64 = Base64.encodeToString(b, Base64.DEFAULT);

                    Log.e("AadharPic B64 Gallery", ">>>>>>>>>>>>>." + base64);

                    File f = new File(path);
                    get_adhar_picname = f.getName();

                    Log.e("Adhar Image name", ">>>>>>>>>>>>>>>"  + get_adhar_picname);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Enrollment_Form.this, "Failed!", Toast.LENGTH_SHORT).show();
                }

            }

        } else if (requestCode == CAMERA) {
            imageview.setVisibility(View.VISIBLE);
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageview.setImageBitmap(thumbnail);

            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG,10,baos1);
            byte[] b1 = baos1.toByteArray();
            base64 = Base64.encodeToString(b1, Base64.DEFAULT);

            Log.e("AadharPic B64 cam", ">>>>>>>>>>>>>." + base64);

            String camera_img = saveImage(thumbnail);

            File f = new File(camera_img);
            get_adhar_picname = f.getName();

            Log.e("Camera IMAGE name", ">>>>>>>>>>>>>>>" + get_adhar_picname);

            Toast.makeText(Enrollment_Form.this, "Image Saved!", Toast.LENGTH_SHORT).show();

        }

    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {

            Log.e("GET STATES DIST", " ??????????????" + getstate + " " + getdist + " " + getpstate + " " + getpdist);

            editTextValidation();


            if(haveNetworkConnection()){

                if(!getfname.isEmpty() && !getlname.isEmpty() &&  !getadname.isEmpty() && !Gender.isEmpty() && !getdob.isEmpty() && !getage.isEmpty() &&
                         dob.length() != 0 && !getmobile.isEmpty() && getmobile.length() >= 10 && !getlandline.isEmpty() && getlandline.length() >= 10 &&
                        !getEmail.isEmpty() && !getadhar.isEmpty() && base64 != null && !getdnum.isEmpty() && !getstrtname.isEmpty() && !getvillage.isEmpty() &&
                        !getdist.isEmpty() && !getstate.isEmpty() && !getpin.isEmpty() && !getpdnum.isEmpty() && !getpstrtn.isEmpty() && !getpvillage.isEmpty() &&
                        !getpdist.isEmpty() && !getpstate.isEmpty() && !getppin.isEmpty()&& !getfathern.isEmpty() && !getmname.isEmpty() &&
                        !getfoccu.isEmpty() && !getmoccu.isEmpty() && !getFmobile.isEmpty() && getFmobile.length() >= 10 && !getfincome.isEmpty() &&
                        !getcaste.isEmpty() && !getcat.isEmpty() && !getqua.isEmpty() && !getpoy.isEmpty() && !getinst.isEmpty() &&
                        !getmarks.isEmpty() && !getwrk.isEmpty() && !getcurrEmp.isEmpty() &&
                        !getempAddr.isEmpty() && !getacntNum.isEmpty() && !getbname.isEmpty() && !getbranch.isEmpty() && !getcode.isEmpty())
                {

                    new EnrollmentFormPostRequest().execute();
                }
            }else{

                Toast.makeText(Enrollment_Form.this, "No Internet Connection!!! Please Enable Internet", Toast.LENGTH_LONG).show();

            }

        }
    };

    public void editTextValidation(){
        getfname = fname.getText().toString().trim();
        getlname = lname.getText().toString().trim();
        getadname = adname.getText().toString().trim();
        getdob = dob.getText().toString().trim();
        getage = age.getText().toString().trim();
        getmobile = mobile.getText().toString().trim();
        getlandline = landline.getText().toString().trim();
        getEmail = email.getText().toString().trim();
        getadhar = adharnum.getText().toString().trim();
        getdnum = doornum.getText().toString().trim();
        getstrtname = streetName.getText().toString().trim();
        getvillage = village.getText().toString().trim();
        getpin = pin.getText().toString().trim();
        getpdnum = pdoornum.getText().toString().trim();
        getpstrtn = pstreetName.getText().toString().trim();
        getpvillage = pvillage.getText().toString().trim();
        getppin = ppin.getText().toString().trim();
        getfathern = fathersName.getText().toString().trim();
        getmname = mname.getText().toString().trim();
        getfoccu = foccu.getText().toString().trim();
        getmoccu = moccu.getText().toString().trim();
        getFmobile = fmobile.getText().toString().trim();
        getfincome = fincome.getText().toString().trim();
        getgname = gname.getText().toString().trim();
        getrship = relationshp.getText().toString().trim();
        getgoccu = goccu.getText().toString().trim();
        getgnum = gnum.getText().toString().trim();
        getcaste = caste.getText().toString().trim();
        getqua = qualify.getText().toString().trim();
        getpoy = poy.getText().toString().trim();
        getinst = inst.getText().toString().trim();
        getmarks = marks.getText().toString().trim();
        getwrk = wrkexp.getText().toString().trim();
        getcurrEmp = currEmployer.getText().toString().trim();
        getempAddr = empAddress.getText().toString().trim();
        getacntNum = accntNum.getText().toString().trim();
        getbname = BankName.getText().toString().trim();
        getbranch = branch.getText().toString().trim();
        getcode = ifsc.getText().toString().trim();

        //Personal Details Validations
        if (getfname.isEmpty()) {
            fname.requestFocus();
            fname.setError("Please enter First Name");
        }  else if (getlname.isEmpty()) {
            lname.requestFocus();
            lname.setError("Please enter Last Name");
        }else if (getadname.isEmpty()) {
            adname.requestFocus();
            adname.setError("Please enter Name as per Aadhaar");
        } else if (!rbfmale.isChecked() && !rbmale.isChecked()) {
            Toast.makeText(getApplicationContext(),
                    "Please select Gender", Toast.LENGTH_LONG).show();
        } else if (getdob.isEmpty()) {
            dob.requestFocus();
            Toast.makeText(getApplicationContext(),
                    "Please enter Date of Birth", Toast.LENGTH_LONG).show();
        }else if(m_status.isEmpty()){
            m_Status_spinner.setFocusable(true);
            m_Status_spinner.setFocusableInTouchMode(true);
            m_Status_spinner.requestFocus();
            Toast.makeText(getApplicationContext(),
                    "Please select Marital Status", Toast.LENGTH_LONG).show();
        } else if(getage.isEmpty()){
            age.requestFocus();
            age.setError("Please select your Age");
        }else if(getmobile.length() < 10){
            mobile.requestFocus();
            mobile.setError("Must exceed 10 characters!");
        }else if (getmobile.isEmpty()) {
            mobile.requestFocus();
            mobile.setError("Please enter Mobile Number");
        } else if(getlandline.length() < 10){
            landline.requestFocus();
            landline.setError("Must exceed 10 characters!");
        }else if (getlandline.isEmpty()) {
            landline.requestFocus();
            landline.setError("Please enter Alt phone Number");
        }else if (getEmail.isEmpty()|| (!isEmailValid(getEmail))) {
            email.requestFocus();
            email.setError( "Please enter valid Email Id");
        }else if(getadhar.isEmpty()){
            adharnum.requestFocus();
            adharnum.setError("Please Enter Aadhaar Card Number");
        } else if(base64 == null){
            Toast.makeText(Enrollment_Form.this, "Select Aadhaar Photo", Toast.LENGTH_SHORT).show();
        }

        //Current Address validation
        else if(getdnum.isEmpty()){
            doornum.requestFocus();
            doornum.setError("Please Enter Door Number");
        }else if(getstrtname.isEmpty()){
            streetName.requestFocus();
            streetName.setError("Enter Street Name");
        }else if(getvillage.isEmpty()){
            village.requestFocus();
            village.setError("Enter Village Name");
        }else if(getstate.isEmpty()){
            state1.setFocusable(true);
            state1.setFocusableInTouchMode(true);
            state1.requestFocus();
            Toast.makeText(Enrollment_Form.this,"Select State from list",Toast.LENGTH_LONG).show();
        }else if(getdist.isEmpty()){
            dist1.setFocusable(true);
            dist1.setFocusableInTouchMode(true);
            dist1.requestFocus();
            Toast.makeText(Enrollment_Form.this,"Select District from list",Toast.LENGTH_LONG).show();
        } else if(getpin.isEmpty()){
            pin.requestFocus();
            pin.setError("Enter Pincode");
        }

        //Permannent Address validations
        else if(getpdnum.isEmpty()){
            pdoornum.requestFocus();
            pdoornum.setError("Please Enter Door Number");
        }else if(getpstrtn.isEmpty()){
            pstreetName.requestFocus();
            pstreetName.setError("Enter Street Name");
        }else if(getpvillage.isEmpty()){
            pvillage.requestFocus();
            pvillage.setError("Enter Village Name");
        }else if(getpstate.isEmpty()){
            pstate1.setFocusable(true);
            pstate1.setFocusableInTouchMode(true);
            pstate1.requestFocus();
            Toast.makeText(Enrollment_Form.this,"Select State from list",Toast.LENGTH_LONG).show();
        }else if(getpdist.isEmpty()){
            pdist1.setFocusable(true);
            pdist1.setFocusableInTouchMode(true);
            pdist1.requestFocus();
            Toast.makeText(Enrollment_Form.this,"Select District from list",Toast.LENGTH_LONG).show();
        } else if(getppin.isEmpty()){
            ppin.requestFocus();
            ppin.setError("Enter Pincode");
        }

        //Family Details Validations
        else if(getfathern.isEmpty()){
            fathersName.requestFocus();
            fathersName.setError("Enter Father Name");
        }else if(getmname.isEmpty()){
            mname.requestFocus();
            mname.setError("Enter Mother Name");
        }else if(getfoccu.isEmpty()){
            foccu.requestFocus();
            foccu.setError("Enter Father Occupation");
        }else if(getmoccu.isEmpty()){
            moccu.requestFocus();
            moccu.setError("Enter Mother Occupation");
        } else if(getFmobile.isEmpty()){
            fmobile.requestFocus();
            fmobile.setError("Enter Mobile Number");
        }else if(getFmobile.length() < 10){
            fmobile.requestFocus();
            fmobile.setError("Must exceed 10 characters!");
        }else if(getfincome.isEmpty()){
            fincome.requestFocus();
            fincome.setError("Enter Family Income");
        }else if(getfreligion.isEmpty()){
            reg.setFocusable(true);
            reg.setFocusableInTouchMode(true);
            reg.requestFocus();
            Toast.makeText(Enrollment_Form.this,"Select Religion",Toast.LENGTH_LONG).show();
        }

        //Guardian Details Validations
       /* else if(getgname.isEmpty()){
            gname.requestFocus();
            gname.setError("Enter Guardian Name");
        } else if(getrship.isEmpty()){
            relationshp.requestFocus();
            relationshp.setError("Enter Relationship");
        } else if(getgoccu.isEmpty()){
            goccu.requestFocus();
            goccu.setError("Enter Guardian Occupation");
        } else if(getgnum.isEmpty()){
            gnum.requestFocus();
            gnum.setError("Enter Guardian Number");
        }*/else if(getcat.isEmpty()){
            categ.setFocusable(true);
            categ.setFocusableInTouchMode(true);
            categ.requestFocus();
            Toast.makeText(Enrollment_Form.this,"Select Category",Toast.LENGTH_LONG).show();
        } else if(getcaste.isEmpty()){
            caste.requestFocus();
            caste.setError("Enter Caste");
        }

        //Educational Details validations
        else if(getqua.isEmpty()){
            qualify.requestFocus();
            qualify.setError("Enter Qualification");
        }else if(getpoy.isEmpty()){
            poy.requestFocus();
            poy.setError("Enter Pass Out Year");
        }else if(getinst.isEmpty()){
            inst.requestFocus();
            inst.setError("Enter Institution");
        }else if(getmarks.isEmpty()){
            marks.requestFocus();
            marks.setError("Enter Marks Obtained");
        }

        //Work Experience validations
        else if(getwrk.isEmpty()){
            wrkexp.requestFocus();
            wrkexp.setError("Enter Experience in Years");
        }else if(getcurrEmp.isEmpty()){
            currEmployer.requestFocus();
            currEmployer.setError("Enter Current Employer");
        }else if(getempAddr.isEmpty()){
            empAddress.requestFocus();
            empAddress.setError("Enter Employer Address");
        }

        //Bank Details Validations
           else if(getacntNum.isEmpty()){
            accntNum.requestFocus();
            accntNum.setError("Enter Account Number");
        }else if(getbname.isEmpty()){
            BankName.requestFocus();
            BankName.setError("Enter Bank Name");
        }else if(getbranch.isEmpty()){
            branch.requestFocus();
            branch.setError("Enter Branch Name");
        }else if(getcode.isEmpty()){
            ifsc.requestFocus();
            ifsc.setError("Enter IFSC Code");
        }
    }


    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        ageS = ageInt.toString();

        Log.e("AGES",">>>>>>>>>" +ageS);

        return ageS;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH);
        int cday = c.get(Calendar.DAY_OF_MONTH);
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, cyear, cmonth, cday);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        // onDateSet method
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String date_selected = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year);

            String new_date = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth);

            //Toast.makeText(getApplicationContext(), "Selected Date is ="+date_selected, Toast.LENGTH_SHORT).show();
            dob.setText(date_selected);

            getdob = dob.getText().toString();

            Log.e("GET_DOB", ">>>>>>>>" +getdob);

            getAge(year, monthOfYear,dayOfMonth);

            age.setText(ageS);

            Log.e("DATEPICKER",">>>>>>>>" +dayOfMonth + monthOfYear +year);
        }
    };


    public void dialogBoxForInternet() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Enrollment_Form.this);
        alertDialogBuilder.setTitle("No Internet Connection.");
        alertDialogBuilder
                .setMessage("Go to Settings to enable Internet Connectivity")
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

    // for getting the questions from server
    private class EnrollmentFormPostRequest extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

            // Showing progress dialog
            pDialog = new ProgressDialog(Enrollment_Form.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                if (jsonObject != null && jsonObject.getString("status_message") != null) {

                    String message = jsonObject.getString("status_message");  // Message

                    Log.e("MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.equals("Success, Details Updated")) {

                        Toast.makeText(getApplicationContext(),
                                "Success, Details Updated", Toast.LENGTH_LONG).show();

                        Log.e("SUCCES MSG", ">>>>>>>>>>>>>>>" + message);

                        Intent intent = new Intent(Enrollment_Form.this, OtpScreen.class);
                        startActivity(intent);
                        pDialog.dismiss();

                    } else if (message.equals("Access Denied ! Authentication Failed")) {


                        Toast.makeText(getApplicationContext(),
                                "Access Denied", Toast.LENGTH_LONG).show();

                        Log.e("FAILED MSG", ">>>>>>>>>>>>>>>" + message);
                        pDialog.dismiss();

                    }else if (message.equals("Failure, Unable to upload aadhar photo")) {


                        Toast.makeText(getApplicationContext(),
                                "Unable to upload aadhar photo", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(Enrollment_Form.this, OtpScreen.class);
                        startActivity(intent);
                        pDialog.dismiss();

                    }
                }else{


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObject(Enroll_Form_url, makingJson());

        }
    }

    private JSONObject makingJson() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", New_API_KEY);
            postDataParams.put("student_id", get_stdID);
            postDataParams.put("first_name", getfname);
            postDataParams.put("last_name", getlname);
            postDataParams.put("name_as_per_aadhar", getadname);
            postDataParams.put("gender", Gender);
            postDataParams.put("date_of_birth", getdob);
            postDataParams.put("marital_status", m_status);
            postDataParams.put("age", getage);
            postDataParams.put("student_mobile", getmobile);
            postDataParams.put("student_landline_no", getlandline);
            postDataParams.put("student_email", getEmail);
            postDataParams.put("aadhaar_number", getadhar);
            postDataParams.put("aadhaar_photo", base64);
            postDataParams.put("aadhaar_photo_name", get_adhar_picname);
            postDataParams.put("curr_door_no", getdnum);
            postDataParams.put("curr_street_name", getstrtname);
            postDataParams.put("curr_village_city", getvillage);
            postDataParams.put("curr_district", getdist);
            postDataParams.put("curr_state", getstate);
            postDataParams.put("curr_pincode", getpin);
            postDataParams.put("perm_door_no", getpdnum);
            postDataParams.put("perm_street_name", getpstrtn);
            postDataParams.put("perm_village_city", getpvillage);
            postDataParams.put("perm_district", getpdist);
            postDataParams.put("perm_state", getpstate);
            postDataParams.put("perm_pincode", getppin);
            postDataParams.put("father_name", getfathern);
            postDataParams.put("mother_name", getmname);
            postDataParams.put("father_occupation", getfoccu);
            postDataParams.put("mother_occupation", getmoccu);
            postDataParams.put("father_cont_no", getFmobile );
            postDataParams.put("family_income", getfincome);
            postDataParams.put("religion", getfreligion );
            postDataParams.put("guardian_name", getgname);
            postDataParams.put("relationship", getrship);
            postDataParams.put("guardian_occupation", getgoccu);
            postDataParams.put("guardian_contact_no", getgnum);
            postDataParams.put("guardian_category", getcat);
            postDataParams.put("guardian_caste", getcaste);
            postDataParams.put("educational_qualification", getqua);
            postDataParams.put("exp_yrs", getwrk);
            postDataParams.put("passed_year", getpoy);
            postDataParams.put("current_employer", getcurrEmp);
            postDataParams.put("institution", getinst);
            postDataParams.put("employer_address", getempAddr);
            postDataParams.put("marks_obtained", getmarks);
            postDataParams.put("bank_account_no", getacntNum);
            postDataParams.put("bank_name", getbname);
            postDataParams.put("bank_branch", getbranch);
            postDataParams.put("ifsc_code", getcode);
            postDataParams.put("geo_latitude", latitude);
            postDataParams.put("geo_longitude", longitude);
            postDataParams.put("geo_address", address);


            Log.e("POST PARAMS", "??????????" + get_stdID + " " + getdnum + " " +getstrtname + " " + getvillage + " " + getdist + " " +
                    getstate + " " + getpin + " " + getpdnum + " " + getpstrtn + " " + getpdist + " " + getpstate + " " + getppin );

            Log.e("LAT LONG ADDR", "??????????????" + latitude + " " + longitude + " " + address);

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


    //Getting the values of marital status from server
    // for getting the questions from server
    private class GetMaritalStatus extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            try {
                if (jsonObject != null && jsonObject.getString("status_message") != null) {

                    String message = jsonObject.getString("status_message");  // Message

                    Log.e("MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.contains("Success")) {

                        JSONArray a = jsonObject.getJSONArray("arr_marital_status_options");
                        for (int i = 0; i < a.length(); i++) {
                            Marital_Status.add(a.getString(i));
                            Log.e("MArital Status list", "????????????" +  a.getString(i));
                        }
                        m_Status_spinner.setAdapter(new ArrayAdapter<String>(Enrollment_Form.this, android.R.layout.simple_spinner_dropdown_item, Marital_Status));


                    } else if (message.equals("Access Denied ! Authentication Failed")) {


                        Toast.makeText(getApplicationContext(),
                                "Access Denied", Toast.LENGTH_LONG).show();


                    }
                }else{


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObjectMstatus(m_status_api, makingJsonMstatus());

        }
    }

    private JSONObject makingJsonMstatus() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", New_API_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postDataParams;
    }



    public JSONObject postJsonObjectMstatus(String url, JSONObject loginJobj) {
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
                result = convertIstostrMstatus(inputStream);
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

    private String convertIstostrMstatus(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }


    // for getting the religion values from server
    private class GetReligionStatus extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            try {
                if (jsonObject != null && jsonObject.getString("status_message") != null) {

                    String message = jsonObject.getString("status_message");  // Message

                    Log.e("RELIGION MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.contains("Success")) {

                        JSONArray a = jsonObject.getJSONArray("arr_religion");
                        for (int i = 0; i < a.length(); i++) {
                            Religion.add(a.getString(i));
                            Log.e("Reg list", "????????????" +  a.getString(i));
                        }

                        Log.e("Religion list", "????????????" +  Religion);

                        reg.setAdapter(new ArrayAdapter<String>(Enrollment_Form.this, android.R.layout.simple_spinner_dropdown_item, Religion));


                    } else if (message.equals("Access Denied ! Authentication Failed")) {


                        Toast.makeText(getApplicationContext(),
                                "Access Denied", Toast.LENGTH_LONG).show();


                    }
                }else{


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObjectReg(get_religion_api, makingJsonReg());

        }
    }

    private JSONObject makingJsonReg() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", New_API_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postDataParams;
    }



    public JSONObject postJsonObjectReg(String url, JSONObject loginJobj) {
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
                result = convertIstostrReg(inputStream);
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

    private String convertIstostrReg(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }


    // for getting the Category values from server
    private class GetCategory extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject1) {
            super.onPostExecute(jsonObject1);

            try {
                if (jsonObject1 != null && jsonObject1.getString("status_message") != null) {

                    String message = jsonObject1.getString("status_message");  // Message

                    Log.e("Category MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.contains("Success")) {

                        JSONArray a = jsonObject1.getJSONArray("arr_category");
                        for (int i = 0; i < a.length(); i++) {
                            Cat.add(a.getString(i));
                            Log.e("MArital Status list", "????????????" +  a.getString(i));
                        }

                        Log.e("Category list", "????????????" +  Cat);

                        categ.setAdapter(new ArrayAdapter<String>(Enrollment_Form.this, android.R.layout.simple_spinner_dropdown_item, Cat));


                    } else if (message.equals("Access Denied ! Authentication Failed")) {


                        Toast.makeText(getApplicationContext(),
                                "Access Denied", Toast.LENGTH_LONG).show();


                    }
                }else{


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObjectCat(get_category, makingJsonCat());

        }
    }

    private JSONObject makingJsonCat() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", New_API_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postDataParams;
    }



    public JSONObject postJsonObjectCat(String url, JSONObject loginJobj) {
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
                result = convertIstostrCat(inputStream);
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

    private String convertIstostrCat(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }


    // for getting the State values from server
    private class GetState extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject1) {
            super.onPostExecute(jsonObject1);

            State stateObject;

            try {
                if (jsonObject1 != null && jsonObject1.getString("status_message") != null) {

                    String message = jsonObject1.getString("status_message");  // Message

                    Log.e("States MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.contains("Success")) {

                        JSONArray a = jsonObject1.getJSONArray("arr_state");
                        for (int i = 0; i < a.length(); i++) {

                            JSONObject jObj = a.getJSONObject(i);
                            String id = jObj.getString("id");
                            String state_name = jObj.getString("name");

                            stateObject = new State(id,state_name);
                            State1.add(stateObject);
                            Log.e("State list", "????????????" +  a.getString(i));
                        }
/*
                        Log.e("Category list", "????????????" +  Cat);

                        state1.setAdapter(new ArrayAdapter<String>(Enrollment_Form.this, android.R.layout.simple_spinner_dropdown_item, State1));*/

                        stateAdapter = new ArrayAdapter<State>(getApplicationContext(), R.layout.spinner_item, State1) {

                            private TextView text;

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {

                                errorText = (TextView) dist1.getSelectedView();

                                if (convertView == null) {
                                    convertView = mInflator.inflate(R.layout.spinner_item, null);
                                }

                                text = (TextView) convertView.findViewById(R.id.spinnerTarget);
                                if (!selected_state) {
                                    text.setText("Select State");
                                } else {
                                    text.setText(State1.get(position).getName());
                                    text.setTextColor(getResources().getColor(R.color.primary_text));
                                }
                                return convertView;
                            }
                        };

                        if(State1.isEmpty()){
                            Toast.makeText(getApplicationContext(), "No States Found",
                                    Toast.LENGTH_LONG).show();
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }else{
                            stateAdapter.setDropDownViewResource(R.layout.spinner_item);
                            state1.setAdapter(stateAdapter);
                            state1.setEnabled(true);

                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }


                        /* dist1.setAdapter(new ArrayAdapter<String>(Enrollment_Form.this, android.R.layout.simple_spinner_dropdown_item, Dist1));*/

                        Log.e("District list", "????????????" +  Dist1);



                    } else if (message.equals("Access Denied ! Authentication Failed")) {


                        Toast.makeText(getApplicationContext(),
                                "Access Denied", Toast.LENGTH_LONG).show();


                    }
                }else{


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObjectState(get_state, makingJsonState());

        }
    }

    private JSONObject makingJsonState() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", New_API_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postDataParams;
    }



    public JSONObject postJsonObjectState(String url, JSONObject loginJobj) {
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
                result = convertIstostrState(inputStream);
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

    private String convertIstostrState(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }



    // for getting the Districts values from server
    private class GetDistrict extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject1) {
            super.onPostExecute(jsonObject1);

            District distObject;

            try {
                if (jsonObject1 != null && jsonObject1.getString("status_message") != null) {

                    String message = jsonObject1.getString("status_message");  // Message

                    Log.e("District MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.contains("Success")) {

                        JSONArray a = jsonObject1.getJSONArray("arr_district");
                        for (int i = 0; i < a.length(); i++) {

                            JSONObject jObj = a.getJSONObject(i);
                            String id = jObj.getString("id");
                            String dist_name = jObj.getString("name");

                            distObject = new District(id, dist_name);

                            Dist1.add(distObject);

                        }


                        distAdapter = new ArrayAdapter<District>(getApplicationContext(), R.layout.spinner_item, Dist1) {

                            private TextView text;

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {

                                errorText = (TextView) dist1.getSelectedView();

                                if (convertView == null) {
                                    convertView = mInflator.inflate(R.layout.spinner_item, null);
                                }

                                text = (TextView) convertView.findViewById(R.id.spinnerTarget);
                                if (!selected_dist) {
                                    text.setText("Select District");
                                } else {
                                    text.setText(Dist1.get(position).getName());
                                    text.setTextColor(getResources().getColor(R.color.primary_text));
                                }
                                return convertView;
                            }
                        };

                        if(Dist1.isEmpty()){
                            Toast.makeText(getApplicationContext(), "No Districts Found",
                                    Toast.LENGTH_LONG).show();
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }else{
                            distAdapter.setDropDownViewResource(R.layout.spinner_item);
                            dist1.setAdapter(distAdapter);
                            dist1.setEnabled(true);

                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }


                       /* dist1.setAdapter(new ArrayAdapter<String>(Enrollment_Form.this, android.R.layout.simple_spinner_dropdown_item, Dist1));*/

                        Log.e("District list", "????????????" +  Dist1);


                    } else if (message.equals("Access Denied ! Authentication Failed")) {


                        Toast.makeText(getApplicationContext(),
                                "Access Denied", Toast.LENGTH_LONG).show();


                    }
                }else{


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObjectDist(get_district, makingJsonDist());

        }
    }

    private JSONObject makingJsonDist() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", New_API_KEY);
            postDataParams.put("state_name", getstate);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postDataParams;
    }



    public JSONObject postJsonObjectDist(String url, JSONObject loginJobj) {
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
                result = convertIstostrDist(inputStream);
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

    private String convertIstostrDist(InputStream inputStream) throws IOException {
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
        Intent a = new Intent(this,MainActivity.class);
        finish();
        startActivity(a);
    }


    // for getting the State values from server [Permanent Address]
    private class PermanentGetState extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject1) {
            super.onPostExecute(jsonObject1);

            State stateObject;

            try {
                if (jsonObject1 != null && jsonObject1.getString("status_message") != null) {

                    String message = jsonObject1.getString("status_message");  // Message

                    Log.e("States MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.contains("Success")) {

                        JSONArray a = jsonObject1.getJSONArray("arr_state");
                        for (int i = 0; i < a.length(); i++) {

                            JSONObject jObj = a.getJSONObject(i);
                            String id = jObj.getString("id");
                            String state_name = jObj.getString("name");

                            stateObject = new State(id,state_name);
                            pState1.add(stateObject);
                            Log.e("State list", "????????????" +  a.getString(i));
                        }
/*
                        Log.e("Category list", "????????????" +  Cat);

                        state1.setAdapter(new ArrayAdapter<String>(Enrollment_Form.this, android.R.layout.simple_spinner_dropdown_item, State1));*/

                        pstateAdapter = new ArrayAdapter<State>(getApplicationContext(), R.layout.spinner_item, pState1) {

                            private TextView text;

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {

                                errorText = (TextView) dist1.getSelectedView();

                                if (convertView == null) {
                                    convertView = mInflator.inflate(R.layout.spinner_item, null);
                                }

                                text = (TextView) convertView.findViewById(R.id.spinnerTarget);
                                if (!selected_pstate) {
                                    text.setText("Select State");
                                } else {
                                    text.setText(pState1.get(position).getName());
                                    text.setTextColor(getResources().getColor(R.color.primary_text));
                                }
                                return convertView;
                            }
                        };

                        if(pState1.isEmpty()){
                            Toast.makeText(getApplicationContext(), "No States Found",
                                    Toast.LENGTH_LONG).show();
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }else{
                            pstateAdapter.setDropDownViewResource(R.layout.spinner_item);
                            pstate1.setAdapter(pstateAdapter);
                            pstate1.setEnabled(true);

                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }


                        /* dist1.setAdapter(new ArrayAdapter<String>(Enrollment_Form.this, android.R.layout.simple_spinner_dropdown_item, Dist1));*/

                        Log.e("District list", "????????????" +  Dist1);



                    } else if (message.equals("Access Denied ! Authentication Failed")) {


                        Toast.makeText(getApplicationContext(),
                                "Access Denied", Toast.LENGTH_LONG).show();


                    }
                }else{


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObjectStatePerm(get_state, makingJsonStatePerm());

        }
    }

    private JSONObject makingJsonStatePerm() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", New_API_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postDataParams;
    }



    public JSONObject postJsonObjectStatePerm(String url, JSONObject loginJobj) {
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
                result = convertIstostrStatePerm(inputStream);
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

    private String convertIstostrStatePerm(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }



    // for getting the Districts values from server [permanent Address]
    private class PermanentGetDistrict extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject1) {
            super.onPostExecute(jsonObject1);

            District distObject;

            try {
                if (jsonObject1 != null && jsonObject1.getString("status_message") != null) {

                    String message = jsonObject1.getString("status_message");  // Message

                    Log.e("District MESSAGE", ">>>>>>>>>>>>>>>>" + message);

                    if (message.contains("Success")) {

                        JSONArray a = jsonObject1.getJSONArray("arr_district");
                        for (int i = 0; i < a.length(); i++) {

                            JSONObject jObj = a.getJSONObject(i);
                            String id = jObj.getString("id");
                            String dist_name = jObj.getString("name");

                            distObject = new District(id, dist_name);

                            pDist1.add(distObject);

                        }


                        pdistAdapter = new ArrayAdapter<District>(getApplicationContext(), R.layout.spinner_item, pDist1) {

                            private TextView text;

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {

                                errorText = (TextView) dist1.getSelectedView();

                                if (convertView == null) {
                                    convertView = mInflator.inflate(R.layout.spinner_item, null);
                                }

                                text = (TextView) convertView.findViewById(R.id.spinnerTarget);
                                if (!selected_pdist) {
                                    text.setText("Select District");
                                } else {
                                    text.setText(pDist1.get(position).getName());
                                    text.setTextColor(getResources().getColor(R.color.primary_text));
                                }
                                return convertView;
                            }
                        };

                        if(pDist1.isEmpty()){
                            Toast.makeText(getApplicationContext(), "No Districts Found",
                                    Toast.LENGTH_LONG).show();
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }else{
                            pdistAdapter.setDropDownViewResource(R.layout.spinner_item);
                            pdist1.setAdapter(pdistAdapter);
                            pdist1.setEnabled(true);

                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }


                        /* dist1.setAdapter(new ArrayAdapter<String>(Enrollment_Form.this, android.R.layout.simple_spinner_dropdown_item, Dist1));*/

                        Log.e("District list", "????????????" +  Dist1);


                    } else if (message.equals("Access Denied ! Authentication Failed")) {


                        Toast.makeText(getApplicationContext(),
                                "Access Denied", Toast.LENGTH_LONG).show();


                    }
                }else{


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObjectDistPerm(get_district, makingJsonDistPerm());

        }
    }

    private JSONObject makingJsonDistPerm() {

        JSONObject postDataParams = new JSONObject();

        try {

            //following parameters to the API
            postDataParams.put("key", New_API_KEY);
            postDataParams.put("state_name", getpstate);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postDataParams;
    }



    public JSONObject postJsonObjectDistPerm(String url, JSONObject loginJobj) {
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
                result = convertIstostrDistPerm(inputStream);
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

    private String convertIstostrDistPerm(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}

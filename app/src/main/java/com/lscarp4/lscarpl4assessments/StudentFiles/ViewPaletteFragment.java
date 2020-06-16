package com.lscarp4.lscarpl4assessments.StudentFiles;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.lscarp4.lscarpl4assessments.Database.DBHelper;
import com.lscarp4.lscarpl4assessments.R;

import java.util.ArrayList;


public class ViewPaletteFragment extends DialogFragment {

    GridView gridView;
    ArrayList<String> QuestionNumbers = new ArrayList<>();
    ArrayList<String> Not_Answered = new ArrayList<>();
    ArrayList<String> Mark_for_Review = new ArrayList<>();
    ArrayList<String> Not_Visited = new ArrayList<>();
    TextView ans,notAns,notVisit,review;/*getans,getmark,getnattend*/;
    DBHelper mydb;
    String DbQnum,Dbq_id,DbSelectedOpt,m_review,notans,notvisit,qns,opa,opb,opc,opd,hqns,hopa,hopb,hopc,hopd;
    AlertDialog alertDialog=null;
    NetworkChangeReceiver br;
    Context context;
    String Db_stdID,Db_nosID,Db_nosCode,Db_nosTitle,Db_qid,Db_ques,Db_op1,Db_op2,Db_op3,Db_op4,Db_hques,Db_hop1,
            Db_hop2,Db_hop3,Db_hop4,Db_sopt,Db_review,Db_na,Db_nv,Db_qnum;


    public ViewPaletteFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ViewPaletteFragment newInstance(String title) {
        ViewPaletteFragment frag = new ViewPaletteFragment();

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.viewpalette, container, false);
        br = new NetworkChangeReceiver();
        gridView = (GridView) RootView.findViewById(R.id.gridview);
        ans = (TextView)RootView.findViewById(R.id.cir1);
        notAns = (TextView) RootView.findViewById(R.id.cir3);
        notVisit = (TextView) RootView.findViewById(R.id.cir2);
        review = (TextView) RootView.findViewById(R.id.cir4);

        //Get Argument that passed from activity in "data" key value
        String getArgument1 = getArguments().getString("Answered");
        String getArgument2 = getArguments().getString("NotAnswered");
        String getArgument3 = getArguments().getString("NotVisited");
        String getArgument4 = getArguments().getString("ReviewLater");
        QuestionNumbers = getArguments().getStringArrayList("Numbers");

        final String gridViewNumbers[] = new String[QuestionNumbers.size()];

        Log.e("QUES_NUMBERFRAGMENT",">>>>>>>>>>.." + QuestionNumbers);

        ans.setText(getArgument1);
        notAns.setText(getArgument2);
        notVisit.setText(getArgument3);
        review.setText(getArgument4);

        database();

        final String gridNotAns[] = new String[Not_Answered.size()];
        final String gridMarked[] = new String[Mark_for_Review.size()];
        final String gridNotvist[] = new String[Not_Visited.size()];

        for(int n = 0; n< QuestionNumbers.size() ; n++) {
            gridViewNumbers[n] = QuestionNumbers.get(n);
            Log.e("gridViewNumbers[n]" ,">>>>>>>>" + gridViewNumbers[n]);

            for (int a = 0; a < Not_Answered.size(); a++) {
                gridNotAns[a] = Not_Answered.get(a);

                for(int m = 0 ; m < Mark_for_Review.size(); m++) {
                    gridMarked[m] = Mark_for_Review.get(m);

                    for(int v = 0; v < Not_Visited.size(); v++) {
                        gridNotvist[v] = Not_Visited.get(v);
                    }

                }
            }

            // Create an object of CustomAdapter and set Adapter to GirdView
            ImageAdapter imageAdapter = new ImageAdapter(getContext(), gridViewNumbers, gridNotAns,gridMarked,gridNotvist);
            gridView.setAdapter(imageAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    ((Test_Main)getActivity()).setDatafromFragment(gridViewNumbers[position]);

                }
            });
        }

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        return RootView;
    }

    private void database() {
        mydb = new DBHelper(getContext());
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

                Not_Answered.add(notans);
                Mark_for_Review.add(m_review);
                Not_Visited.add(notvisit);

                Log.e("NOTANS",">>>>>>>>>>>>" + Not_Answered);
                Log.e("MARKED",">>>>>>>>>>>>" + Mark_for_Review);
                Log.e("Not_Visited",">>>>>>>>>>>>" + Not_Visited);

                Log.e("DB_FRAGMENT", ">>>>>>>>>>" +  DbQnum + " " + "" + Dbq_id+ " " +  DbSelectedOpt +
                        " " + m_review +" " +notans+" " +notvisit + " " + qns + " " + opa + " " + opb + " " + opc + " " + opd +
                        " " + hqns + " " + hopa + " " + hopb + " " + hopc + " " + hopd);
            }
        }else{

            Log.e("NULLDB",">>>>>>>>>>>>>>>>" + DbQnum);
        }
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


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public void dialogBoxForInternet() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
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
                                getActivity().finish();
                            }
                        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        // Fetch arguments from bundle and set title
        getActivity().setTitle("");

    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);

       // window.setLayout((int) (size.x * 0.90),(int) (size.y * 0.80));

        window.setGravity(Gravity.RIGHT);
        // Call super onResume after sizing
        super.onResume();
    }
}

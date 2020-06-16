package com.lscarp4.lscarpl4assessments.StudentFiles;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;

/**
 * AsyncTask for Upload File using FTP
 */
public class UploadFileToFTPServerAsyncTask extends AsyncTask<String, String, String> {
    private final TaskUploadCompleted mCallback;
    private Context context;
    //    private ProgressDialog pDialog;
    private String fileName, filePath, directoryName;
    private boolean uploadStatusFlag = false;
    private String ftpHost,ftpHostUserName,ftpHostPassword;

    public UploadFileToFTPServerAsyncTask(Context context, TaskUploadCompleted mCallback, String fileName,
                                          String filePath, String directoryName, String ftpHost, String ftpHostUserName, String ftpHostPassword) {
        super();
        this.context = context;
        this.mCallback = mCallback;
        this.fileName = fileName;
        this.filePath = filePath;
        this.directoryName = directoryName;
        this.ftpHost = ftpHost;
        this.ftpHostUserName = ftpHostUserName;
        this.ftpHostPassword = ftpHostPassword;

        Log.e("Asyncconstructor","ftpHost" + ftpHost);
        Log.e("Asyncconstructor","ftpHostUserName" + ftpHostUserName);
        Log.e("Asyncconstructor","ftpHostPassword" + ftpHostPassword);
        Log.e("Asyncconstructor","directoryName" + directoryName);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        System.out.println("UploadFileToFTPServerAsyncTask onPreExecute");
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            boolean ftpStatus1 = false;
            MyFTPClientFunctions ftpClientFunctions = new MyFTPClientFunctions();
            //  final String host = "",username = "",password = "";
          //  ftpStatus1 = ftpClientFunctions.ftpConnect(prefManager.getLOGIN_FTP_HOST(), prefManager.getLOGIN_FTP_USERNAME(), prefManager.getLOGIN_FTP_PASSWORD(), 21);
            ftpStatus1 = ftpClientFunctions.ftpConnect(ftpHost,ftpHostUserName, ftpHostPassword, 21);
            System.out.println("UploadFileToFTPServerAsyncTask doInBackground  ftpStatus1 " +ftpStatus1);

            Log.e("PASSING VALUES", "????" + ftpHost + " " + ftpHostUserName + "  " + ftpHostPassword);

            Log.e("ASYCK CLASS", ">FTP STATUS" + "FALSE VALUE");

            if (ftpStatus1) {
                uploadStatusFlag = ftpClientFunctions.ftpUpload(filePath,
                     fileName, directoryName, context);
                System.out.println("UploadFileToFTPServerAsyncTask doInBackground  uploadStatusFlag " +uploadStatusFlag);

                Log.e("FILE PATH", " ??????" + filePath);
                Log.e("FILE Name", " ??????" + fileName);
                Log.e("Directory name", " ??????" + directoryName);

                Log.e("ASYNC", "FTP STATUS TRUE" + " UPLOAD FLAG STATUS FALSE");

                if (uploadStatusFlag) {
                    boolean disConnectStatus3 = false;
                    disConnectStatus3 = ftpClientFunctions.ftpDisconnect();

                    Log.e("UPLOAD FLAG STATS", "TRUE + DISCONNECT " + " FFALSE");

                    if (disConnectStatus3) {

                        Log.e("AYNSCL", "DISCONN TRUE"+ "TRUE VALUE");

                        System.out.println("UploadFileToFTPServerAsyncTask constructor  disConnectStatus3 "+disConnectStatus3);
                   //     deleteRecursive(new File(filePath));


                    }
                } else {

                    Log.e("UPLOAD STATUS VAL", "IT IS TRUE" + " ");
                }
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        //    pDialog.dismiss();
        if (uploadStatusFlag) {
            //  Toast.makeText(context, "Uploaded Successfully!",
            //        Toast.LENGTH_LONG).show();
            //This is where you return data back to caller
            mCallback.onTaskComplete(result, "",uploadStatusFlag,fileName);

            Log.e("ON POST METHOD", ">>>> CALLING ONTASK COMPLETE METHOD"+"");
        }

        Log.e("ON POST METHOD", ">>>> OUT OF METHOD"+"");
    }

    private  boolean deleteRecursive(File fileOrDirectory)
    {
        if (fileOrDirectory.isDirectory())
        {
            for (File child : fileOrDirectory.listFiles())
            {
                System.out.println("UploadFileToFTPServerAsyncTask deleteRecursive  child "+child);
                Log.e("DELETE METHOD", "FILE DELETION"+ "");

                deleteRecursive(child);
            }
        }

        boolean flag= fileOrDirectory.delete();
        System.out.println("UploadFileToFTPServerAsyncTask deleteRecursive  flag "+flag);
        return  flag;
    }
}

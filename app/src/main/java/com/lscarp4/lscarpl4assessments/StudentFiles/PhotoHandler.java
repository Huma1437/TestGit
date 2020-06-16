package com.lscarp4.lscarpl4assessments.StudentFiles;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoHandler implements Camera.PictureCallback ,TaskUploadCompleted {

    private final Context context;
    String std_id;
    String filename;
    String get_tbID, base64;
    private static final String API_KEY = "YIA0Uub5QLhH9hxXRL1A6tXDwFWzbaUL";
    final static String UPLOAD_URL = "http://lscmis.com/arpl4/student/student_api/upload_snapshots_api";
    JSONObject reader;
    AlertDialog alertDialog = null;
    NetworkChangeReceiver br;
    double latitude,longitude;
    private String imageName;
    String ftphost,ftpuname,ftppwd;


    public PhotoHandler(Context context, String std_id,String tbID,double latitude,double longitude) {
        this.context = context;
        this.std_id = std_id;
        this.get_tbID = tbID;
        this.latitude =latitude;
        this.longitude = longitude;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        br = new NetworkChangeReceiver();

        File pictureFileDir = getDir();

        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

            Log.d(Test_Main.DEBUG_TAG, "Can't create directory to save image.");
            Toast.makeText(context, "Can't create directory to save image.",
                    Toast.LENGTH_LONG).show();
            return;

        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = dateFormat.format(new Date());
        imageName = std_id + "_" + date + ".jpg";

        filename = pictureFileDir.getPath() + File.separator + imageName;
        Log.e("FILENAME", ">>>>>" + filename + "      " + imageName);

        File pictureFile = new File(filename);

        Bitmap picture = BitmapFactory.decodeByteArray(data, 0, data.length);

        Log.e("BITMAP",">>>>>>>>>>>>" +picture);


        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            Toast.makeText(context, "New Image saved:" + imageName,
                    Toast.LENGTH_LONG).show();


            Bitmap bm1 = BitmapFactory.decodeFile(filename);
            bm1=mark(bm1, String.valueOf(latitude) +  " , " + String.valueOf(longitude)); //watermark

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bm1.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bm1, "Title", null);
            upload(Uri.parse(path));

        } catch (Exception error) {
            Log.d(Test_Main.DEBUG_TAG, "File" + filename + "not saved: "
                    + error.getMessage());
            Toast.makeText(context, "Image could not be saved.",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void upload(Uri resultUri) {

        ftphost = Splash_Screen.sh.getString("ftp_snapshot_host",null);
        ftpuname = Splash_Screen.sh.getString("ftp_snapshot_username",null);
        ftppwd = Splash_Screen.sh.getString("ftp_snapshot_password",null);

        Log.e("FTP VALUES", "??????????/" + ftphost + " " + ftpuname + " " + ftppwd);

        try {

            Log.e("PASS TO ASYNCK", "????????????" + imageName + "   " + filename + "    " + ftphost + "  " + ftpuname + " " + ftppwd);
            new UploadFileToFTPServerAsyncTask(context, this, imageName, filename, "/",
                    ftphost,ftpuname,ftppwd).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private File getDir() {
        File lscarpl4 = new File(Environment.getExternalStorageDirectory().getPath() + "/.lscarpl4/"+std_id);
        lscarpl4.mkdirs();

        return new File(lscarpl4, "My_images");

    }



    public static Bitmap mark(Bitmap src, String watermark) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(15);
        paint.setAntiAlias(true);
        canvas.drawText(watermark, 15, 25, paint);

        return result;
    }



    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
    public void onTaskComplete(String result, String apiRequestName, boolean statusFlag, String fileName) {

        Log.e("PHOTO HANDLER", "ONTASK COMPLETE" + imageName + statusFlag);
        if (statusFlag) {

            Log.e("INSIDE STATUS FLAG", "IF TRUE" + "CALLING ASYNCK TASK");
            new UploadImagePostRequest().execute();
        }
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

            }
        }
    }


    public class UploadImagePostRequest extends AsyncTask<Void, String, JSONObject> {

        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);


            try {
                if (jsonObject != null && jsonObject.getString("status_message") != null) {

                    String message = jsonObject.getString("status_message");  // Message

                    reader = jsonObject;
                    Log.e("MESSAGE_UPLOAD_IMG", ">>>>>>>>>>>>>>>>" + message);

                    if (message.equals("Success, Snapshot uploaded")) {

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    } else if (message.equals("Access Denied ! Authentication Failed")) {

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    }else if (message.equals("Failure, Unable to upload Snapshot photo")) {

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    }else if (message.equals("Invalid POST Parameters")) {

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    }
                }else{


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        protected JSONObject doInBackground(Void... params) {

            return postJsonObject2(UPLOAD_URL, makingJson2());

        }
    }



    private JSONObject makingJson2() {

        JSONObject postDataParams = new JSONObject();

        try {
            //following parameters to the API

            postDataParams.put("key", API_KEY);
            postDataParams.put("batch_id", get_tbID);
            postDataParams.put("image_name", imageName);

            Log.e("UPLOAD IMAGE ONLINE", ">>>>>>>>" + API_KEY + " " + get_tbID +   " "+ imageName + " ");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postDataParams;
    }



    public JSONObject postJsonObject2(String url, JSONObject loginJobj1) {
        InputStream inputStream2 = null;
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
            inputStream2 = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream2 != null) {
                result = convertInputStreamToString2(inputStream2);
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

    private String convertInputStreamToString2(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }


}

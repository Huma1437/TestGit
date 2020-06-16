package com.lscarp4.lscarpl4assessments;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;


public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();

    /*Constructor
    Handles a given request and generates an appropriate response.*/
    public HttpHandler() {
    }

    //makeServiceCall() makes http call to particular url and fetches the response
    public String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(false);
            URL secondURL = new URL(conn.getHeaderField("Location"));
            URLConnection conn1 = secondURL.openConnection();
            conn.setRequestMethod("GET");

            // read the response
            InputStream in = new BufferedInputStream(conn1.getInputStream());
            response = convertStreamToString(in);

            int code = conn.getResponseCode();

            if (code==200)
            {
                System.out.println("OK");
            }



        } catch (MalformedURLException e) {

        } catch (ProtocolException e) {

        } catch (IOException e) {

        } catch (Exception e) {

        }

        return response;

    }

    ////To convert Input Stream to String
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}

package com.mahyco.cmr_app.leegality;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utility {
    private  static HttpClient mHhttpclient = new DefaultHttpClient();
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static String POSTJSON(String url, String obj, String accesstoken){
        Log.i("JSONPOSTBEGIN", "Beginning of JSON POST");
        Log.d("JsonObject",obj.toString());
        InputStream inputStream = null;
        String result = "";


        try{

            HttpPost post = new HttpPost("https://sandbox.leegality.com/api/v3.0/sign/request");
            post.setHeader("Content-type", "application/json");
            post.setHeader("Accept", "application/json");
            post.setHeader("X-Auth-Token", "qNFl00egodH9JV3T8jvwsEvAJ17miwEi");


            byte[] vart= obj.toString().trim().getBytes("UTF-8");
            String utf=convertStringToUTF8(obj.toString().trim());
            //  String normal=convertUTF8ToString(utf);
            // String  out=vart.toString()
            StringEntity se = new StringEntity(utf);
            post.setEntity(se);

            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            int timeoutConnection = 3000;
            //  HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 20000;
            // HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            mHhttpclient = new DefaultHttpClient(httpParameters);

            HttpResponse httpResponse = mHhttpclient.execute(post);
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if(inputStream != null){
                result = convertInputStreamToString(inputStream);
            }
            else
                result = "Did not work!";
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        Log.i("JSONPOSTEND", "End of JSON data post methos...");

       /* Log.d("Leegality","1 <<<======================================== Leegality START RESULT============================================>>>");
        Log.d("Leegality","1 RESULT::"+result);
        Log.d("Leegality","1 <<<======================================== Leegality END RESULT============================================>>>");*/

        return result;
    }
    // convert internal Java String format to UTF-8
    public static String convertStringToUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
        } catch (Exception e) {
            return null;
        }
        return out;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(inputStream))) {

            String line;
            StringBuilder response = new StringBuilder();

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            //print result
            System.out.println(response.toString());

            return  response.toString();
        }
    }

}
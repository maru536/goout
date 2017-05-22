package com.iotaddon.goout;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by maru5 on 2017-05-09.
 */

public class ServerProtocol {
    private static final String serverAddress = "http://13.124.126.90:8080";
    private static String mResponse;

    public static JSONObject post(String url, JSONObject body) {
        JSONObject response = new JSONObject();

        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, body.toString());
            Request request = new Request.Builder()
                    .url(serverAddress+url)
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "9f321e22-0c77-3e92-e315-493293174615")
                    .build();

            Log.d("KimDC", serverAddress+url);

            //Response result = client.newCall(request).execute();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                    else {
                        mResponse = response.toString();
                    }
                }
            });

            response = new JSONObject();
        }
        finally {

        }

        return response;
    }

    public static JSONObject get(String url) {
        JSONObject response = new JSONObject();

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(serverAddress+url)
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "4d9324a7-0db1-b170-3852-b15b7ea6910a")
                    .build();

            Log.d("KimDC", serverAddress+url);


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                    else {
                        mResponse = response.toString();
                    }
                }
            });

            response = new JSONObject();
        }
        finally {


        }

        return response;
    }

    /*
    public static JSONObject post(String StrUrl, JSONObject body) {
        try {
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setDoOutput(true);

            // HTTP request header
            con.setRequestProperty("Content-Type", "application/json");
            //con.setRequestProperty("Authorization", "key=AAAATq7NFIE:APA91bH4duimExAt8-D41AHMrd4I3amepEjmSpF2RI5-u8UfLp1FH4k959-2Sl5i1Jl_HQwmSGfx76XR-BygTjkSj7iwLwbBmL8bgRLgYAnZB2VOXr9Hhn335-96g3y8jXkCLGxms2lm");
            con.setRequestMethod("POST");
            con.connect();

            OutputStream os = con.getOutputStream();
            os.write(body.toString().getBytes("UTF-8"));
            os.close();

            // Read the response into a string
            InputStream is = con.getInputStream();
            String responseString = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
            is.close();

            // Parse the JSON string and return the notification key
            JSONObject response = new JSONObject(responseString);
            Log.e("KimDC", response.toString());
            return response;
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        catch (JSONException je) {
            je.printStackTrace();
        }
        finally {
            return new JSONObject();
        }
    }

    public static JSONObject get(String strUrl, JSONObject header) {
        try {
            // Get HTTPS URL connection
            URL url = new URL(strUrl);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();

            // Set Hostname verification
            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    // Ignore host name verification. It always returns true.
                    return true;
                }

            });

            // SSL setting
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, null, null);  // No validation for now
            conn.setSSLSocketFactory(context.getSocketFactory());

            // Connect to host
            conn.connect();
            conn.setInstanceFollowRedirects(true);

            // Print response from host
            InputStream is = conn.getInputStream();
            //BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String responseString = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.printf("%s\n", line);
            }

            //reader.close();
            is.close();

            return new JSONObject(responseString);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (JSONException jsone) {
            jsone.printStackTrace();
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        } catch (KeyManagementException kme) {
            kme.printStackTrace();
        }
        finally {
            return new JSONObject();
        }
    }*/
}

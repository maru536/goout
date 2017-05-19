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

/**
 * Created by maru5 on 2017-05-09.
 */

public class ServerProtocol {
    private static final String serverAddr = "http://ourserver";

    public static JSONObject post(String StrUrl, JSONObject body) {
        try {
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setDoOutput(true);

            // HTTP request header
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "key=AAAATq7NFIE:APA91bH4duimExAt8-D41AHMrd4I3amepEjmSpF2RI5-u8UfLp1FH4k959-2Sl5i1Jl_HQwmSGfx76XR-BygTjkSj7iwLwbBmL8bgRLgYAnZB2VOXr9Hhn335-96g3y8jXkCLGxms2lm");
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
            /*String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.printf("%s\n", line);
            }*/

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
    }
}

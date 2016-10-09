package com.bit_prince.otp.bitprince.tasks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.bit_prince.otp.bitprince.activities.MainActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;

/**
 * Created by Laotsezu on 08/15/2016.
 */
public class LogoutTask extends AsyncTask<String,Void,Boolean> {
    String TAG = "Logout Task : ";
    String url  ="http://member.bit-prince.com/api/removedevice";
    String userName = "auth@bit-prince.com";
    String passWord = "accessapi@1";
    OnLogoutTask listener;
    public interface OnLogoutTask{
        void onLogoutComplete();
        void onLogoutErr();
    }
    public LogoutTask(OnLogoutTask listener){
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        String srv_dv = strings[0];
        String device_token = strings[1];

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        post.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(userName,passWord),"UTF-8",false));

        List<NameValuePair> list = new Vector<>();
        list.add(new BasicNameValuePair("srv_dv",srv_dv));
        list.add(new BasicNameValuePair("device_token",device_token));

        Log.e(TAG,"Start Log Out Task Exe cute");

        try {
            post.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse response = httpClient.execute(post);

            if(response.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                InputStreamReader isReader = new InputStreamReader(is);

                BufferedReader reader = new BufferedReader(isReader);

                String line;
                line = reader.readLine();
                Log.e(TAG,"Line == " + line);

                JSONObject resultObject = new JSONObject(line).getJSONObject("response");
                boolean status = resultObject.getBoolean("status");

                Log.e(TAG,"Status = " + status);

                return status;
            }
            else{
                Log.e(TAG,"Log out Execute Failure!");
            }

        } catch (java.io.IOException | JSONException e) {
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }
        return false;
    }
    @Override
    protected void onPostExecute(Boolean b) {
        super.onPostExecute(b);
        if(b)
            listener.onLogoutComplete();
        else
            listener.onLogoutErr();
    }
}

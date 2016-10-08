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
 * Created by laotsezu on 28/07/2016.
 */
public class GetOptCodeTask extends AsyncTask<String,Void,MainActivity.OtpCode> {
    String TAG = "Get Opt AsyncTask: ";
    String url  ="http://member.bit-prince.com/api/requestotp";
    String userName = "auth@bit-prince.com";
    String passWord = "accessapi@1";
    OnGetOptAsyncTaskListener listener;
    public interface OnGetOptAsyncTaskListener{
        void onGetOptComplete(MainActivity.OtpCode otpCode);
        void onGetOptErr(String err);
    }
    public GetOptCodeTask(OnGetOptAsyncTaskListener listener){
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected MainActivity.OtpCode doInBackground(String... strings) {

        String srv_dv = strings[0];
        String device_token = strings[1];

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        post.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(userName,passWord),"UTF-8",false));

        List<NameValuePair> list = new Vector<>();
        list.add(new BasicNameValuePair("srv_dv",srv_dv));
        list.add(new BasicNameValuePair("device_token",device_token));

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

                Bundle bundle = new Bundle();
                if(status){

                    String otp_code = resultObject.getString("otp");

                    Long start_string_time = Long.valueOf(resultObject.getString("start_time"));
                    Long end_string_time = Long.valueOf(resultObject.getString("end_time"));

                    Long expire_time = Math.abs(start_string_time - end_string_time);

                    Log.e(TAG,"Unix Time == " + System.currentTimeMillis() / 1000);

                    return new MainActivity.OtpCode(otp_code,System.currentTimeMillis()/ 1000,expire_time);
                }
                else{
                    ///
                    Log.e(TAG,"Status ==  false!!");
                    bundle.putBoolean("status",false);
                    bundle.putString("message",resultObject.getString("message"));

                }
                return null;
            }
            else{
                Log.e(TAG,"Get Opt Execute Failure!");
            }

        } catch (java.io.IOException | JSONException e) {
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
            return null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(MainActivity.OtpCode s) {
        super.onPostExecute(s);
        if(s != null){
            listener.onGetOptComplete(s);
        }
        else{
            listener.onGetOptErr("Loi Loi Loi");
        }
    }
}

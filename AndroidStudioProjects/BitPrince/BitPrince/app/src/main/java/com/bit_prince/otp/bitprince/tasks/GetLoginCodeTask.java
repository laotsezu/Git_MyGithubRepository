package com.bit_prince.otp.bitprince.tasks;

import android.os.AsyncTask;
import android.util.Log;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;

/**
 * Created by laotsezu on 28/07/2016.
 */
public class GetLoginCodeTask extends AsyncTask<String,String,String[]> {
    static String TAG = "Get Temp Code Task : ";
    String url = "http://member.bit-prince.com/api/adddevice";
    String userName = "auth@bit-prince.com";
    String passWord = "accessapi@1";
    OnGetTempCodeTaskListener listener;
    public interface OnGetTempCodeTaskListener{
        void onGetTempCodeTaskComplete(String[] result);
        void onGetTempCodeTaskErr(String err);
    }
    public GetLoginCodeTask(OnGetTempCodeTaskListener listener){
        this.listener = listener;
    }
    @Override
    protected String[] doInBackground(String... strings) {

        String deviceToken = strings[0];
        String deviceName = strings[1];
        String qrCode = strings[2];

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        post.addHeader(BasicScheme.authenticate(
                new UsernamePasswordCredentials(userName,passWord),
                "UTF-8", false));

        List<NameValuePair> listParams = new Vector<>();
        listParams.add(new BasicNameValuePair("device_token",deviceToken));
        listParams.add(new BasicNameValuePair("device_name",deviceName));
        listParams.add(new BasicNameValuePair("device_qr",qrCode));

        try {
            post.setEntity(new UrlEncodedFormEntity(listParams));
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == 200){

                HttpEntity httpEntity = response.getEntity();
                InputStream is = httpEntity.getContent();

                InputStreamReader isReader = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isReader);

                String responseLine = reader.readLine();
                Log.e(TAG,"Reponse Line = " + responseLine);

                JSONObject responseObject = new JSONObject(responseLine).getJSONObject("response");

                boolean status = responseObject.getBoolean("status");
                if(status){
                    Log.e(TAG,"TEmp Code = " + responseObject.getString("code"));
                    return new String[]{responseObject.getString("code"),responseObject.getString("username")};
                }
                else{
                    Log.e(TAG,"Get Temp Code Failure! Because " + responseObject.getString("message"));
                    publishProgress("Get Temp Code Failure! Because " + responseObject.getString("message"));
                    return null;
                }
            }
            else{
                Log.e(TAG,"ExeCute HttpPost Failure!");
                publishProgress("ExeCute HttpPost Failure! Because: " + response.getStatusLine().getReasonPhrase());
                return null;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e(TAG,"To This! Because: " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        listener.onGetTempCodeTaskErr(values[0]);
    }

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);
        if(strings != null) {
            listener.onGetTempCodeTaskComplete(strings);
        }
        else{
            Log.e(TAG,"Temp Code = Null");
        }
    }
}

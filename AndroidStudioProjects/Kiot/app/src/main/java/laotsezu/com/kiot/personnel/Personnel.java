package laotsezu.com.kiot.personnel;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import laotsezu.com.kiot.resources.MyUtility;

/**
 * Created by laotsezu on 17/09/2016.
 */
public class Personnel extends BaseObservable{
    static String TAG = "Personnel: ";
    String personnel_id;
    String personnel_ten;
    String personnel_loai;
    String personnel_sdt;
    String personnel_dia_chi;
    long personnel_tong_tien_ban;
    String personnel_tai_khoan;
    String personnel_mat_khau;
    int personnel_status;
    String personnel_ten_info;
    String personnel_sdt_info;
    public interface OnLoginListener{
        void onLoginSucessfull(Personnel personnel);
        void onLoginFailed(String message);
        void onLoginStart();
        Context getContext();
    }
    public interface OnGetInfoListener{
        void onGetInfoStart();
        void onGetInfoFailed(String message);
        void onGetInfoSuccessful(JSONObject personnel_json);
    }

    public Personnel(String personnel_id, String personnel_ten, String personnel_loai, String personnel_sdt, String personnel_dia_chi, long personnel_tong_tien_ban, String personnel_tai_khoan, String personnel_mat_khau, int personnel_status) {
        this.personnel_id = personnel_id;
        this.personnel_ten = personnel_ten;
        this.personnel_loai = personnel_loai;
        this.personnel_sdt = personnel_sdt;
        this.personnel_dia_chi = personnel_dia_chi;
        this.personnel_tong_tien_ban = personnel_tong_tien_ban;
        this.personnel_tai_khoan = personnel_tai_khoan;
        this.personnel_mat_khau = personnel_mat_khau;
        this.personnel_status = personnel_status;
    }

    public Personnel(JSONObject input){
        try {
            this.personnel_id = input.getString("personnel_id");
            this.personnel_ten = input.getString("personnel_ten");
            this.personnel_loai = input.getString("personnel_loai");
            this.personnel_sdt = input.getString("personnel_sdt");
            this.personnel_dia_chi = input.getString("personnel_dia_chi");
            this.personnel_tong_tien_ban = input.getLong("personnel_tong_tien_ban");
            this.personnel_tai_khoan = input.getString("personnel_tai_khoan");
            this.personnel_mat_khau = input.getString("personnel_mat_khau");
            this.personnel_status = input.getInt("personnel_status");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Personnel(Bundle input){
        this.personnel_id = input.getString("personnel_id");
        this.personnel_ten = input.getString("personnel_ten");
        this.personnel_loai = input.getString("personnel_loai");
        this.personnel_sdt = input.getString("personnel_sdt");
        this.personnel_dia_chi = input.getString("personnel_dia_chi");
        this.personnel_tong_tien_ban = input.getLong("personnel_tong_tien_ban",0);
        this.personnel_tai_khoan = input.getString("personnel_tai_khoan");
        this.personnel_mat_khau = input.getString("personnel_mat_khau");
        this.personnel_status = input.getInt("personnel_status",1);

    }
    public Bundle getBundle(){
        Bundle data = new Bundle();
        data.putString("personnel_id",getPersonnel_id());
        data.putString("personnel_ten",getPersonnel_ten());
        data.putString("personnel_loai",getPersonnel_loai());
        data.putString("personnel_sdt",getPersonnel_sdt());
        data.putString("personnel_dia_chi",getPersonnel_dia_chi());
        data.putLong("personnel_tong_tien_ban",getPersonnel_tong_tien_ban());
        data.putString("personnel_tai_khoan",getPersonnel_tai_khoan());
        data.putString("personnel_mat_khau",getPersonnel_mat_khau());
        data.putInt("personnel_status",getPersonnel_status());
        return data;
    }
    public static void getInfo(String taiKhoan,String matKhau,OnGetInfoListener listener){
        GetInfoAsyncTask task = new GetInfoAsyncTask(listener);
        task.execute(taiKhoan,matKhau);
    }
    private static class GetInfoAsyncTask extends AsyncTask<String,Void,JSONObject>{
        OnGetInfoListener listener;
        static String url = "http://kiot.igarden.vn/personnel/laythongtin";
        public GetInfoAsyncTask(OnGetInfoListener listener){
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onGetInfoStart();
        }

        @Override
        protected JSONObject doInBackground(String... infos) {
            String tai_khoan = infos[0];
            String mat_khau = infos[1];

            return getJsonInfo(tai_khoan,mat_khau,url);
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            super.onPostExecute(response);

            if(response == null){
                listener.onGetInfoFailed("Response from DoinBackground == Null");
            }
            else {
                try {
                    if(!response.getBoolean("status")){
                        listener.onGetInfoFailed(response.getString("message"));
                    }
                    else{
                        listener.onGetInfoSuccessful(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onGetInfoFailed("xem lai Personnel Class");
                    Log.e(TAG,"Response Of Get Info Asyntask == Null");
                }
            }
        }
    }
    public static void login(String tai_khoan,String mat_khau,OnLoginListener listener){
        SharedPreferences sharedPreferences = listener.getContext().getSharedPreferences(MyUtility.SHAREPREFERENCE_ID,Context.MODE_PRIVATE);

        sharedPreferences.edit().putString("personnel_tai_khoan",tai_khoan).apply();
        sharedPreferences.edit().putString("personnel_mat_khau",mat_khau).apply();

        LoginAsyncTask task = new LoginAsyncTask(listener);
        task.execute(tai_khoan,mat_khau);
    }
    private static class LoginAsyncTask extends AsyncTask<String,Void,JSONObject>{
        OnLoginListener listener;
        static String url = "http://kiot.igarden.vn/personnel/dangnhap";
        public LoginAsyncTask(OnLoginListener listener){
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onLoginStart();
        }

        @Override
        protected JSONObject doInBackground(String... personnel_info) {
            String tai_khoan = personnel_info[0];
            String mat_khau = personnel_info[1];

            return getJsonInfo(tai_khoan,mat_khau,url);
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            super.onPostExecute(response);
            if(response == null){
                listener.onLoginFailed("Response from DoinBackground == Null");
            }
            else {
                try {
                    if(!response.getBoolean("status")){
                        listener.onLoginFailed(response.getString("message"));
                    }
                    else{
                        Personnel personnel = new Personnel(response);
                        saveLoginData(listener.getContext(),personnel);
                        listener.onLoginSucessfull(new Personnel(response));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onLoginFailed("xem lai Personnel Class");
                    Log.e(TAG,"Response Of Login Asyntask == Null");
                }
            }
        }
    }
    private  static JSONObject getJsonInfo(String tai_khoan,String mat_khau,String url){
        JSONObject user_info = new JSONObject();
        try {
            user_info.put("status", false);
            if (tai_khoan.isEmpty() || mat_khau.isEmpty()) {
                user_info.put("message","Username or password missing!");
            }
            else{
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                List<NameValuePair> params = new LinkedList<>();

                NameValuePair username = new BasicNameValuePair("personnel_tai_khoan",tai_khoan);
                NameValuePair password = new BasicNameValuePair("personnel_mat_khau",mat_khau);

                params.add(username);
                params.add(password);

                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);

                if(httpResponse.getStatusLine().getStatusCode() == 200){
                    InputStream is = httpResponse.getEntity().getContent();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);

                    JSONObject response = new JSONObject(br.readLine());
                    if(response.getBoolean("status")){
                        return response;
                    }
                    else{
                        user_info.put("message",response.getString("message"));
                    }
                }
                else{
                    user_info.put("message","Status response Http != 200");
                }
            }
        }
        catch (Exception e){
            return null;
        }

        return user_info;
    }
    @Bindable
    public String getPersonnel_id() {
        return personnel_id;
    }
    @Bindable
    public String getPersonnel_ten() {
        return personnel_ten;
    }
    @Bindable
    public String getPersonnel_loai() {
        return personnel_loai;
    }
    @Bindable
    public String getPersonnel_sdt() {
        return personnel_sdt;
    }
    @Bindable
    public String getPersonnel_dia_chi() {
        return personnel_dia_chi;
    }
    @Bindable
    public long getPersonnel_tong_tien_ban() {
        return personnel_tong_tien_ban;
    }
    @Bindable
    public String getPersonnel_tai_khoan() {
        return personnel_tai_khoan;
    }
    @Bindable
    public String getPersonnel_mat_khau() {
        return personnel_mat_khau;
    }
    @Bindable
    public int getPersonnel_status() {
        return personnel_status;
    }
    @Bindable
    public String getPersonnel_ten_info(){
        return personnel_ten == null ? "Chưa cập nhật" : personnel_ten;
    }
    @Bindable
    public String getPersonnel_sdt_info(){
        return personnel_sdt == null ? "Chưa cập nhật" : personnel_sdt;
    }

    public void setPersonnel_sdt_info(String personnel_sdt_info) {
        this.personnel_sdt_info = personnel_sdt_info;
    }

    public void setPersonnel_ten(String personnel_ten) {
        this.personnel_ten = personnel_ten;
    }

    public void setPersonnel_loai(String personnel_loai) {
        this.personnel_loai = personnel_loai;
    }

    public void setPersonnel_sdt(String personnel_sdt) {
        this.personnel_sdt = personnel_sdt;
    }

    public void setPersonnel_dia_chi(String personnel_dia_chi) {
        this.personnel_dia_chi = personnel_dia_chi;
    }

    public void setPersonnel_tong_tien_ban(long personnel_tong_tien_ban) {
        this.personnel_tong_tien_ban = personnel_tong_tien_ban;
    }

    public void setPersonnel_tai_khoan(String personnel_tai_khoan) {
        this.personnel_tai_khoan = personnel_tai_khoan;
    }

    public void setPersonnel_mat_khau(String personnel_mat_khau) {
        this.personnel_mat_khau = personnel_mat_khau;
    }

    public void setPersonnel_status(int personnel_status) {
        this.personnel_status = personnel_status;
    }

    public void setPersonnel_ten_info(String personnel_ten_info) {
        this.personnel_ten_info = personnel_ten_info;
    }
    public static boolean isLogin(Context context){
        return context.getSharedPreferences(MyUtility.SHAREPREFERENCE_ID,Context.MODE_PRIVATE).getBoolean("isLogin",false);
    }
    public static void saveLoginData(Context context,Personnel personnel){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyUtility.SHAREPREFERENCE_ID,Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("isLogin",true).apply();
        sharedPreferences.edit().putString("personnel_id",personnel.getPersonnel_id()).apply();
        sharedPreferences.edit().putString("personnel_ten",personnel.getPersonnel_id()).apply();
        sharedPreferences.edit().putString("personnel_loai",personnel.getPersonnel_id()).apply();
        sharedPreferences.edit().putString("personnel_sdt",personnel.getPersonnel_id()).apply();
        sharedPreferences.edit().putString("personnel_dia_chi",personnel.getPersonnel_id()).apply();
        sharedPreferences.edit().putString("personnel_tong_tien_ban",personnel.getPersonnel_id()).apply();
        sharedPreferences.edit().putString("personnel_status",personnel.getPersonnel_id()).apply();
    }
    public static Personnel getLoginData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyUtility.SHAREPREFERENCE_ID,Context.MODE_PRIVATE);
        return new Personnel(
                sharedPreferences.getString("personnel_id",null)
                ,sharedPreferences.getString("personnel_ten",null)
                ,sharedPreferences.getString("personnel_loai",null)
                ,sharedPreferences.getString("personnel_sdt",null)
                ,sharedPreferences.getString("personnel_dia_chi",null)
                ,Long.parseLong(sharedPreferences.getString("personnel_tong_tien_ban","0"))
                ,sharedPreferences.getString("personnel_tai_khoan",null)
                ,sharedPreferences.getString("personnel_mat_khau",null)
                ,Integer.parseInt(sharedPreferences.getString("personnel_status","0"))
        );
    }
    public static void clearLoginData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyUtility.SHAREPREFERENCE_ID,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("isLogin").apply();
        editor.remove("personnel_tai_khoan").apply();
        editor.remove("personnel_mat_khau").apply();
        editor.remove("personnel_id").apply();
        editor.remove("personnel_ten").apply();
        editor.remove("personnel_loai").apply();
        editor.remove("personnel_sdt").apply();
        editor.remove("personnel_dia_chi").apply();
        editor.remove("personnel_tong_tien_ban").apply();
    }
}

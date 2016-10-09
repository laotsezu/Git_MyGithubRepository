package laotsezu.com.kiot.partner;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by laotsezu on 17/09/2016.
 */
public class Customer extends BaseObservable{
    public static String TAG = "Customer: ";
    String customer_id;
    String customer_ten;
    String customer_sdt;
    String customer_nhom;
    String customer_gioitinh;
    String customer_ngay_sinh;
    String customer_email;
    String customer_dia_chi;
    long customer_no_nan;
    long customer_tong_tien_mua;
    String customer_ma_so_thue;
    int customer_status;
    boolean isDefault = false;
    public interface OnAddCustomerListener{
        void onAddCustomerSuccessful();
        void onAddCustomerFailed(String message);
        void onAddCustomerStart();
    }
    public interface OnLoadCustomerListener{
        void onLoadCustomerStart();
        void onLoadCustomerFailed(String message);
        void onLoadCustomerSuccessful(JSONArray items);
    }
    public Customer(JSONObject customer_input){
        try{
            this.customer_id = customer_input.getString("customer_id");
            this.customer_ten = customer_input.getString("customer_ten");
            this.customer_sdt = customer_input.getString("customer_sdt");
            this.customer_nhom = customer_input.getString("customer_nhom");
            this.customer_gioitinh = customer_input.getString("customer_gioitinh");
            this.customer_ngay_sinh = customer_input.getString("customer_ngay_sinh");
            this.customer_email = customer_input.getString("customer_email");
            this.customer_dia_chi = customer_input.getString("customer_dia_chi");
            this.customer_no_nan = customer_input.getLong("customer_no_nan");
            this.customer_tong_tien_mua = customer_input.getLong("customer_tong_tien_mua");
            this.customer_ma_so_thue = customer_input.getString("customer_ma_so_thue");
            this.customer_status = customer_input.getInt("customer_status");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private Customer(){
        this.customer_id = "0";
        this.customer_ten = "Khách Lẻ ";
        this.customer_sdt = "Mặc định ";
        this.customer_nhom = "Khách Lẻ";
        this.customer_gioitinh = "Nam";
        this.customer_ngay_sinh = "Mặc định ";
        this.customer_email = "Mặc định ";
        this.customer_dia_chi = "Mặc định ";
        this.customer_no_nan = 0;
        this.customer_tong_tien_mua = 0;

        this.customer_ma_so_thue = "Mặc định ";
        this.customer_status = 1;

        this.isDefault = true;
    }
    public static Customer getDefaultCustomer(){
        return new Customer();
    }
    public boolean isDefault(){
        return isDefault;
    }
    public static void addCustomer(Customer_Add_Info info , OnAddCustomerListener listener){
        CustomerAddAsyncTask task = new CustomerAddAsyncTask(listener);
        task.execute(info);
    }
    public static void loadCustomers(OnLoadCustomerListener listener){
        CustomerLoadAsyncTask task = new CustomerLoadAsyncTask(listener);
        task.execute();
    }
    static class CustomerLoadAsyncTask extends AsyncTask<Void,Void,JSONObject>{
        String url = "http://kiot.igarden.vn/partner/laykhachhang";
        OnLoadCustomerListener listener;
        public CustomerLoadAsyncTask(OnLoadCustomerListener listener){
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onLoadCustomerStart();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject result = new JSONObject();
            try{
                result.put("status",false);
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    JSONObject response = new JSONObject(new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent())).readLine());
                    if (response.getBoolean("status")) {
                        return response;
                    } else {
                        result.put(TAG, response.getString("message"));
                    }
                } else {
                    result.put(TAG, "Status Code Http Response != 200");
                }
            }
            catch (Exception e){
                Log.e(TAG,"Err: " + e.getMessage());
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            super.onPostExecute(response);
            if(response == null){
                listener.onLoadCustomerFailed("Load Failed, Input from DoinBackground == null");
            }
            else{
                try {
                    if(response.getBoolean("status")){
                        listener.onLoadCustomerSuccessful(response.getJSONArray("items"));

                    }
                    else{
                        listener.onLoadCustomerFailed(response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onLoadCustomerFailed(e.getMessage());
                }
            }
        }
    }
    static class CustomerAddAsyncTask extends AsyncTask<Customer_Add_Info,Void,JSONObject>{
        OnAddCustomerListener listener;
        String url = "http://kiot.igarden.vn/partner/themkhachhang";
        public CustomerAddAsyncTask(OnAddCustomerListener listener){
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onAddCustomerStart();
        }

        @Override
        protected JSONObject doInBackground(Customer_Add_Info... paramss) {
            Customer_Add_Info info = paramss[0];
            JSONObject result = new JSONObject();
            try{
                result.put("status",false);
                if(info != null){
                    if(!info.isAvailable()){
                        result.put("message","Input is unavailable or missing some fields!");
                    }
                    else {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(url);

                        List<NameValuePair> params = info.getParams();
                        httpPost.setEntity(new UrlEncodedFormEntity(params));

                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                            JSONObject response = new JSONObject(new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent())).readLine());
                            if (response.getBoolean("status")) {
                                return response;
                            } else {
                                result.put("message", response.getString("message"));
                            }
                        } else {
                            result.put("message", "Status Code Http Response != 200");
                        }
                    }
                }
                else{
                    result.put("message","Failed, Customer_Add_Info == null");
                }
            }
            catch(Exception e){
                Log.e(TAG,"Err: " + e.getMessage());
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            super.onPostExecute(response);
            if(response == null){
                listener.onAddCustomerFailed("Insert Failed, Input from DoinBackground == null");
            }
            else{
                try {
                    if(response.getBoolean("status")){
                        listener.onAddCustomerSuccessful();
                    }
                    else{
                        listener.onAddCustomerFailed(response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onAddCustomerFailed("xem lai Customer Class");
                }
            }
        }
    }
    @Bindable
    public String getCustomer_ten_info(){
        if(customer_sdt != null && !customer_sdt.isEmpty())
            return customer_ten + " - " + customer_sdt;
        else
            return customer_ten;
    }
    @Bindable
    public String getCustomer_id() {
        return customer_id;
    }

    @Bindable
    public String getCustomer_ten() {
        return customer_ten;
    }

    @Bindable
    public String getCustomer_sdt() {
        return customer_sdt;
    }

    @Bindable
    public String getCustomer_nhom() {
        return customer_nhom;
    }

    @Bindable
    public String getCustomer_gioitinh() {
        return customer_gioitinh;
    }

    @Bindable
    public String getCustomer_ngay_sinh() {
        return customer_ngay_sinh;
    }

    @Bindable
    public String getCustomer_email() {
        return customer_email;
    }

    @Bindable
    public String getCustomer_dia_chi() {
        return customer_dia_chi;
    }

    @Bindable
    public long getCustomer_no_nan() {
        return customer_no_nan;
    }

    @Bindable
    public long getCustomer_tong_tien_mua() {
        return customer_tong_tien_mua;
    }

    @Bindable
    public String getCustomer_ma_so_thue() {
        return customer_ma_so_thue;
    }

    @Bindable
    public int getCustomer_status() {
        return customer_status;
    }

    @Bindable public static String getDefaultId(){
        return "0";
    }

    @Bindable  public static String getDefaultTen(){
        return "Khách lẻ";
    }
}

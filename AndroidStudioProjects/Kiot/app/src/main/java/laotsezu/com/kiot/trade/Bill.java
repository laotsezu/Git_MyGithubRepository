package laotsezu.com.kiot.trade;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import laotsezu.com.kiot.partner.Customer;
import laotsezu.com.kiot.personnel.Personnel;
import laotsezu.com.kiot.resources.BooleanStringObject;

/**
 * Created by laotsezu on 17/09/2016.
 */
public class Bill {
    public static String TAG = "Bill: ";
    String bill_id;
    String bill_thoi_gian;
    Customer customer;
    Agency agency;
    Personnel personnel;
    String bill_ghi_chu;
    long  bill_tong_tien_hang;
    long bill_giam_gia;
    long bill_tien_can_tra;
    long bill_tien_da_tra;
    String bill_phuong_thuc;
    int bill_status;
    public interface OnBillPaymentListener{
        void onPreBillPayment();
        void onPaymentFailed(String message);
        void onPaymentSucessful();
    }
    public Bill(JSONObject input){
        try {
            this.bill_id = input.getString("bill_id");
            this.bill_thoi_gian = input.getString("bill_thoi_gian");
            if(!input.getString("customer_id").isEmpty()){
                this.customer = new Customer(input);
            }
            else{
                this.customer = Customer.getDefaultCustomer();
            }
            if(!input.getString("agency_id").isEmpty()){
                this.agency = new Agency(input);
            }
            else{
                this.agency = Agency.getDefautAgency();
            }
            this.personnel = new Personnel(input);
            this.bill_ghi_chu = input.getString("bill_ghi_chu");
            this.bill_tong_tien_hang = input.getLong("bill_tong_tien_hang");
            this.bill_giam_gia = input.getLong("bill_giam_gia");
            this.bill_tien_can_tra = input.getLong("bill_tien_can_tra");
            this.bill_tien_da_tra = input.getLong("bill_tien_da_tra");
            this.bill_phuong_thuc = input.getString("bill_phuong_thuc");
            this.bill_status = input.getInt("bill_status");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void payment(NewBillInfo info,OnBillPaymentListener listener){
        BillPaymentAsyncTask task = new BillPaymentAsyncTask(listener);
        task.execute(info);
    }
    private static class BillPaymentAsyncTask extends AsyncTask<NewBillInfo,Void,BooleanStringObject>{
        OnBillPaymentListener listener;
        String url = "http://kiot.igarden.vn/trade/banhang";
        public BillPaymentAsyncTask(OnBillPaymentListener listener){
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onPreBillPayment();
        }

        @Override
        protected BooleanStringObject doInBackground(NewBillInfo... lz) {
            NewBillInfo info = lz[0];

            BooleanStringObject booleanStringObject = null;

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(info.getParams()));
                HttpResponse httpResponse = httpClient.execute(httpPost);

                if(httpResponse.getStatusLine().getStatusCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                    String line = br.readLine();

                    JSONObject response = new JSONObject(line);
                    boolean status = response.getBoolean("status");
                    if(status){
                        booleanStringObject = new BooleanStringObject(true,"");
                    }
                    else{
                        booleanStringObject = new BooleanStringObject(false,response.getString("message"));
                    }
                }
                else{
                    booleanStringObject = new BooleanStringObject(false,TAG + "Http Response Status code != 200");
                }
            } catch (Exception e) {
                e.printStackTrace();
                booleanStringObject = new BooleanStringObject(false,TAG + e.getMessage());
            }

            return booleanStringObject;
        }

        @Override
        protected void onPostExecute(BooleanStringObject response) {
            super.onPostExecute(response);
            if(response != null) {
                if (response.getStatus()) {
                    listener.onPaymentSucessful();
                } else {
                    listener.onPaymentFailed(response.getMessage());
                }
            }
            else{
                listener.onPaymentFailed(TAG + "Response from DoInBackground == null");
            }
        }
    }
    public String getBill_id() {
        return bill_id;
    }

    public String getBill_thoi_gian() {
        return bill_thoi_gian;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Agency getAgency() {
        return agency;
    }

    public Personnel getPersonnel() {
        return personnel;
    }

    public String getBill_ghi_chu() {
        return bill_ghi_chu;
    }

    public long getBill_tong_tien_hang() {
        return bill_tong_tien_hang;
    }

    public long getBill_giam_gia() {
        return bill_giam_gia;
    }

    public long getBill_tien_can_tra() {
        return bill_tong_tien_hang - bill_giam_gia;
    }

    public long getBill_tien_da_tra() {
        return bill_tien_da_tra;
    }

    public int getBill_status() {
        return bill_status;
    }

    public String getBill_phuong_thuc() {
        return bill_phuong_thuc;
    }
}

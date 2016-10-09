package laotsezu.com.kiot.partner;

import android.animation.ObjectAnimator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by laotsezu on 17/09/2016.
 */
public class Customer_Add_Info {
    String customer_loai;
    String customer_id;
    String customer_ten;
    String customer_gioi_tinh;
    String customer_ngay_sinh;
    String customer_nhom;
    String customer_sdt;
    String customer_email;

    public Customer_Add_Info(String customer_loai, String customer_id, String customer_ten, String customer_gioi_tinh, String customer_ngay_sinh, String customer_nhom, String customer_sdt, String customer_email) {
        this.customer_loai = customer_loai;
        this.customer_id = customer_id;
        this.customer_ten = customer_ten;
        this.customer_gioi_tinh = customer_gioi_tinh;
        this.customer_ngay_sinh = customer_ngay_sinh;
        this.customer_nhom = customer_nhom;
        this.customer_sdt = customer_sdt;
        this.customer_email = customer_email;
    }

    public List<NameValuePair> getParams(){
        List<NameValuePair> params = new LinkedList<>();

        params.add(new BasicNameValuePair("customer_loai",customer_loai));
        params.add(new BasicNameValuePair("customer_id",null));
        params.add(new BasicNameValuePair("customer_ten",customer_ten));
        params.add(new BasicNameValuePair("customer_gioi_tinh",customer_gioi_tinh));
        params.add(new BasicNameValuePair("customer_ngay_sinh",customer_ngay_sinh));
        params.add(new BasicNameValuePair("customer_sdt",customer_sdt));
        params.add(new BasicNameValuePair("customer_sdt",customer_sdt));
        params.add(new BasicNameValuePair("customer_email",customer_email));


        return params;
    }
    public boolean isAvailable(){
        if(customer_ten.isEmpty())
            return false;
        return true;
    }
}

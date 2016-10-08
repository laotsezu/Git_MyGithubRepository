package laotsezu.com.kiot.trade;

import android.databinding.BaseObservable;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

import laotsezu.com.kiot.goods.Goods;

/**
 * Created by Laotsezu on 9/19/2016.
 */
public class NewBillInfo extends BaseObservable{
    String customer_id;
    String agency_id;
    String personnel_id;
    String current_goods_id;
    long bill_tong_tien_hang;

    long bill_tien_da_tra;
    long bill_giam_gia;
    long bill_tien_can_tra;
    String bill_phuong_thuc;

    public NewBillInfo(String customer_id, String agency_id, String personnel_id, String current_goods_id,String bill_phuong_thuc, long bill_tong_tien_hang, long bill_tien_da_tra, long bill_giam_gia) {
        this.customer_id = customer_id;
        this.agency_id = agency_id;
        this.personnel_id = personnel_id;
        this.current_goods_id = current_goods_id;
        this.bill_phuong_thuc = bill_phuong_thuc;
        this.bill_tong_tien_hang = bill_tong_tien_hang;
        this.bill_tien_da_tra = bill_tien_da_tra;
        this.bill_giam_gia = bill_giam_gia;
        this.bill_tien_can_tra = bill_tong_tien_hang - bill_giam_gia;
    }
    public static String calculateTienCanTra(String bill_tong_tien_hang,String bill_giam_gia){
        return String.valueOf(Long.parseLong(bill_tong_tien_hang) - Long.parseLong(bill_giam_gia));
    }
    public List<NameValuePair> getParams(){
        List<NameValuePair> params = new LinkedList<>();

        params.add(new BasicNameValuePair("customer_id",customer_id));
        params.add(new BasicNameValuePair("agency_id",agency_id));
        params.add(new BasicNameValuePair("personnel_id",personnel_id));
        params.add(new BasicNameValuePair("current_goods_ids",current_goods_id));
        params.add(new BasicNameValuePair("bill_tong_tien_hang",String.valueOf(bill_tong_tien_hang)));
        params.add(new BasicNameValuePair("bill_tien_da_tra",String.valueOf(bill_tien_da_tra)));
        params.add(new BasicNameValuePair("bill_giam_gia",String.valueOf(bill_giam_gia)));
        params.add(new BasicNameValuePair("bill_tien_can_tra",String.valueOf(bill_tien_can_tra)));

        return params;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(String agency_id) {
        this.agency_id = agency_id;
    }

    public String getPersonnel_id() {
        return personnel_id;
    }

    public void setPersonnel_id(String personnel_id) {
        this.personnel_id = personnel_id;
    }

    public String getCurrent_goods_id() {
        return current_goods_id;
    }

    public void setCurrent_goods_id(String current_goods_id) {
        this.current_goods_id = current_goods_id;
    }

    public long getBill_tong_tien_hang() {
        return bill_tong_tien_hang;
    }

    public void setBill_tong_tien_hang(long bill_tong_tien_hang) {
        this.bill_tong_tien_hang = bill_tong_tien_hang;
    }

    public long getBill_tien_da_tra() {
        return bill_tien_da_tra;
    }

    public void setBill_tien_da_tra(long bill_tien_da_tra) {
        this.bill_tien_da_tra = bill_tien_da_tra;
    }

    public long getBill_giam_gia() {
        return bill_giam_gia;
    }

    public void setBill_giam_gia(long bill_giam_gia) {
        this.bill_giam_gia = bill_giam_gia;
    }

    public long getBill_tien_can_tra() {
        return bill_tien_can_tra;
    }

    public void setBill_tien_can_tra(long bill_tien_can_tra) {
        this.bill_tien_can_tra = bill_tien_can_tra;
    }

    public String getBill_phuong_thuc() {
        return bill_phuong_thuc;
    }

    public void setBill_phuong_thuc(String bill_phuong_thuc) {
        this.bill_phuong_thuc = bill_phuong_thuc;
    }
    public String getBill_giam_gia_info(){
        return Goods.toVietNameMoneyFormat(bill_giam_gia);
    }
    public String getBill_tien_can_tra_info() {
        return Goods.toVietNameMoneyFormat(bill_tien_can_tra);
    }

    public String getBill_tien_da_tra_info() {
        return Goods.toVietNameMoneyFormat(bill_tien_da_tra);
    }

    public String getBill_tong_tien_hang_info() {
        return Goods.toVietNameMoneyFormat(bill_tong_tien_hang);
    }
}

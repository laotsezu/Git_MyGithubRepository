package laotsezu.com.kiot.goods;

import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import laotsezu.com.kiot.resources.CenterLineSpan;
import laotsezu.com.kiot.resources.MyUtility;
import laotsezu.com.kiot.utilities.StartAndLimit;

public class Goods extends BaseObservable {
    static String TAG = "Goods: ";
    public static int GOODS_NAME_MAX_WIDTH = MyUtility.getScreenWidth() / 4;
    private String goods_id;
    private  String goods_ten;
    private   String goods_nhom;
    private   String goods_loai;
    private   long goods_gia_ban;
    private    long goods_gia_von;
    private    long goods_so_luong;
    private    long goods_ton_kho;
    private    int goods_giam_gia;
    private   String goods_don_vi;
    private   String goods_icon;
    private  int goods_status;

    Map<String,String> properties = new HashMap<>();

    public interface OnLoadGoodsListener{
        void onPreLoadGoods();
        void onLoadGoodsSucessful(JSONArray goods_array);
        void onLoadGoodsFailed(String message);
    }
    public void addPropertiy(String property,String value){
        this.properties.put(property,value);
    }
    public String getPropertyValue(String property,String value_default){
        if(properties.containsKey(property) && properties.get(property) != null) {
            return properties.get(property);
        }
        else{
            return value_default;
        }
    }
    public Goods(Cursor cursor){
        goods_id = cursor.getString(1);
        goods_ten = cursor.getString(2);
        goods_nhom = cursor.getString(3);
        goods_loai = cursor.getString(4);
        goods_gia_ban = cursor.getLong(5);
        goods_gia_von = cursor.getLong(6);
        goods_so_luong = cursor.getLong(7);
        goods_don_vi = cursor.getString(8);
        goods_status = cursor.getInt(9);
        goods_icon = cursor.getString(10);
    }
    public Goods(JSONObject input){
        try {
            goods_id = input.getString("goods_id");
            goods_ten = input.getString("goods_ten");
            goods_nhom = input.getString("goods_nhom");
            goods_loai = input.getString("goods_loai");
            goods_gia_ban = input.getLong("goods_gia_ban");
            goods_gia_von = input.getLong("goods_gia_von");
            goods_giam_gia = input.getInt("goods_giam_gia");
            goods_so_luong = input.getLong("goods_so_luong");
            goods_don_vi = input.getString("goods_don_vi");
            goods_status = input.getInt("goods_status");
            goods_icon = input.getString("goods_icon");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public Goods(Bundle input){
        goods_id = input.getString("goods_id");
        goods_ten = input.getString("goods_ten");
        goods_nhom = input.getString("goods_nhom");
        goods_loai = input.getString("goods_loai");
        goods_gia_ban = input.getLong("goods_gia_ban");
        goods_gia_von = input.getLong("goods_gia_von");
        goods_so_luong = input.getLong("goods_so_luong");
        goods_don_vi = input.getString("goods_don_vi");
        goods_icon = input.getString("goods_icon");
        goods_giam_gia = input.getInt("goods_giam_gia");
        goods_status = input.getInt("goods_status");
    }
    public Bundle getBundle(){

        return getBundle(this);
    }
    public static Bundle getBundle(Goods _goods){
        Bundle bundle = new Bundle();
        bundle.putString("goods_id",_goods.getGoods_id());
        bundle.putString("goods_ten",_goods.getGoods_ten());
        bundle.putString("goods_nhom",_goods.getGoods_nhom());
        bundle.putString("goods_loai",_goods.getGoods_loai());
        bundle.putLong("goods_gia_ban",_goods.getGoods_gia_ban());
        bundle.putLong("goods_gia_von",_goods.getGoods_gia_von());
        bundle.putLong("goods_so_luong",_goods.getGoods_so_luong());
        bundle.putString("goods_don_vi",_goods.getGoods_don_vi());
        bundle.putString("goods_icon",_goods.getGoods_icon());
        bundle.putInt("goods_giam_gia",_goods.getGoods_giam_gia());
        bundle.putInt("goods_status",_goods.getGoods_status());
        return bundle;
    }
    public boolean matches(Goods _goods){
        return this.getGoods_id().equalsIgnoreCase(_goods.getGoods_id());
    }
    public static void loadGoods(StartAndLimit startAndLimit,OnLoadGoodsListener listener){
        LoadGoodsAsyncTask task = new LoadGoodsAsyncTask(listener);
        task.execute(startAndLimit);
    }
    private static class LoadGoodsAsyncTask extends AsyncTask<StartAndLimit,Void,JSONObject>{
        OnLoadGoodsListener listener;
        static String TAG = "LoadGoodsAsyncTask: ";
        String url  = "http://kiot.igarden.vn/repository/duyetkho";
        public LoadGoodsAsyncTask(OnLoadGoodsListener listener){
            this.listener = listener;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onPreLoadGoods();
        }

        @Override
        protected JSONObject doInBackground(StartAndLimit... startAndLimits) {
            String url = this.url;
            StartAndLimit startAndLimit = startAndLimits[0];
            JSONObject result = new JSONObject();

            try {
                result.put("status", false);

                if(startAndLimit == null){
                    result.put("message",TAG  + "Input Invalid, Start and limit = ??");
                }
                else {
                    url += "?start=" + startAndLimit.getStart() + "&limit=" + startAndLimit.getLimit();

                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(url);

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        return new JSONObject(new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent())).readLine());
                    } else {
                        result.put("message", "Http Response Status Line != 200 ");
                    }
                }

            } catch (Exception e) {
                return null;
            }

            return result;
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            super.onPostExecute(response);
            if(response == null){
                listener.onLoadGoodsFailed("Response from DoInBackground == Null ");
            }
            else{
                try {
                    if(response.getBoolean("status")){
                        listener.onLoadGoodsSucessful(response.getJSONArray("items"));
                    }
                    else{
                        listener.onLoadGoodsFailed(response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onLoadGoodsFailed(e.getMessage());
                }
            }
        }
    }
    public void increaseAmount(long amount){
        Log.e("TAG","Increase Amount = " + amount);
        setGoods_so_luong(goods_so_luong + amount);
    }
    public void printGoods(){
        Log.e("Goods: ","% Giam GIa = " + getGiamGiaPercentInfo());
        Log.e("Goods: ","Giam Gia = " + String.valueOf(getTotalTienGiamGia()));
    }
    public void decreaseAmount(long amount){
        setGoods_so_luong(goods_so_luong - amount);
    }
    @Bindable
    public int getGoods_giam_gia(){
        return goods_giam_gia;
    }
    @Bindable
    public String getGoods_id() {
        return goods_id;
    }
    @Bindable
    public String getGoods_ten() {
        return goods_ten;
    }
    @Bindable
    public String getGoods_nhom() {
        return goods_nhom;
    }
    @Bindable
    public String getGoods_loai() {
        return goods_loai;
    }
    @Bindable
    public long getGoods_gia_ban() {
        return goods_gia_ban;
    }
    @Bindable
    public long getGoods_gia_von() {
        return goods_gia_von;
    }
    @Bindable
    public long getGoods_so_luong() {
        return goods_so_luong;
    }
    public String getGoods_so_luong_text(){
        return String.valueOf(goods_so_luong);
    }
    @Bindable
    public String getGoods_so_luong_info() {
        return "x" + goods_so_luong;
    }
    @Bindable
    public String getGoods_don_vi() {
        return goods_don_vi;
    }
    @Bindable
    public int getGoods_status() {
        return goods_status;
    }

    public String getGoods_icon() {
        return goods_icon;
    }

    public void setGoods_icon(String goods_icon) {
        this.goods_icon = goods_icon;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public void setGoods_ten(String goods_ten) {
        this.goods_ten = goods_ten;
    }

    public void setGoods_nhom(String goods_nhom) {
        this.goods_nhom = goods_nhom;
    }

    public void setGoods_loai(String goods_loai) {
        this.goods_loai = goods_loai;
    }

    public void setGoods_gia_ban(long goods_gia_ban) {
        this.goods_gia_ban = goods_gia_ban;
    }

    public void setGoods_gia_von(long goods_gia_von) {
        this.goods_gia_von = goods_gia_von;
    }

    public void setGoods_so_luong(long goods_so_luong) {
        this.goods_so_luong = goods_so_luong;
    }
    public void setGoods_don_vi(String goods_don_vi) {
        this.goods_don_vi = goods_don_vi;
    }

    public void setGoods_giam_gia(int goods_giam_gia) {
        this.goods_giam_gia = goods_giam_gia;
    }

    public void setGoods_status(int goods_status) {
        this.goods_status = goods_status;
    }
    public static String buildGoods_so_luong_info(long goods_so_luong,String goods_don_vi){
        return goods_so_luong + " x " + goods_don_vi;
    }

    public String getGoods_gia_ban_info() {
        return toVietNameMoneyFormat(goods_gia_ban);
    }
    public long getGoods_tien_giam_gia(){
        return getGoods_gia_ban() * goods_giam_gia / 100;
    }
    public long getGoods_gia_ban_giam_gia(){
        return goods_gia_ban - getGoods_tien_giam_gia();
    }
    public String getGoods_gia_ban_giam_gia_info(){
        return toVietNameMoneyFormat(getGoods_gia_ban_giam_gia());
    }
    public static String toVietNameMoneyFormat(long input){
        if (Math.abs(input) < 1000) {
            return String.valueOf(input) + "đ";
        }
        try {
            NumberFormat formatter = new DecimalFormat("###,###");
            return formatter.format(input) + "đ";
        } catch (Exception e) {
            return "0";
        }
    }
    public static String toVietNameMoneyFormat(String input){
        if(input == null)
            return "0đ";
        long num = Long.parseLong(input);
        if (num < 1000) {
            return input;
        }
        try {
            NumberFormat formatter = new DecimalFormat("###,###");
            return formatter.format(num) + "đ";
        } catch (Exception e) {
            return "0";
        }
    }
    @BindingAdapter("android:loadUrl")
    public static void loadImage(ImageView imageView,String url){
        if(url != null && url.length() > 0) {
            Picasso.with(imageView.getContext())
                    .load(url)
                    .fit()
                    .into(imageView);
        }
    }
    @BindingAdapter("android:ifGiamGiaNull")
    public static void onGiamGiaNull(View view,int goods_giam_gia){
        if(goods_giam_gia == 0){
            view.setVisibility(View.GONE);
        }
        else{
            view.setVisibility(View.VISIBLE);
        }
    }
    @BindingAdapter("android:setTextForTextView")
    public static void onSetTextForTextView(TextView textView,String text){
        SpannableString content = new SpannableString(text);
        content.setSpan(new CenterLineSpan(),0,text.length(),0);
        textView.setText(content);
    }
    @BindingAdapter("android:setWidthForGoodsName")
    public static void setWidthForGoodsName(View view,int width){
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        view.setLayoutParams(layoutParams);
    }
    public boolean hasProperty(String property){
         return properties.containsKey(property);
    }
    public long getTotalTienPhaiTra(){
        Log.e(TAG,goods_so_luong + " * " + getGoods_gia_ban_giam_gia() + " = " + goods_so_luong * getGoods_gia_ban_giam_gia());
        return  goods_so_luong * getGoods_gia_ban_giam_gia();
    }
    public String getTotalTienPhaiTraInfo(){
        return toVietNameMoneyFormat(getTotalTienPhaiTra());
    }
    public long getTotal_price(){
       return goods_so_luong * getGoods_gia_ban();
    }
    public long getTotalTienGiamGia(){
        return  getGoods_tien_giam_gia() * goods_so_luong;
    }
    public String getTotal_price_info() {
        return toVietNameMoneyFormat(getTotal_price());
    }
    public String getGiamGiaPercentInfo(){
        return String.valueOf(goods_giam_gia) + "%";
    }
}
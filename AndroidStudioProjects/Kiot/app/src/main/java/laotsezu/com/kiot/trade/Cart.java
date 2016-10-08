package laotsezu.com.kiot.trade;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import laotsezu.com.kiot.goods.Goods;
import laotsezu.com.kiot.resources.MyUtility;

/**
 * Created by Laotsezu on 9/18/2016.
 */
public class Cart extends BaseObservable{
    public static String TAG = "Cart: ";
    List<Goods> goodses = new LinkedList<>();
    OnCartListener listener;
    public interface OnCartListener{
        void onCartGoodsChange(int count);
    }
    public Cart(){

    }
    public Cart(List<Goods> goodses){
        if(goodses != null) {
            this.goodses = goodses;
        }
    }
    public Cart(Bundle data_contain_count_and_items){
        if(data_contain_count_and_items != null) {
            int count = data_contain_count_and_items.getInt("count");
            Bundle items = data_contain_count_and_items.getBundle("items");
            if(items != null) {
                for (int i = 0; i < count; i++) {
                    Bundle item = items.getBundle(String.valueOf(i));
                    Goods goods = new Goods(item);
                    goodses.add(goods);
                }
            }
        }
    }
    public void setCartListener(OnCartListener listener){
        this.listener = listener;
    }
    public Goods getGoods(String id) throws Exception {
        for(int i = 0;i < goodses.size() ; i++){
            Goods goods = goodses.get(i);
            if(goods.getGoods_id().equalsIgnoreCase(id))
                return goods;
        }
        throw new Exception("Goods is not exist in Cart!");
    }
    private void addGoods(Goods _goods){
        _goods.addPropertiy("goods_max_so_luong",String.valueOf(_goods.getGoods_so_luong()));
        _goods.setGoods_so_luong(0);
        goodses.add(_goods);
        if(listener != null){
            listener.onCartGoodsChange(goodses.size());
        }
    }
    void removeGoods(Goods _goods){
        try {
            int position = getGoodsPosition(_goods);
            goodses.remove(position);
            _goods.setGoods_so_luong(0);
            if (listener != null) {
                listener.onCartGoodsChange(goodses.size());
            }
        }
        catch (Exception e){
            Log.e(TAG,"Remove 1 phần tử không tồn tại");
        }
    }
    public int getGoodsPosition(Goods _goods){
        for(int i =0; i< goodses.size() ; i++){
            Goods goods = goodses.get(i);
            if(_goods.matches(goods))
                return i;
        }
        return -1;
    }
    private boolean isHaving(Goods _goods){
        int position = getGoodsPosition(_goods);
        if(position == -1)
            return false;
        else
            return true;
    }
    public void increaseGoods(Goods _goods,int amount){
        if(amount > 0){
           try{
               Goods goods = getGoods(_goods.getGoods_id());
               goods.increaseAmount(amount);
           }
           catch (Exception e){
               addGoods(_goods);
               increaseGoods(_goods,amount);
           }
        }
    }
    public void decreaseGoods(Goods _goods , long amount){
        if(amount > 0){
            try {
                Goods goods = getGoods(_goods.getGoods_id());
                if(goods.getGoods_so_luong() - amount == 0){
                    goods.setGoods_so_luong(0);
                    removeGoods(goods);
                }
                else{
                    goods.decreaseAmount(amount);
                }
            }
            catch (Exception e){

            }
        }
    }
    public void clearCart(){
        goodses = new LinkedList<>();
    }
    public void setGoodsesAmount(String id,long amount){
        try {
            Goods goods = getGoods(id);
            if(amount == 0){
                removeGoods(goods);
            }
            else {
                goods.setGoods_so_luong(amount);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"Goods is not exist");
        }
    }
    public void setGoodses(Goods _goods){
        try{
            Goods goods = getGoods(_goods.getGoods_id());
            goods.setGoods_so_luong(_goods.getGoods_so_luong());
        }
        catch (Exception e){
            addGoods(_goods);
        }
    }
    public long getTotalPrice(){
        long sum = 0;
        for(int i = 0; i < goodses.size() ; i++){
            Goods goods = goodses.get(i);
            sum += goods.getTotal_price();
        }
        return sum;
    }
    public void setGoodsesGiamGia(String id,long goods_giam_gia){
        try {
            Goods goods = getGoods(id);
            goods.setGoods_giam_gia((int)goods_giam_gia);
            Log.e(TAG,"Set giam gia goods_id = " + id + ", giam gia = " + goods_giam_gia);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getTotalPriceInfo(){
        return Goods.toVietNameMoneyFormat(getTotalPrice());
    }
    public void browseCart(){
        for(int i =0; i < goodses.size() ; i++){
            Goods goods = goodses.get(i);
            Log.e(goods.getGoods_ten(),goods.getGoods_so_luong_info());
        }
    }
    public Bundle getBundle(){
        Bundle data = new Bundle();
        for(int i = 0; i < goodses.size() ; i++){
            Goods goods = goodses.get(i);
            if(goods.getGoods_so_luong() > 0)
                data.putBundle(String.valueOf(i),goods.getBundle());
        }
        return data;
    }
    public int getGoodsCount(){
        int result = 0;
        for(int i = 0; i < goodses.size() ; i++){
            if (goodses.get(i).getGoods_so_luong() > 0)
                result++;
        }
        return result;
    }
    public List<Goods> getGoodses(){
        return goodses;
    }
    public String getCurrentGoodsIds(){
        String result = "";

        for(int i =0; i < goodses.size() ; i++){
            Goods goods = goodses.get(i);
            result += goods.getGoods_id() + "=" + goods.getGoods_so_luong() + ",";
        }

        return result.substring(0,result.length() - 1);
    }
    public long getTotalGiamGia(){
        long sum = 0;
        for(int i = 0; i < goodses.size(); i++){
            Goods goods = goodses.get(i);
            sum += goods.getTotalGiamGia();
        }
        return sum;
    }
    public long getTotalTienPhaiTra(){
        long sum = 0;
        for(int i = 0; i < goodses.size(); i++){
            Goods goods = goodses.get(i);
            sum += goods.getTotalTienPhaiTra();
        }
        return sum;
    }

    public String getTotalTienPhaiTraInfo(){
        return Goods.toVietNameMoneyFormat(getTotalTienPhaiTra());
    }
    public String getTotalGiamGiaInfo(){
        return Goods.toVietNameMoneyFormat(getTotalGiamGia());
    }
    public void updateGoodsSoLuong(Bundle data){
        if (data != null) {
            try {
                int count = data.getInt("count");
                for (int i = 0; i < count; i++) {
                    Bundle goods_data = data.getBundle(String.valueOf(i));
                    String goods_id = goods_data.getString("goods_id","-1");
                    long goods_so_luong = goods_data.getLong("goods_so_luong");
                    setGoodsesAmount(goods_id,goods_so_luong);
                }
            }
            catch (Exception e){
                Log.e(TAG,e.getMessage());
            }
        }
    }
}

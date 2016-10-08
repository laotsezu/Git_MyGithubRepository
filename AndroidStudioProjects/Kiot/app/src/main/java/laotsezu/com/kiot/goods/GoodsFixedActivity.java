package laotsezu.com.kiot.goods;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import laotsezu.com.kiot.R;
import laotsezu.com.kiot.databinding.ViewGoodsFixedBinding;
import laotsezu.com.kiot.resources.MyUtility;


public class GoodsFixedActivity extends AppCompatActivity {
    public static String TAG = "GoodsFixedActivity: ";
    ViewGoodsFixedBinding binding;
    Goods goods;
    int total_price_max_size = 15;
    Intent result = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.view_goods_fixed);

        if(getIntentData()){
            init();
        }
    }
    public void init(){
        //
        binding.goodsFixedSoLuong.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s == null || s.length() <= 0){
                    goods.setGoods_so_luong(0);
                }
                else if(String.valueOf(s).contains("-")){
                    String old_count = String.valueOf(goods.getGoods_so_luong());
                    binding.goodsFixedSoLuong.setText(old_count);
                    binding.goodsFixedSoLuong.setSelection(old_count.length());
                }
                else{
                        String new_tong_tien = String.valueOf((long)(Float.parseFloat(String.valueOf(s)) * goods.getGoods_gia_ban()));
                        Log.e(TAG,"New Tong Tien Length = " + new_tong_tien.length());
                        if(new_tong_tien.length() > total_price_max_size){
                            binding.goodsFixedSoLuong.setText(goods.getGoods_so_luong_text());
                            binding.goodsFixedSoLuong.setSelection(goods.getGoods_so_luong_text().length());
                            Toast.makeText(GoodsFixedActivity.this, "Số lượng quá nhiều", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            goods.setGoods_so_luong(Long.parseLong(String.valueOf(s)));
                            result.putExtra("goods_so_luong",goods.getGoods_so_luong());
                            binding.goodsFixedTongTien.setText(goods.getTotalTienPhaiTraInfo());
                        }
                }
            }
            public void afterTextChanged(Editable s) {}
        });
        binding.goodsFixedSoLuong.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    if(String.valueOf(binding.goodsFixedSoLuong.getText()).isEmpty()){
                        setResult(RESULT_OK,null);
                    }
                    supportFinishAfterTransition();
                }
                return false;
            }
        });
        /////
       /* binding.goodsFixedGiamGia.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s == null || s.length() <= 0){
                    goods.setGoods_giam_gia(0);
                }
                else if(String.valueOf(s).contains("-")){
                    binding.goodsFixedSoLuong.setText(goods.getGoods_so_luong_text());
                    binding.goodsFixedSoLuong.setSelection(goods.getGoods_so_luong_text().length());
                    Toast.makeText(GoodsFixedActivity.this, "Giảm giá < 100%", Toast.LENGTH_SHORT).show();
                }
                else{
                    goods.setGoods_giam_gia(Integer.parseInt(String.valueOf(s)));
                    result.putExtra("goods_giam_gia",goods.getGoods_giam_gia());
                    binding.goodsFixedTongTien.setText(goods.getTotalTienPhaiTraInfo());
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });
        binding.goodsFixedGiamGia.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    if(goods.getGoods_so_luong() == 0)
                        binding.goodsFixedGiamGia.setText("0");
                }
                return false;
            }
        });*/
    }
    public boolean getIntentData(){
        Bundle data;
        if(getIntent() != null && (data = getIntent().getExtras()) != null){
            Bundle item = data.getBundle("item");
            goods = new Goods(item);
            binding.setGoods(goods);
            goods.printGoods();
            ///
            result = new Intent();
            setResult(RESULT_OK,result);
            result.putExtra("goods_id",goods.getGoods_id());
            //
            return true;
        }
        return false;
    }
    public void onSaveButtonClick(View v){
        supportFinishAfterTransition();
    }
    public void onBackButtonClick(View v){
        setResult(RESULT_OK,null);
        supportFinishAfterTransition();
    }
}

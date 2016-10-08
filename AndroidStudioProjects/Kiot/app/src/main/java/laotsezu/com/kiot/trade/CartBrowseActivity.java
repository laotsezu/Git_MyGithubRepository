package laotsezu.com.kiot.trade;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import laotsezu.com.kiot.R;
import laotsezu.com.kiot.databinding.ViewCartBrowseBinding;
import laotsezu.com.kiot.databinding.ViewGoods2Binding;
import laotsezu.com.kiot.goods.Goods;
import laotsezu.com.kiot.goods.GoodsFixedActivity;
import laotsezu.com.kiot.partner.Customer;
import laotsezu.com.kiot.partner.CustomerSelectActivity;
import laotsezu.com.kiot.resources.MyUtility;
import laotsezu.com.kiot.utilities.GoodsCartAdapter;

public class CartBrowseActivity extends AppCompatActivity {
    static String TAG = "CartBrowseActivity: ";
    Cart cart;
    ViewCartBrowseBinding binding;
    String customer_id = Customer.getDefaultId();
    String customer_ten = Customer.getDefaultTen();
    String agency_id = Agency.getDefaultId();
    GoodsCartAdapter adapter;
    public static int SELECT_CUSTOMER = 5151,SELECT_PRICE_TABLE = 5152,GOODS_FIXED = 5153;
    Intent result_intent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.view_cart_browse);
        if(getIntentData()){
            init();
        }
    }

    public void init(){
        ////

        ////
        adapter = new GoodsCartAdapter(cart.getGoodses());
        binding.cartCustomerName.setText(customer_ten);
        binding.cartGoodsList.setAdapter(adapter);

        binding.cartTotalPrice.setText(cart.getTotalTienPhaiTraInfo());

        binding.cartGoodsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewGoods2Binding binding = DataBindingUtil.getBinding(view);
                Goods goods = binding.getGoods();
                Intent intent = new Intent(CartBrowseActivity.this, GoodsFixedActivity.class);
                intent.putExtra("item",goods.getBundle());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        CartBrowseActivity.this,
                        Pair.create((View)binding.goodsGiamGiaText,"goods_giam_gia"),
                        Pair.create((View)binding.goodsSoLuongText,"goods_so_luong"),
                        Pair.create((View)binding.goodsTongTienText,"goods_tong_tien")
                );

                getWindow().setExitTransition(TransitionInflater.from(CartBrowseActivity.this).inflateTransition(android.R.transition.fade));

                startActivityForResult(intent,GOODS_FIXED,options.toBundle());
            }
        });
    }
    public void onBackButtonClick(View v){
        supportFinishAfterTransition();
    }
    public void onPaymentButtonClick(View v){
        if(cart.getGoodsCount() <= 0){
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
        }
        else{

            Intent intent = new Intent(this, PaymentActivity.class);

            intent.putExtra("customer_id", customer_id);
            intent.putExtra("agency_id", agency_id);
            intent.putExtra("personnel_id", getSharedPreferences(MyUtility.SHAREPREFERENCE_ID, MODE_PRIVATE).getString("personnel_id", "0"));
            intent.putExtra("current_goods_ids", cart.getCurrentGoodsIds());
            intent.putExtra("bill_tong_tien_hang", cart.getTotalPrice());
            intent.putExtra("bill_giam_gia", cart.getTotalGiamGia());

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    Pair.create((View) binding.cartTotalPrice, "totalPriceValue")

            );

            startActivity(intent, options.toBundle());
        }
    }
    public boolean getIntentData(){
        Bundle data ;
        if(getIntent() != null &&(data = getIntent().getExtras()) != null){
            customer_id = data.getString("customer_id",customer_id);
            customer_ten = data.getString("customer_ten",customer_ten);

            Bundle items = data.getBundle("items");
            if(items != null) {
                List<Goods> list = new LinkedList<>();
                for (int i = 0; i < data.getInt("count", 0); i++) {
                    Bundle item = items.getBundle(String.valueOf(i));
                    Goods goods = new Goods(item);
                    list.add(goods);
                }

                cart = new Cart(list);
                cart.browseCart();

                return true;
            }
        }
        return  false;
    }
    public void onCustomerContainerClick(View v){
        Intent intent = new Intent(this, CustomerSelectActivity.class);
        intent.putExtra("customer_id",customer_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent,SELECT_CUSTOMER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(intent != null){
            Bundle data = intent.getExtras();
            if(requestCode == SELECT_CUSTOMER){
                customer_id = data.getString("customer_id");
                customer_ten = data.getString("customer_ten");
                binding.cartCustomerName.setText(customer_ten);
                result_intent.putExtra("customerIsChange",true);
                result_intent.putExtra("customer_ten",customer_ten);
                result_intent.putExtra("customer_id",customer_id);
                setResult(RESULT_OK,result_intent);
            }
            else if(requestCode == GOODS_FIXED){
                String goods_id = data.getString("goods_id");
                long goods_so_luong = data.getLong("goods_so_luong",-1);
                if(goods_so_luong != -1){
                    cart.setGoodsesAmount(goods_id,goods_so_luong);

                    Log.e(TAG,"Goods So Luong = " + goods_so_luong);
                    adapter.notifyDataSetChanged();
                    binding.cartTotalPrice.setText(cart.getTotalTienPhaiTraInfo());
                    result_intent.putExtra("cartIsChange",true);
                    int old_count = result_intent.getExtras().getInt("count",0);
                    result_intent.putExtra("count",old_count + 1);

                    Bundle goods_id_and_so_luong = new Bundle();
                    goods_id_and_so_luong.putString("goods_id",goods_id);
                    goods_id_and_so_luong.putLong("goods_so_luong",goods_so_luong);

                    result_intent.putExtra(String.valueOf(old_count),goods_id_and_so_luong);
                    setResult(RESULT_OK,result_intent);
                }
/////////////////////
            }
        }
    }
}

package laotsezu.com.kiot.trade;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import laotsezu.com.kiot.databinding.ViewGoods4Binding;
import laotsezu.com.kiot.databinding.ViewGoodsSelectBinding;
import laotsezu.com.kiot.partner.Customer;
import laotsezu.com.kiot.partner.CustomerSelectActivity;
import laotsezu.com.kiot.personnel.Personnel;
import laotsezu.com.kiot.personnel.PersonnelLoginActivity;
import laotsezu.com.kiot.resources.MyUtility;
import laotsezu.com.kiot.utilities.GoodsSelectAdapter4;
import laotsezu.com.kiot.R;
import laotsezu.com.kiot.goods.Goods;
import laotsezu.com.kiot.utilities.GoodsSearchEditText;
import laotsezu.com.kiot.resources.ResizeAnimation;

public class GoodsSelectActivity extends AppCompatActivity implements Goods.OnLoadGoodsListener,Cart.OnCartListener {
    ViewGoodsSelectBinding binding;
    GoodsSelectAdapter4 adapter;
    static String TAG = "GoodsSelectActivity: ";
    Personnel personnel;
    boolean input_keyboard_visible = false;

    Cart cart;
    String customer_id = Customer.getDefaultId();
    String customer_ten = Customer.getDefaultTen();
    public static int SELECT_CUSTOMER = 1234,SELECT_PRICE_TABLE = 1235,TO_CART_BROWSE = 1236;

    float origin_addtionInfo_size;
    float origin_paymentNavigator_size;

    private long mLastBackPress = 0;
    int TOAST_DURATION = 1000;
    Toast onBackPressedToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.view_goods_select);

        binding.getRoot().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                binding.getRoot().removeOnLayoutChangeListener(this);
                startCoolAnimation();
            }
        });

        try {

            Bundle extra = getIntent().getExtras();
            personnel = new Personnel(extra.getBundle("personnel_data"));
            binding.setPersonnel(personnel);

            Log.e(TAG,"Ten " + personnel.getPersonnel_ten());
            Log.e(TAG,"Dia Chi " + personnel.getPersonnel_dia_chi());
            Log.e(TAG,"Sdt " + personnel.getPersonnel_sdt());
            Log.e(TAG,"Tong TIen Ban " + personnel.getPersonnel_tong_tien_ban());

            Goods.loadGoods("",GoodsSelectActivity.this);

            initLeftDrawer();
        }
        catch (Exception e){
            Toast.makeText(GoodsSelectActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
        //

    }
    public void initLeftDrawer(){

    }
    public void onBackPressed(){
        Log.e(TAG,"Back Press!");
        long currentTime = System.currentTimeMillis();
        if(currentTime - mLastBackPress > TOAST_DURATION){
            onBackPressedToast = Toast.makeText(this,"Nhấp 2 lần liên tiếp để thoát",Toast.LENGTH_SHORT);
            onBackPressedToast.show();
            mLastBackPress = currentTime;
        }
        else{
            if(onBackPressedToast != null){
                onBackPressedToast.cancel();
            }
            finishAffinity();
        }
    }
    public void onHomeButtonOnClick(View v){
        binding.goodsSelectDrawerContainer.openDrawer(GravityCompat.START);
    }
    public void onSearchEditTextClick(View v){
        hideSomeComponent();
    }
    public void onPaymentContainerClick(View v){
        if(cart != null && cart.getGoodsCount() != 0) {
            naviToCartBrowseActivity();
        }
        else{
            Toast.makeText(GoodsSelectActivity.this, "Cart is empty, add some goods please!", Toast.LENGTH_SHORT).show();
        }
    }

    public void hideSomeComponent(){
        synchronized (this) {
            if(!input_keyboard_visible){
                input_keyboard_visible = true;

                ResizeAnimation animation1 = new ResizeAnimation(binding.goodsSelectAdditionalInfo,origin_addtionInfo_size,0);
                final ResizeAnimation animation2 = new ResizeAnimation(binding.goodsSelectPaymentNavigator,origin_paymentNavigator_size,0);

                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        animation2.startResize();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                animation1.startResize();

            }
        }
    }
    public void showSomeComponent(){
        Log.e(TAG,"show Some Component");
        synchronized (this) {
            if(input_keyboard_visible){
                input_keyboard_visible = false;

                ResizeAnimation animation1 = new ResizeAnimation(binding.goodsSelectAdditionalInfo,0,origin_addtionInfo_size);
                final ResizeAnimation animation2 = new ResizeAnimation(binding.goodsSelectPaymentNavigator,0,origin_paymentNavigator_size);

                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        animation2.startResize();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                animation1.startResize();
            }
        }

    }
    @Override
    public void onPreLoadGoods() {
        Toast.makeText(GoodsSelectActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
        binding.goodsSelectProgressbar.setVisibility(View.VISIBLE);
    }
    @Override
    public void onLoadGoodsSucessful(JSONArray goods_array) {

        origin_addtionInfo_size = binding.goodsSelectAdditionalInfo.getHeight();
        origin_paymentNavigator_size = binding.goodsSelectPaymentNavigator.getHeight();

        binding.goodsSelectProgressbar.setVisibility(View.GONE);

        Log.e(TAG,"Origin Addtion Info Height = " + origin_addtionInfo_size);

        List<Goods> list = new LinkedList<>();
        for(int i =0; i < goods_array.length() ; i++){
            try {
                JSONObject item = goods_array.getJSONObject(i);
                Goods goods = new Goods(item);
                goods.addPropertiy("position" ,String.valueOf(i));
                list.add(goods);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new GoodsSelectAdapter4(list, new MyListenerForGoodsSelectAdapter());
        cart = new Cart();

        /////////
        binding.goodsSelectGoodsList.setAdapter(adapter);
       // binding.goodsSelectGoodsList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.goodsSelectGoodsList.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
       // binding.goodsSelectGoodsList.setLayoutManager(new GridLayoutManager(this,3));
        //////
        binding.goodsSelectSearchText.setOnEditTextListener(new GoodsSearchEditText.OnEditTextListener() {
            @Override
            public void onEditTextBackPressed() {
                showSomeComponent();
                binding.goodsSelectSearchText.setText("");
            }
        });
        binding.goodsSelectSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        binding.goodsSelectSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    showSomeComponent();
                   // binding.goodsSelectSearchText.setText("");
                }
                return false;
            }
        });
        ///
        cart.setCartListener(this);
        //////
        MyUtility.setSwipeRefreshLayoutColor(binding.goodsSelectGoodsListContainer,getResources());

        binding.goodsSelectGoodsListContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyItemRangeChanged(0,adapter.getItemCount());
                binding.goodsSelectGoodsListContainer.setRefreshing(false);
            }
        });
    }
    @Override
    public void onLoadGoodsFailed(String message) {
        Toast.makeText(GoodsSelectActivity.this, "Load Goods Failed : " + message, Toast.LENGTH_SHORT).show();
        binding.goodsSelectProgressbar.setVisibility(View.GONE);
        binding.goodsSelectLoadfailedContainer.setVisibility(View.VISIBLE);
    }
    public void onRetryButtonClick(View v){
        binding.goodsSelectLoadfailedContainer.setVisibility(View.GONE);
        Goods.loadGoods("",this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(intent != null){
            Bundle data = intent.getExtras();
            if(data == null) {
                Toast.makeText(GoodsSelectActivity.this, "Result Data = Null", Toast.LENGTH_SHORT).show();
            }
            else {
                if (requestCode == SELECT_CUSTOMER) {
                    customer_id = data.getString("customer_id");
                    customer_ten = data.getString("customer_ten");

                    binding.goodsSelectCustomerName.setText(customer_ten);

                } else if (requestCode == TO_CART_BROWSE) {
                    Log.e(TAG,"Result from Cart Browse");
                    boolean cartIsChange = data.getBoolean("cartIsChange",false);
                    if(cartIsChange) {
                        Log.e(TAG,"Cart change");
                        cart.updateGoodsSoLuong(data);
                        adapter.notifyItemRangeChanged(0,adapter.getItemCount());
                        MyUtility.changeTextViewValueSmoothy(binding.goodsSelectTotalCount,String.valueOf(cart.getGoodsCount()));
                        MyUtility.changeTextViewValueSmoothy(binding.goodsSelectTotalPrice,cart.getTotalTienPhaiTraInfo());
                    }
                    boolean customerIsChange = data.getBoolean("customerIsChange",false);
                    if(customerIsChange) {

                        customer_id = data.getString("customer_id", customer_id);
                        customer_ten = data.getString("customer_ten", customer_ten);
                        Log.e(TAG,"Customer change, new name = " + customer_ten);

                        MyUtility.changeTextViewValueSmoothy(binding.goodsSelectCustomerName,customer_ten);
                    }
                }
            }
        }
    }
    public void resetGoodses(){
        final long current_total_count = cart.getGoodsCount();
        final long current_total_prince = cart.getTotalPrice();

        Log.e(TAG,"Current Count = " + current_total_count + "Current Price = " + current_total_prince);

        Log.e(TAG,"Start Reset Goodses");
        cart.clearCart();
        adapter.resetAmountOfGoods();

        final Handler handler = new Handler();

        handler.post(new Runnable() {
            int i = 10;
            @Override
            public void run() {
                i--;
                if(i >= 0) {
                    String count = String.valueOf(current_total_count * i / 10);
                    binding.goodsSelectTotalCount.setText(count);
                    binding.goodsSelectTotalPrice.setText(Goods.toVietNameMoneyFormat((long)(current_total_prince * i / 10)));
                    handler.post(this);
                }
            }
        });

        Toast.makeText(this, "Xóa thành công dữ liệu giỏ hàng!", Toast.LENGTH_SHORT).show();

    }
    public void naviToCartBrowseActivity(){
        binding.goodsSelectAdditionalInfo.setAlpha(0.5f);
        Intent intent = new Intent(this,CartBrowseActivity.class);
        cart.browseCart();
        intent.putExtra("items",cart.getBundle());
        intent.putExtra("count",cart.getGoodsCount());
        intent.putExtra("customer_id",customer_id);
        intent.putExtra("customer_ten",customer_ten);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                Pair.create((View)binding.goodsSelectPaymentNavigator,"totalPrice")
        );

        startActivityForResult(intent,TO_CART_BROWSE,options.toBundle());
    }
    public void naviToCartBrowseActivity(View v){

        binding.goodsSelectAdditionalInfo.setAlpha(0.5f);
        Intent intent = new Intent(this,CartBrowseActivity.class);
        cart.browseCart();
        intent.putExtra("items",cart.getBundle());
        intent.putExtra("count",cart.getGoodsCount());
        intent.putExtra("customer_id",customer_id);
        intent.putExtra("customer_ten",customer_ten);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivityForResult(intent,TO_CART_BROWSE);

    }

    public void naviToSelectCustomerActivity(View v){
        Intent intent = new Intent(this, CustomerSelectActivity.class);
        intent.putExtra("customer_id",customer_id);
        intent.putExtra("customer_ten",customer_ten);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent,SELECT_CUSTOMER);
    }
    public void naviToPersonnelInfoActivity(View v){

    }
    public void naviToReturnGoodsActivity(View v){

    }
    public void naviToSettingsActivity(View v){

    }
    public void onLogoutButtonClick(View v){
        Personnel.clearLoginData(this);
        Intent intent = new Intent(this, PersonnelLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void onCartGoodsChange(int count){
        binding.goodsSelectTotalCount.setText(String.valueOf(count));
        MyUtility.jumpViewAnimation(binding.goodsSelectTotalCount);

    }
    public void onCleanCartButtonClick(View view){
        resetGoodses();
    }
    private class MyListenerForGoodsSelectAdapter implements GoodsSelectAdapter4.OnItemClickListener {
        @Override
        public void onItemClick(View view) {
           // ViewGoodsSelectBinding binding = weakBinding.get();
           // Cart cart = weakCart.get();
            ViewGoods4Binding _binding = DataBindingUtil.getBinding(view);
            Goods goods = _binding.getGoods();
            cart.increaseGoods(goods,1);
            _binding.setGoods(goods);
            binding.goodsSelectTotalPrice.setText(cart.getTotalTienPhaiTraInfo());
            MyUtility.jumpViewAnimation(binding.goodsSelectTotalPrice);
        }

        @Override
        public void letRemoveGoodsFromCart(View view) {
            ViewGoods4Binding _binding = DataBindingUtil.getBinding(view);
            Goods goods = _binding.getGoods();
            cart.setGoodsesAmount(goods.getGoods_id(),0);

            binding.goodsSelectTotalPrice.setText(cart.getTotalTienPhaiTraInfo());
            MyUtility.jumpViewAnimation(binding.goodsSelectTotalPrice);
        }

        @Override
        public void onSubtractIconClick(View view) {
           // ViewGoodsSelectBinding binding = weakBinding.get();
          //  Cart cart = weakCart.get();
            ViewGoods4Binding _binding = DataBindingUtil.getBinding(view);
            Goods goods = _binding.getGoods();
            cart.decreaseGoods(goods,1);
            _binding.setGoods(goods);
            binding.goodsSelectTotalPrice.setText(cart.getTotalTienPhaiTraInfo());

            MyUtility.jumpViewAnimation(binding.goodsSelectTotalPrice);
        }
    }
    public void startCoolAnimation(){
        int cx = MyUtility.getScreenWidth() /2;
        int cy = MyUtility.getScreenHeight() - binding.goodsSelectDinhIcon.getHeight();
        float radius = (float) Math.hypot(cx,cy);

        binding.goodsSelectRingIcon.setPivotY(binding.goodsSelectRingIcon.getHeight());

        final Animator last_animator = ViewAnimationUtils.createCircularReveal(binding.getRoot(),cx,cy,0,radius).setDuration(500);
        last_animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                binding.goodsSelectRingIcon.setVisibility(View.GONE);
                binding.goodsSelectDinhIcon.setVisibility(View.GONE);
                binding.goodsSelectBody.setVisibility(View.VISIBLE);
                binding.goodsSelectAppbar.setVisibility(View.VISIBLE);
            }

        });
        float height = MyUtility.getScreenHeight() - binding.goodsSelectRingIcon.getBottom();
        float sai_so = binding.goodsSelectRingIcon.getHeight() / 4  * 1.11f;

        Animator first_animator1 = ObjectAnimator.ofFloat(binding.goodsSelectRingIcon,"translationY",0,-200).setDuration(400);
        Animator first_animator2 = ObjectAnimator.ofFloat(binding.goodsSelectRingIcon,"translationY",-200,height - sai_so).setDuration(400);
        Animator first_animator31 = ObjectAnimator.ofFloat(binding.goodsSelectRingIcon,"scaleX",1,2,1).setDuration(400);
        final Animator first_animator32 = ObjectAnimator.ofFloat(binding.goodsSelectRingIcon,"scaleY",1,0.5f,1).setDuration(400);
        Animator first_animator4 = ObjectAnimator.ofFloat(binding.goodsSelectRingIcon,"translationY",height - sai_so,height - 400).setDuration(400);
        Animator first_animator5 = ObjectAnimator.ofFloat(binding.goodsSelectRingIcon,"translationY",height - 400,height - binding.goodsSelectDinhIcon.getHeight()).setDuration(600);

        first_animator1.setInterpolator(new DecelerateInterpolator());
        first_animator2.setInterpolator(new AccelerateInterpolator());
        first_animator31.setInterpolator(new AccelerateDecelerateInterpolator());
        first_animator32.setInterpolator(new AccelerateDecelerateInterpolator());
        first_animator4.setInterpolator(new AccelerateDecelerateInterpolator());
        first_animator5.setInterpolator(new DecelerateInterpolator());

        first_animator31.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                first_animator32.start();
            }
        });

        first_animator4.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.goodsSelectDinhIcon.setVisibility(View.VISIBLE);
            }
        });

        AnimatorSet first_animatorSet = new AnimatorSet();
        first_animatorSet.playSequentially(first_animator1,first_animator2,first_animator31/*,first_animator32*/,first_animator4,first_animator5,last_animator);

        first_animatorSet.start();
    }
}

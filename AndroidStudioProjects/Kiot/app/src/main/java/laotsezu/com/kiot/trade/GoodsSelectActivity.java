package laotsezu.com.kiot.trade;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
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
import laotsezu.com.kiot.resources.MyAnimatorFactory;
import laotsezu.com.kiot.resources.MyUtility;
import laotsezu.com.kiot.utilities.GoodsSelectAdapter4;
import laotsezu.com.kiot.R;
import laotsezu.com.kiot.goods.Goods;
import laotsezu.com.kiot.utilities.GoodsSearchEditText;
import laotsezu.com.kiot.resources.ResizeAnimation;
import laotsezu.com.kiot.utilities.StartAndLimit;

public class GoodsSelectActivity extends AppCompatActivity implements Goods.OnLoadGoodsListener,Cart.OnCartListener {
    ViewGoodsSelectBinding binding;
    GoodsSelectAdapter4 adapter;
    static String TAG = "GoodsSelectActivity: ";
    Personnel personnel;
    boolean input_keyboard_visible = false;
    boolean isResetingCart = false;
    boolean isLoadingMoreGoods = false;

    Cart cart;
    String customer_id = Customer.getDefaultId();
    String customer_ten = Customer.getDefaultTen();
    public static int SELECT_CUSTOMER = 1234,SELECT_PRICE_TABLE = 1235,TO_CART_BROWSE = 1236;

    float origin_addtionInfo_size;
    float origin_paymentNavigator_size;

    private long mLastBackPress = 0;
    int TOAST_DURATION = 1000;
    Toast onBackPressedToast;

    int moreGoodsJump = 12;
    Toast noticeToast;

    static int ADAPTER_CHILD_IN_ONE_ROW = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.view_goods_select);

        noticeToast = new Toast(this);

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

            Goods.loadGoods(new StartAndLimit(0,24),GoodsSelectActivity.this);

            initLeftDrawer();
        }
        catch (Exception e){
            noticeToast = Toast.makeText(GoodsSelectActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
            noticeToast.show();
            finish();
        }
        //

    }
    public void loadMoreGoods(){
        if(adapter != null) {
            synchronized (this) {
                if(!isLoadingMoreGoods) {
                    isLoadingMoreGoods = true;
                    Log.e(TAG,"Start Load More Goods");
                    StartAndLimit startAndLimit = new StartAndLimit(adapter.getTotalListCount(), adapter.getTotalListCount() + moreGoodsJump);
                    MyListenerForLoadMoreGoods listener = new MyListenerForLoadMoreGoods(this);
                    Goods.loadGoods(startAndLimit, listener);
                }
            }
        }
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
            noticeToast.cancel();
            noticeToast = Toast.makeText(GoodsSelectActivity.this, "Cart is empty, add some goods please!", Toast.LENGTH_SHORT);
            noticeToast.show();
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
        noticeToast.cancel();
        noticeToast = Toast.makeText(GoodsSelectActivity.this, "Loading...", Toast.LENGTH_SHORT);
        noticeToast.show();
        binding.goodsSelectProgressbar.setVisibility(View.VISIBLE);
    }
    @Override
    public void onLoadGoodsSucessful(JSONArray goods_array) {

        origin_addtionInfo_size = binding.goodsSelectAdditionalInfo.getHeight();
        origin_paymentNavigator_size = binding.goodsSelectPaymentNavigator.getHeight();

        binding.goodsSelectProgressbar.setVisibility(View.GONE);

        List<Goods> list = new LinkedList<>();
        for(int i =0; i < goods_array.length() ; i++){
            try {
                JSONObject item = goods_array.getJSONObject(i);
                Goods goods = new Goods(item);
                goods.addPropertiy("position" ,String.valueOf(i));
                list.add(goods);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG,e.getMessage());
            }
        }

        adapter = new GoodsSelectAdapter4(list, new MyListenerForGoodsSelectAdapter(this));
        cart = new Cart();

        /////////
        binding.goodsSelectGoodsList.setAdapter(adapter);
        binding.goodsSelectGoodsList.setLayoutManager(new StaggeredGridLayoutManager(ADAPTER_CHILD_IN_ONE_ROW,StaggeredGridLayoutManager.VERTICAL));
        //////
        binding.goodsSelectSearchText.setOnEditTextListener(new GoodsSearchEditText.OnEditTextListener() {
            @Override
            public void onEditTextBackPressed() {
                showSomeComponent();
                binding.goodsSelectSearchText.setText("");
            }
        });
        binding.goodsSelectSearchText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {adapter.getFilter().filter(s);}
            public void afterTextChanged(Editable s) {}
        });
        binding.goodsSelectSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    showSomeComponent();
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
        binding.goodsSelectGoodsList.addOnScrollListener(new MyListenerForRecyclerViewToBottom(this));
    }
    @Override
    public void onLoadGoodsFailed(String message) {
        noticeToast.cancel();
        noticeToast = Toast.makeText(GoodsSelectActivity.this, "Load Goods Failed : " + message, Toast.LENGTH_SHORT);
        noticeToast.show();
        binding.goodsSelectProgressbar.setVisibility(View.GONE);
        binding.goodsSelectLoadfailedContainer.setVisibility(View.VISIBLE);
    }
    public void onRetryButtonClick(View v){
        binding.goodsSelectLoadfailedContainer.setVisibility(View.GONE);
        Goods.loadGoods(new StartAndLimit(0,24),this);
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
        synchronized (this){
            if(!isResetingCart){
                isResetingCart = true;
                final long current_total_count = cart.getGoodsCount();
                final long current_total_prince = cart.getTotalTienPhaiTra();

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
                        else{
                            isResetingCart = false;
                        }
                    }
                });

                Toast.makeText(this, "Xóa thành công dữ liệu giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void naviToCartBrowseActivity(){
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
    private static class MyListenerForLoadMoreGoods implements Goods.OnLoadGoodsListener{
        WeakReference<GoodsSelectActivity> weakReference;
        public MyListenerForLoadMoreGoods(GoodsSelectActivity activity){
            weakReference = new WeakReference<>(activity);
        }
        @Override
        public void onPreLoadGoods() {
            final GoodsSelectActivity activity = weakReference.get();

            activity.noticeToast.cancel();
            activity.noticeToast = Toast.makeText(activity, "On Preload More GOodses!", Toast.LENGTH_LONG);
            activity.noticeToast.show();

            //MyAnimatorFactory.visibleProgressbarLoadMore(activity.binding);
        }

        @Override
        public void onLoadGoodsSucessful(JSONArray goods_array) {
            List<Goods> more_goods_list = new LinkedList<>();
            for(int i = 0 ; i < goods_array.length() ; i++){
                try {
                    JSONObject goods_data = goods_array.getJSONObject(i);
                    Goods goods = new Goods(goods_data);
                    more_goods_list.add(goods);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG,e.getMessage());
                }
            }

            GoodsSelectActivity activity = weakReference.get();
            activity.adapter.addMoreGoodses(more_goods_list);
            activity.isLoadingMoreGoods = false;

           // MyAnimatorFactory.goneProgressbarLoadMore(activity.binding);
        }

        @Override
        public void onLoadGoodsFailed(String message) {
            Log.e(TAG,message);

            GoodsSelectActivity activity = weakReference.get();

          //  activity.isLoadingMoreGoods = false;

            activity.noticeToast.cancel();
            activity.noticeToast = Toast.makeText(activity,message,Toast.LENGTH_LONG);
            activity.noticeToast.show();
        }
    }
    private static class MyListenerForRecyclerViewToBottom extends RecyclerView.OnScrollListener{
        WeakReference<GoodsSelectActivity> weakReference;
        public MyListenerForRecyclerViewToBottom(GoodsSelectActivity activity){
            weakReference = new WeakReference<>(activity);
        }
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            int visibleChildCount = manager.getChildCount();
            int totalChildCount = manager.getItemCount();
            int wasVisibleChildCount = manager.findFirstVisibleItemPositions(null)[0];

            if(visibleChildCount + wasVisibleChildCount + 2 * ADAPTER_CHILD_IN_ONE_ROW >= totalChildCount){
                weakReference.get().loadMoreGoods();
            }

        }
    }
    private static class MyListenerForGoodsSelectAdapter implements GoodsSelectAdapter4.OnItemClickListener {
        WeakReference<GoodsSelectActivity> weakReference;
        MyListenerForGoodsSelectAdapter(GoodsSelectActivity activity){
            weakReference = new WeakReference<>(activity);
        }
        @Override
        public void onItemClick(View view) {
            GoodsSelectActivity activity = weakReference.get();
            Cart cart = activity.cart;
            ViewGoodsSelectBinding binding = activity.binding;

            ViewGoods4Binding _binding = DataBindingUtil.getBinding(view);
            Goods goods = _binding.getGoods();
            cart.increaseGoods(goods,1);
            _binding.setGoods(goods);
            binding.goodsSelectTotalPrice.setText(cart.getTotalTienPhaiTraInfo());
            MyUtility.jumpViewAnimation(binding.goodsSelectTotalPrice);
        }

        @Override
        public void letRemoveGoodsFromCart(View view) {
            GoodsSelectActivity activity = weakReference.get();
            Cart cart = activity.cart;
            ViewGoodsSelectBinding binding = activity.binding;

            ViewGoods4Binding _binding = DataBindingUtil.getBinding(view);
            Goods goods = _binding.getGoods();
            cart.setGoodsesAmount(goods.getGoods_id(),0);

            binding.goodsSelectTotalPrice.setText(cart.getTotalTienPhaiTraInfo());
            MyUtility.jumpViewAnimation(binding.goodsSelectTotalPrice);
        }

        @Override
        public void onSubtractIconClick(View view) {
            GoodsSelectActivity activity = weakReference.get();
            Cart cart = activity.cart;
            ViewGoodsSelectBinding binding = activity.binding;

            ViewGoods4Binding _binding = DataBindingUtil.getBinding(view);
            Goods goods = _binding.getGoods();
            cart.decreaseGoods(goods,1);
            _binding.setGoods(goods);
            binding.goodsSelectTotalPrice.setText(cart.getTotalTienPhaiTraInfo());

            MyUtility.jumpViewAnimation(binding.goodsSelectTotalPrice);
        }
    }
    public void startCoolAnimation(){
        MyAnimatorFactory.startCoolAnimationForGoodsSelectActivity(binding);
    }
}

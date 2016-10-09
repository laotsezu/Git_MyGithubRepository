package laotsezu.com.kiot.trade;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
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
import laotsezu.com.kiot.databinding.ViewPaymentBinding;
import laotsezu.com.kiot.goods.Goods;
import laotsezu.com.kiot.partner.Customer;
import laotsezu.com.kiot.personnel.PersonnelLoginActivity;
import laotsezu.com.kiot.resources.MyUtility;

public class PaymentActivity extends AppCompatActivity implements Bill.OnBillPaymentListener{
    public static String TAG = "PaymentActivity: ";
    ViewPaymentBinding binding;

    String customer_id;
    String agency_id;
    String personnel_id;
    String current_goods_ids;
    long bill_tong_tien_hang;

    long bill_tien_da_tra = 0;
    long bill_giam_gia;
    long bill_tien_can_tra;
    String bill_phuong_thuc;

    boolean isPaymenting = false;

    Toast isPaymentingToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.view_payment);

        if(getIntentData()){
            init();
        }
    }
    public void onPaymentButtonClick(View v){
        synchronized (this) {
            if(!isPaymenting) {
                isPaymenting = true;

                String tien_thua_info = String.valueOf(binding.billTienTraKhachText.getText());
                String tien_da_tra_info = String.valueOf(binding.billTienDaTra.getText());
               // long tien_thua = bill_tien_can_tra - bill_tien_da_tra;

                if(tien_da_tra_info == null || tien_da_tra_info.length() <= 0 || tien_da_tra_info.equals("0") || tien_thua_info.contains("-")){
                    if(customer_id.equalsIgnoreCase(Customer.getDefaultId())) {
                        Toast.makeText(this, "Khách chưa trả đủ tiền!", Toast.LENGTH_SHORT).show();
                        isPaymenting = false;
                    }
                    else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Chấp nhận thanh toán, Trừ tiền nợ vào tài khoản?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postPayment(bill_tien_da_tra);
                            }
                        });
                        builder.setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postPayment(bill_tien_can_tra);
                            }
                        });
                        builder.setNeutralButton("Quay lại",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                isPaymenting = false;
                            }
                        });
                        builder.create().show();
                    }
                }
                else {
                    if(tien_thua_info.length() > 4 && !customer_id.equalsIgnoreCase(Customer.getDefaultId())) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Chấp nhận thanh toán, Cộng tiền thừa vào tài khoản?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postPayment(bill_tien_da_tra);
                            }
                        });
                        builder.setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postPayment(bill_tien_can_tra);
                            }
                        });
                        builder.setNeutralButton("Quay lại", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                isPaymenting = false;
                            }
                        });
                        builder.create().show();
                    }
                    else{
                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Chấp nhận thanh toán?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postPayment(bill_tien_can_tra);
                            }
                        });
                        builder.setNeutralButton("Quay lại", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                isPaymenting = false;
                            }
                        });
                        builder.create().show();
                    }
                }
                ///

                ///
            }
        }
    }
    public void postPayment(long bill_tien_da_tra){
        bill_phuong_thuc = String.valueOf(binding.billPhuongThucText.getText());
        bill_tien_da_tra = Long.parseLong(String.valueOf(binding.billTienDaTra.getText()));

        NewBillInfo info = new NewBillInfo(customer_id, agency_id, personnel_id, current_goods_ids, bill_phuong_thuc, bill_tong_tien_hang, bill_tien_da_tra, bill_giam_gia);
        Bill.payment(info, this);
    }
    public void init(){
        binding.billTongTienText.setText(Goods.toVietNameMoneyFormat(bill_tong_tien_hang));
        binding.billGiamGiaText.setText(Goods.toVietNameMoneyFormat(bill_giam_gia));
        binding.billTienCanTra.setText(Goods.toVietNameMoneyFormat(bill_tien_can_tra));
        binding.billTienDaTra.setText("0");
        binding.billTienTraKhachText.setText(Goods.toVietNameMoneyFormat(bill_tien_da_tra - bill_tien_can_tra));
        //
        binding.billTienDaTra.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ////
                if(s == null || s.length() <= 0) {
                    bill_tien_da_tra = 0;
                }
                else{
                    bill_tien_da_tra = Long.parseLong(String.valueOf(s));
                    long tien_thua = bill_tien_da_tra - bill_tien_can_tra;
                    if(tien_thua >= 0) {
                        binding.billTienTraKhachText.setText(Goods.toVietNameMoneyFormat(tien_thua));
                    }
                    else{
                        String tien_thua_info = "-" + Goods.toVietNameMoneyFormat(Math.abs(tien_thua));
                        binding.billTienTraKhachText.setText(tien_thua_info);
                    }
                }
            }
            public void afterTextChanged(Editable s) {}
        });
        binding.billTienDaTra.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    if(bill_tien_da_tra == 0){
                        binding.billTienDaTra.setText("0");
                    }
                }
                return false;
            }
        });
    }
    public boolean getIntentData(){
        Bundle data;
        if(getIntent() != null &&(data = getIntent().getExtras()) != null){

            customer_id = data.getString("customer_id");
            agency_id = data.getString("agency_id");
            personnel_id =  data.getString("personnel_id");
            current_goods_ids =  data.getString("current_goods_ids");
            bill_tong_tien_hang =  data.getLong("bill_tong_tien_hang");
            bill_giam_gia =  data.getLong("bill_giam_gia");
            bill_tien_can_tra = bill_tong_tien_hang - bill_giam_gia;

            return true;
        }
        return false;
    }
    public void onBackButtonClick(View v){
        supportFinishAfterTransition();
    }

    @Override
    public void onPreBillPayment() {
        isPaymentingToast = Toast.makeText(PaymentActivity.this,"Paymenting...",Toast.LENGTH_LONG);
        isPaymentingToast.show();

    }

    @Override
    public void onPaymentFailed(String message) {
        isPaymentingToast.cancel();
        isPaymentingToast = Toast.makeText(PaymentActivity.this,TAG + message, Toast.LENGTH_SHORT);
        isPaymentingToast.show();
        isPaymenting = false;
    }

    @Override
    public void onPaymentSucessful() {
        isPaymentingToast.cancel();
        isPaymentingToast = Toast.makeText(PaymentActivity.this, TAG + "Payment Sucessful!", Toast.LENGTH_LONG);
        isPaymentingToast.show();
        Intent intent = new Intent(this,PersonnelLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

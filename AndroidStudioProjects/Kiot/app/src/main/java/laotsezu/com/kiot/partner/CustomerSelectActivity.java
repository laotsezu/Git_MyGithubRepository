package laotsezu.com.kiot.partner;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

import laotsezu.com.kiot.R;
import laotsezu.com.kiot.databinding.CardCustomerBinding;
import laotsezu.com.kiot.databinding.ViewSelectCustomerBinding;
import laotsezu.com.kiot.resources.MyUtility;
import laotsezu.com.kiot.utilities.CustomerSelectAdapter;
import laotsezu.com.kiot.utilities.CustomerSelectAdapter2;

public class CustomerSelectActivity extends AppCompatActivity implements Customer.OnLoadCustomerListener{
    static String TAG = "CustomerSelectActivity: ";
    ViewSelectCustomerBinding binding;
    String customer_id = Customer.getDefaultId();
    String customer_ten = Customer.getDefaultTen();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.view_select_customer);

        if(getIntentData()){
            Customer.loadCustomers(this);
        }

    }
    public boolean getIntentData(){
        Bundle data;
        if(getIntent() != null && (data = getIntent().getExtras())!= null){
            customer_id = data.getString("customer_id",customer_id);
            customer_ten = data.getString("customer_ten",customer_ten);
            return true;
        }
        return false;
    }
    @Override
    public void onLoadCustomerStart() {
        binding.selectCustomerProgressbar.setVisibility(View.VISIBLE);
    }
    public void onAddCustomerIconClick(View v){
        Intent intent = new Intent(this,CustomerAddActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent,45345);
    }
    @Override
    public void onLoadCustomerFailed(String message) {
        Toast.makeText(CustomerSelectActivity.this,TAG + message, Toast.LENGTH_SHORT).show();
        binding.selectCustomerProgressbar.setVisibility(View.INVISIBLE);
        binding.selectCustomerLoadfailedContainer.setVisibility(View.VISIBLE);
    }
    public void onRetryButtonClick(View v){
        binding.selectCustomerLoadfailedContainer.setVisibility(View.GONE);
        Customer.loadCustomers(this);
    }
    @Override
    public void onLoadCustomerSuccessful(JSONArray items) {
        binding.selectCustomerProgressbar.setVisibility(View.INVISIBLE);
        List<Customer> list = new LinkedList<>();
        try {
            for(int i = 0;i<items.length(); i++){
                list.add(new Customer(items.getJSONObject(i)));
            }

            final CustomerSelectAdapter2 adapter = new CustomerSelectAdapter2(list, new CustomerSelectAdapter2.OnCustomerSelectAdapterListener() {
                @Override
                public void onSelectIconClick(View v) {
                    CardCustomerBinding binding = DataBindingUtil.getBinding(v);
                    Intent intent = new Intent();
                    Customer customer = binding.getCustomer();
                    intent.putExtra("customer_id",customer.getCustomer_id());
                    intent.putExtra("customer_ten",customer.getCustomer_ten());
                    setResult(RESULT_OK,intent);
                    finish();
                }

                @Override
                public String getCurrentCustomer_id() {
                    return customer_id;
                }

                public void onDetailIconClick(View v){
                    Toast.makeText(CustomerSelectActivity.this, "Hiển thị thông tin khách hàng!", Toast.LENGTH_SHORT).show();
                }
            });
            binding.selectCustomerList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            binding.selectCustomerList.setAdapter(adapter);
            ////
            MyUtility.setSwipeRefreshLayoutColor(binding.selectCustomerListContainer,getResources());
            binding.selectCustomerListContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    adapter.notifyDataSetChanged();
                    binding.selectCustomerListContainer.setRefreshing(false);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null && data.getExtras() != null){
            Bundle data_bundle = data.getExtras();
            if(data_bundle.getBoolean("status")){
                Customer.loadCustomers(this);
            }
        }
    }

}

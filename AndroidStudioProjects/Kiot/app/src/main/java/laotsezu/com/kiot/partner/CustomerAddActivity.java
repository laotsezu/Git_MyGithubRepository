package laotsezu.com.kiot.partner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import laotsezu.com.kiot.R;

public class CustomerAddActivity extends AppCompatActivity implements Customer.OnAddCustomerListener{
    Spinner customer_loai;
    EditText customer_id;
    EditText customer_ten;
    Spinner customer_gioi_tinh;
    TextView customer_ngay_sinh;
    Spinner customer_nhom;
    EditText customer_sdt;
    EditText customer_email;

    boolean isAdding = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_add_customer);

        customer_loai = (Spinner) findViewById(R.id.customer_loai);
        customer_id = (EditText) findViewById(R.id.customer_id);
        customer_ten = (EditText) findViewById(R.id.customer_ten);
        customer_gioi_tinh = (Spinner) findViewById(R.id.customer_gioi_tinh);
        customer_ngay_sinh = (TextView) findViewById(R.id.customer_ngay_sinh);
        customer_nhom = (Spinner) findViewById(R.id.customer_nhom);
        customer_sdt = (EditText) findViewById(R.id.customer_sdt);
        customer_email = (EditText) findViewById(R.id.customer_email);

        /////adapter

        ArrayAdapter sex_adapter = ArrayAdapter.createFromResource(this,R.array.sex,android.R.layout.simple_spinner_item);
        sex_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customer_gioi_tinh.setAdapter(sex_adapter);

        ArrayAdapter group_adapter = ArrayAdapter.createFromResource(this,R.array.customer_group,android.R.layout.simple_spinner_item);
        group_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customer_nhom.setAdapter(group_adapter);

        ArrayAdapter type_adapter = ArrayAdapter.createFromResource(this,R.array.customer_type,android.R.layout.simple_spinner_item);
        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customer_loai.setAdapter(type_adapter);



    }
    public void onAddCustomerClick(View v){
        synchronized (this){
            if(!isAdding){
                isAdding = true;

                Customer_Add_Info info = new Customer_Add_Info(
                        String.valueOf(customer_loai.getSelectedItem().toString())
                        ,String.valueOf(customer_id.getText())
                        ,String.valueOf(customer_ten.getText())
                        ,String.valueOf(customer_gioi_tinh.getSelectedItem().toString())
                        ,String.valueOf(customer_ngay_sinh.getText())
                        ,String.valueOf(customer_nhom.getSelectedItem().toString())
                        ,String.valueOf(customer_sdt.getText())
                        ,String.valueOf(customer_email.getText())
                );

                Customer.addCustomer(info,this);
            }
        }
    }

    @Override
    public void onAddCustomerSuccessful() {
        Toast.makeText(this,"Add Customer Successful",Toast.LENGTH_LONG).show();

        Intent intent = new Intent();
        intent.putExtra("status",true);
        setResult(RESULT_OK,intent);

        finish();
    }

    @Override
    public void onAddCustomerFailed(String message) {
        Toast.makeText(this,"Add Customer Failed," + message,Toast.LENGTH_LONG).show();
        isAdding = true;


    }

    @Override
    public void onAddCustomerStart() {

    }

    public void onBackButtonClick(View v){
        finish();
    }
}

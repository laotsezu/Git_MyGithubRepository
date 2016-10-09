package laotsezu.com.kiot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import laotsezu.com.kiot.partner.CustomerAddActivity;
import laotsezu.com.kiot.personnel.PersonnelLoginActivity;
import laotsezu.com.kiot.trade.CartBrowseActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        onLoginClick(new View(this));
    }
    public void onAddCustomerClick(View v){
        Intent intent = new Intent(this, CustomerAddActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void onCartClick(View v){
        Intent intent = new Intent(this, CartBrowseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void onPaymentClick(View v){
        setContentView(R.layout.view_payment);
    }
    public void onLoginClick(View v){
        Intent intent = new Intent(this, PersonnelLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

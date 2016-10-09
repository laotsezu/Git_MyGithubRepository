package laotsezu.com.kiot.personnel;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import laotsezu.com.kiot.R;
import laotsezu.com.kiot.databinding.ViewPersonnelActionBinding;
import laotsezu.com.kiot.trade.GoodsSelectActivity;

public class PersonnelActionActivity extends AppCompatActivity{
    Personnel personnel;
    ViewPersonnelActionBinding binding;
    ActionBarDrawerToggle drawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.view_personnel_action);

        setSupportActionBar(binding.toolbar);

        try {
            Bundle extra = getIntent().getExtras();
            Bundle personnel_data = extra.getBundle("personnel_data");
            personnel = new Personnel(personnel_data);

            init();
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void init(){
        initDrawerToggle();
    }
    public void initDrawerToggle(){
        drawerToggle = new ActionBarDrawerToggle(
                this
                ,binding.personnelActionDrawerContainer
                ,binding.toolbar
                ,R.string.drawer_open
                ,R.string.drawer_close
        ){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
            }
        };

        binding.personnelActionDrawerContainer.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }


    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public void naviToGoodsSelectActivity(){
        Intent intent = new Intent(this, GoodsSelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("personnel_ten",personnel.getPersonnel_ten());
        intent.putExtra("personnel_id",personnel.getPersonnel_id());
        startActivity(intent);
    }

}

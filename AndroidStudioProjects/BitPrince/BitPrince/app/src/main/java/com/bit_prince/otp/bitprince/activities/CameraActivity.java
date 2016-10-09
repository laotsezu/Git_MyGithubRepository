package com.bit_prince.otp.bitprince.activities;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.bit_prince.otp.bitprince.tasks.GetLoginCodeTask;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CameraActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler,GetLoginCodeTask.OnGetTempCodeTaskListener{
    ZXingScannerView scannerView;
    String TAG = "Cameraa Activity: ";
    static int REQUEST_PERMISSION_INT = 5154;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkAndRequestPermission();
        }
        else{
            startCamera();
        }
    }
    public void startCamera(){
        scannerView = new ZXingScannerView(this);
        scannerView.setResultHandler(this);
        setContentView(scannerView);
    }
    public void checkAndRequestPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_PHONE_STATE}, REQUEST_PERMISSION_INT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION_INT){
            startCamera();
        }
    }

    @Override
    public void handleResult(Result result) {
        String qrCode = result.getText();
        String deviceToken = getDeviceToken();
        String deviceName = Build.MODEL;


        Log.e("main Activity: ","Qr Code = " + qrCode);
        Log.e("main Activity: ","Device Token = " + deviceToken);
        Log.e("main Activity: ","Device Name = " + deviceName);

        if(coMang()) {
            GetLoginCodeTask task = new GetLoginCodeTask(this);
            task.execute(deviceToken,deviceName,qrCode);
        }
        else{
            Toast.makeText(this,"Khong Co Mang", Toast.LENGTH_LONG).show();
        }
    }
    public String getDeviceToken(){
        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }
    public boolean coMang(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isAvailable() && manager.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onGetTempCodeTaskComplete(String[] result) {
        SharedPreferences.Editor editor = getApplication().getSharedPreferences(MainActivity.PRIVATE_KEY,MODE_PRIVATE).edit();
        editor.putBoolean("isLogin",true);
        editor.putString("log_code",result[0]);
        editor.putString("user_name",result[1]);
        editor.apply();

        Log.e(TAG,"Start Finish Came Ra Activity becau Complete!");

        Intent intent = new Intent();

        intent.putExtra("isLogin",true);
        intent.putExtra("log_code",result[0]);
        intent.putExtra("user_name",result[1]);

        setResult(RESULT_OK,intent);

        finish();
    }

    @Override
    public void onGetTempCodeTaskErr(String err) {
        Toast.makeText(this,err,Toast.LENGTH_LONG).show();

        Log.e(TAG,"Start Finish Came Ra Activity because Failed!");
        Toast.makeText(this,"Get Logging Code Err: ",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();

        intent.putExtra("isLogin",false);

        setResult(RESULT_OK,intent);

        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(scannerView != null){
            scannerView.startCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(scannerView != null){
            scannerView.stopCamera();
        }
    }


}

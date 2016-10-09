package com.bit_prince.otp.bitprince.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bit_prince.otp.bitprince.R;
import com.bit_prince.otp.bitprince.tasks.GetOptCodeTask;
import com.bit_prince.otp.bitprince.tasks.LogoutTask;

public class MainActivity extends AppCompatActivity implements GetOptCodeTask.OnGetOptAsyncTaskListener,LogoutTask.OnLogoutTask{
    static String TAG = "Main Activity: ";
    public final static String PRIVATE_KEY = "laotsezu";
    SharedPreferences sharedPreferences;
    User user;
    String user_name;
    String log_code;
    /////Phan Nay` cua log activity
    ImageView scanButton;

    ///Phan nay cua get otp activity
    ImageView short_icon;
    ProgressBar progressBar;
    TextView textView_user_name;
    TextView textView_otp_code;
    TextView textView_log_out;
    String preOtpCode;
    String null_code;
    boolean getOtpEnable = true;
    boolean progressBarEnableRunning = false;

    boolean lauch = false;

    @Override
    public void onLogoutComplete() {
        sharedPreferences.edit().clear().apply();
        startGetLogCodeActivity();
    }
///97f19dbc456c649ae100855d62de5038
    @Override
    public void onLogoutErr() {
       Toast.makeText(this,"Không Đăng Xuất Được",Toast.LENGTH_SHORT).show();
    }

    public static class User{
        static String[] fields = {"isLogin","user_name","log_code"};
        boolean isLogin;
        String user_name;
        String log_code;
        public User(boolean isLogin,String user_name,String log_code){
            this.isLogin = isLogin;
            this.user_name = user_name;
            this.log_code = log_code;
        }

        public String getLog_code() {
            return log_code;
        }

        public String getUser_name() {
            return user_name;
        }

        public boolean isLogin() {
            return isLogin;
        }
    }

    public void setGetOtpEnable(boolean getOtpEnable) {
        synchronized (this){
            this.getOtpEnable = getOtpEnable;
        }
    }

    public boolean isGetOtpEnable() {
        synchronized (this){
            return getOtpEnable;
        }
    }

    public static class OtpCode{
        String code;
        long expire_time;
        long start_time;
        public OtpCode(String code,Long start_time,Long expire_time){
            this.code = code;
            this.expire_time = expire_time;
            this.start_time = start_time;
        }

        public long getExpire_time() {
            return expire_time;
        }

        public long getStart_time() {
            return start_time;
        }

        public String getCode() {
            return code;
        }
    }
    public void startAlphaAnimation(){

        AlphaAnimation animation = new AlphaAnimation(0,1);
        animation.setDuration(1500);

        findViewById(R.id.root_view).startAnimation(animation);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!lauch) {
            lauch = true;
            setContentView(R.layout.activity_lauch);
            VideoView videoView = (VideoView) findViewById(R.id.m_video_view);
            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bit1));
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    startApp();
                }
            });
            videoView.start();
        }
        else {
            startApp();
        }
        ////Actiivty nay` se chu nhiem 2 view do la Login va Get Otp
        ///Truoc het kiem tra xem da login chua

    }
    public void startApp(){
        sharedPreferences = getSharedPreferences(PRIVATE_KEY,MODE_PRIVATE);

        if(sharedPreferences.getBoolean("isLogin",false)){
            user_name = sharedPreferences.getString("user_name",null);
            log_code = sharedPreferences.getString("log_code",null);

            if(user_name == null || log_code == null)
                startGetLogCodeActivity();
            else {
                startGetOtpCodeActivity(new User(true,user_name,log_code));
            }
        }
        else{
            ///xu ly neu chua dang nhap
            startGetLogCodeActivity();
        }
    }
    public void startGetLogCodeActivity(){
        setContentView(R.layout.activity_login);

        startAlphaAnimation();

        scanButton = (ImageView) findViewById(R.id.button_scan);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CameraActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            Bundle result = data.getExtras();
            if(result.getBoolean("isLogin",false)){
                String user_name = result.getString("user_name");
                String log_code = result.getString("log_code");
                startGetOtpCodeActivity(new User(true,user_name,log_code));
            }
            else{
                Log.e(TAG,"On Activity Result : Is Login = False!");
            }
        }
    }

    public void startGetOtpCodeActivity(final User user){
        this.user = user;
        setContentView(R.layout.activity_get_opt);

        startAlphaAnimation();

        short_icon = (ImageView) findViewById(R.id.imageview_short_icon);
        progressBar = (ProgressBar) findViewById(R.id.imageview_progressbar);
        textView_user_name = (TextView) findViewById(R.id.textview_user_name);
        textView_otp_code = (TextView) findViewById(R.id.textview_otp_code);
        textView_log_out = (TextView) findViewById(R.id.textview_logout);
        null_code = getResources().getString(R.string.null_code);

        textView_user_name.setText(user.getUser_name());
        preOtpCode = sharedPreferences.getString("preOtpCode",null_code);
        textView_otp_code.setText(preOtpCode);
        Log.e(TAG,"Set Pre OtpCode = " + preOtpCode);


        findViewById(R.id.main_block).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isGetOtpEnable()) {
                    setGetOtpEnable(false);
                    GetOptCodeTask task = new GetOptCodeTask(MainActivity.this);
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, user.getLog_code(), getDeviceToken());
                }
                else{
                    Log.e(TAG,"Khong Cho Request vi isGetOtpEnable() tra ve false");
                }
            }
        });

        textView_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LogoutTask task = new LogoutTask(MainActivity.this);
                final String log_code = getApplication().getSharedPreferences(PRIVATE_KEY,MODE_PRIVATE).getString("log_code",null);
                if(log_code != null) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Bạn muốn đăng xuất?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    task.execute(log_code,getDeviceToken());
                                }})
                            .setNegativeButton(android.R.string.no, null).show()

                    ;
                }
                else{
                    Log.e(TAG,"Log Code = Null Thi Phai, Chua co ai dang nhap");
                }
            }
        });

        if(!preOtpCode.equalsIgnoreCase(null_code)){

            startProgress(sharedPreferences.getInt("expire_time",0),sharedPreferences.getLong("start_time",0),preOtpCode);
        }
    }

    public String getDeviceToken(){
        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }


    @Override
    public void onGetOptComplete(final OtpCode otpCode) {
        setGetOtpEnable(true);
        final String currentOtpCode = otpCode.getCode();

        if(!currentOtpCode.equalsIgnoreCase(preOtpCode)) {
            sharedPreferences.edit().putString("preOtpCode",currentOtpCode).apply();

            preOtpCode = currentOtpCode;

            final int expire_time = (int) otpCode.getExpire_time();
            final long start_time = otpCode.getStart_time();

            sharedPreferences.edit().putInt("expire_time",expire_time).apply();
            sharedPreferences.edit().putLong("start_time",start_time).apply();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startProgress(expire_time, start_time,currentOtpCode);
                }
            },1200);

        }
    }
    public void startProgress(final int expire_time, final long start_time, final String code){

        final int conlai = (int) ((start_time + expire_time) - System.currentTimeMillis() / 1000);


        short_icon.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        textView_otp_code.setText(code);

        progressBar.setMax(expire_time);
        progressBar.setProgress(conlai);
        Log.e(TAG,"Start Progress With max =  " + expire_time + ",Con Lai = " + conlai + ", Code = " + code);

        final Handler mHandler = new Handler();
        Runnable runnable = new Runnable() {
            int current_time = conlai;
            @Override
            public void run() {
                current_time--;
                Log.e(TAG,"Set Progress = " + current_time);
                if(sharedPreferences.getString("preOtpCode","").equalsIgnoreCase(code)){
                    if(current_time > 0){
                        mHandler.postDelayed(this,1000);
                    }
                    else if(current_time <= 0){
                        textView_otp_code.setText(getResources().getText(R.string.null_code));
                        progressBar.setVisibility(View.INVISIBLE);
                        short_icon.setVisibility(View.VISIBLE);

                        sharedPreferences.edit().remove("preOtpCode").apply();
                    }
                }
                progressBar.setProgress(current_time);
            }
        };

        mHandler.post(runnable);

    }

    public void setProgressBarEnableRunning(boolean progressBarEnableRunning) {
        synchronized (this) {
            this.progressBarEnableRunning = progressBarEnableRunning;
        }
    }

    public boolean isProgressBarEnableRunning() {
        synchronized (this) {
            return progressBarEnableRunning;
        }
    }

    @Override
    public void onGetOptErr(String err) {
        setGetOtpEnable(true);
        Toast.makeText(this,"Hiện tại chưa có otp code.",Toast.LENGTH_SHORT).show();
    }
}


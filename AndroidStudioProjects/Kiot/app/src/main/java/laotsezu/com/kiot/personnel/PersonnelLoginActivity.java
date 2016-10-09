package laotsezu.com.kiot.personnel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Collection;

import laotsezu.com.kiot.R;
import laotsezu.com.kiot.databinding.ViewLoginBinding;
import laotsezu.com.kiot.goods.Goods;
import laotsezu.com.kiot.resources.MyUtility;
import laotsezu.com.kiot.trade.GoodsSelectActivity;
import laotsezu.com.kiot.resources.MyAnimatorFactory;

public class PersonnelLoginActivity extends AppCompatActivity implements Personnel.OnLoginListener{
    static String TAG = "PersonnelLoginActivity";
    Personnel personnel;
    boolean loginable = false;
    ViewLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Personnel.isLogin(this)){
            personnel = Personnel.getLoginData(this);
            naviToGoodsSelectActivity(personnel,null);
        }
        else{
            binding = DataBindingUtil.setContentView(this, R.layout.view_login);

            binding.getRoot().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    binding.getRoot().removeOnLayoutChangeListener(this);
                    visit();
                }
            });

        }
    }
    public void onLoginButtonClick(View v){
        synchronized (this) {

            if(!loginable) {
                loginable = true;
                String tai_khoan = String.valueOf(binding.loginAccountText.getText());
                String mat_khau = String.valueOf(binding.loginPasswordText.getText());

                Personnel.login(tai_khoan, mat_khau, this);
            }
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    public Context getContext() {
        return this;
    }

    public void visit(){

        AnimatorSet set = new AnimatorSet();

        Collection<Animator> collection = MyAnimatorFactory.buildAnimatorCollection(binding.getRoot());
        set.playTogether(collection);

        set.start();
    }
    @Override
    public void onLoginSucessfull(final Personnel personnel) {
        this.personnel = personnel;
        Toast.makeText(this,"Login Successful!",Toast.LENGTH_LONG).show();
        ViewPropertyAnimator animator  = binding.loginLoginProgressbar.animate().scaleX(0).scaleY(0).setDuration(1000).setInterpolator(new AccelerateDecelerateInterpolator());
        final ScaleAnimation mAnimation = new ScaleAnimation(0,1,0,1);
        mAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimation.setDuration(1000);
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.loginLoginChecked.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                final ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(PersonnelLoginActivity.this,binding.loginLoginChecked,"login_checked");
                naviToGoodsSelectActivity(personnel,options);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.loginLoginProgressbar.setVisibility(View.INVISIBLE);

                binding.loginLoginChecked.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_check));
                binding.loginLoginChecked.startAnimation(mAnimation);
            }
        });

        animator.start();


    }
    public void naviToGoodsSelectActivity(Personnel personnel,ActivityOptionsCompat options){
        Log.e(TAG,"Navigate To Goods Select Activity!");
        Intent intent = new Intent(this, GoodsSelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("personnel_data",personnel.getBundle());

        if(options != null)
            startActivity(intent, options.toBundle());
        else{
            startActivity(intent);
        }
    }
    @Override
    public void onLoginFailed(String message) {
        Toast.makeText(this,"Login Failed: " + message,Toast.LENGTH_LONG).show();
        loginable = false;

        binding.loginLoginButton.setAlpha(1F);
        binding.loginLoginProgressbar.setVisibility(View.INVISIBLE);

        binding.loginLoginChecked.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_errordp));

        final ScaleAnimation mAnimation = new ScaleAnimation(0,1,0,1);
        mAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimation.setDuration(1000);

        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.loginLoginChecked.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        binding.loginLoginChecked.startAnimation(mAnimation);
    }

    @Override
    public void onLoginStart() {
        binding.loginLoginButton.setAlpha(0.5F);
        binding.loginLoginProgressbar.setVisibility(View.VISIBLE);
        binding.loginLoginChecked.setVisibility(View.INVISIBLE);
    }
}

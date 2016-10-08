package laotsezu.com.kiot.resources;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import java.util.Set;
import java.util.Vector;

import laotsezu.com.kiot.R;


/**
 * Created by Laotsezu on 9/19/2016.
 */
public class MyUtility {
    public static String SHAREPREFERENCE_ID = "kbfg6bfgbf4gbfg51fg5nf1gn";
    static String TAG = "MyUtility: ";
    public static void setSwipeRefreshLayoutColor(SwipeRefreshLayout layout , Resources resources){
        int orange = resources.getColor(R.color.orange);
        int green = resources.getColor(R.color.green);
        int blue = resources.getColor(R.color.blue);
        layout.setColorSchemeColors(orange,green,blue);
    }
    public static AlertDialog buildWarningDialog(Context context,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cảnh báo");
        builder.setMessage(message);
        builder.setNeutralButton("Tôi hiểu rồi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
       return builder.create();
    }
    public static String calculateMultString(String a, String b){
        return String.valueOf((long)(Float.parseFloat(a) * Float.parseFloat(b)));
    }
    public static String calculateAddString(String a,String b){
        return String.valueOf((long)(Float.parseFloat(a) + Float.parseFloat(b)));
    }
    public static String calculateSubString(String a,String b){
        return String.valueOf((long)(Float.parseFloat(a) - Float.parseFloat(b)));
    }
    public static String calculateDivString(String a,String b){
        return String.valueOf((long)(Float.parseFloat(a) / Float.parseFloat(b)));
    }
    public static int getScreenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    public static int getScreenWidth(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    public static void changeTextViewValueSmoothy(final TextView textView, final String value){
        Animator first_animator = ObjectAnimator.ofFloat(textView,"alpha",1,0).setDuration(500);
        Animator second_animator = ObjectAnimator.ofFloat(textView,"alpha",0,1).setDuration(500);
        first_animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                textView.setText(value);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(first_animator,second_animator);
        animatorSet.start();
    }
    public static void jumpViewAnimation(View v){
        Animator first_animator = ObjectAnimator.ofFloat(v,"translationY",0,-10,0).setDuration(200);
        //Animator second_animator = ObjectAnimator.ofFloat(v,"translationY",-10,0).setDuration(200);

        first_animator.setInterpolator(new DecelerateInterpolator());
        //second_animator.setInterpolator(new DecelerateInterpolator());

        /*AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(first_animator,second_animator);
        animatorSet.start();*/
        first_animator.start();
    }
    public static void slideBottomToTop(View v){
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "translationY", MyUtility.getScreenHeight(), 0);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(400);
        animator.start();
    }
}

package laotsezu.com.kiot.resources;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Vector;

import laotsezu.com.kiot.databinding.ViewGoodsSelectBinding;
import laotsezu.com.kiot.resources.MyUtility;

/**
 * Created by laotsezu on 06/10/2016.
 */

public class MyAnimatorFactory {
    static String TAG = "MyAnimatorFactory: ";
    public static int MAX_WIDTH = MyUtility.getScreenWidth();
    public static int MAX_HEIGHT = MyUtility.getScreenHeight();
    public static Animator buildAnimatorWithRandomPivot(final View v, float radiusFrom, float radiusTo){
        int cx = new Random().nextInt(MAX_WIDTH);
        int cy = new Random().nextInt(MAX_HEIGHT);

        Animator animator = ViewAnimationUtils.createCircularReveal(v,cx,cy,radiusFrom,radiusTo);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(1000);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                v.setVisibility(View.VISIBLE);
            }
        });

        return animator;
    }
    public static Collection<Animator> buildAnimatorCollection(View view){
        Collection<Animator> animators = new Vector<>();
        ArrayList<View> listView = getAllWrapInvisibleView(view);
        for(int i =0; i < listView.size() ; i++){
            View v = listView.get(i);
            animators.add(buildAnimatorWithRandomPivot(v,0,MAX_HEIGHT));
        }

        Log.e(TAG,"Collection Have " + listView.size() + " elements");

        return  animators;
    }
    private static ArrayList<View> getAllWrapInvisibleView(View view){
        ArrayList<View> result = new ArrayList<>();
        if(view.getVisibility() == View.INVISIBLE) {
            result.add(view);
        }
        else if(view instanceof ViewGroup){
            ViewGroup view_group = (ViewGroup) view;
            for(int i = 0; i < view_group.getChildCount() ; i++){
                View view_child = view_group.getChildAt(i);
                result.addAll(getAllWrapInvisibleView(view_child));
            }
        }

        return result;
    }

    /*public static void visibleProgressbarLoadMore(final ViewGoodsSelectBinding binding){
        int duration = 1000;
        ObjectAnimator second_animator = ObjectAnimator.ofFloat(binding.goodsSelectLoadmoreProgressbar,"scaleX",0,1);
        ObjectAnimator three_animator = ObjectAnimator.ofFloat(binding.goodsSelectLoadmoreProgressbar,"scaleY",0,1);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(duration);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                binding.goodsSelectLoadmoreProgressbar.setVisibility(View.VISIBLE);
            }
        });

        animatorSet.playTogether(second_animator,three_animator);
        animatorSet.start();

    }

    public static void goneProgressbarLoadMore(final  ViewGoodsSelectBinding binding){
        int duration = 1000;

        ObjectAnimator second_animator = ObjectAnimator.ofFloat(binding.goodsSelectLoadmoreProgressbar,"scaleX",1,0);
        ObjectAnimator three_animator = ObjectAnimator.ofFloat(binding.goodsSelectLoadmoreProgressbar,"scaleY",1,0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.goodsSelectLoadmoreProgressbar.setVisibility(View.GONE);
            }
        });

        animatorSet.playTogether(second_animator,three_animator);
        animatorSet.start();
    }*/

    public static void startCoolAnimationForGoodsSelectActivity(final ViewGoodsSelectBinding binding){
        int cx = MyUtility.getScreenWidth() /2;
        int cy = MyUtility.getScreenHeight() - binding.goodsSelectDinhIcon.getHeight();
        float radius = (float) Math.hypot(cx,cy);

        binding.goodsSelectRingIcon.setPivotY(binding.goodsSelectRingIcon.getHeight());

        final Animator last_animator = ViewAnimationUtils.createCircularReveal(binding.getRoot(),cx,cy,0,radius).setDuration(500);
        last_animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                binding.goodsSelectRingIcon.setVisibility(View.GONE);
                binding.goodsSelectDinhIcon.setVisibility(View.GONE);
                binding.goodsSelectBody.setVisibility(View.VISIBLE);
                binding.goodsSelectAppbar.setVisibility(View.VISIBLE);
            }

        });
        float height = MyUtility.getScreenHeight() - binding.goodsSelectRingIcon.getBottom();
        float sai_so = binding.goodsSelectRingIcon.getHeight() / 4  * 1.11f;

        Animator first_animator1 = ObjectAnimator.ofFloat(binding.goodsSelectRingIcon,"translationY",0,-200).setDuration(400);
        Animator first_animator2 = ObjectAnimator.ofFloat(binding.goodsSelectRingIcon,"translationY",-200,height - sai_so).setDuration(400);
        Animator first_animator31 = ObjectAnimator.ofFloat(binding.goodsSelectRingIcon,"scaleX",1,2,1).setDuration(400);
        final Animator first_animator32 = ObjectAnimator.ofFloat(binding.goodsSelectRingIcon,"scaleY",1,0.5f,1).setDuration(400);
        Animator first_animator4 = ObjectAnimator.ofFloat(binding.goodsSelectRingIcon,"translationY",height - sai_so,height - 400).setDuration(400);
        Animator first_animator5 = ObjectAnimator.ofFloat(binding.goodsSelectRingIcon,"translationY",height - 400,height - binding.goodsSelectDinhIcon.getHeight()).setDuration(600);

        first_animator1.setInterpolator(new DecelerateInterpolator());
        first_animator2.setInterpolator(new AccelerateInterpolator());
        first_animator31.setInterpolator(new AccelerateDecelerateInterpolator());
        first_animator32.setInterpolator(new AccelerateDecelerateInterpolator());
        first_animator4.setInterpolator(new AccelerateDecelerateInterpolator());
        first_animator5.setInterpolator(new DecelerateInterpolator());

        first_animator31.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                first_animator32.start();
            }
        });

        first_animator4.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.goodsSelectDinhIcon.setVisibility(View.VISIBLE);
            }
        });

        AnimatorSet first_animatorSet = new AnimatorSet();
        first_animatorSet.playSequentially(first_animator1,first_animator2,first_animator31/*,first_animator32*/,first_animator4,first_animator5,last_animator);

        first_animatorSet.start();
    }
}

package laotsezu.com.kiot.resources;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Vector;

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
}

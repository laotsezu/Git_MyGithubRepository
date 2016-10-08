package laotsezu.com.kiot.resources;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

/**
 * Created by laotsezu on 05/10/2016.
 */

public class ResizeAnimation extends Animation {
    private View view;
    private float valueFrom,valueTo;
    private int mDuration = 400;
    public ResizeAnimation(View view , float valueFrom,float valueTo){
        Log.e("Resize Animation: ","Create Resize Animation ValueFrom = " + valueFrom + " to " + valueTo);
        this.view = view;
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
        setDuration(mDuration);
        setInterpolator(new DecelerateInterpolator());
    }
    public void startResize(){
        view.startAnimation(this);
    }
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        Log.e("Resize Animation: ","Apply Transformation!,with interpolated time = " + interpolatedTime);

        float newHeight = valueFrom + (valueTo - valueFrom) * interpolatedTime;

        Log.e("Resize Animation: " ,"New Height = " + newHeight );

        view.getLayoutParams().height = (int) newHeight;

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) newHeight;
        view.setLayoutParams(layoutParams);

      //  view.requestFocus();
    }
}

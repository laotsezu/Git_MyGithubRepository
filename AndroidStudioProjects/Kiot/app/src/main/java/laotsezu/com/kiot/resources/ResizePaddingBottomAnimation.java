package laotsezu.com.kiot.resources;

import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by laotsezu on 10/10/2016.
 */

public class ResizePaddingBottomAnimation extends Animation {
    RecyclerView v;
    float valueStart,valueTo;
    public ResizePaddingBottomAnimation(RecyclerView v,float valueStart,float valueTo){
        this.v =v;
        this.valueStart = valueStart;
        this.valueTo = valueTo;
    }
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
    }
    public void myStart(){
        v.startAnimation(this);
    }
}

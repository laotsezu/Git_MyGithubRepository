package laotsezu.com.kiot.utilities;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by Laotsezu on 9/19/2016.
 */
public class GoodsSearchEditText extends EditText {
    OnEditTextListener listener;
    public interface OnEditTextListener{
        void onEditTextBackPressed();
    }
    public void setOnEditTextListener( OnEditTextListener listener){
        this.listener = listener;
    }

    public GoodsSearchEditText(Context context) {
        super(context);
    }

    public GoodsSearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GoodsSearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            // do your stuff
            if(listener != null){
                listener.onEditTextBackPressed();
            }
        }
        return super.dispatchKeyEvent(event);
    }
}

package laotsezu.com.kiot.utilities;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.textservice.TextInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import laotsezu.com.kiot.R;

/**
 * Created by Laotsezu on 9/22/2016.
 */
public class MyCustomEditText extends EditText {
    static String TAG = "MyCustomEditText: ";
    boolean isFocus = false;
    public MyCustomEditText(Context context) {
        super(context);
        my_custom();
    }

    public MyCustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        my_custom();
    }

    public MyCustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        my_custom();
    }
    public void my_custom(){
        setBackground(getContext().getResources().getDrawable(R.drawable.white_background));
    }
}

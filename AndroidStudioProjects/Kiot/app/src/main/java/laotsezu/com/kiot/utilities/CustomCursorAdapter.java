package laotsezu.com.kiot.utilities;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import laotsezu.com.kiot.R;
import laotsezu.com.kiot.databinding.ViewGoods1Binding;
import laotsezu.com.kiot.goods.Goods;

/**
 * Created by laotsezu on 17/09/2016.
 */
public class CustomCursorAdapter extends CursorAdapter {
    public CustomCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewGoods1Binding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_goods_1,parent,false);
        Goods new_goods = new Goods(cursor);
        binding.setGoods(new_goods);

        return binding.getRoot();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewGoods1Binding binding = DataBindingUtil.getBinding(view);
        Goods new_goods = new Goods(cursor);
        binding.setGoods(new_goods);
    }
}

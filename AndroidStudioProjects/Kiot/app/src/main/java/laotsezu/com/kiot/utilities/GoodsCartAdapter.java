package laotsezu.com.kiot.utilities;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.LinkedList;
import java.util.List;

import laotsezu.com.kiot.R;
import laotsezu.com.kiot.databinding.ViewGoods2Binding;
import laotsezu.com.kiot.goods.Goods;

/**
 * Created by Laotsezu on 9/19/2016.
 */
public class GoodsCartAdapter extends BaseAdapter {
    List<Goods> list = new LinkedList<>();
    public GoodsCartAdapter(List<Goods> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Goods getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewGoods2Binding binding;
        if(convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_goods_2,parent,false);
        }
        else{
            binding = DataBindingUtil.getBinding(convertView);
        }

        binding.setGoods(list.get(position));

        return binding.getRoot();
    }
}

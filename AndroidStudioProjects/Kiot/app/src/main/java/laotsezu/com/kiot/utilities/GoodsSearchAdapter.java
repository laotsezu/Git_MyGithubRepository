package laotsezu.com.kiot.utilities;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.LinkedList;
import java.util.List;

import laotsezu.com.kiot.R;
import laotsezu.com.kiot.databinding.ViewGoods1Binding;
import laotsezu.com.kiot.goods.Goods;
import laotsezu.com.kiot.resources.MyUtility;

/**
 * Created by laotsezu on 16/09/2016.
 */
public class GoodsSearchAdapter extends ArrayAdapter {
    static String TAG = "GoodsSearchAdapter: ";
    List<Goods> total_list = new LinkedList<>();
    List<Goods> current_list = new LinkedList<>();
    MyFilter myFilter ;
    int lastItemTranslationY = -1;
    int itemTranslationYCount = 5;
    public interface OnCreateCustomArrayAdatperListener{
        void onPublishFilterResult(int count);
    }
    public void resetAmountOfGoods(){
        for(int i = 0; i < total_list.size(); i++){
            total_list.get(i).setGoods_so_luong(0);
        }
        notifyDataSetChanged();
    }
    public GoodsSearchAdapter(Context context, int resource, List<Goods> list) {
        super(context, resource);
        this.total_list = list;
        for(int i = 0; i < total_list.size() ; i++){
            Goods goods = total_list.get(i);
            goods.setGoods_so_luong(0);
        }
        this.current_list = list;
        Log.e(TAG,"List size = " + list.size());
    }

    @Override
    public int getCount() {
        return current_list.size();
    }

    @Override
    public Goods getItem(int position) {
        return current_list.get(position);
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
    public void startTranslationYAnimation(View convertView){

        ObjectAnimator animator = ObjectAnimator.ofFloat(convertView,"translationY",MyUtility.getScreenHeight(),0);

        animator.setInterpolator(new DecelerateInterpolator());

        animator.setDuration(500);

        animator.start();


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewGoods1Binding binding;

        if(convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_goods_1,parent,false);
            binding.setGoods(getItem(position));
        }
        else{

            binding = DataBindingUtil.getBinding(convertView);
            binding.setGoods(getItem(position));

        }
        return binding.getRoot();
    }
    @Override
    public Filter getFilter() {
        if(myFilter == null){
            myFilter = new MyFilter();
        }
        return myFilter;
    }
    private class MyFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            FilterResults results = new FilterResults();
            List<Goods> match_goodses = new LinkedList<>();

            if(keyword != null && keyword.length() > 0){
                for(int i = 0; i < total_list.size() ; i++){
                    Goods goods = total_list.get(i);
                    String goods_ten = goods.getGoods_ten().toLowerCase();
                    String lower_keyword = keyword.toString().toLowerCase().trim();
                    if(goods_ten.contains(lower_keyword)){
                        match_goodses.add(goods);
                    }
                }
                results.count = match_goodses.size();
                results.values = match_goodses;
            }
            else{
                results.count = getCount();
                results.values = total_list;
            }

            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            current_list = (List<Goods>)results.values;
            notifyDataSetChanged();
        }
    }
}

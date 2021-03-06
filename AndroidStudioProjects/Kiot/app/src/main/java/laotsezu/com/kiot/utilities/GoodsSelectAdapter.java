package laotsezu.com.kiot.utilities;

import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import laotsezu.com.kiot.R;
import laotsezu.com.kiot.databinding.ViewGoods3Binding;
import laotsezu.com.kiot.goods.Goods;
import laotsezu.com.kiot.resources.MyUtility;

/**
 * Created by laotsezu on 05/10/2016.
 */

public class GoodsSelectAdapter extends RecyclerView.Adapter<GoodsSelectAdapter.MyViewHolder> {
    private static String TAG = "GoodsSelectAdapter: ";
    private List<Goods> total_list = new LinkedList<>();
    private List<Goods> current_list = new LinkedList<>();
    private GoodsSelectAdapter.MyFilter myFilter ;
    private int lastItemTranslationY = -1;
    private int itemTranslationYCount = 4;

    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(View view);
        void letRemoveGoodsFromCart(View view);
        void onSubtractIconClick(View view);
    }

    public GoodsSelectAdapter(List<Goods> list, OnItemClickListener listener){
        this.listener = listener;
        this.total_list = list;
        for(int i = 0; i < total_list.size() ; i++){
            Goods goods = total_list.get(i);
            goods.setGoods_so_luong(0);
        }
        this.current_list = list;
        Log.e(TAG,"List size = " + list.size());
    }
    public int getTotalListCount(){
        return total_list.size();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGoods3Binding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_goods_3,parent,false);
      //  itemTranslationYCount++;
       // Log.e(TAG,"Item Translation Y Count ++ , Current = " + itemTranslationYCount);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ViewGoods3Binding binding = holder.binding;
        binding.setGoods(current_list.get(position));

        if(binding.getGoods().getGoods_so_luong() == 0){
            binding.goodsCartIcon.setImageDrawable(binding.getRoot().getResources().getDrawable(R.drawable.ic_cart_red_24dp));
        }
        else{
            binding.goodsCartIcon.setImageDrawable(binding.getRoot().getResources().getDrawable(R.drawable.ic_cart_green_24dp));
        }

        if(lastItemTranslationY < itemTranslationYCount && position > lastItemTranslationY){
            lastItemTranslationY++;
            MyUtility.slideBottomToTop(binding.getRoot());
        }

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.getGoods().getGoods_so_luong() == 0){
                    binding.goodsCartIcon.setImageDrawable(v.getContext().getResources().getDrawable(R.drawable.ic_cart_green_24dp));
                }
                listener.onItemClick(binding.getRoot());

                Log.e(TAG,"Root BInding OnClick!");
            }
        });

        binding.goodsCartIconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.getGoods().getGoods_so_luong() != 0){
                    binding.goodsCartIcon.setImageDrawable(v.getContext().getResources().getDrawable(R.drawable.ic_cart_red_24dp));
                    listener.letRemoveGoodsFromCart(binding.getRoot());
                    notifyItemChanged(position);
                }
                else{
                    binding.goodsCartIcon.setImageDrawable(v.getContext().getResources().getDrawable(R.drawable.ic_cart_green_24dp));
                    listener.onItemClick(binding.getRoot());
                }
                Log.e(TAG,"Cart Icon Container Click");
            }
        });

        binding.goodsDetailIconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Xem thông tin chi tiết hàng hóa", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return current_list.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{
        ViewGoods3Binding binding;
        public MyViewHolder(ViewGoods3Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public Filter getFilter() {
        if(myFilter == null){
            myFilter = new GoodsSelectAdapter.MyFilter();
        }
        return myFilter;
    }
    private class MyFilter extends Filter {
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
                results.count = getItemCount();
                results.values = total_list;
            }

            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            int count = getItemCount();
            current_list = (List<Goods>)results.values;
            notifyItemRangeChanged(0,getItemCount());
            notifyItemRangeRemoved(getItemCount(),count);
        }
    }
    public void resetAmountOfGoods(){
        for(int i = 0; i < total_list.size(); i++){
            total_list.get(i).setGoods_so_luong(0);
        }
        notifyItemRangeChanged(0,getItemCount());
    }
}

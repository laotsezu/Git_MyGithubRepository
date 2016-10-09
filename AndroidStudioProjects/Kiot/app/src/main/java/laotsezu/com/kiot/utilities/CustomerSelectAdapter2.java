package laotsezu.com.kiot.utilities;

import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

import laotsezu.com.kiot.R;
import laotsezu.com.kiot.databinding.CardCustomerBinding;
import laotsezu.com.kiot.partner.*;
import laotsezu.com.kiot.resources.MyUtility;

/**
 * Created by laotsezu on 07/10/2016.
 */

public class CustomerSelectAdapter2 extends RecyclerView.Adapter<CustomerSelectAdapter2.MyViewHolder> {
    static String TAG = "CustomerSelectAdapter2:";
    private List<Customer> list = new LinkedList<>();
    private OnCustomerSelectAdapterListener listener;
    private int lastPositionTranslationY = -1;
    private String current_custom_id;
    public interface OnCustomerSelectAdapterListener{
        void onDetailIconClick(View v);
        void onSelectIconClick(View v);
        String getCurrentCustomer_id();
    }
    public CustomerSelectAdapter2(List<Customer> list,OnCustomerSelectAdapterListener listener){
        if(list != null)
            this.list= list;
        this.listener = listener;
        this.current_custom_id = listener.getCurrentCustomer_id();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardCustomerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.card_customer,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CardCustomerBinding binding = holder.binding;
        Customer customer = list.get(position);
        binding.setCustomer(customer);

        Log.e(TAG,"Card Customer Id = " + customer.getCustomer_id());


        if ( listener.getCurrentCustomer_id().equals(customer.getCustomer_id())) {
            binding.cardCustomerContainer.setBackgroundColor(binding.getRoot().getContext().getResources().getColor(android.R.color.darker_gray));
        }
        else{
            binding.cardCustomerContainer.setBackground(binding.getRoot().getContext().getResources().getDrawable(R.drawable.ripped_with_graycolor));
        }

        if(position > lastPositionTranslationY) {
            lastPositionTranslationY++;
            MyUtility.slideBottomToTop(binding.getRoot());
        }
        binding.cardCustomerDetailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDetailIconClick(binding.getRoot());
            }
        });
        binding.cardCustomerSelectIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectIconClick(binding.getRoot());
                ObjectAnimator.ofArgb(binding.cardCustomerContainer,"background",v.getContext().getResources().getColor(android.R.color.darker_gray)).setDuration(200).start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CardCustomerBinding binding;
        public MyViewHolder(CardCustomerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

package laotsezu.com.kiot.utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import laotsezu.com.kiot.R;
import laotsezu.com.kiot.partner.Customer;

/**
 * Created by Laotsezu on 9/20/2016.
 */
public class CustomerSelectAdapter extends BaseAdapter {
    List<Customer> list = new LinkedList<>();
    String selected_customer_id;
    public CustomerSelectAdapter(List<Customer> list,String selected_customer_id){
        if(list != null)
            this.list = list;
        if(selected_customer_id != null){
            this.selected_customer_id = selected_customer_id;
        }
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Customer getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(list.get(position).getCustomer_id());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,parent,false);
        }

        Customer customer = list.get(position);

        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        String text = customer.getCustomer_ten();

        if(customer.getCustomer_dia_chi() != null && customer.getCustomer_dia_chi().length() > 0)
            text += customer.getCustomer_dia_chi();

        textView.setText(text);

        if(customer.getCustomer_id().equalsIgnoreCase(selected_customer_id)){
            textView.setBackgroundColor(parent.getContext().getResources().getColor(R.color.light_gray));
        }

        return convertView;
    }
}

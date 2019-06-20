package com.tcckj.juli.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcckj.juli.R;

import java.util.List;
import java.util.Map;

public class ContactRecordAdapter extends RecyclerView.Adapter<ContactRecordAdapter.MyHolder> {
    private Context context;
    private List<Map<String, Object>> list;

    public ContactRecordAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact_record, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.tv_contact_describe.setText((String)list.get(position).get("describe"));
        holder.tv_contact_date.setText((String)list.get(position).get("date"));
        switch ((String)list.get(position).get("type")){
            case "0":
                holder.tv_contact_value.setText("-" + list.get(position).get("value"));
                break;
            case "1":
                holder.tv_contact_value.setText("+" + list.get(position).get("value"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (null == list){
            return 0;
        }else {
            return list.size();
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView tv_contact_describe,tv_contact_value,tv_contact_date;

        public MyHolder(View itemView) {
            super(itemView);

            tv_contact_describe = (TextView) itemView.findViewById(R.id.tv_contact_describe);
            tv_contact_value = (TextView) itemView.findViewById(R.id.tv_contact_value);
            tv_contact_date = (TextView) itemView.findViewById(R.id.tv_contact_date);
        }
    }
}

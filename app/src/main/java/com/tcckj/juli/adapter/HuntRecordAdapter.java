package com.tcckj.juli.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcckj.juli.R;
import com.tcckj.juli.util.StringUtil;

import java.util.List;
import java.util.Map;

public class HuntRecordAdapter extends RecyclerView.Adapter<HuntRecordAdapter.MyHolder> {
    private Context context;
    private List<Map<String, Object>> list;

    public HuntRecordAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hunt_record,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        String dateStr = (String) list.get(position).get("date");
        if (!StringUtil.isSpace(dateStr)) {
            String date[] = dateStr.split(" ");
            holder.tv_contract_time.setText(date[0]+"\n"+date[1]);
        }

        holder.tv_contract_name.setText((String) list.get(position).get("name"));
        if (0.0 == (double)list.get(position).get("money")){
            holder.tv_contract_cost.setText(0 + "");
        }else {
            holder.tv_contract_cost.setText(StringUtil.doubleToString((double)list.get(position).get("money")));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView tv_contract_name,tv_contract_time,tv_contract_cost;

        public MyHolder(View itemView) {
            super(itemView);
            tv_contract_name = (TextView) itemView.findViewById(R.id.tv_contract_name);
            tv_contract_time = (TextView) itemView.findViewById(R.id.tv_contract_time);
            tv_contract_cost = (TextView) itemView.findViewById(R.id.tv_contract_cost);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

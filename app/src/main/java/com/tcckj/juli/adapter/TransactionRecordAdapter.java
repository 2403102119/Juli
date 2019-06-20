package com.tcckj.juli.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcckj.juli.R;
import com.tcckj.juli.util.StringUtil;

import java.util.List;
import java.util.Map;

public class TransactionRecordAdapter extends RecyclerView.Adapter<TransactionRecordAdapter.MyHolder>{
    private Context context;
    private List<Map<String, Object>> list;

    public TransactionRecordAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction_record, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        holder.tv_transaction_title.setText((String)list.get(position).get("name"));
        holder.tv_transaction_integral.setText("积分：" + StringUtil.doubleToString((double)list.get(position).get("value")));
        holder.tv_transaction_date.setText((String)list.get(position).get("date"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClickListener(position);
            }
        });
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
        TextView tv_transaction_title,tv_transaction_integral,tv_transaction_date;

        public MyHolder(View itemView) {
            super(itemView);

            tv_transaction_title = (TextView) itemView.findViewById(R.id.tv_transaction_title);
            tv_transaction_integral = (TextView) itemView.findViewById(R.id.tv_transaction_integral);
            tv_transaction_date = (TextView) itemView.findViewById(R.id.tv_transaction_date);
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

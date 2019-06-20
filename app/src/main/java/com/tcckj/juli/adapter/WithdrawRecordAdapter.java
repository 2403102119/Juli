package com.tcckj.juli.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tcckj.juli.R;
import com.tcckj.juli.util.StringUtil;

import java.util.List;
import java.util.Map;
/*
提现记录的适配器
 */
public class WithdrawRecordAdapter extends RecyclerView.Adapter<WithdrawRecordAdapter.MyHolder>{
    private List<Map<String, Object>> list;
    private Context context;

    public WithdrawRecordAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_withdraw_record, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder myHolder, final int i) {
        switch ((String)list.get(i).get("type")){
            case "0":
                myHolder.tv_wallet_type.setText("积分钱包");
                break;
            case "1":
                myHolder.tv_wallet_type.setText("推荐钱包");
                break;
            case "2":
                myHolder.tv_wallet_type.setText("动态钱包");
                break;
            case "3":
                myHolder.tv_wallet_type.setText("静态钱包");
                break;
            case "4":
                myHolder.tv_wallet_type.setText("冻结钱包");
                break;
        }
        switch ((String)list.get(i).get("status")){
            case "0":
                myHolder.tv_wallet_state.setText("审核中");
                break;
            case "1":
                myHolder.tv_wallet_state.setText("审核成功");
                break;
            case "2":
                myHolder.tv_wallet_state.setText("审核失败");
                break;
        }
        myHolder.tv_wallet_money.setText("提现金额："+ StringUtil.doubleToString((double)list.get(i).get("money")));
        myHolder.tv_wallet_time.setText((String)list.get(i).get("date"));

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClickListener(i);
            }
        });

        myHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnDeleteListener(i);
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
        TextView tv_wallet_type,tv_wallet_state,tv_wallet_money,tv_wallet_time;
        Button btnDelete;

        public MyHolder(View itemView) {
            super(itemView);

            tv_wallet_type = (TextView) itemView.findViewById(R.id.tv_wallet_type);
            tv_wallet_state = (TextView) itemView.findViewById(R.id.tv_wallet_state);
            tv_wallet_money = (TextView) itemView.findViewById(R.id.tv_wallet_money);
            tv_wallet_time = (TextView) itemView.findViewById(R.id.tv_wallet_time);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void OnItemClickListener(int position);
        void OnDeleteListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

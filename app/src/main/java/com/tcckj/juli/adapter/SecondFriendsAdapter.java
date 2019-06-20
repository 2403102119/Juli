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

public class SecondFriendsAdapter extends RecyclerView.Adapter<SecondFriendsAdapter.MyHolder> {
    private Context context;
    private List<Map<String, Object>> list;

    public SecondFriendsAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_second_friend, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        holder.tv_first_name.setText((String)list.get(position).get("name"));
        holder.tv_first_date.setText((String)list.get(position).get("registDate"));
        holder.tv_first_phone.setText((String)list.get(position).get("account"));
        holder.tv_first_integral.setText("积分：" + list.get(position).get("money"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClickListener(position);
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
        TextView tv_first_name,tv_first_phone,tv_first_integral,tv_first_date;

        public MyHolder(View itemView) {
            super(itemView);

            tv_first_name = (TextView) itemView.findViewById(R.id.tv_first_name);
            tv_first_phone = (TextView) itemView.findViewById(R.id.tv_first_phone);
            tv_first_integral = (TextView) itemView.findViewById(R.id.tv_first_integral);
            tv_first_date = (TextView) itemView.findViewById(R.id.tv_first_date);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void OnItemClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}

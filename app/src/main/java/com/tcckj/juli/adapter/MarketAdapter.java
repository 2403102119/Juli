package com.tcckj.juli.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcckj.juli.R;
import com.tcckj.juli.util.ImageLoadUtil;
import com.tcckj.juli.util.UriUtil;

import java.util.List;
import java.util.Map;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MyHolder> {
    private Context context;
    private List<Map<String, Object>> list;

    public MarketAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_market,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        ImageLoadUtil.showImage((Activity) context, UriUtil.ip + list.get(position).get("photo"), holder.img_item_market);
//        holder.tv_market_averageRate.setText(list.get(position).get("name") + "     " + list.get(position).get("averageRate") + "%");

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
        ImageView img_item_market;
//        TextView tv_market_averageRate;

        public MyHolder(View itemView) {
            super(itemView);

            img_item_market = (ImageView) itemView.findViewById(R.id.img_item_market);
//            tv_market_averageRate = (TextView) itemView.findViewById(R.id.tv_market_averageRate);
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

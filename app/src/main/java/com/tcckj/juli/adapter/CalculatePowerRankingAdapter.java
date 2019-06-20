package com.tcckj.juli.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcckj.juli.R;

import java.util.List;
import java.util.Map;

public class CalculatePowerRankingAdapter extends RecyclerView.Adapter<CalculatePowerRankingAdapter.MyHolder> {
    private Context context;
    List<Map<String, Object>> list;

    public CalculatePowerRankingAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_calculate_power_ranking, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.tv_ranking_number.setText(position+1+"");
        holder.tv_calculate_account.setText((String) list.get(position).get("name"));
        holder.tv_calculate_forceVlue.setText((Integer) list.get(position).get("forceVlue")+"");
        switch (position){
            case 0:
                holder.img_ranking_bg.setVisibility(View.VISIBLE);
                holder.img_ranking_right.setVisibility(View.VISIBLE);
                holder.img_ranking_bg.setImageResource(R.mipmap.first_icon);
                holder.img_ranking_right.setImageResource(R.mipmap.first_right_icon);
                holder.tv_ranking_number.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case 1:
                holder.img_ranking_bg.setVisibility(View.VISIBLE);
                holder.img_ranking_right.setVisibility(View.VISIBLE);
                holder.img_ranking_bg.setImageResource(R.mipmap.second_icon);
                holder.img_ranking_right.setImageResource(R.mipmap.second_right_icon);
                holder.tv_ranking_number.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case 2:
                holder.img_ranking_bg.setVisibility(View.VISIBLE);
                holder.img_ranking_right.setVisibility(View.VISIBLE);
                holder.img_ranking_bg.setImageResource(R.mipmap.third_icon);
                holder.img_ranking_right.setImageResource(R.mipmap.third_right_icon);
                holder.tv_ranking_number.setTextColor(context.getResources().getColor(R.color.white));
                break;
                default:
                    holder.img_ranking_bg.setVisibility(View.GONE);
                    holder.img_ranking_right.setVisibility(View.GONE);
                    holder.tv_ranking_number.setTextColor(context.getResources().getColor(R.color.hintColor));
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
        ImageView img_ranking_bg,img_ranking_right;
        TextView tv_ranking_number,tv_calculate_account,tv_calculate_forceVlue;

        public MyHolder(View itemView) {
            super(itemView);

            img_ranking_bg = (ImageView) itemView.findViewById(R.id.img_ranking_bg);
            img_ranking_right = (ImageView) itemView.findViewById(R.id.img_ranking_right);
            tv_ranking_number = (TextView) itemView.findViewById(R.id.tv_ranking_number);
            tv_calculate_account = (TextView) itemView.findViewById(R.id.tv_calculate_account);
            tv_calculate_forceVlue = (TextView) itemView.findViewById(R.id.tv_calculate_forceVlue);
        }
    }
}

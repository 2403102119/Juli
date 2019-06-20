package com.tcckj.juli.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcckj.juli.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhy.http.okhttp.OkHttpUtils.TAG;

public class PortAdapter extends RecyclerView.Adapter<PortAdapter.MyHolder>{
    private Context context;
    private List<Map<String, Object>> list;
//    private List<Boolean> isClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色

    public PortAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
//        isClicks = new ArrayList<>();
//        for(int i = 0;i<list.size();i++){
//            isClicks.add(false);
//        }
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_choose_port, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        /*if (isClicks.get(position)){
            holder.tv_port_name.setBackgroundColor(context.getResources().getColor(R.color.text_blue2));
            holder.tv_port_name.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            holder.tv_port_name.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.tv_port_name.setTextColor(context.getResources().getColor(R.color.nomalText));
        }*/

        holder.tv_port_name.setText(list.get(position).get("name")+"");

        if ((boolean)list.get(position).get("isClick")){
            holder.tv_port_name.setBackgroundColor(context.getResources().getColor(R.color.hintColor));
            holder.tv_port_name.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            holder.tv_port_name.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.tv_port_name.setTextColor(context.getResources().getColor(R.color.nomalText));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*for(int i = 0; i <isClicks.size();i++){
                    isClicks.set(i,false);
                }
                Log.i("111111111", "onClick: " + position);
                isClicks.set(position,true);*/
//                notifyDataSetChanged();

                onItemClickListener.onItemClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == list){
//            isClicks.clear();
            return 0;
        }else {
           /* isClicks.clear();
            for (int i = 0; i < list.size(); i++) {
                isClicks.add(false);
            }*/
            return list.size();
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView tv_port_name;

        public MyHolder(View itemView) {
            super(itemView);
            tv_port_name = (TextView) itemView.findViewById(R.id.tv_port_name);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClickListener(int position);
    }
}

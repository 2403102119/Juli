package com.tcckj.juli.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tcckj.juli.R;
import com.tcckj.juli.thread.HttpInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * kylin on 2017/11/18.
 */

public class PickTypeUtils {
    private Activity activity;
    private HttpInterface httpInterface;
    /*筛选对话框*/
    private MyPopuWindow pickTypeDialog;
//    private NiceRecyclerView type1,type2;
//    private FilterAdapter type1Adapter;
    private List<Map<String,Object>> type1Data = new ArrayList<>();
//    private JobTypeListAdapter type2Adapter;
    private List<Map<String,Object>> type2Data = new ArrayList<>();
    private LinearLayout back;
    private String type1P_ID;
    private String type1Name;

    public PickTypeUtils(Activity activity, HttpInterface httpInterface){
        this.activity=activity;
        this.httpInterface=httpInterface;
        /*头像选择*/
        LayoutInflater in = LayoutInflater.from(activity);
//        @SuppressLint("InflateParams") View pickTypeDialogView = in.inflate(R.layout.pick_type, null);

//        back = (LinearLayout) pickTypeDialogView.findViewById(R.id.back);

      /*  type1 = (NiceRecyclerView) pickTypeDialogView.findViewById(R.id.type1);
        type1Adapter = new FilterAdapter(activity,R.layout.item_filter,type1Data);
        type1.setAdapter(type1Adapter);

        type2 = (NiceRecyclerView) pickTypeDialogView.findViewById(R.id.type2);
        type2Adapter = new JobTypeListAdapter(R.layout.item_filter_type2_found_fragment,type2Data);
        type2.setAdapter(type2Adapter);
*/
//        pickTypeDialog = new MyPopuWindow(activity,pickTypeDialogView);
        pickTypeDialog.getWindow().setGravity(Gravity.TOP);
        pickTypeDialog.getWindow().setWindowAnimations(R.style.topDialog);

        initEvent();
        initData();
    }

    private void initEvent() {
        /*type2Adapter.setOnItemClickListener(new JobTypeListAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                String P_ID=(String)type2Data.get(position).get("TclassifyId");
                String str=(String)type2Data.get(position).get("Tname");

//                MUIToast.show(activity,str);
                onTypeSelectedListener.onClick(position,str,P_ID,type1Name,type1P_ID);
                pickTypeDialog.dismiss();
            }
        });
        type1Adapter.setOnItemClickListener(new FilterAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                String P_ID=(String)type1Data.get(position).get("OclassifyId");
                pokedexIndexContent(P_ID);
                type1P_ID=P_ID;
                type1Name=type1Data.get(position).get("Oname")+"";
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTypeDialog.dismiss();
            }
        });*/
    }
/*

    //一级分类列表
    private void pokedexIndex() {
        if (NetUtil.isNetWorking(activity)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    httpInterface.pokedexIndexData(new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Bean.PokedexIndexModel data = new Gson().fromJson(result, Bean.PokedexIndexModel.class);
                            if (data.status == 1) {
                                List<Bean.PokedexIndexTypeModel> pokedex_index_type_list = data.list;
                                for (int i = 0; i < pokedex_index_type_list.size(); i++) {
                                    Map<String, Object> map = new HashMap<>();
                                    if (i == 0) {
                                        map.put("check", true);
                                    } else {
                                        map.put("check", false);
                                    }
                                    map.put("OclassifyId", pokedex_index_type_list.get(i).OclassifyId);
                                    map.put("Oname", pokedex_index_type_list.get(i).Oname);
                                    map.put("Osequence", pokedex_index_type_list.get(i).Osequence);
                                    map.put("head", pokedex_index_type_list.get(i).head);
                                    type1Data.add(map);
                                }
                                type1Adapter.notifyDataSetChanged();
                                if (pokedex_index_type_list.size() > 0) {
                                    type1P_ID=type1Data.get(0).get("OclassifyId")+"";
                                    type1Name=type1Data.get(0).get("Oname")+"";
                                    pokedexIndexContent((String) type1Data.get(0).get("OclassifyId"));
                                }
                            }
                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("获取.异常", response);
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            Log.e("onError", call + "-----" + exception);
                        }

                        @Override
                        public void onTokenError(String response) {
                            Log.e("onTokenError", response);
                        }
                    });
                }
            });
        } else {
            MUIToast.show(activity,activity.getResources().getString(R.string.system_busy));
        }
    }


    //根据一级分类查二级分类
    private void pokedexIndexContent(final String OclassifyId) {
        if (NetUtil.isNetWorking(activity)) {
            ThreadPoolManager.getInstance().getNetThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    httpInterface.pokedexIndexContentData(OclassifyId, new MApiResultCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("获取.成功", result);

                            Bean.PokedexIndexContentAllModel data = new Gson().fromJson(result,
                                    Bean.PokedexIndexContentAllModel.class);
                            if (data.status == 1) {
                                type2Data.clear();
                                List<Bean.PokedexIndexContent> pokedex_content_list = data.list;
                                for (int i = 0; i < pokedex_content_list.size(); i++) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("check", false);
                                    map.put("TclassifyId", pokedex_content_list.get(i).TclassifyId);
                                    map.put("Tname", pokedex_content_list.get(i).Tname);
                                    map.put("Thead", pokedex_content_list.get(i).Thead);
                                    map.put("Tsequence", pokedex_content_list.get(i).Tsequence);

                                    type2Data.add(map);
                                }
                                type2Adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFail(String response) {
                            Log.e("获取.异常", response);
                        }

                        @Override
                        public void onError(Call call, Exception exception) {
                            Log.e("onError", call + "-----" + exception);
                        }

                        @Override
                        public void onTokenError(String response) {
                            Log.e("onTokenError", response);
                        }
                    });
                }
            });
        } else {
            MUIToast.show(activity,activity.getResources().getString(R.string.system_busy));
        }
    }
*/

    private void initData() {
//        pokedexIndex();
    }

    public void show() {
        pickTypeDialog.show();
    }

    public interface OnTypeSelectedListener {
        void onClick(int position, String type2Name, String type2_Oid, String type1Name, String type1_Oid);
    }
//
    private OnTypeSelectedListener onTypeSelectedListener;
//
    public void setTypeSelectedListener(OnTypeSelectedListener listener){
        this.onTypeSelectedListener  = listener;
    }
//
//    public interface OnShowAllListener{
//        void onClick();
//    }
//
//    private OnShowAllListener onShowAllListener;
//
//    public void setShowAllListener(OnShowAllListener onShowAllListener){
//        this.onShowAllListener  = onShowAllListener;
//    }
}

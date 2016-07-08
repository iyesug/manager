package com.vis.custom.customersmanage.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vis.custom.customersmanage.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/7/4.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    public Context mContext;
    public List<String> mData;
    public List<String> mId;
    public LayoutInflater mLayoutinflater;
    public OnItemClickListener mOnitemclicklistener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnitemclicklistener = listener;
    }

    public  RecyclerViewAdapter(Context mContext,List<String> mData,List<String> mId ){
        this.mContext = mContext;
        mLayoutinflater = LayoutInflater.from(mContext);
       this.mData=mData;
        this.mId=mId;
//        //数据源
//        mData = new ArrayList<>();
//        for (int i = 'A'; i <= 'z'; i++) {
//            mData.add((char) i + "");
//        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView=mLayoutinflater.inflate(R.layout.item,parent,false);
        RecyclerViewHolder holder=new RecyclerViewHolder(mView);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        if(mOnitemclicklistener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    mOnitemclicklistener.onItemClick(holder.itemView,position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){


                @Override
                public boolean onLongClick(View view) {
                    mOnitemclicklistener.onItemLongClick(holder.itemView,position);
                    return true;
                }
            });
        }
        try {
            JSONObject ob=new JSONObject(mData.get(position));
            String str = String.format("%03d", position+1);

            if(ob.has("c_name")){
            holder.mTextView.setText(ob.getString("c_name"));
            holder.mTextView1.setText("   "+str);
            holder.mTextView2.setText(ob.getString("c_adr"));}else{
                holder.mTextView.setText(ob.getString("d_name"));
                holder.mTextView1.setText("   "+str);
                holder.mTextView2.setText(ob.getString("d_adr"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        if(mData!=null){
        return mData.size();
     }
        return 0;
    }

    public interface  OnItemClickListener{
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);

    }
}

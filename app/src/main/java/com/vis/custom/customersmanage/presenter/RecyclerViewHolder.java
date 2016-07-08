package com.vis.custom.customersmanage.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vis.custom.customersmanage.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView mTextView;
    public TextView mTextView1;
    public TextView mTextView2;
    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(R.id.id_textview);
        mTextView1 = (TextView) itemView.findViewById(R.id.id_textview1);
        mTextView2 = (TextView) itemView.findViewById(R.id.id_textview_adr);
    }
}

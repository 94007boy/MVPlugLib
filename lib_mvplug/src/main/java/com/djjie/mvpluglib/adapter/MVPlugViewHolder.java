package com.djjie.mvpluglib.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xiaolv on 16/4/8.
 */
public class MVPlugViewHolder<T extends ViewDataBinding,M> extends RecyclerView.ViewHolder {

    protected T dataBinding;

    public MVPlugViewHolder(View view) {
        super(view);
    }

    public MVPlugViewHolder(T t) {
        super(t.getRoot());
        dataBinding = t;
    }

    /**
     * 给Item上view填充数据
     * @param data
     * @param position data在数据集合中的角标位置，不是列表item的位置，减去了头部size
     */
    public void setData(M data,int position) {
    }

}

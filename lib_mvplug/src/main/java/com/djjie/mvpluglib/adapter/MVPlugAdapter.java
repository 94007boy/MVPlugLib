package com.djjie.mvpluglib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djjie.mvpluglib.MVPlug;
import com.djjie.mvpluglib.MVPlugConfig;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by xiaolv on 16/4/11.
 */
abstract public class MVPlugAdapter<M> extends RecyclerView.Adapter<MVPlugViewHolder>   {

    protected List<M> itemDatas;
    private Context context;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;
    private final Object mLock = new Object();
    private boolean mNotifyOnChange = true;
    private ArrayList<ItemView> headers = new ArrayList<>();
    private ArrayList<ItemView> footers = new ArrayList<>();
    private View mFooterView;
    public static final int VIEW_TYPE_FOOTER = Integer.MAX_VALUE;
    public LayoutInflater inflater;
    private MVPlugFooterViewEvent footerViewEvent;
    private MVPlugConfig plugConfig;
    private boolean hasLoadMore = false;

    public interface ItemView {
        View onCreateView(ViewGroup parent);
        void onBindView(View headerView);//写错了，headerView改为itemView
    }

    private class StateViewHolder extends MVPlugViewHolder {
        public StateViewHolder(View itemView) {
            super(itemView);
        }
    }

    public MVPlugAdapter(Context context) {
        init(context,  new ArrayList<M>());
    }

    public MVPlugAdapter(Context context, M[] itemDatas) {
        init(context, Arrays.asList(itemDatas));
    }

    public MVPlugAdapter(Context context, List<M> itemDatas) {
        init(context, itemDatas);
    }

    private void init(Context context , List<M> itemDatas) {
        this.context = context;
        this.itemDatas = itemDatas;
        plugConfig = MVPlug.getInstance().getConfiguration();
        inflater = LayoutInflater.from(context);
        footerViewEvent = new MVPlugFooterViewEvent(context,inflater);
        setFooterView(footerViewEvent.getFooterViewContainer());//自动添加尾部
    }

    public Context getContext() {
        return context;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        hasLoadMore = true;
        if(footerViewEvent != null)footerViewEvent.setOnLoadMoreListener(onLoadMoreListener);
    }

    @Override
    public MVPlugViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (plugConfig.ismIsDebugMode()){
//            Logger.d("onCreateViewHolder");
//        }
        if(viewType== VIEW_TYPE_FOOTER){
            return new MVPlugViewHolder(mFooterView);
        }
        View view = createSpViewByType(parent, viewType);
        if (view != null){
            return new StateViewHolder(view);
        }
        final MVPlugViewHolder viewHolder = OnCreateViewHolder(parent, viewType);

        if (mItemClickListener!=null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(viewHolder.getAdapterPosition() - headers.size());
                }
            });
        }

        if (mItemLongClickListener!=null){
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mItemLongClickListener.onItemClick(viewHolder.getAdapterPosition() - headers.size());
                }
            });
        }
        return viewHolder;
    }

    public void onScrollToBottom(){
        if (footerViewEvent != null){
            footerViewEvent.onFooterViewBinded(hasLoadMore);//末尾
        }
    }

    @Override
    public void onBindViewHolder(MVPlugViewHolder holder, final int position) {
        holder.itemView.setId(position);
        int totalSize = headers.size() + itemDatas.size() + footers.size() + 1;
//        if (plugConfig.ismIsDebugMode()){
//            Logger.d("onBindViewHolder position = "+position+" , totalSize = "+totalSize);
//        }
        if (position == totalSize - 1){
//            footerViewEvent.onFooterViewBinded(hasLoadMore);//末尾
            return;
        }
        if (headers.size() != 0 && position < headers.size()){//头部区域
            headers.get(position).onBindView(holder.itemView);
            try {
                StaggeredGridLayoutManager.LayoutParams lp =
                        (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                lp.setFullSpan(true);
                holder.itemView.setLayoutParams(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        /**
         * h1   0
         * h2   1
         * b1   2
         * b1   3
         * f1   4
         * f2   5
         * last 6
         * i = 4 - 2 - 2 = 0
         * i = 3 - 2
         */
        int i = position - headers.size() - itemDatas.size() ;//尾部区域
        if (footers.size() != 0 && i >= 0){
            footers.get(i).onBindView(holder.itemView);
            return;
        }
        OnBindViewHolder(holder,position - headers.size());
    }


    /**
     * 添加头部视图
     * @param view
     */
    public void addHeader(ItemView view){
        if (view==null)throw new NullPointerException("ItemView can't be null");
        headers.add(view);
    }

    public void addFooter(ItemView view){
        if (view==null)throw new NullPointerException("ItemView can't be null");
        footers.add(view);
    }

    /**
     * 添加底部视图,私有，不允许主动添加
     * @param footer
     */
    private void setFooterView(View footer){
        this.mFooterView = footer;
        notifyDataSetChanged();
    }

    public void dismissFooterView(){
        footerViewEvent.hideFooterView();
    }

    @Override
    public int getItemViewType(int position) {//复用机制
        /**
         * head0
         * head1
         * body0
         * body1
         * body2
         * footer0
         * footer1
         * position = 7
         */
        if(position == headers.size() + itemDatas.size() + footers.size()){
            return VIEW_TYPE_FOOTER;
        }
        if (headers.size() != 0){
            if (position < headers.size())return headers.get(position).hashCode();
        }
        if (footers.size() != 0){
            int i = position - headers.size() - itemDatas.size();
            if (i >= 0){
                return footers.get(i).hashCode();
            }
        }
        return GetItemViewType(getItem(position),position);
    }

    /**
     * 刷新数据
     * @param datas
     */
    public void refreshDatas(List<M> datas){
        this.itemDatas.clear();
        this.itemDatas.addAll(datas);
        footerViewEvent.changeViewByDatasLength(datas == null ? 0 : datas.size(),hasLoadMore);
        notifyDataSetChanged();
        if (plugConfig.ismIsDebugMode()){
            Logger.d("refreshDatas size = "+datas.size());
        }
    }

    /**
     * 获取所有数据源
     * @return
     */
    public List<M> getAllDatas(){
        return itemDatas;
    }
    /**
     * 添加更多数据
     * @param collection
     */
    public void addMoreDatas(Collection<? extends M> collection) {
        if (plugConfig.ismIsDebugMode()){
            Logger.d("addMoreDatas");
        }
        footerViewEvent.changeViewByDatasLength(collection == null ? 0 : collection.size(),hasLoadMore);
        if (collection!=null && collection.size() != 0){
            synchronized (mLock) {
                itemDatas.addAll(collection);
            }
            int dataCount = collection==null?0:collection.size();
            if (mNotifyOnChange) notifyItemRangeInserted(headers.size()+getCount()-dataCount+1,dataCount);
        }

//        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * 在指定位置插入元素
     */
    public void insert(M object, int index) {
        synchronized (mLock) {
            itemDatas.add(index, object);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * 删除指定元素
     */
    public void remove(M object) {
        synchronized (mLock) {
            itemDatas.remove(object);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * 删除指定位置的元素
     */
    public void remove(int position) {
        synchronized (mLock) {
            itemDatas.remove(position);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }


    /**
     * 清空全部数据
     */
    public void clear() {
        footerViewEvent.hideFooterView();
        synchronized (mLock) {
            itemDatas.clear();
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * 包含了头部和尾部view的item总个数。
     * @return
     */
    @Override
    public int getItemCount() {
//        int size = itemDatas.size();
//        if (mFooterView != null)size ++;//有尾部，因此加1
//        if(headers.size() != 0)size += headers.size();
//        if (plugConfig.ismIsDebugMode()){
//            Logger.d("getItemCount size = "+size);
//        }
//        return size;
        return itemDatas.size()+headers.size()+footers.size()+1;
    }

    private View createSpViewByType(ViewGroup parent, int viewType){

        if (headers != null && !headers.isEmpty()){
            for (ItemView headerView:headers){
                if (headerView.hashCode() == viewType){
                    View view = headerView.onCreateView(parent);
                    if(view == null || view.getLayoutParams() == null)return null;
                    StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(view.getLayoutParams());
                    layoutParams.setFullSpan(true);
                    view.setLayoutParams(layoutParams);
                    return view;
                }
            }
        }

        if (footers != null && !footers.isEmpty()){
            for (ItemView footerview:footers){
                if (footerview.hashCode() == viewType){
                    View view = footerview.onCreateView(parent);
                    StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(view.getLayoutParams());
                    layoutParams.setFullSpan(true);
                    view.setLayoutParams(layoutParams);
                    return view;
                }
            }
        }
        return null;
    }

    public void removeAllHeader(){
        headers.clear();
    }
    public void removeAllFooter(){
        footers.clear();
        footerViewEvent.hideFooterView();
    }

    public void removeFooter(){
        mFooterView = null;
    }

    /**
     * 不包含头和尾的item个数
     * @return
     */
    public int getCount(){
        return itemDatas.size();
    }

    public M getItem(int position) {
        if (itemDatas == null || itemDatas.isEmpty() || position < 0 || position >= itemDatas.size())return null;
        return itemDatas.get(position);
    }

    public void OnBindViewHolder(MVPlugViewHolder holder, final int position){
        holder.setData(getItem(position),position);
    }

    abstract public MVPlugViewHolder OnCreateViewHolder(ViewGroup parent, int viewType);
    abstract public int GetItemViewType(M m,int postion);

    public void onLoadMoreError() {
        footerViewEvent.onLoadMoreError();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemClick(int position);
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
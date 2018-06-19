package com.djjie.mvpluglib.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.djjie.mvpluglib.R;
import com.djjie.mvpluglib.view.MVPlugView;

import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class MVPlugRecyclerView<M> extends FrameLayout {

    private Context context;
    private boolean loadMoreLocked;
    private MVPlugRefreshLayout refreshLayout;
    private MVPlugAdapter<M> adapter;
    private MVPlugAdapterPresenter presenter;
    private MVPlugView attachedView;
    private RecyclerView recyclerView;
    private RecyclerPullListener pullListener;
    private View headerView;
    private MVPlugView.RefreshHeaderView refreshHeaderView;
    private LinearLayoutManager mLayoutManager;

    public void refreshDatas(List<M> m) {
        adapter.refreshDatas(m);
    }

    private abstract class RecyclerPullListener{
        public abstract void onRefresh();
        public abstract void onLoadMore();
    }

    public void setPullListener(RecyclerPullListener pullListener) {
        this.pullListener = pullListener;
    }

    public MVPlugRecyclerView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public MVPlugRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MVPlugRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        View layout = inflate(context, R.layout.mvplug_recycler_view, this);
        refreshLayout = layout.findViewById(R.id.mvplug_refresh_layout);
        refreshLayout.setLoadMore(false);
        refreshLayout.setOnPullRefreshListener(new MVPlugRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                if (pullListener != null)pullListener.onRefresh();
                if(refreshHeaderView != null) refreshHeaderView.onStartAnima(headerView);
                setLoadMoreLocked(false);
                presenter.refreshData();
            }

            @Override
            public void onPullDistance(int distance) {

            }

            @Override
            public void onPullEnable(boolean enable) {
            }
        });
        recyclerView = layout.findViewById(R.id.mvplug_recycler_view);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (adapter != null && (newState == SCROLL_STATE_DRAGGING || newState == SCROLL_STATE_IDLE)
                        && lastVisibleItem + 1 == adapter.getItemCount()) {
                    adapter.onScrollToBottom();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        presenter = new MVPlugAdapterPresenter(this);
    }

    public void setAdapter(MVPlugView attachedView,MVPlugAdapter adapter){
        this.attachedView = attachedView;
        this.adapter = adapter;
        attachedView.setAdapterPresenter(presenter);
        recyclerView.setAdapter(adapter);
        initRecyclerHeaderView();
    }

    private void initRecyclerHeaderView() {
        refreshHeaderView = getAttachedView().getRefreshHeaderView();
        if (refreshHeaderView != null){
            headerView = refreshHeaderView.onCreateView(this);
            refreshLayout.setHeaderView(headerView);
            refreshLayout.setHeaderViewBackgroundColor(refreshHeaderView.onSetHeaderViewBgColor());
        }
    }

    public void initItemView(MVPlugView attachedView,final int layoutId, final int variableId){
        MVPlugAdapter adapter = new MVPlugAdapter(getContext()) {
            @Override
            public MVPlugViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                final ViewDataBinding dataBinding = DataBindingUtil.inflate(inflater, layoutId, parent, false);
                return new MVPlugViewHolder<ViewDataBinding,M>(dataBinding){
                    @Override
                    public void setData(M data, int position) {
                        dataBinding.setVariable(variableId,data);
                    }
                };
            }

            @Override
            public int GetItemViewType(Object o, int postion) {
                return postion;
            }

        };
        setAdapter(attachedView,adapter);
    }

    public MVPlugAdapter<M> getAdapter() {
        return adapter;
    }

    public MVPlugView getAttachedView() {
        return attachedView;
    }

    public MVPlugAdapterPresenter getPresenter() {
        return presenter;
    }

    public void enableLoadMore() {
        setLoadMoreLocked(false);
        adapter.setOnLoadMoreListener(new MVPlugAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (loadMoreLocked)return;//锁住，避免多次重复请求
                if (pullListener != null)pullListener.onLoadMore();
                presenter.loadMoreData();
                loadMoreLocked = true;
            }
        });
    }

    public void setLoadMoreLocked(boolean loadMoreLocked) {
        this.loadMoreLocked = loadMoreLocked;
    }

    public void onLoadMoreError() {
        adapter.onLoadMoreError();
    }

    public void stopRefreshing(){
        refreshLayout.setRefreshing(false);
        setLoadMoreLocked(false);
        if(refreshHeaderView != null) refreshHeaderView.onStopAnima(headerView);
    }

    public void setOnItemClickListener(MVPlugAdapter.OnItemClickListener onItemClickListener){
        if (adapter == null)return;
        adapter.setOnItemClickListener(onItemClickListener);
    }
}

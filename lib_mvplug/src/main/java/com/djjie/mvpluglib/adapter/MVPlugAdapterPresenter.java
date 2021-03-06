package com.djjie.mvpluglib.adapter;

import java.lang.ref.WeakReference;
import java.util.List;

public class MVPlugAdapterPresenter<T> {

    private WeakReference<MVPlugRecyclerView> view;

    public MVPlugAdapterPresenter(MVPlugRecyclerView view){
        this.view = new WeakReference<>(view);
    }
    public void onDataRefresh(List<T> datas){
        view.get().refreshDatas(datas);
        view.get().stopRefreshing();
    }

    public void refreshData(){
        view.get().getAttachedView().getPresenter().onRecyclerRefreshData();
    }

    public void loadMoreData(){
        view.get().getAttachedView().getPresenter().onRecyclerLoadMoreData();
    }

    public void refreshData(String tab){
        view.get().getAttachedView().getPresenter().onRecyclerRefreshData(tab);
    }

    public void loadMoreData(String tab){
        view.get().getAttachedView().getPresenter().onRecyclerLoadMoreData(tab);
    }

    public void onLoadMoreData(List<T> datas){
        view.get().getAdapter().addMoreDatas(datas);
        view.get().setLoadMoreLocked(false);
    }

    public void stopRefreshing() {
        view.get().stopRefreshing();
    }

    public void onLoadMoreError() {
        view.get().onLoadMoreError();
    }

    public void setLoadMoreLocked(boolean loadMoreLocked) {
        view.get().setLoadMoreLocked(loadMoreLocked);
    }

    public void clearData() {
        view.get().getAdapter().clear();
    }

    public void removeFooterView() {
        view.get().disableLoadMore();
        view.get().removeFooterView();
    }
}

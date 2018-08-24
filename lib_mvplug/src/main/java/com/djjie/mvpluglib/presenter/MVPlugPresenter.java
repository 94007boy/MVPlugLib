package com.djjie.mvpluglib.presenter;

import android.support.annotation.NonNull;

import com.djjie.mvpluglib.MVPlug;
import com.djjie.mvpluglib.MVPlugConfig;
import com.djjie.mvpluglib.adapter.MVPlugAdapterPresenter;
import com.djjie.mvpluglib.model.MVPlugFailReason;
import com.djjie.mvpluglib.view.MVPlugFragment;
import com.djjie.mvpluglib.view.MVPlugView;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shf2 on 2016/12/20.
 */

public abstract class MVPlugPresenter<V extends MVPlugView> {
    protected CompositeSubscription mSubscriptions = new CompositeSubscription();
    private V view;

    public void setView(@NonNull V view) {
        this.view = view;
        onLoad();
    }

    public V getView() {
        return view;
    }

    public abstract void onLoad();

    public void clearTask() {
        if (mSubscriptions != null && mSubscriptions.hasSubscriptions()){
            mSubscriptions.clear();
        }
        mSubscriptions = null;
        view = null;
    }

    protected void subscribe(int state,Observable observable, Observer observer){
        if (view == null)return;
        if(state == MVPlugConfig.STATE_LOADING){
            view.showLoadingView();
        }
        mSubscriptions.add(
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer));
    }

    protected void subscribe(String tag,int state,Observable observable, Observer observer){
        if (view == null)return;
        if(state == MVPlugConfig.STATE_LOADING){
            view.showLoadingView(tag);
        }
        mSubscriptions.add(
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer));
    }

    public void onRecyclerRefreshData() {

    }

    public void onRecyclerLoadMoreData() {

    }

    public void onRecyclerRefreshData(String tab) {

    }

    public void onRecyclerLoadMoreData(String tab) {

    }

    public void managerView(MVPlugFragment fragment,String tag) {
        view.managerView(tag,fragment);
    }

    public static abstract class ResObserver<M> implements Observer<M> {
        protected WeakReference<MVPlugView> view;
        public abstract void onResult(M m);
        public abstract void onResErrorMsg(String resErrorMsg,int code);
        protected boolean showErrCoverView = true;
        protected String tag;
        protected int state;

        public ResObserver(MVPlugPresenter presenter, int state, String tag){
            this.view = new WeakReference<>(presenter.getView());
            this.state = state;
            this.tag = tag;
        }

        public ResObserver(MVPlugPresenter presenter, int state){
            this.view = new WeakReference<>(presenter.getView());
            this.state = state;
        }

        public ResObserver(MVPlugPresenter presenter){
            this.view = new WeakReference<>(presenter.getView());
        }

        public ResObserver(MVPlugPresenter presenter, boolean showErrCoverView){
            this.showErrCoverView = showErrCoverView;
            this.view = new WeakReference<>(presenter.getView());
        }

        @Override
        public void onCompleted() {

        }

        protected void dealWithException(Throwable e){
            if(e instanceof MVPlugFailReason){
                MVPlugFailReason api = (MVPlugFailReason) e;
                onResErrorMsg(api.getMessage(),api.getCode());
            }else if(e instanceof HttpException){
                HttpException exception = (HttpException) e;
                onResErrorMsg(exception.message(),exception.code());
            }else if(e instanceof SocketTimeoutException || e instanceof SocketException){
                onResErrorMsg("请求超时，请重试！",9999);
            }else{
                onResErrorMsg(e.toString(),-1);
            }
        }

        @Override
        public void onError(Throwable e) {
            Logger.e(e.toString());
            if (view == null || view.get() == null)return;
            view.get().dismissLoadingView(tag);
            if (showErrCoverView){
                if (MVPlug.isNetConnected(view.get().getContext())){
                    view.get().showServerErrorView(tag);
                }else {
                    view.get().showBadInternetView(tag);
                }
            }
            dealWithException(e);
        }

        @Override
        public void onNext(M m) {
            if (view == null || view.get() == null)return;
            onResult(m);
            if(!view.get().isCloseAutoDismiss()){
                view.get().dismissLoadingView(tag);
            }
        }

        public void showEmtyView() {
            view.get().showEmtyView(tag);
        }
    }

    public static abstract class ResRecyclerObserver<M> extends ResObserver<M> implements Observer<M> {

        private MVPlugAdapterPresenter adapterPresenter;

        public abstract void onResult(M m,int state);

        @Override
        public void onResult(M m) {

        }

        @Override
        public void onResErrorMsg(String resErrorMsg, int code) {

        }

        public ResRecyclerObserver(MVPlugPresenter presenter, int state) {
            super(presenter, state);
            this.view = new WeakReference<>(presenter.getView());
        }

        public ResRecyclerObserver(MVPlugPresenter presenter, int state, String tag) {
            super(presenter, state, tag);
            this.view = new WeakReference<>(presenter.getView());
            this.tag = tag;
        }

        public ResRecyclerObserver(MVPlugPresenter presenter, int state, String tag, MVPlugAdapterPresenter adapterPresenter) {
            super(presenter, state, tag);
            this.view = new WeakReference<>(presenter.getView());
            this.adapterPresenter = adapterPresenter;
            this.tag = tag;
        }

        public ResRecyclerObserver(MVPlugPresenter presenter) {
            super(presenter);
        }

        public ResRecyclerObserver(MVPlugPresenter presenter, int state, boolean showErrCoverView) {
            super(presenter,showErrCoverView);
        }

        @Override
        public void onError(Throwable e) {
            if (view == null || view.get() == null)return;
            view.get().getAdapterPresenter().setLoadMoreLocked(false);
            if (state != MVPlugConfig.STATE_LOADMORE){
                view.get().dismissLoadingView(tag);
                if (adapterPresenter != null){
                    adapterPresenter.stopRefreshing();
                }else{
                    view.get().getAdapterPresenter().stopRefreshing();
                }
                if (state == MVPlugConfig.STATE_LOADING){
                    if (MVPlug.isNetConnected(view.get().getContext())){
                        view.get().showServerErrorView(tag);
                    }else {
                        view.get().showBadInternetView(tag);
                    }
                }
            }else {
                view.get().getAdapterPresenter().onLoadMoreError();
            }
            dealWithException(e);
        }

        @Override
        public void onNext(M m) {
            if (view == null || view.get() == null)return;
            view.get().getAdapterPresenter().setLoadMoreLocked(false);
            onResult(m,state);
            if (!view.get().isCloseAutoDismiss() && state != MVPlugConfig.STATE_LOADMORE){
                view.get().getAdapterPresenter().stopRefreshing();
                view.get().dismissLoadingView(tag);
            }
        }
    }

}

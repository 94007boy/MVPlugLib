package com.djjie.mvpluglib.view;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.djjie.mvpluglib.adapter.MVPlugAdapterPresenter;
import com.djjie.mvpluglib.presenter.MVPlugPresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shf2 on 2016/12/20.
 */

public abstract class MVPlugView<P extends MVPlugPresenter,B extends ViewDataBinding> extends Fragment {

    protected IndicatedViewManager indicatedView;
    protected Context context;
    protected P presenter = createPresenter();
    public B viewBinding;
    private Activity act;
    private MVPlugAdapterPresenter adapterPresenter;
    private Map<String,MVPlugFragment> childFragmentMaps = new HashMap<>();
    private boolean closeAutoDismiss = false;

    public void setCloseAutoDismiss(boolean closeAutoDismiss) {
        this.closeAutoDismiss = closeAutoDismiss;
    }

    public boolean isCloseAutoDismiss() {
        return closeAutoDismiss;
    }

    public Activity getAct() {
        return act;
    }

    protected View getAnchorView(){
        return null;
    }

    public void managerView(String tag, MVPlugFragment fragment) {
        childFragmentMaps.put(tag,fragment);
    }

    public interface RefreshHeaderView{
        int refreshHeaderViewLayout();
        int headerViewBgColor();
        void doStartAnima(View headerView);
        void doStopAnima(View headerView);
    }


    public interface LoadingView {
        int loadingViewLayout();
        void doStartAnima(View loadingView);
    }

    public void setAdapterPresenter(MVPlugAdapterPresenter adapterPresenter) {
        this.adapterPresenter = adapterPresenter;
    }

    public MVPlugAdapterPresenter getAdapterPresenter() {
        return adapterPresenter;
    }

    public void initPresenter() {
        presenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        act = getActivity();
        context = act.getApplicationContext();
        initIndicateView(act);
        init();
        initPresenter();
    }

    private void initIndicateView(Activity act) {
        indicatedView = new IndicatedViewManager(act,getAnchorView() == null?getView():getAnchorView());
        indicatedView.setOnExceptionBtnClicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRetryBtnClick();
            }
        });
        indicatedView.setOnEmpertyBtnClicked(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onRetryBtnClick();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null)presenter.clearTask();
        act = null;
    }

    public void showLoadingView(String tag) {
        if (!TextUtils.isEmpty(tag) && childFragmentMaps.get(tag) != null){
            childFragmentMaps.get(tag).showLoadingLayout();
        }else {
            showLoadingView();
        }
    }

    public void showLoadingView() {
        indicatedView.showLoadingLayout();
    }

    public void dismissLoadingView(String tag) {
        if (!TextUtils.isEmpty(tag) && childFragmentMaps.get(tag) != null){
            childFragmentMaps.get(tag).hideAll();
        }else {
            dismissLoadingView();
        }
    }

    public void dismissLoadingView() {
        indicatedView.hideAll();
    }

    public void showToast(String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    public void showEmtyView(String tag) {
        if (!TextUtils.isEmpty(tag) && childFragmentMaps.get(tag) != null){
            childFragmentMaps.get(tag).showDataEmptyLayout();
        }else {
            showEmtyView();
        }
    }

    public void showEmtyView() {
        indicatedView.showDataEmptyLayout();
    }

    public void showServerErrorView(String tag) {
        if (!TextUtils.isEmpty(tag) && childFragmentMaps.get(tag) != null){
            childFragmentMaps.get(tag).showExceptionLayout();
        }else {
            showServerErrorView();
        }
    }

    public void showServerErrorView() {
        indicatedView.showExceptionLayout();
    }

    public void showBadInternetView(String tag) {
        if (!TextUtils.isEmpty(tag) && childFragmentMaps.get(tag) != null){
            childFragmentMaps.get(tag).showInternetOffLayout();
        }else {
            showBadInternetView();
        }
    }

    public void showBadInternetView() {
        indicatedView.showInternetOffLayout();
    }

    public void showSpecialViewByTag(String tag) {
        indicatedView.showViewByTag(tag);
    }

    public void setSpecialView(int layout, String tag) {
        indicatedView.initViews(layout,tag);
    }

    public View getSpecialViewByTag(String tag) {
        return indicatedView.getViewByTag(tag);
    }

    protected void onRetryBtnClick(){
        getPresenter().onLoad();
    }

    public abstract int getLayoutId();

    public abstract P createPresenter();

    public abstract void init();

    public P getPresenter() {
        return presenter;
    }

}

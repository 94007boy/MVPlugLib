package com.djjie.mvpluglib.view;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.djjie.mvpluglib.R;
import com.djjie.mvpluglib.adapter.MVPlugAdapterPresenter;
import com.djjie.mvpluglib.presenter.MVPlugPresenter;

/**
 * Created by shf2 on 2016/12/20.
 */

public abstract class MVPlugView<P extends MVPlugPresenter,B extends ViewDataBinding> extends Fragment {

    protected IndicatedViewManager indicatedView;
    protected Context context;
    protected P presenter = createPresenter();
    public B mViewBinding;
    private Activity act;
    private MVPlugAdapterPresenter adapterPresenter;

    public interface RefreshHeaderView{
        View onCreateView(ViewGroup root);
        int onSetHeaderViewBgColor();
        void onStartAnima(View headerView);
        void onStopAnima(View headerView);
    }

    public RefreshHeaderView getRefreshHeaderView() {
        return new RefreshHeaderView(){
            @Override
            public View onCreateView(ViewGroup root) {
                View headerView = LayoutInflater.from(context).inflate(R.layout.pull_header_view, root,false);
                return headerView;
            }

            @Override
            public int onSetHeaderViewBgColor() {
                return 0x50bfbfbf;
            }

            @Override
            public void onStartAnima(View headerView) {
                ImageView pullAnime = headerView.findViewById(R.id.pull_anime);
                AnimationDrawable animation = (AnimationDrawable) pullAnime.getDrawable();
                animation.start();
            }

            @Override
            public void onStopAnima(View headerView) {
                ImageView pullAnime = headerView.findViewById(R.id.pull_anime);
                AnimationDrawable animation = (AnimationDrawable) pullAnime.getDrawable();
                animation.stop();
            }
        };
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
        mViewBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (act == null){
            act = getActivity();
            context = act.getApplicationContext();
            initIndicateView(act);
            initView();
            initPresenter();
        }
    }

    private void initIndicateView(Activity act) {
        indicatedView = new IndicatedViewManager(act,getView());
        indicatedView.setOnExceptionBtnClicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onLoad();
            }
        });
        indicatedView.setOnEmpertyBtnClicked(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                presenter.empertyTask();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null)presenter.clearTask();
    }

    public void showLoadingView() {
        indicatedView.showLoadingLayout();
    }

    public void dismissLoadingView() {
        indicatedView.hideAll();
    }

    public void showToast(String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    public void showEmtyView() {
        indicatedView.showDataEmptyLayout();
    }

    public void showServerErrorView() {
        indicatedView.showExceptionLayout();
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

    public abstract int getLayoutId();

    public abstract P createPresenter();

    public abstract void initView();

    public P getPresenter() {
        return presenter;
    }
}

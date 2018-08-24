package com.djjie.mvpluglib.view;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.djjie.mvpluglib.MVPlug;
import com.djjie.mvpluglib.MVPlugConfig;
import com.djjie.mvpluglib.presenter.MVPlugPresenter;

import java.util.ArrayList;
import java.util.List;

public abstract class MVPlugFragment<P extends MVPlugPresenter,V extends ViewDataBinding> extends Fragment {

    public V viewBinding;
    public LayoutInflater inflater;
    private MVPlugConfig mvPlugConfig = MVPlug.getInstance().getConfiguration();
    private List<View> indicatedViews = new ArrayList<>();
    private final String TAG_INTERNET_OFF 	 =  "INTERNET_OFF";
    private final String TAG_LOADING_CONTENT =  "LOADING_CONTENT";
    private final String TAG_RES_ERROR_EXCEPTION =  "RES_ERROR_EXCEPTION";
    private final String TAG_EMPTY_DATA =  "EMPTY_DATA";
    private LinearLayout indicatedRoot;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        viewBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initIndicatedView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideAll();
    }

    private void initIndicatedView() {
        indicatedRoot = new LinearLayout(getContext());
        indicatedViews.clear();
        initViews();
    }

    private void initViews() {
        initView(mvPlugConfig.getBadInternetLayoutRes(),TAG_INTERNET_OFF);
        MVPlugView.LoadingView loadingView = mvPlugConfig.getLoadingViewObj();
        View mLayoutLoadingContent = initView(loadingView.loadingViewLayout(),TAG_LOADING_CONTENT);
        loadingView.doStartAnima(mLayoutLoadingContent);
        initView(mvPlugConfig.getResFailureLayoutRes(), TAG_RES_ERROR_EXCEPTION);
        initView(mvPlugConfig.getEmptyDataViewRes(), TAG_EMPTY_DATA);
    }

    private View initView(int layout, String tag){
        View view = inflater.inflate(layout,indicatedRoot,false);
        view.setTag(tag);
        indicatedViews.add(view);
        View exceButtonView = view.findViewById(mvPlugConfig.getExceptionViewBtnResId());
        View empertyButtonView = view.findViewById(mvPlugConfig.getEmptyDataViewBtnResId());

        if(exceButtonView != null)
            exceButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRetryBtnClick();
                }
            });
        if (empertyButtonView != null){
            empertyButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRetryBtnClick();
                }
            });
        }
        return view;
    }

    protected void onRetryBtnClick() {

    }

    private void show(String tag){
        indicatedRoot.removeAllViews();
        setAllChildViewVisible(View.GONE);
        for(View view : indicatedViews){
            String vieTag = view.getTag().toString();
            if(vieTag!= null && vieTag.equals(tag)){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                indicatedRoot.addView(view,params);
                break;
            }
        }
        ViewGroup.LayoutParams rootParams = getTargetView().getLayoutParams();
        rootParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        rootParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        if(getTargetView() == null)return;
        if (getTargetView().indexOfChild(indicatedRoot) > -1){
            getTargetView().removeView(indicatedRoot);
        }
        getTargetView().addView(indicatedRoot,rootParams);
    }

    private void setAllChildViewVisible(int visible) {
        if(getTargetView() == null)return;
        int childCount = getTargetView().getChildCount();
        for(int i = 0; i < childCount;i++){
            View child = getTargetView().getChildAt(i);
            child.setVisibility(visible);
        }
    }

    public void showDataEmptyLayout(){
        show(TAG_EMPTY_DATA);
    }

    public void showLoadingLayout(){
        show(TAG_LOADING_CONTENT);
    }

    public void showInternetOffLayout(){
        show(TAG_INTERNET_OFF);
    }

    public void showExceptionLayout(){
        show(TAG_RES_ERROR_EXCEPTION);
    }

    public abstract int getLayoutId();
    public abstract void init();

    public abstract ViewGroup getTargetView();

    public void hideAll(){
        indicatedRoot.removeAllViews();
        if (getTargetView() != null && getTargetView().indexOfChild(indicatedRoot) > -1){
            getTargetView().removeView(indicatedRoot);
        }
        setAllChildViewVisible(View.VISIBLE);
    }

    public void showToast(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
    }
}

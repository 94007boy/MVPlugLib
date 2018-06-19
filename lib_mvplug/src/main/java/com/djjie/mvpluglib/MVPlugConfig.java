package com.djjie.mvpluglib;

import android.content.Context;
import android.text.TextUtils;

import com.djjie.mvpluglib.model.ResConverterFactory;

import retrofit2.Converter;

/**
 * Created by shf2 on 2016/12/16.
 */

public class MVPlugConfig {

    private Builder mBuilder;
    public Converter.Factory defaultCoverterFactory = ResConverterFactory.create();// TODO 默认转换器
    public static final int STATE_LOADING = 0;
    public static final int STATE_REFRESH = 1;
    public static final int STATE_LOADMORE = 2;

    private MVPlugConfig(Builder builder) {
        if (builder == null) {
            throw new NullPointerException("builder can not be empty!");
        }
        this.mBuilder = builder;
    }

    public int TIMEOUT() {
        return mBuilder.TIMEOUT;
    }

    public int RES_SUCCESS_CODE() {
        return mBuilder.RES_SUCCESS_CODE;
    }

    public String BASE_URL() {
        if (TextUtils.isEmpty(mBuilder.BASE_URL)) {
            throw new NullPointerException("base url can not be empty!");
        }
        return mBuilder.BASE_URL;
    }

    public int getFooterLoadMoreLayout() {
        return mBuilder.footerLoadMoreLayoutRes;
    }

    public Converter.Factory getConverterFactory() {
        return mBuilder.converterFactory;
    }

    public int getFooterNoMoreLayout() {
        return mBuilder.footerNoMoreLayoutRes;
    }

    public int getFooterErrorLayout() {
        return mBuilder.footerErrorLayoutRes;
    }

    public int getBadInternetLayoutRes() {
        return mBuilder.badInternetLayoutRes;
    }

    public int getLoadingLayoutRes() {
        return mBuilder.loadingLayoutRes;
    }

    public int getResFailureLayoutRes() {
        return mBuilder.resFailureLayoutRes;
    }

    public int getExceptionViewBtnResId() {
        return mBuilder.exceptionViewBtnResId;
    }
    public int getEmptyDataViewBtnResId() {
        return mBuilder.emptyDataViewBtnResId;
    }

    public int loadingAnimaIvResId() {
        return mBuilder.loadingAnimaIvResId;
    }

    public int getEmptyDataViewRes() {
        return mBuilder.emptyDataViewRes;
    }

    public int getRecyclerHeaderViewBgColor() {
        return mBuilder.recyclerHeaderViewBgColor;
    }

    public int getRrecyclerHeaderViewResId() {
        return mBuilder.recyclerHeaderViewResId;
    }

    public boolean ismIsDebugMode() {
        return mBuilder.mIsDebugMode;
    }

    public OnResponseSuccess getOnResponseBody() {
        return mBuilder.onResponseBody;
    }

    public Context getContext() {
        return mBuilder.mContext;
    }

    public interface OnResponseSuccess {
        abstract String decodeResBody(String encodeRes);
    }

    public static final class Builder {
        private Context mContext;
        private int footerLoadMoreLayoutRes;
        private int footerNoMoreLayoutRes;
        private int footerErrorLayoutRes;
        private int badInternetLayoutRes;
        private int loadingLayoutRes;
        private int resFailureLayoutRes;
        private int emptyDataViewRes;
        private int exceptionViewBtnResId;
        private int emptyDataViewBtnResId;
        private int loadingAnimaIvResId;
        private int recyclerHeaderViewBgColor;
        private int recyclerHeaderViewResId;
        private static String BASE_URL;
        private static int TIMEOUT = 30;
        private static int RES_SUCCESS_CODE = 0;
        private boolean mIsDebugMode = false;
        private Converter.Factory converterFactory;


        private OnResponseSuccess onResponseBody;

        public Builder(Context context) {
            this.mContext = context.getApplicationContext();
        }

        public Builder BASE_URL(String BASE_URL) {
            this.BASE_URL = BASE_URL;
            return this;
        }

        public Builder TIMEOUT(int TIMEOUT) {
            this.TIMEOUT = TIMEOUT;
            return this;
        }

        public Builder RES_SUCCESS_CODE(int RES_SUCCESS_CODE) {
            this.RES_SUCCESS_CODE = RES_SUCCESS_CODE;
            return this;
        }

        public Builder onResponseSuccess(OnResponseSuccess onResponseBody) {
            this.onResponseBody = onResponseBody;
            return this;
        }

        public Builder converterFactory(Converter.Factory converterFactory) {
            this.converterFactory = converterFactory;
            return this;
        }

        public Builder recyclerHeaderViewBgColor(int recyclerHeaderViewBgColor) {
            this.recyclerHeaderViewBgColor = recyclerHeaderViewBgColor;
            return this;
        }

        public Builder recyclerHeaderViewResId(int recyclerHeaderViewResId) {
            this.recyclerHeaderViewResId = recyclerHeaderViewResId;
            return this;
        }

        public Builder footerLoadMoreLayout(int footerLoadMoreLayout) {
            this.footerLoadMoreLayoutRes = footerLoadMoreLayout;
            return this;
        }

        public Builder footerNoMoreLayout(int footerNoMoreLayout) {
            this.footerNoMoreLayoutRes = footerNoMoreLayout;
            return this;
        }

        public Builder footerErrorLayout(int footerErrorLayout) {
            this.footerErrorLayoutRes = footerErrorLayout;
            return this;
        }

        public Builder badInternetLayoutRes(int badInternetLayoutRes) {
            this.badInternetLayoutRes = badInternetLayoutRes;
            return this;
        }

        public Builder loadingLayoutRes(int loadingLayoutRes) {
            this.loadingLayoutRes = loadingLayoutRes;
            return this;
        }

        public Builder resFailureLayoutRes(int resFailureLayoutRes) {
            this.resFailureLayoutRes = resFailureLayoutRes;
            return this;
        }

        public Builder exceptionViewBtnResId(int exceptionViewBtnResId) {
            this.exceptionViewBtnResId = exceptionViewBtnResId;
            return this;
        }

        public Builder emptyDataViewBtnResId(int emptyDataViewBtnResId) {
            this.emptyDataViewBtnResId = emptyDataViewBtnResId;
            return this;
        }

        public Builder loadingAnimaIvResId(int loadingAnimaIvResId) {
            this.loadingAnimaIvResId = loadingAnimaIvResId;
            return this;
        }

        public Builder emptyDataViewRes(int emptyDataViewRes) {
            this.emptyDataViewRes = emptyDataViewRes;
            return this;
        }

        public Builder configDebugMode(boolean isDebugMode) {
            this.mIsDebugMode = isDebugMode;
            return this;
        }

        public MVPlugConfig build() {
            return new MVPlugConfig(this);
        }
    }

}

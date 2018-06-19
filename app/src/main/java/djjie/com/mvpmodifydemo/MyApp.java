package djjie.com.mvpmodifydemo;

import android.app.Application;

import com.djjie.mvpluglib.MVPlug;
import com.djjie.mvpluglib.MVPlugConfig;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MVPlugConfig.Builder builder = new MVPlugConfig.Builder(this);
        builder.BASE_URL("https://www.shengyin.shop/api/v1/")
                .configDebugMode(false)
                .RES_SUCCESS_CODE(200)
                .loadingLayoutRes(R.layout.app_res_loading_view)
                .badInternetLayoutRes(R.layout.app_res_bad_view)
                .resFailureLayoutRes(R.layout.app_res_error_view)
                .emptyDataViewRes(R.layout.app_res_common_view_empty)
                .footerErrorLayout(R.layout.app_res_footer_error)
                .footerLoadMoreLayout(R.layout.view_more)
                .footerNoMoreLayout(R.layout.view_nomore)
                .exceptionViewBtnResId(R.id.bt_data_error)
                .emptyDataViewBtnResId(R.id.empty_click_btn)
                .loadingAnimaIvResId(R.id.loading_anima)
                .onResponseSuccess(new MVPlugConfig.OnResponseSuccess() {
                    @Override
                    public String decodeResBody(String encodeRes) {
                        return encodeRes;
                    }
                });
        MVPlug.getInstance().init(builder.build());
    }
}

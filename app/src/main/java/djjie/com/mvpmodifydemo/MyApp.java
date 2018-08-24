package djjie.com.mvpmodifydemo;

import android.app.Application;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

import com.djjie.mvpluglib.MVPlug;
import com.djjie.mvpluglib.MVPlugConfig;
import com.djjie.mvpluglib.view.MVPlugView;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MVPlugConfig.Builder builder = new MVPlugConfig.Builder(this);
        builder.BASE_URL("http://47.100.23.124:8080/api/v1/")
                .configDebugMode(false)
                .RES_SUCCESS_CODE(200)
                .badInternetLayoutRes(R.layout.app_res_bad_view)
                .resFailureLayoutRes(R.layout.app_res_error_view)
                .emptyDataViewRes(R.layout.app_res_common_view_empty)
                .footerErrorLayout(R.layout.app_res_footer_error)
                .footerLoadMoreLayout(R.layout.view_more)
                .footerNoMoreLayout(R.layout.view_nomore)
                .emptyDataViewBtnResId(R.id.empty_click_btn)
                .viewRetryBtnResId(R.id.bt_data_error)
                .loadingViewObj(new MVPlugView.LoadingView() {
                    @Override
                    public int loadingViewLayout() {
                        return R.layout.app_res_loading_view;
                    }

                    @Override
                    public void doStartAnima(View loadingView) {
                        ImageView anim = loadingView.findViewById(R.id.loading_anima);
                        if (anim != null){
                            AnimationDrawable animationDrawable = (AnimationDrawable) anim.getDrawable();
                            animationDrawable.start();
                        }
                    }
                })
                .pullHeaderViewObj(new MVPlugView.RefreshHeaderView() {
                    @Override
                    public int refreshHeaderViewLayout() {
                        return com.djjie.mvpluglib.R.layout.pull_header_view;
                    }

                    @Override
                    public int headerViewBgColor() {
                        return 0x50bfbfbf;
                    }

                    @Override
                    public void doStartAnima(View headerView) {
                        ImageView pullAnime = headerView.findViewById(com.djjie.mvpluglib.R.id.pull_anime);
                        AnimationDrawable animation = (AnimationDrawable) pullAnime.getDrawable();
                        animation.start();
                    }

                    @Override
                    public void doStopAnima(View headerView) {
                        ImageView pullAnime = headerView.findViewById(com.djjie.mvpluglib.R.id.pull_anime);
                        AnimationDrawable animation = (AnimationDrawable) pullAnime.getDrawable();
                        animation.stop();
                    }
                })
                .onResponseSuccess(new MVPlugConfig.OnResponseSuccess() {
                    @Override
                    public String decodeResBody(String encodeRes) {
                        return encodeRes;
                    }
                });
        MVPlug.getInstance().init(builder.build());
    }
}

package djjie.com.mvpmodifydemo.main.detail;

import djjie.com.mvpmodifydemo.R;
import djjie.com.mvpmodifydemo.main.detail.model.DemoDetail;

public class DetailFragment extends DetailContract.View{

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragement_detail;
    }

    @Override
    public DetailPresenter createPresenter() {
        return new DetailPresenter();
    }

    @Override
    void onSuccess(DemoDetail demoDetail) {
        viewBinding.setDetail(demoDetail);
    }

    @Override
    public void init() {

    }

    @Override
    protected void onRetryBtnClick() {
        super.onRetryBtnClick();
        getPresenter().onLoad();
    }
}

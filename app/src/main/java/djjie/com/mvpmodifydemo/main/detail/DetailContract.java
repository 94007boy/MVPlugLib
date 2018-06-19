package djjie.com.mvpmodifydemo.main.detail;

import com.djjie.mvpluglib.presenter.MVPlugPresenter;
import com.djjie.mvpluglib.view.MVPlugView;

import djjie.com.mvpmodifydemo.databinding.FragementDetailBinding;
import djjie.com.mvpmodifydemo.main.detail.model.DemoDetail;

public interface DetailContract {

    abstract class View extends MVPlugView<DetailPresenter,FragementDetailBinding> {
        abstract void onSuccess(DemoDetail demoDetail);
    }

    abstract class Presenter extends MVPlugPresenter<DetailFragment>{

    }
}

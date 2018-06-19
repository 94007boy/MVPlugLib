package djjie.com.mvpmodifydemo.main.list;

import com.djjie.mvpluglib.presenter.MVPlugPresenter;
import com.djjie.mvpluglib.view.MVPlugView;

import djjie.com.mvpmodifydemo.databinding.FragementDemoBinding;

public interface DemoContract {
    abstract class  View extends MVPlugView<DemoPresenter,FragementDemoBinding>{
    }

    abstract class Presenter extends MVPlugPresenter<DemoFragment>{
        abstract void getDemoList(int state);
    }
}

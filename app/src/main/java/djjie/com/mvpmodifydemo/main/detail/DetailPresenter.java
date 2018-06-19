package djjie.com.mvpmodifydemo.main.detail;

import com.djjie.mvpluglib.MVPlugConfig;

import djjie.com.mvpmodifydemo.main.DemoRepository;
import djjie.com.mvpmodifydemo.main.detail.model.DemoDetail;

public class DetailPresenter extends DetailContract.Presenter{

    DemoRepository repository = DemoRepository.getInstance();

    @Override
    public void onLoad() {
        subscribe(MVPlugConfig.STATE_LOADING,repository.getDemoDetail(),new ResObserver<DemoDetail>(this){
            @Override
            public void onResult(DemoDetail demoDetail) {
                getView().onSuccess(demoDetail);
            }

            @Override
            public void onResErrorMsg(String resErrorMsg, int code) {
                getView().showToast(resErrorMsg);
            }
        });
    }
}

package djjie.com.mvpmodifydemo.main.list;

import com.djjie.mvpluglib.MVPlugConfig;
import com.djjie.mvpluglib.adapter.MVPlugAdapterPresenter;

import djjie.com.mvpmodifydemo.main.DemoRepository;
import djjie.com.mvpmodifydemo.main.list.model.DemoRes;

public class DemoPresenter extends DemoContract.Presenter {

    DemoRepository repository = DemoRepository.getInstance();

    @Override
    public void onLoad() {//列表页面载入
        repository.setPageFlag(null);
        getDemoList(MVPlugConfig.STATE_LOADING);
    }

    @Override
    public void onRecyclerRefreshData() {//列表刷新
        repository.setPageFlag(null);
        getDemoList(MVPlugConfig.STATE_REFRESH);
    }

    @Override
    public void onRecyclerLoadMoreData() {//列表加载更多
        getDemoList(MVPlugConfig.STATE_LOADMORE);
    }

    @Override
    void getDemoList(int state) {
        subscribe(state,repository.getDemoList(),new ResRecyclerObserver<DemoRes>(this,state){
            @Override
            public void onResult(DemoRes demoRes, int state) {
                if (demoRes != null && demoRes.getDataShows() != null && !demoRes.getDataShows().isEmpty()){
                    String pageFlag = demoRes.getDataShows().get(demoRes.getDataShows().size() - 1).getCreate_time();
                    repository.setPageFlag(pageFlag);//记录分页标示
                }
                MVPlugAdapterPresenter adapterPresenter = getView().mViewBinding.demoRecyclerView.getPresenter();
                if(state != MVPlugConfig.STATE_LOADMORE){//刷新
                    adapterPresenter.onDataRefresh(demoRes.getDataShows());
                }else {
                    adapterPresenter.onLoadMoreData(demoRes.getDataShows());
                }
            }

            @Override
            public void onResErrorMsg(String resErrorMsg, int code) {
                getView().showToast(resErrorMsg);
            }
        });
    }

}

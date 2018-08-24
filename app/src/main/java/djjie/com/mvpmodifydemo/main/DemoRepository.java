package djjie.com.mvpmodifydemo.main;

import com.djjie.mvpluglib.MVPlugConfig;
import com.djjie.mvpluglib.model.MVPlugModel;
import com.djjie.mvpluglib.model.MVPlugRepository;
import java.util.HashMap;
import java.util.Map;

import djjie.com.mvpmodifydemo.main.detail.model.DemoDetail;
import djjie.com.mvpmodifydemo.main.list.model.DemoRes;
import rx.Observable;

public class DemoRepository extends MVPlugRepository{
    private DemoApi demoApi;
    private static DemoRepository INSTANCE;

    private DemoRepository(){
        demoApi = MVPlugModel.getInstance().retrofit.create(DemoApi.class);
    }

    public static DemoRepository getInstance(){
        if (INSTANCE == null) INSTANCE = new DemoRepository();
        return INSTANCE;
    }

    public Observable<DemoRes> getDemoList(int state){
        Map<String,Object> map = new HashMap<>();
        map.put("version",1);
        map.put("clientType",1);
        map.put("deviceId","12345");
        map.put("deviceName","Samsung note 5");
        map.put("page_size","8");
        if(state == MVPlugConfig.STATE_LOADMORE){
            String  createTime = getPageFlagByTag("list") == null ? "":(String)getPageFlagByTag("list");
            map.put("createTime", createTime);
        }
        return demoApi.getDemoList("shows",map).map(new MVPlugModel.GetPureDataFunc<DemoRes>());
    }

    public Observable<DemoDetail> getDemoDetail(){
        Map<String,Object> map = new HashMap<>();
        map.put("version",1);
        map.put("clientType",1);
        map.put("deviceId","12345");
        map.put("deviceName","Samsung note 5");
        map.put("bid_id", "5b286b44b00841da18fa63d0");
        return demoApi.getDemoDetail("bid",map).map(new MVPlugModel.GetPureDataFunc<DemoDetail>());
    }
}

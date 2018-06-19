package djjie.com.mvpmodifydemo.main;

import com.djjie.mvpluglib.model.MVPlugBaseResp;

import java.util.Map;

import djjie.com.mvpmodifydemo.main.detail.model.DemoDetail;
import djjie.com.mvpmodifydemo.main.list.model.DemoRes;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface DemoApi {

    @GET("{path}")
    Observable<MVPlugBaseResp<DemoRes>> getDemoList(@Path("path") String path, @QueryMap Map<String, Object> map);

    @GET("{path}")
    Observable<MVPlugBaseResp<DemoDetail>> getDemoDetail(@Path("path") String path, @QueryMap Map<String, Object> map);

}

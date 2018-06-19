# MVPlugLib
如何用最低的改动成本，在老代码中使用MVP模式开发，本项目给出一种不对老代码有任何侵入性的插件式解决方案。by Databinding+MVP+Retrofit+Rxjava

## 实现功能
* 统一配置和管理异常视图，并根据网络请求结果自动切换展示对应的视图类型，主要有：载入中，网络异常，服务器异常，空视图，加载更多异常，没有更多了等,
并支持异常视图的多样化配置，可实现每个页面有独立的风格。
``` java
MVPlugConfig.Builder builder = new MVPlugConfig.Builder(this);
        builder.loadingLayoutRes(R.layout.app_res_loading_view)
                .badInternetLayoutRes(R.layout.app_res_bad_view)
                .resFailureLayoutRes(R.layout.app_res_error_view)
                .emptyDataViewRes(R.layout.app_res_common_view_empty)
                .footerErrorLayout(R.layout.app_res_footer_error)
                .footerLoadMoreLayout(R.layout.view_more)
                .footerNoMoreLayout(R.layout.view_nomore);
        MVPlug.getInstance().init(builder.build());
```

* 视图层基于Fragment，不强制要求继承任何BaseActivity.
``` java
public abstract class MVPlugView<P extends MVPlugPresenter,B extends ViewDataBinding> extends Fragment
```

* 列表视图基于RecyclerView进行二次封装，支持自定义的下拉刷新动画，提供加载更多功能的开关，简单列表无需写任何adapter和viewHolder代码，支持自定义复杂适配器
``` java
mViewBinding.demoRecyclerView.initItemView(this,R.layout.item_demo, BR.item);
mViewBinding.demoRecyclerView.enableLoadMore();
```
* 数据层，内部封装了基于常用的数据响应模型：code,msg，data 的解析

package djjie.com.mvpmodifydemo.main.list;

import android.content.Intent;

import com.djjie.mvpluglib.adapter.MVPlugAdapter;

import djjie.com.mvpmodifydemo.BR;
import djjie.com.mvpmodifydemo.R;
import djjie.com.mvpmodifydemo.main.detail.DetailActivity;

public class DemoFragment extends DemoContract.View {

    public static DemoFragment newInstance() {
        return new DemoFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragement_demo;
    }

    @Override
    public DemoPresenter createPresenter() {
        return new DemoPresenter();
    }

    @Override
    public void initView() {
        mViewBinding.demoRecyclerView.initItemView(this,R.layout.item_demo, BR.item);
        mViewBinding.demoRecyclerView.enableLoadMore();
        mViewBinding.demoRecyclerView.setOnItemClickListener(new MVPlugAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                startActivity(intent);
            }
        });
    }
}

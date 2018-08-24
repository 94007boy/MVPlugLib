package djjie.com.mvpmodifydemo.main.list;

import android.content.Intent;

import com.djjie.mvpluglib.adapter.MVPlugAdapter;
import com.djjie.mvpluglib.adapter.MVPlugRecyclerView;

import djjie.com.mvpmodifydemo.R;
import djjie.com.mvpmodifydemo.databinding.ItemDemoBinding;
import djjie.com.mvpmodifydemo.main.detail.DetailActivity;
import djjie.com.mvpmodifydemo.main.list.model.DemoItem;

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
    public void init() {
        viewBinding.demoRecyclerView.initItemView(this, R.layout.item_demo, new MVPlugRecyclerView.OnBindDefaultViewHolder<ItemDemoBinding,DemoItem>() {
            @Override
            public void setItemData(ItemDemoBinding dataBinding, DemoItem data, int position) {
                dataBinding.setItem(data);
            }
        });
        viewBinding.demoRecyclerView.enableLoadMore();
        viewBinding.demoRecyclerView.setOnItemClickListener(new MVPlugAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRetryBtnClick() {
        super.onRetryBtnClick();
        getPresenter().onLoad();
    }
}

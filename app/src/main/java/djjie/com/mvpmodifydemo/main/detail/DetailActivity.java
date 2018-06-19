package djjie.com.mvpmodifydemo.main.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import djjie.com.mvpmodifydemo.R;
import djjie.com.mvpmodifydemo.main.utils.FragmentUtil;

public class DetailActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        DetailFragment fragment = (DetailFragment)getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null){
            fragment = DetailFragment.newInstance();
            FragmentUtil.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
    }
}

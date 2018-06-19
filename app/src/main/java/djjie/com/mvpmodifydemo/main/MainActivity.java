package djjie.com.mvpmodifydemo.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import djjie.com.mvpmodifydemo.R;
import djjie.com.mvpmodifydemo.main.list.DemoFragment;
import djjie.com.mvpmodifydemo.main.utils.FragmentUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DemoFragment fragment = (DemoFragment)getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null){
            fragment = DemoFragment.newInstance();
            FragmentUtil.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
    }
}

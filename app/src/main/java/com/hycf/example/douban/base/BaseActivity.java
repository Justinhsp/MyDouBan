package com.hycf.example.douban.base;

import android.os.Bundle;

import com.aitangba.swipeback.SwipeBackActivity;
import com.hycf.example.douban.MyApplication;
import com.hycf.example.douban.R;

import butterknife.ButterKnife;

/**
 * Created by Hui on 2018/3/5.
 */

public abstract class BaseActivity<V,T extends BasePresenter<V>> extends SwipeBackActivity {
    protected T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (MyApplication.NIGHT_MODE) {
            setTheme(R.style.NightThemeTransNav);
        } else {
            setTheme(getIntent().getIntExtra("theme", R.style.MovieThemeTransNav));
        }
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
        ButterKnife.bind(this);
        presenter = initPresenter();
        presenter.attach((V) this);
        initView();
        //夜间模式颜色
        if (MyApplication.NIGHT_MODE) {
            findViewById(R.id.root_view).setBackgroundColor(getResources().getColor(R.color.colorNightBg));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dettach();
        presenter.onDestroy();
    }

    abstract protected T initPresenter();

    abstract protected int setLayoutId();

    abstract protected void initView();


}

package com.hycf.example.douban;

import com.aitangba.swipeback.ActivityLifecycleHelper;

import org.litepal.LitePalApplication;

/**
 * Created by Hui on 2018/3/5.
 */

public class MyApplication extends LitePalApplication {

    public static int REQUESTCODE = 0x00;

    public static int RESULTCODE = 0x01;

    //是否夜间模式
    public static boolean NIGHT_MODE = false;

    @Override
    public void onCreate() {
        super.onCreate();
        //滑动返回注册
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());
    }

}

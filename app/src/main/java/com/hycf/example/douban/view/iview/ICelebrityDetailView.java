package com.hycf.example.douban.view.iview;

import com.hycf.example.douban.model.CelebrityDetailModel;

/**
 * Created by Hui on 2018/3/6.
 */

public interface ICelebrityDetailView {

    /**
     * showLoading 方法主要用于页面请求数据时显示加载状态
     */
    void showLoading();

    /**
     * showEmpty 方法用于请求的数据为空的状态
     */
    void showEmpty();

    /**
     * showError 方法用于请求数据出错
     */
    void showError();

    /**
     * loadingComplete 方法用于请求数据完成
     */
    void showComplete(CelebrityDetailModel celebrityDetailModel);
}

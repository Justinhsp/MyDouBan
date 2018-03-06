package com.hycf.example.hsp.view.iview;

import com.hycf.example.hsp.model.MovieDetailModel;

/**
 * Created by Hui on 2018/3/5.
 */

public interface IMovieDetailView {
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
    void showError(boolean is404);

    /**
     * loadingComplete 方法用于请求数据完成
     */
    void showComplete(MovieDetailModel movieDetailModel);
}

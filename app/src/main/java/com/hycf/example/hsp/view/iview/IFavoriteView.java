package com.hycf.example.hsp.view.iview;

import com.hycf.example.hsp.model.MovieSubjectsModel;

import java.util.List;

/**
 * Created by Hui on 2018/3/6.
 */

public interface IFavoriteView {
    /**
     * showLoading 方法主要用于页面请求数据时显示加载状态
     */
    void showLoading();

    /**
     * showEmpty 方法用于请求的数据为空的状态
     */
    void showEmpty();

    /**
     * loadingComplete 方法用于请求数据完成
     */
    void showComplete(List<MovieSubjectsModel> movieSubjectsModels);
}

package com.hycf.example.douban.presenter.ipresenter;

import com.hycf.example.douban.model.MovieSubjectsModel;

/**
 * Created by Hui on 2018/3/5.
 */

public interface IMovieDetailPresenter {
    //加载数据
    void loadingData(String id);
    //判断是否已收藏
    boolean isFavorite(String id);
    //插入数据
    boolean saveFavorite(MovieSubjectsModel movieSubjectsModel);
    //删除数据
    boolean deleteFavorite(String id);
}

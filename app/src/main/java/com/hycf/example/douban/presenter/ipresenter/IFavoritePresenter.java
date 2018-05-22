package com.hycf.example.douban.presenter.ipresenter;

import java.util.ArrayList;

/**
 * Created by Hui on 2018/3/6.
 */

public interface IFavoritePresenter {
    //加载内容
    void loadData();

    //删除内容
    boolean deleteData(ArrayList<String> selectIds);
}

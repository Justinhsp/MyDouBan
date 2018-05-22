package com.hycf.example.douban.presenter;

import com.hycf.example.douban.base.BasePresenter;
import com.hycf.example.douban.presenter.ipresenter.ITagPresenter;
import com.hycf.example.douban.view.iview.ITagView;

/**
 * Created by Hui on 2018/3/7.
 */

public class TagPresenter extends BasePresenter<ITagView> implements ITagPresenter {
    @Override
    protected void onDestroy() {

    }
}

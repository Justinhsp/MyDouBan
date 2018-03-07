package com.hycf.example.hsp.presenter;

import com.hycf.example.hsp.base.BasePresenter;
import com.hycf.example.hsp.presenter.ipresenter.ITagPresenter;
import com.hycf.example.hsp.view.iview.ITagView;

/**
 * Created by Hui on 2018/3/7.
 */

public class TagPresenter extends BasePresenter<ITagView> implements ITagPresenter {
    @Override
    protected void onDestroy() {

    }
}

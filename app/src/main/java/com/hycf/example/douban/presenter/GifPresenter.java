package com.hycf.example.douban.presenter;

import android.util.Log;

import com.hycf.example.douban.base.BasePresenter;
import com.hycf.example.douban.http.NetWork;
import com.hycf.example.douban.model.GiftModel;
import com.hycf.example.douban.presenter.ipresenter.IGifPresenter;
import com.hycf.example.douban.view.iview.IGifView;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Hui on 2018/3/6.
 */

public class GifPresenter extends BasePresenter<IGifView> implements IGifPresenter {

    private static final String TAG = "GifPresenter";


    //起始页
    private int page=1;


    private Observer<GiftModel> observer=new Observer<GiftModel>() {
        /**
         * 请求数据时
         */
        @Override
        public void onCompleted() {

        }

        /**
         * 请求失败
         * @param e
         */
        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: ",e );
            mView.showError();
        }

        /**
         * RxJava 的事件回调方法普通事件onNext()
         * @param giftModel
         */
        @Override
        public void onNext(GiftModel giftModel) {
             if (giftModel.getResults().size()>0){
                 mView.showComplete((ArrayList<?>) giftModel.getResults());
                 page++;
            }else {
                 mView.showEmpty();
             }
        }
    };



    @Override
    public void loadingData(boolean isRefresh) {
        mView.showLoading();
        if (isRefresh){
            page=1;
        }
        subscription= NetWork.getGankApi()
                .getGifts(30,page)
                .subscribeOn(Schedulers.io())                //指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())  // 指定 Subscriber 的回调发生在主线程
                .subscribe(observer);
    }

    @Override
    protected void onDestroy() {
       unsubscribe();
    }
}

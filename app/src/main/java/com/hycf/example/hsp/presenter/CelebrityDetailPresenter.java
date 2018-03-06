package com.hycf.example.hsp.presenter;

import android.util.Log;

import com.hycf.example.hsp.base.BasePresenter;
import com.hycf.example.hsp.http.NetWork;
import com.hycf.example.hsp.model.CelebrityDetailModel;
import com.hycf.example.hsp.presenter.ipresenter.ICelebrityDetailPresenter;
import com.hycf.example.hsp.view.iview.ICelebrityDetailView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Hui on 2018/3/6.
 */

public class CelebrityDetailPresenter extends BasePresenter<ICelebrityDetailView> implements ICelebrityDetailPresenter {

    private static final String TAG = "CelebrityDetailPresente";


    private Observer<CelebrityDetailModel> observer=new Observer<CelebrityDetailModel>() {
        /**
         * 请求数据完成
         */
        @Override
        public void onCompleted() {
        }

        /**
         * 请求数据失败
         * @param e
         */
        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: ",e );
            mView.showError();
        }

        /**
         * RxJava 的事件回调方法普通事件onNext()
         * @param celebrityDetailModel
         */
        @Override
        public void onNext(CelebrityDetailModel celebrityDetailModel) {
               mView.showComplete(celebrityDetailModel);
        }
    };


    /**
     * 加载数据
     * @param id
     */
    @Override
    public void loadingData(String id) {
         mView.showLoading();
         subscription= NetWork.getDouBanApi()
                 .getCelebrityDetail(id)
                 .subscribeOn(Schedulers.io())               //指定 subscribe() 发生在 IO 线程
                 .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                 .subscribe(observer);
    }

    @Override
    protected void onDestroy() {
       unsubscribe();
    }
}

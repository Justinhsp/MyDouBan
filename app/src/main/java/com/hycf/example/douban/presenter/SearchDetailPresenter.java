package com.hycf.example.douban.presenter;

import android.util.Log;

import com.hycf.example.douban.base.BasePresenter;
import com.hycf.example.douban.http.NetWork;
import com.hycf.example.douban.model.MovieModel;
import com.hycf.example.douban.presenter.ipresenter.ISearchDetailPresenter;
import com.hycf.example.douban.view.iview.ISearchDetailView;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Hui on 2018/3/6.
 */

public class SearchDetailPresenter extends BasePresenter<ISearchDetailView> implements ISearchDetailPresenter {

    private static final String TAG = "SearchDetailPresenter";

    private int start=0;

    private Observer<MovieModel> observer=new Observer<MovieModel>() {
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
         * @param movieModel
         */
        @Override
        public void onNext(MovieModel movieModel) {
                if (movieModel.getSubjects().size()>0){
                    mView.showComplete((ArrayList<?>) movieModel.getSubjects());
                    start=start+movieModel.getSubjects().size();
                }else {
                    mView.showEmpty();
                }
        }
    };



    @Override
    public void search(String keyword, boolean isTag, boolean isRefresh) {
            mView.showLoading();
            if (isRefresh){
                start=0;
            }
            if (isTag){
                subscription= NetWork.getDouBanApi()
                        .searchTag(keyword,start,10)
                        .subscribeOn(Schedulers.io())                //指定 subscribe() 发生在 IO 线程
                        .observeOn(AndroidSchedulers.mainThread())  // 指定 Subscriber 的回调发生在主线程
                        .subscribe(observer);
            }else {
                subscription = NetWork.getDouBanApi()
                        .searchKeyword(keyword, start, 20)
                        .subscribeOn(Schedulers.io())               //指定 subscribe() 发生在 IO 线程
                        .observeOn(AndroidSchedulers.mainThread())  // 指定 Subscriber 的回调发生在主线程
                        .subscribe(observer);
            }
    }


    @Override
    protected void onDestroy() {
       unsubscribe();
    }
}

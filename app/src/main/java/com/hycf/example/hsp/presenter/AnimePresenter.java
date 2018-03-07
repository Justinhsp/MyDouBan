package com.hycf.example.hsp.presenter;

import android.util.Log;

import com.hycf.example.hsp.base.BasePresenter;
import com.hycf.example.hsp.http.NetWork;
import com.hycf.example.hsp.model.MovieModel;
import com.hycf.example.hsp.presenter.ipresenter.IAnimePresenter;
import com.hycf.example.hsp.view.iview.IAnimeView;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Hui on 2018/3/7.
 */

public class AnimePresenter extends BasePresenter<IAnimeView> implements IAnimePresenter{

    private static final String TAG = "AnimePresenter";

    //关键字
    private int start=0;


    private Observer<MovieModel> observer=new Observer<MovieModel>() {
        /**
         * 请求完成
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
         * @param movieModel
         */
        @Override
        public void onNext(MovieModel movieModel) {
               if (movieModel.getSubjects().size()>0){
                   mView.showComplete((ArrayList<?>)movieModel.getSubjects());
                   start=start+movieModel.getSubjects().size();
               }else {
                   mView.showEmpty();
               }
        }
    };


    /**
     * 加载数据
     */
    @Override
    public void loadingData() {
        mView.showLoading();
        subscription= NetWork.getDouBanApi()
                .searchTag("动漫",start,10)
                .subscribeOn(Schedulers.io())                  //指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())    // 指定 Subscriber 的回调发生在主线程
                .subscribe(observer);
    }

    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
         unsubscribe();
    }
}

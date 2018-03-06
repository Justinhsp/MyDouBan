package com.hycf.example.hsp.presenter;

import android.util.Log;

import com.hycf.example.hsp.base.BasePresenter;
import com.hycf.example.hsp.http.NetWork;
import com.hycf.example.hsp.model.MovieModel;
import com.hycf.example.hsp.model.TagData;
import com.hycf.example.hsp.presenter.ipresenter.IMoviePresenter;
import com.hycf.example.hsp.view.iview.IMovieView;

import java.util.ArrayList;
import java.util.Random;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Hui on 2018/3/5.
 */

public class MoviePresenter extends BasePresenter<IMovieView> implements IMoviePresenter {

    private static final String TAG = "MoviePresenter";

    private int start=0;
    //关键字
    private String artist;


    private Observer<MovieModel> observer=new Observer<MovieModel>() {
        /**
         * 请求数据完成
         */
        @Override
        public void onCompleted() {
        }

        /**
         * 请求数据完成
         * @param e
         */
        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: ",e);
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
    public void loadingData(boolean isRefresh) {
        mView.showLoading();
        if (isRefresh) {
            artist = TagData.artists[new Random().nextInt(36)];
            start = 0;
        }
        subscription = NetWork.getDouBanApi()
                .searchTag(artist, start, 15)
                .subscribeOn(Schedulers.io())               //指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(observer);
    }

    @Override
    protected void onDestroy() {
         unsubscribe();
    }
}

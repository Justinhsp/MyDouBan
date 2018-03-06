package com.hycf.example.hsp.presenter;

import android.util.Log;

import com.hycf.example.hsp.base.BasePresenter;
import com.hycf.example.hsp.http.NetWork;
import com.hycf.example.hsp.model.MovieDetailModel;
import com.hycf.example.hsp.model.MovieSubjectsModel;
import com.hycf.example.hsp.presenter.ipresenter.IMovieDetailPresenter;
import com.hycf.example.hsp.view.iview.IMovieDetailView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Hui on 2018/3/5.
 */

public class MovieDetailPresenter extends BasePresenter<IMovieDetailView> implements IMovieDetailPresenter {

    private static final String TAG = "MovieDetailPresenter";


    private Observer<MovieDetailModel> observer=new Observer<MovieDetailModel>() {
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
            Log.e(TAG, "onError: ", e);
            if (e.getMessage().contains("HTTP 404 Not Found")) {
                mView.showError(true);
            } else {
                mView.showError(false);
            }
        }


        /**
         * RxJava 的事件回调方法普通事件onNext()
         * @param movieDetailModel
         */
        @Override
        public void onNext(MovieDetailModel movieDetailModel) {
            mView.showComplete(movieDetailModel);
            //数据库不存在此数据就插入
            if (DataSupport.where("movie_id = ?", movieDetailModel.getMovie_id()).find(MovieDetailModel.class).size() < 1) {
                for (MovieDetailModel.CastsBean castsBean : movieDetailModel.getCasts()) {
                    if (castsBean.getAvatars() != null) {
                        castsBean.getAvatars().save();
                        castsBean.save();
                    }
                }
                for (MovieDetailModel.DirectorsBean directorsBean : movieDetailModel.getDirectors()) {
                    if (directorsBean.getAvatars() != null) {
                        directorsBean.getAvatars().save();
                        directorsBean.save();
                    }
                }
                movieDetailModel.getImages().save();
                movieDetailModel.getRating().save();
                movieDetailModel.save();
            }
        }
    };


    /**
     * 加载数据
     * @param id
     */
    @Override
    public void loadingData(String id) {
        mView.showLoading();
        //先检测数据库数据库是否存在数据
        final MovieDetailModel movieDetailModel = DataSupport.where("movie_id = ?", id).findFirst(MovieDetailModel.class, true);
        if (movieDetailModel == null) {
            //无则网络请求
            subscription = NetWork.getDouBanApi()
                    .getMovieDetail(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } else {
            //有则直接显示数据
            Observable.create(new Observable.OnSubscribe<MovieDetailModel>() {
                @Override
                public void call(Subscriber<? super MovieDetailModel> subscriber) {
                    //先相继取出内部类中相关联表的数据
                    //取出演员信息
                    List<MovieDetailModel.CastsBean> castsBeans = new ArrayList<>();
                    for (MovieDetailModel.CastsBean castsBean : movieDetailModel.getCasts()) {
                        castsBeans.add(DataSupport.where("cast_id = ?", castsBean.getId()).findFirst(MovieDetailModel.CastsBean.class, true));
                    }
                    movieDetailModel.setCasts(castsBeans);
                    //取出导演信息
                    List<MovieDetailModel.DirectorsBean> directorsBeans = new ArrayList<>();
                    for (MovieDetailModel.DirectorsBean directorsBean : movieDetailModel.getDirectors()) {
                        directorsBeans.add(DataSupport.where("director_id = ?", directorsBean.getId()).findFirst(MovieDetailModel.DirectorsBean.class, true));
                    }
                    movieDetailModel.setDirectors(directorsBeans);
                    //显示数据
                    subscriber.onNext(movieDetailModel);
                }
            })
                    .subscribeOn(Schedulers.io())               // 指定 subscribe() 发生在 IO 线程
                    .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                    .subscribe(new Action1<MovieDetailModel>() {
                        @Override
                        public void call(MovieDetailModel movieDetailModel) {
                            //显示数据
                            mView.showComplete(movieDetailModel);
                        }
                    });
            }
    }


    /**
     * 是否已经收藏
     * @param id
     * @return
     */
    @Override
    public boolean isFavorite(String id) {
        return DataSupport.where("movie_id = ?", id).find(MovieSubjectsModel.class).size() > 0;
    }


    /**
     * 收藏
     * @param movieSubjectsModel
     * @return
     */
    @Override
    public boolean saveFavorite(MovieSubjectsModel movieSubjectsModel) {
        try {
            for (MovieSubjectsModel.CastsBean castsBean : movieSubjectsModel.getCasts()) {
                if (castsBean.getAvatars() != null) {
                    castsBean.getAvatars().saveThrows();
                    castsBean.saveThrows();
                }
            }
            for (MovieSubjectsModel.DirectorsBean directorsBean : movieSubjectsModel.getDirectors()) {
                if (directorsBean.getAvatars() != null) {
                    directorsBean.getAvatars().saveThrows();
                    directorsBean.saveThrows();
                }
            }
            movieSubjectsModel.getImages().saveThrows();
            movieSubjectsModel.getRating().saveThrows();
            movieSubjectsModel.saveThrows();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 取消收藏
     * @param id
     * @return
     */
    @Override
    public boolean deleteFavorite(String id) {
        return DataSupport.deleteAll(MovieSubjectsModel.class, "movie_id = ?", id) > 0;
    }


    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
          unsubscribe();
    }
}

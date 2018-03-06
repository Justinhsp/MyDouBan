package com.hycf.example.hsp.http;

import com.hycf.example.hsp.model.CelebrityDetailModel;
import com.hycf.example.hsp.model.MovieDetailModel;
import com.hycf.example.hsp.model.MovieModel;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Hui on 2018/3/5.
 */

public interface DouBanApi {

    @GET("top250")
    Observable<MovieModel> getTop250(@Query("start") int start, @Query("count") int count);

    @GET("search")
    Observable<MovieModel> searchKeyword(@Query("q") String q, @Query("start") int start, @Query("count") int count);

    @GET("search")
    Observable<MovieModel> searchTag(@Query("tag") String tag, @Query("start") int start, @Query("count") int count);

    @GET("subject/{id}")
    Observable<MovieDetailModel> getMovieDetail(@Path("id") String id);

    @GET("celebrity/{id}")
    Observable<CelebrityDetailModel> getCelebrityDetail(@Path("id") String id);

}

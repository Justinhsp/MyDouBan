package com.hycf.example.douban.http;

import com.hycf.example.douban.model.GiftModel;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Hui on 2018/3/5.
 */

public interface GankApi {
    @GET("data/福利/{number}/{page}")
    Observable<GiftModel> getGifts(@Path("number") int number, @Path("page") int page);
}

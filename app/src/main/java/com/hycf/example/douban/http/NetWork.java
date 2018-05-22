package com.hycf.example.douban.http;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hui on 2018/3/5.
 */

public class NetWork {

    private static  DouBanApi douBanApi;
    private static GankApi gankApi;

    public static  DouBanApi getDouBanApi(){
        if (douBanApi==null){
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl("https://api.douban.com/v2/movie/")
                    //使用工厂模式创建Gson解析器
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            douBanApi=retrofit.create(DouBanApi.class);
        }
        return  douBanApi;
    }


    public static  GankApi getGankApi(){
        if (gankApi==null){
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl("http://gank.io/api/")
                    //使用工厂模式创建Gson解析器
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            gankApi=retrofit.create(GankApi.class);
        }
        return gankApi;
    }

}

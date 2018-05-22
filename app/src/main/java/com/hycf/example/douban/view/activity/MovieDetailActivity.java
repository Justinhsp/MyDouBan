package com.hycf.example.douban.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hycf.example.douban.MyApplication;
import com.hycf.example.douban.R;
import com.hycf.example.douban.adapter.MovieDetailAdapter;
import com.hycf.example.douban.base.BaseActivity;
import com.hycf.example.douban.model.MovieDetailModel;
import com.hycf.example.douban.model.MovieSubjectsModel;
import com.hycf.example.douban.presenter.MovieDetailPresenter;
import com.hycf.example.douban.view.iview.IMovieDetailView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Hui on 2018/3/5.
 */

public class MovieDetailActivity extends BaseActivity<IMovieDetailView, MovieDetailPresenter> implements IMovieDetailView, Toolbar.OnMenuItemClickListener, View.OnClickListener, MovieDetailAdapter.mOnItemClickListener {
    @BindView(R.id.iv_movie_detail)
    ImageView ivMovieDetail;
    @BindView(R.id.toolbar_movie_detail)
    Toolbar toolbarMovieDetail;
    @BindView(R.id.collapsing_movie_detail)
    CollapsingToolbarLayout collapsingMovieDetail;
    @BindView(R.id.tv_movie_detail_year)
    TextView tvMovieDetailYear;
    @BindView(R.id.tv_movie_detail_country)
    TextView tvMovieDetailCountry;
    @BindView(R.id.tv_movie_detail_type)
    TextView tvMovieDetailType;
    @BindView(R.id.tv_movie_detail_average)
    TextView tvMovieDetailAverage;
    @BindView(R.id.rv_movie_detail_director)
    RecyclerView rvMovieDetailDirector;
    @BindView(R.id.rv_movie_detail_cast)
    RecyclerView rvMovieDetailCast;
    @BindView(R.id.tv_movie_detail_summary)
    TextView tvMovieDetailSummary;
    @BindView(R.id.ll_movie_detail)
    LinearLayout llMovieDetail;
    @BindView(R.id.root_view)
    CoordinatorLayout rootView;
    @BindView(R.id.srl_movie_detail)
    SwipeRefreshLayout srlMovieDetail;


    private static final String TAG = "MovieDetailActivity";
    //判断电影是否收藏过
    private boolean isFavorite=false;
    //电影条目
    private MovieSubjectsModel movieSubjectsModel;

    /**
     * 返回对应的Presenter
     *
     * @return
     */
    @Override
    protected MovieDetailPresenter initPresenter() {
        return new MovieDetailPresenter();
    }

    /**
     * 返回对应布局文件
     *
     * @return
     */
    @Override
    protected int setLayoutId() {
        return R.layout.activity_movie_detail;
    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {
        //加载图片
        Glide.with(this).load(getIntent().getStringExtra("img_url")).into(ivMovieDetail);
        //设置标题
        collapsingMovieDetail.setTitle(getIntent().getStringExtra("title"));
        setSupportActionBar(toolbarMovieDetail);
        toolbarMovieDetail.setNavigationIcon(R.drawable.ic_back_white_24dp);
        //配置下拉刷新控件
        srlMovieDetail.setEnabled(false);
        srlMovieDetail.setColorSchemeColors(getIntent().getIntExtra("color", getResources().getColor(R.color.colorMovie)));
        //检查是否夜间模式
        checkIsNightMode();
        toolbarMovieDetail.setOnMenuItemClickListener(this);//子菜单点击事件 , 即收藏
        toolbarMovieDetail.setNavigationOnClickListener(this);//导航点击事件 , 即返回
        //配置RecycleView
        initRecycleView(rvMovieDetailDirector);
        initRecycleView(rvMovieDetailCast);
        //获取条目信息
        movieSubjectsModel = (MovieSubjectsModel) getIntent().getSerializableExtra("movieSubject");
        //加载数据
        presenter.loadingData(getIntent().getStringExtra("id"));
    }

    /**
     * 加载toolbar菜单布局
     * 只会调用一次，他只会在Menu显示之前去调用一次，之后就不会在去调用。
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单文件
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }
    /*onPrepareOptionsMenu是每次在display Menu之前，都会去调用，
    只要按一次Menu按鍵，就会调用一次。所以可以在这里动态的改变menu*/
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //判断是否已收藏
        if (presenter.isFavorite(getIntent().getStringExtra("id"))) {
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_favorite_white_24dp);
            isFavorite = true;
        } else {
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_favorite_border_white_24dp);
            isFavorite = false;
        }
        return super.onPrepareOptionsMenu(menu);
    }


    /**
     * recycleView相关配置
     * @param recyclerView
     */
    private void initRecycleView(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);//设置布局管理器
       //1.为了更好的适应NestedScrollView的特性，应该把布局文件的根布局设为CoordinatorLayout；
       //2.给NestedScrollView设置behavior，即 app:layout_behavior="@string/appbar_scrolling_view_behavior"
       //3.RecyclerView的高度设为match parent或者wrap content
       // 这样就可以保证RecyclerView的内容可以显示出来，但是为了解决滑动不流畅的问题，还需要最后一步:
        recyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * 检查是否夜间模式
     */
    private void checkIsNightMode() {
        if (MyApplication.NIGHT_MODE) {
            srlMovieDetail.setColorSchemeColors(getResources().getColor(R.color.colorNight));
            tvMovieDetailYear.setTextColor(getResources().getColor(R.color.color999));
            tvMovieDetailCountry.setTextColor(getResources().getColor(R.color.color999));
            tvMovieDetailType.setTextColor(getResources().getColor(R.color.color999));
            tvMovieDetailSummary.setTextColor(getResources().getColor(R.color.color999));
        }
    }

    /**
     * 加载数据时
     */
    @Override
    public void showLoading() {
        srlMovieDetail.setRefreshing(true);
    }

    /**
     * 加载数据为空时
     */
    @Override
    public void showEmpty() {
        srlMovieDetail.setRefreshing(false);
    }


    /**
     * 加载数据失败时
     *
     * @param is404
     */
    @Override
    public void showError(boolean is404) {
        srlMovieDetail.setRefreshing(false);
        if (is404) {
            Toast.makeText(this, getText(R.string.error_tips3), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getText(R.string.error_tips), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 加载数据完成时
     *
     * @param movieDetailModel
     */
    @Override
    public void showComplete(MovieDetailModel movieDetailModel) {
        llMovieDetail.setVisibility(View.VISIBLE);
        srlMovieDetail.setRefreshing(false);
        //年份
        tvMovieDetailYear.setText(String.format(getResources().getString(R.string.detail_year), movieDetailModel.getYear()));
        //国家地区
        String country = "";
        for (int i = 0; i < movieDetailModel.getCountries().size(); i++) {
            if (i == movieDetailModel.getCountries().size() - 1) {
                country = country + movieDetailModel.getCountries().get(i);
            } else {
                country = country + movieDetailModel.getCountries().get(i) + "、";
            }
        }
        tvMovieDetailCountry.setText(String.format(getResources().getString(R.string.detail_country), country));
        //类型
        String type = "";
        for (int i = 0; i < movieDetailModel.getGenres().size(); i++) {
            if (i == movieDetailModel.getGenres().size() - 1) {
                type = type + movieDetailModel.getGenres().get(i);
            } else {
                type = type + movieDetailModel.getGenres().get(i) + "、";
            }
        }
        tvMovieDetailType.setText(String.format(getResources().getString(R.string.detail_type), type));
        //分数
        tvMovieDetailAverage.setText(String.format(getResources().getString(R.string.average), "" + movieDetailModel.getRating().getAverage()));
        //导演
        ArrayList<String> imgs = new ArrayList<>();//存储导演图片的集合
        ArrayList<String> names = new ArrayList<>();//存储导演名字的集合
        ArrayList<String> ids = new ArrayList<>();  //存储导演Id的集合
        for (MovieDetailModel.DirectorsBean directorsBean : movieDetailModel.getDirectors()) {
            if (directorsBean.getAvatars() != null && directorsBean.getId() != null) {
                imgs.add(directorsBean.getAvatars().getMedium());
                names.add(directorsBean.getName());
                ids.add(directorsBean.getId());
            }
        }
        MovieDetailAdapter directorsAdapter = new MovieDetailAdapter(imgs, names, ids);
        //导演Icon点击监听
        rvMovieDetailDirector.setAdapter(directorsAdapter);
        directorsAdapter.setmOnItemClickListener(this);
        //演员
        imgs = new ArrayList<>();   //存储演员图片的集合
        names = new ArrayList<>(); //存储演员名字的集合
        ids = new ArrayList<>();   //存储演员Id的集合
        for (MovieDetailModel.CastsBean castsBean : movieDetailModel.getCasts()) {
            if (castsBean.getAvatars() != null && castsBean.getId() != null) {
                imgs.add(castsBean.getAvatars().getMedium());
                names.add(castsBean.getName());
                ids.add(castsBean.getId());
            }
        }
        MovieDetailAdapter castsAdapter = new MovieDetailAdapter(imgs, names, ids);
        rvMovieDetailCast.setAdapter(castsAdapter);
        //演员Icon点击监听
        castsAdapter.setmOnItemClickListener(this);
        //简介
        tvMovieDetailSummary.setText(String.format(getResources().getString(R.string.detail_summary), movieDetailModel.getSummary()));
    }


    /**
     * 点击图片,查看图片
     */
    @OnClick(R.id.iv_movie_detail)
    public void onViewClicked() {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("img_url", getIntent().getStringExtra("img_url"));
        startActivity(intent);
    }

    /**
     * 收藏
     * toolbar 菜单点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (srlMovieDetail.isRefreshing()){
            //加载数据时不允许操作
            Toast.makeText(this,getResources().getString(R.string.favorite_tip3), Toast.LENGTH_SHORT).show();
        }else {
            //判断是否已经收藏
            if (!isFavorite){
                //没有收藏就插入数据
                if (presenter.saveFavorite(movieSubjectsModel)){
                    item.setIcon(R.drawable.ic_favorite_white_24dp);
                    isFavorite=true;
                }else {
                    Toast.makeText(this,R.string.favorite_tip1, Toast.LENGTH_SHORT).show();
                }
            }else {
                //已经收藏过则删除数据
                if (presenter.deleteFavorite(getIntent().getStringExtra("id"))){
                    item.setIcon(R.drawable.ic_favorite_border_white_24dp);
                    isFavorite = false;
                }else {
                    Toast.makeText(this, getResources().getString(R.string.favorite_tip2), Toast.LENGTH_SHORT).show();
                }
            }
        }
        return true;
    }


    /**
     * 导演、演员Icon点击事件
     * @param id
     * @param name
     */
    @Override
    public void onItemClick(String id, String name) {
        Intent intent = new Intent(this, CelebrityDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("theme", getIntent().getIntExtra("theme", R.style.MovieThemeTransNav));
        intent.putExtra("title", name);
        intent.putExtra("color", getIntent().getIntExtra("color", getResources().getColor(R.color.colorMovie)));
        startActivity(intent);
    }


    /**
     * 返回
     * @param view
     */
    @Override
    public void onClick(View view) {
        finish();
    }

}

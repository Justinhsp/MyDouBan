package com.hycf.example.douban.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hycf.example.douban.R;
import com.hycf.example.douban.adapter.MovieAdapter;
import com.hycf.example.douban.base.BaseLazyFragment;
import com.hycf.example.douban.model.MovieSubjectsModel;
import com.hycf.example.douban.msg.MessageEvent;
import com.hycf.example.douban.presenter.AnimePresenter;
import com.hycf.example.douban.view.activity.MovieDetailActivity;
import com.hycf.example.douban.view.iview.IAnimeView;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;

/**
 * Created by Hui on 2018/3/5.
 */

public class AnimeFragment extends BaseLazyFragment<IAnimeView, AnimePresenter> implements IAnimeView, MovieAdapter.IOnItemClickListener, View.OnClickListener {
    @BindView(R.id.tv_tip_anime)
    TextView tvTipAnime;
    @BindView(R.id.rv_anime)
    RecyclerView rvAnime;
    @BindView(R.id.srl_anime)
    SwipeRefreshLayout srlAnime;


    private static final String TAG = "AnimeFragment";
    //判断是否第一次显示
    private boolean isFirst = true;
    //电影条目列表
    private ArrayList<MovieSubjectsModel> movieModelBeans = new ArrayList<>();
    //添加footview的适配器
    private HeaderAndFooterWrapper animeWrapperAdapter;
    //适配器
    private MovieAdapter animeAdapter;
    //footView文字显示
    private TextView animeFooterViewInfo;

    /**
     * 返回对应的Presenter
     *
     * @return
     */
    @Override
    protected AnimePresenter initPresenter() {
        return new AnimePresenter();
    }


    /**
     * 返回对应布局文件
     *
     * @return
     */
    @Override
    protected int setLayoutId() {
        return R.layout.main_anime;
    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {
        //配置下拉刷新控件
        srlAnime.setEnabled(false);
        srlAnime.setColorSchemeResources(R.color.colorAnime);
        //适配器
        animeAdapter = new MovieAdapter(movieModelBeans);
        animeWrapperAdapter = new HeaderAndFooterWrapper(animeAdapter);
        animeAdapter.setiOnItemClickListener(this);
        //添加footerView
        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.footerview, null);
        animeFooterViewInfo = (TextView) footerView.findViewById(R.id.footerview_info);
        animeWrapperAdapter.addFootView(footerView);
        //配置RecyclerView
        rvAnime.setLayoutManager(new LinearLayoutManager(getContext()));//设置list列风格
        rvAnime.setAdapter(animeWrapperAdapter);
        //监听列表上拉
        rvAnime.addOnScrollListener(mOnScrollListener);
        //监听点击提示文本
        tvTipAnime.setOnClickListener(this);
    }

    /**
     * 实现懒加载,当屏幕显示这个界面的时候才会触发次方法
     */
    @Override
    protected void lazyLoad() {
        //显示数据
        if (isFirst & isPrepared & isVisible){
            isFirst=false;
            //加载数据
            presenter.loadingData();
        }
    }

    /**
     * 加载数据时
     */
    @Override
    public void showLoading() {
         srlAnime.setRefreshing(true);
    }

    /**
     * 加载数据为空时
     */
    @Override
    public void showEmpty() {
        srlAnime.setRefreshing(false);
        if (movieModelBeans.size() > 0) {
            animeFooterViewInfo.setText(getText(R.string.no_data_tips));
        } else {
            tvTipAnime.setText(getText(R.string.empty_tips));
        }
    }

    /**
     * 加载数据失败时
     */
    @Override
    public void showError() {
        srlAnime.setRefreshing(false);
        if (movieModelBeans.size() > 0) {
            Toast.makeText(getContext(), getText(R.string.error_tips), Toast.LENGTH_SHORT).show();
        } else {
            tvTipAnime.setText(getText(R.string.error_tips2));
        }
    }

    /**
     * 加载数据完成时
     *
     * @param modelBeans
     */
    @Override
    public void showComplete(ArrayList<?> modelBeans) {
        tvTipAnime.setVisibility(View.GONE);
        srlAnime.setRefreshing(false);
        movieModelBeans.addAll((Collection<? extends MovieSubjectsModel>) modelBeans);
        animeWrapperAdapter.notifyDataSetChanged();
    }


    /**
     * 监听刷新控件颜色
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.isNight) {
            srlAnime.setColorSchemeResources(R.color.colorNight);
        } else {
            srlAnime.setColorSchemeResources(R.color.colorAnime);
        }
        animeWrapperAdapter.notifyDataSetChanged();
    }


    /**
     * 上拉加载
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            // 手指不能向上滑动了并且不在加载状态
            if (!recyclerView.canScrollVertically(1) && dy != 0 && !srlAnime.isRefreshing()) {
                animeFooterViewInfo.setText(getText(R.string.loading_tips));
                srlAnime.setRefreshing(true);
                presenter.loadingData();//刷新
            }
        }
    };


    /**
     * 提示文本点击回调
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tip_anime:
                presenter.loadingData();//重新加载
                break;
            default:
                break;
        }
    }

    /**
     * RecycleView Item点击回调
     * @param position
     * @param id
     * @param img_url
     * @param title
     */
    @Override
    public void onItemClick(int position, String id, String img_url, String title) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("theme", R.style.AnimeThemeTransNav);
        intent.putExtra("img_url", img_url);
        intent.putExtra("title", title);
        intent.putExtra("movieSubject", movieModelBeans.get(position));
        intent.putExtra("color", getResources().getColor(R.color.colorAnime));
        startActivity(intent);
    }
}

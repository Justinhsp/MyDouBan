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
import com.hycf.example.douban.presenter.MoviePresenter;
import com.hycf.example.douban.view.activity.MovieDetailActivity;
import com.hycf.example.douban.view.iview.IMovieView;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;
import butterknife.BindView;
import butterknife.Unbinder;

/**
 * Created by Hui on 2018/3/5.
 */

public class MovieFragment extends BaseLazyFragment<IMovieView, MoviePresenter> implements IMovieView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, MovieAdapter.IOnItemClickListener {
    @BindView(R.id.tv_tip_movie)
    TextView tvTipMovie;
    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;
    @BindView(R.id.srl_movie)
    SwipeRefreshLayout srlMovie;
    Unbinder unbinder;

    //判断是否第一次显示
    private boolean isFirst=true;
    //电影条目列表
    private ArrayList<MovieSubjectsModel> movieModelBeans=new ArrayList<>();
    //添加FootView的适配器
    private HeaderAndFooterWrapper movieWrapperAdapter;
    //适配器
    private MovieAdapter movieAdapter;
    //footview文字显示
    private TextView movieFooterViewInfo;
    //是否刷新
    private boolean isRefresh=true;


    /**
     * 初始化对应Presenter
     * @return
     */
    @Override
    protected MoviePresenter initPresenter() {
        return new MoviePresenter();
    }


    /**
     * 布局文件
     *
     * @return
     */
    @Override
    protected int setLayoutId() {
        return R.layout.main_movie;
    }


    /**
     * 初始化布局
     */
    @Override
    protected void initView() {
        //配置SwipeRefreshLayout下拉刷新控件
        srlMovie.setColorSchemeResources(R.color.colorMovie);
        srlMovie.setOnRefreshListener(this);
        //适配器
        movieAdapter =new MovieAdapter(movieModelBeans);
        movieWrapperAdapter = new HeaderAndFooterWrapper(movieAdapter);
        //添加footerView
        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.footerview, null);
        movieFooterViewInfo = (TextView) footerView.findViewById(R.id.footerview_info);
        movieWrapperAdapter.addFootView(footerView);
        //配置RecyclerView
        rvMovie.setLayoutManager(new LinearLayoutManager(getContext()));//设置list列风格
        rvMovie.setAdapter(movieWrapperAdapter);
        //上拉加载监听
        rvMovie.addOnScrollListener(mOnScrollListener);
        //监听点击提示文本
        tvTipMovie.setOnClickListener(this);
        //item点击事件
        movieAdapter.setiOnItemClickListener(this);
    }


    /**
     * 实现懒加载,当屏幕显示这个界面的时候才会触发次方法
     */
    @Override
    protected void lazyLoad() {
        //显示数据
        if (isFirst & isPrepared && isVisible) {
            isFirst = false;
            //加载数据
            presenter.loadingData(true);
        }
    }

    /**
     * 加载数据时
     */
    @Override
    public void showLoading() {
        srlMovie.setRefreshing(true);
    }

    /**
     * 数据为空时
     */
    @Override
    public void showEmpty() {
        srlMovie.setRefreshing(false);
        if (movieModelBeans.size() > 0) {
            movieFooterViewInfo.setText(getText(R.string.no_data_tips));
        } else {
            tvTipMovie.setText(getText(R.string.empty_tips));
        }
    }

    /**
     * 加载失败时
     */
    @Override
    public void showError() {
        srlMovie.setRefreshing(false);
        if (movieModelBeans.size() > 0) {
            Toast.makeText(getContext(), getText(R.string.error_tips), Toast.LENGTH_SHORT).show();
        } else {
            tvTipMovie.setText(getText(R.string.error_tips2));
        }
    }


    /**
     * 加载数据完成时
     *
     * @param modelBeans
     */
    @Override
    public void showComplete(ArrayList<?> modelBeans) {
        if (isRefresh) {
            movieModelBeans.clear();
        }
        isRefresh = false;
        tvTipMovie.setVisibility(View.GONE);
        srlMovie.setRefreshing(false);
        movieModelBeans.addAll((Collection<? extends MovieSubjectsModel>) modelBeans);
        movieWrapperAdapter.notifyDataSetChanged();
    }



    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        isRefresh = true;
        presenter.loadingData(true);//下拉刷新
    }


    /**
     * 上拉加载更多
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            // 如果手指不能向上滑动了并且不在加载状态
            if (!recyclerView.canScrollVertically(1) && dy != 0 && !srlMovie.isRefreshing()) {
                movieFooterViewInfo.setText(getText(R.string.loading_tips));
                srlMovie.setRefreshing(true);
                presenter.loadingData(false);//刷新
            }
        }
    };

    /**
     * 点击提示文本
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tip_movie:
                //重新加载
                presenter.loadingData(true);
                break;
            default:
                break;
        }
    }

    /**
     * RecycleView Item点击事件
     * @param position
     * @param id
     * @param img_url
     * @param title
     */
    @Override
    public void onItemClick(int position, String id, String img_url, String title) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("theme", R.style.MovieThemeTransNav);
        intent.putExtra("img_url", img_url);
        intent.putExtra("title", title);
        intent.putExtra("movieSubject", movieModelBeans.get(position));
        intent.putExtra("color", getResources().getColor(R.color.colorMovie));
        startActivity(intent);
    }


    /**
     * 使用EventBus更新UI颜色
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.isNight) {
            srlMovie.setColorSchemeResources(R.color.colorNight);
        } else {
            srlMovie.setColorSchemeResources(R.color.colorMovie);
        }
        movieWrapperAdapter.notifyDataSetChanged();
    }


    /**
     * 销毁
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

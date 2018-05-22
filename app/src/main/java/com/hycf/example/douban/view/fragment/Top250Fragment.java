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
import com.hycf.example.douban.presenter.Top250Presenter;
import com.hycf.example.douban.view.activity.MovieDetailActivity;
import com.hycf.example.douban.view.iview.ITop250View;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;

/**
 * Created by Hui on 2018/3/5.
 */

public class Top250Fragment extends BaseLazyFragment<ITop250View, Top250Presenter> implements ITop250View, MovieAdapter.IOnItemClickListener, View.OnClickListener {
    @BindView(R.id.tv_tip_top250)
    TextView tvTipTop250;
    @BindView(R.id.rv_top250)
    RecyclerView rvTop250;
    @BindView(R.id.srl_top250)
    SwipeRefreshLayout srlTop250;


    //判断是否第一次显示
    private boolean isFirst = true;
    //电影条目列表
    private ArrayList<MovieSubjectsModel> movieModelBeans = new ArrayList<>();
    //添加FooterView的适配器
    private HeaderAndFooterWrapper top250WrapperAdapter;
    //适配器
    private MovieAdapter top250Adapter;
    //footerView文字显示
    private TextView top250FooterViewInfo;

    /**
     * 初始化对应的Presenter
     *
     * @return
     */
    @Override
    protected Top250Presenter initPresenter() {
        return new Top250Presenter();
    }

    /**
     * 返回对应布局文件
     *
     * @return
     */
    @Override
    protected int setLayoutId() {
        return R.layout.main_top250;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        //配置下拉刷新控件
        srlTop250.setEnabled(false);
        srlTop250.setColorSchemeResources(R.color.colorTop250);
        //适配器
        top250Adapter = new MovieAdapter(movieModelBeans);
        top250Adapter.setiOnItemClickListener(this);
        top250WrapperAdapter = new HeaderAndFooterWrapper(top250Adapter);
        //添加footerView
        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.footerview, null);
        top250FooterViewInfo = (TextView) footerView.findViewById(R.id.footerview_info);
        top250WrapperAdapter.addFootView(footerView);
        //配置RecyclerView
        rvTop250.setLayoutManager(new LinearLayoutManager(getContext()));//设置list列风格
        rvTop250.setAdapter(top250WrapperAdapter);
        //监听列表上拉
        rvTop250.addOnScrollListener(mOnScrollListener);
        //监听点击提示文本
        tvTipTop250.setOnClickListener(this);
    }

    /**
     * 实现懒加载,当这个页面获取焦点时才会触动此方法
     */
    @Override
    protected void lazyLoad() {
         //显示数据
        if (isFirst & isPrepared && isVisible){
            isFirst=false;
            //加载数据
            presenter.loadingData();
        }
    }

    /**
     * 是否夜间模式
     * 更新刷新控件颜色
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.isNight) {
            srlTop250.setColorSchemeResources(R.color.colorNight);
        } else {
            srlTop250.setColorSchemeResources(R.color.colorTop250);
        }
        top250WrapperAdapter.notifyDataSetChanged();
    }

    /**
     * 加载数据时
     */
    @Override
    public void showLoading() {
         srlTop250.setRefreshing(true);
    }

    /**
     * 加载数据为空时
     */
    @Override
    public void showEmpty() {
      srlTop250.setRefreshing(false);
      if (movieModelBeans.size()>0){
          top250FooterViewInfo.setText(getText(R.string.no_data_tips));
      }else {
          tvTipTop250.setText(getText(R.string.empty_tips));
      }
    }

    /**
     * 加载数据失败时
     */
    @Override
    public void showError() {
        srlTop250.setRefreshing(false);
        if (movieModelBeans.size() > 0) {
            Toast.makeText(getContext(), getText(R.string.error_tips), Toast.LENGTH_SHORT).show();
        } else {
            tvTipTop250.setText(getText(R.string.error_tips2));
        }
    }

    /**
     * 加载数据完成时
     *
     * @param modelBeans
     */
    @Override
    public void showComplete(ArrayList<?> modelBeans) {
        tvTipTop250.setVisibility(View.GONE);
        srlTop250.setRefreshing(false);
        movieModelBeans.addAll((Collection<? extends MovieSubjectsModel>) modelBeans);
        top250WrapperAdapter.notifyDataSetChanged();
    }

    /**
     * 上拉加载更多
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            // 手指不能向上滑动了并且不在加载状态
            if (!recyclerView.canScrollVertically(1) && dy != 0 && !srlTop250.isRefreshing()) {
                top250FooterViewInfo.setText(getText(R.string.loading_tips));
                srlTop250.setRefreshing(true);
                //刷新
                presenter.loadingData();
            }
        }
    };


    /**
     * 点击提示文本
     * 重新加载
     * @param view
     */
     @Override
    public void onClick(View view) {
         switch (view.getId()) {
             case R.id.tv_tip_top250:
                 //重新加载
                 presenter.loadingData();
                 break;
             default:
                 break;
         }
    }


    /**
     * RecycleView Item点击
     * @param position
     * @param id
     * @param img_url
     * @param title
     */
    @Override
    public void onItemClick(int position, String id, String img_url, String title) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("theme", R.style.Top250ThemeTransNav);
        intent.putExtra("img_url", img_url);
        intent.putExtra("title", title);
        intent.putExtra("movieSubject", movieModelBeans.get(position));
        intent.putExtra("color", getResources().getColor(R.color.colorTop250));
        startActivity(intent);
    }
}

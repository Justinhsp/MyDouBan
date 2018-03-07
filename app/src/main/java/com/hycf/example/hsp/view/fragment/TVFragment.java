package com.hycf.example.hsp.view.fragment;

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

import com.hycf.example.hsp.R;
import com.hycf.example.hsp.adapter.MovieAdapter;
import com.hycf.example.hsp.base.BaseLazyFragment;
import com.hycf.example.hsp.model.MovieSubjectsModel;
import com.hycf.example.hsp.msg.MessageEvent;
import com.hycf.example.hsp.presenter.TVPresenter;
import com.hycf.example.hsp.view.activity.MovieDetailActivity;
import com.hycf.example.hsp.view.iview.ITvView;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;

/**
 * Created by Hui on 2018/3/5.
 */

public class TVFragment extends BaseLazyFragment<ITvView, TVPresenter> implements ITvView, MovieAdapter.IOnItemClickListener, View.OnClickListener {
    @BindView(R.id.tv_tip_tv)
    TextView tvTipTv;
    @BindView(R.id.rv_tv)
    RecyclerView rvTv;
    @BindView(R.id.srl_tv)
    SwipeRefreshLayout srlTv;


    //判断是否第一次显示
    private boolean isFirst = true;
    //电影条目列表
    private ArrayList<MovieSubjectsModel> movieModelBeans = new ArrayList<>();
    //添加FooterView的适配器
    private HeaderAndFooterWrapper tvWrapperAdapter;
    //适配器
    private MovieAdapter tvAdapter;
    //footerView文字显示
    private TextView tvFooterViewInfo;


    /**
     * 初始化对应Presenter
     *
     * @return
     */
    @Override
    protected TVPresenter initPresenter() {
        return new TVPresenter();
    }

    /**
     * 返回对应布局文件
     *
     * @return
     */
    @Override
    protected int setLayoutId() {
        return R.layout.main_tv;
    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {
          //配置下拉刷新控件
          srlTv.setEnabled(false);
          srlTv.setColorSchemeResources(R.color.colorTV);
        //适配器
        tvAdapter = new MovieAdapter(movieModelBeans);
        tvAdapter.setiOnItemClickListener(this);
        tvWrapperAdapter = new HeaderAndFooterWrapper(tvAdapter);
        //添加footerView
        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.footerview, null);
        tvFooterViewInfo = (TextView) footerView.findViewById(R.id.footerview_info);
        tvWrapperAdapter.addFootView(footerView);
        //配置RecyclerView
        rvTv.setLayoutManager(new LinearLayoutManager(getContext()));//设置list列风格
        rvTv.setAdapter(tvWrapperAdapter);
        //监听列表上拉
        rvTv.addOnScrollListener(mOnScrollListener);
        //监听点击提示文本
        tvTipTv.setOnClickListener(this);
    }

    /**
     * 实现懒加载,当这个页面获取焦点时才会触发此方法
     */
    @Override
    protected void lazyLoad() {
        //显示数据
        if (isFirst & isPrepared && isVisible) {
            isFirst = false;
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
            srlTv.setColorSchemeResources(R.color.colorNight);
        } else {
            srlTv.setColorSchemeResources(R.color.colorTV);
        }
        tvWrapperAdapter.notifyDataSetChanged();
    }


    /**
     * 加载数据时
     */
    @Override
    public void showLoading() {
       srlTv.setRefreshing(true);
    }

    /**
     * 加载数据为空时
     */
    @Override
    public void showEmpty() {
        srlTv.setRefreshing(false);
        if (movieModelBeans.size() > 0) {
            tvFooterViewInfo.setText(getText(R.string.no_data_tips));
        } else {
            tvTipTv.setText(getText(R.string.empty_tips));
        }
    }

    /**
     * 加载数据失败时
     */
    @Override
    public void showError() {
        srlTv.setRefreshing(false);
        if (movieModelBeans.size() > 0) {
            Toast.makeText(getContext(), getText(R.string.error_tips), Toast.LENGTH_SHORT).show();
        } else {
            tvTipTv.setText(getText(R.string.error_tips2));
        }
    }

    /**
     * 加载数据完成时
     *
     * @param modelBeans
     */
    @Override
    public void showComplete(ArrayList<?> modelBeans) {
        tvTipTv.setVisibility(View.GONE);
        srlTv.setRefreshing(false);
        movieModelBeans.addAll((Collection<? extends MovieSubjectsModel>) modelBeans);
        tvWrapperAdapter.notifyDataSetChanged();
    }


    /**
     * 上拉加载
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            // 手指不能向上滑动了并且不在加载状态
            if (!recyclerView.canScrollVertically(1) && dy != 0 && !srlTv.isRefreshing()) {
                tvFooterViewInfo.setText(getText(R.string.loading_tips));
                srlTv.setRefreshing(true);
                //刷新
                presenter.loadingData();
            }
        }
    };


    /**
     * 提示文本点击
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tip_tv:
                //重新加载
                presenter.loadingData();
                break;
            default:
                break;
        }
    }


    /**
     * RecycleView Item回调
     * @param position
     * @param id
     * @param img_url
     * @param title
     */
    @Override
    public void onItemClick(int position, String id, String img_url, String title) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("theme", R.style.TVThemeTransNav);
        intent.putExtra("img_url", img_url);
        intent.putExtra("title", title);
        intent.putExtra("movieSubject", movieModelBeans.get(position));
        intent.putExtra("color", getResources().getColor(R.color.colorTV));
        startActivity(intent);
    }

}

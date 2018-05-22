package com.hycf.example.douban.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hycf.example.douban.R;
import com.hycf.example.douban.adapter.GifAdapter;
import com.hycf.example.douban.base.BaseActivity;
import com.hycf.example.douban.model.GiftModel;
import com.hycf.example.douban.presenter.GifPresenter;
import com.hycf.example.douban.view.iview.IGifView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hui on 2018/3/6.
 */

public class GiftActivity extends BaseActivity<IGifView, GifPresenter> implements IGifView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, GifAdapter.IOnItemClickListener {
    @BindView(R.id.tb_gift)
    Toolbar tbGift;
    @BindView(R.id.tv_tip_gift)
    TextView tvTipGift;
    @BindView(R.id.rv_gift)
    RecyclerView rvGift;
    @BindView(R.id.srl_gift)
    SwipeRefreshLayout srlGift;
    @BindView(R.id.root_view)
    LinearLayout rootView;

    private static final String TAG = "GiftActivity";

    //是否刷新
    private boolean isRefresh = true;

    private List<GiftModel.ResultsBean> gifModels = new ArrayList<>();

    private GifAdapter gifAdapter;


    /**
     * 返回对应presenter
     *
     * @return
     */
    @Override
    protected GifPresenter initPresenter() {
        return new GifPresenter();
    }

    /**
     * 返回对应布局
     *
     * @return
     */
    @Override
    protected int setLayoutId() {
        return R.layout.activity_gift;
    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {
        //toolbar设置标题
        tbGift.setTitle(getResources().getString(R.string.gift));
        //以上属性必须在setSupportActionBar之前调用
        setSupportActionBar(tbGift);
        tbGift.setBackgroundColor(getIntent().getIntExtra("color", getResources().getColor(R.color.colorMovie)));
        tbGift.setNavigationIcon(R.drawable.ic_back_white_24dp);
        //配置下拉刷新控件
        srlGift.setColorSchemeColors(getIntent().getIntExtra("color",getResources().getColor(R.color.colorWhite)));
        //适配器
        gifAdapter=new GifAdapter(gifModels);
        //配置RecycleView
        rvGift.setLayoutManager(new GridLayoutManager(this,3));//设置瀑布流分格
        rvGift.setAdapter(gifAdapter);
        //上拉加载监听
        rvGift.addOnScrollListener(mOnScrollListener);
        //下拉刷新监听
        srlGift.setOnRefreshListener(this);
        //加载数据
        presenter.loadingData(isRefresh);
        //监听返回键
        tbGift.setNavigationOnClickListener(this);
        //RecycleView列表item监听
        gifAdapter.setOnItemClickListener(this);
    }

    /**
     * 加载数据时
     */
    @Override
    public void showLoading() {
        srlGift.setRefreshing(true);
    }

    /**
     * 加载数据为空时
     */
    @Override
    public void showEmpty() {
        srlGift.setRefreshing(false);
        if (gifModels.size()>0){
            Toast.makeText(this,"已经没有更多了", Toast.LENGTH_SHORT).show();
        }else {
            tvTipGift.setText(getText(R.string.error_tips2));
        }

    }

    /**
     * 加载数据失败
     */
    @Override
    public void showError() {
         srlGift.setRefreshing(false);
         if (gifModels.size()>0){
             Toast.makeText(this, getText(R.string.error_tips), Toast.LENGTH_SHORT).show();
         }else {
             tvTipGift.setText(getText(R.string.error_tips2));
         }
    }

    /**
     * 加载数据完成
     *
     * @param models
     */
    @Override
    public void showComplete(ArrayList<?> models) {
          if (isRefresh){
              gifModels.clear();
          }
          isRefresh=false;
          tvTipGift.setVisibility(View.GONE);
          srlGift.setRefreshing(false);
          gifModels.addAll((Collection<? extends GiftModel.ResultsBean>) models);
          gifAdapter.notifyDataSetChanged();
    }


    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
         isRefresh=true;
         presenter.loadingData(isRefresh);
    }

    /**
     * 上拉加载更多
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!recyclerView.canScrollVertically(1) && dy != 0 && !srlGift.isRefreshing()) {// 手指不能向上滑动了并且不在加载状态
                presenter.loadingData(false);//加载下一页
            }
        }
    };

    /**
     * 返回
     * @param view
     */
    @Override
    public void onClick(View view) {
        finish();
    }

    /**
     * RecycleView item 点击回调
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("img_url", gifModels.get(position).getUrl());
        startActivity(intent);
    }
}

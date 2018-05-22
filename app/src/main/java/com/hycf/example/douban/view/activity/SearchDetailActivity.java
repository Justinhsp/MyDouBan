package com.hycf.example.douban.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hycf.example.douban.MyApplication;
import com.hycf.example.douban.R;
import com.hycf.example.douban.adapter.MovieAdapter;
import com.hycf.example.douban.base.BaseActivity;
import com.hycf.example.douban.model.MovieSubjectsModel;
import com.hycf.example.douban.presenter.SearchDetailPresenter;
import com.hycf.example.douban.view.iview.ISearchDetailView;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hui on 2018/3/6.
 */

public class SearchDetailActivity extends BaseActivity<ISearchDetailView, SearchDetailPresenter> implements ISearchDetailView, MovieAdapter.IOnItemClickListener, View.OnClickListener, IOnSearchClickListener, Toolbar.OnMenuItemClickListener {


    @BindView(R.id.tb_search_detail)
    Toolbar tbSearchDetail;
    @BindView(R.id.tv_tip_search_detail)
    TextView tvTipSearchDetail;
    @BindView(R.id.rv_search_detail)
    RecyclerView rvSearchDetail;
    @BindView(R.id.srl_search_detail)
    SwipeRefreshLayout srlSearchDetail;
    @BindView(R.id.root_view)
    LinearLayout rootView;


    //判断是否标签
    private boolean isTag = false;
    //关键字
    String keyword = "";
    //电影条目列表
    private ArrayList<MovieSubjectsModel> movieModelBeans = new ArrayList<>();
    //添加FootView的适配器
    private HeaderAndFooterWrapper searchWrapperAdapter;
    //适配器
    private MovieAdapter seatchAdapter;
    //footView文字显示
    private TextView searchFootViewInfo;
    //搜索框
    private SearchFragment searchFragment;

    /**
     * 返回对应presenter
     *
     * @return
     */
    @Override
    protected SearchDetailPresenter initPresenter() {
        return new SearchDetailPresenter();
    }

    /**
     * 返回对应布局文件
     *
     * @return
     */
    @Override
    protected int setLayoutId() {
        return R.layout.activity_searchdetail;
    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {
        keyword = getIntent().getStringExtra("keyword");
        if (TextUtils.isEmpty(keyword)) {
            keyword = getIntent().getStringExtra("tag");
            isTag = true;
        }
        //toolbar设置标题
        tbSearchDetail.setTitle(keyword);
        //以上属性必须在setSupportActionBar(toolbar)之前调用
        setSupportActionBar(tbSearchDetail);
        tbSearchDetail.setBackgroundColor(getIntent().getIntExtra("color", getResources().getColor(R.color.colorMovie)));
        tbSearchDetail.setNavigationIcon(R.drawable.ic_back_white_24dp);
        //配置下拉刷新控件
        srlSearchDetail.setEnabled(false);
        srlSearchDetail.setColorSchemeColors(getIntent().getIntExtra("color",getResources().getColor(R.color.colorMovie)));
        //适配器
        seatchAdapter=new MovieAdapter(movieModelBeans);
        //recycleView Item点击事件
        seatchAdapter.setiOnItemClickListener(this);
        searchWrapperAdapter=new HeaderAndFooterWrapper(seatchAdapter);
        //添加foorView
        View footerView = LayoutInflater.from(this).inflate(R.layout.footerview, null);
        searchFootViewInfo = (TextView) footerView.findViewById(R.id.footerview_info);
        searchWrapperAdapter.addFootView(footerView);
        //配置RecycleView
        rvSearchDetail.setLayoutManager(new LinearLayoutManager(this));//设置list列风格
        rvSearchDetail.setAdapter(searchWrapperAdapter);
        //上拉加载监听
        rvSearchDetail.addOnScrollListener(mOnScrollListener);
        //监听点击提示文本
        tvTipSearchDetail.setOnClickListener(this);
        //检查是否夜间模式
        checkIsNightMode();
        //实例化搜索框
        searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener(this);//搜索监听事件
        tbSearchDetail.setOnMenuItemClickListener(this);//添加子菜单点击事件
        tbSearchDetail.setNavigationOnClickListener(this);
        //加载数据
        presenter.search(keyword, isTag, true);
    }

    /**
     * 是否夜间模式
     */
    private void checkIsNightMode() {
        if (MyApplication.NIGHT_MODE) {
            tbSearchDetail.setBackgroundColor(getResources().getColor(R.color.colorNight));
            srlSearchDetail.setColorSchemeColors(getResources().getColor(R.color.colorNight));
        }
    }

    /**
     * 加载toolbar菜单布局
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单文件
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 加载数据时
     */
    @Override
    public void showLoading() {
       srlSearchDetail.setRefreshing(true);
    }

    /**
     * 加载数据为空
     */
    @Override
    public void showEmpty() {
       srlSearchDetail.setRefreshing(false);
        if (movieModelBeans.size() > 0) {
            if (searchFootViewInfo.getVisibility() == View.GONE)
                searchFootViewInfo.setVisibility(View.VISIBLE);
            searchFootViewInfo.setText(getText(R.string.no_data_tips));
        } else {
            if (tvTipSearchDetail.getVisibility() == View.GONE)
                tvTipSearchDetail.setVisibility(View.VISIBLE);
            tvTipSearchDetail.setText(getText(R.string.empty_tips));
        }
    }

    /**
     * 加载数据失败
     */
    @Override
    public void showError() {
          srlSearchDetail.setRefreshing(false);
        if (movieModelBeans.size() > 0) {
            Toast.makeText(this, getText(R.string.error_tips), Toast.LENGTH_SHORT).show();
        } else {
            if (tvTipSearchDetail.getVisibility() == View.GONE)
                tvTipSearchDetail.setVisibility(View.VISIBLE);
                tvTipSearchDetail.setText(getText(R.string.error_tips2));
        }
    }

    /**
     * 加载数据完成
     *
     * @param modelBeans
     */
    @Override
    public void showComplete(ArrayList<?> modelBeans) {
        tvTipSearchDetail.setVisibility(View.GONE);
        srlSearchDetail.setRefreshing(false);
        movieModelBeans.addAll((Collection<? extends MovieSubjectsModel>) modelBeans);
        searchWrapperAdapter.notifyDataSetChanged();
        if (searchFootViewInfo.getVisibility() == View.GONE)
            searchFootViewInfo.setVisibility(View.VISIBLE);
            searchFootViewInfo.setText(getText(R.string.no_data_tips));
    }


    /**
     * 上拉加载更多
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!recyclerView.canScrollVertically(1) && dy != 0 && !srlSearchDetail.isRefreshing()) {// 手指不能向上滑动了并且不在加载状态
                searchFootViewInfo.setText(getText(R.string.loading_tips));
                srlSearchDetail.setRefreshing(true);
                presenter.search(keyword, isTag, false);//刷新
            }
        }
    };

    /**
     * RecycleView Item 点击事件
     * @param position
     * @param id
     * @param img_url
     * @param title
     */
    @Override
    public void onItemClick(int position, String id, String img_url, String title) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("theme", getIntent().getIntExtra("theme", R.style.MovieThemeTransNav));
        intent.putExtra("img_url", img_url);
        intent.putExtra("title", title);
        intent.putExtra("movieSubject", movieModelBeans.get(position));
        intent.putExtra("color", getIntent().getIntExtra("color", getResources().getColor(R.color.colorMovie)));
        startActivity(intent);
    }

    /**
     * 提示文本点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tip_search_detail:
                presenter.search(keyword, isTag, false);//重新加载
                break;
            default:
                finish();
                break;
        }
    }

    /**
     * 搜索框回调
     * @param keyword
     */
    @Override
    public void OnSearchClick(String keyword) {
        movieModelBeans.clear();
        searchWrapperAdapter.notifyDataSetChanged();
        searchFootViewInfo.setVisibility(View.GONE);
        tbSearchDetail.setTitle(keyword);
        isTag = false;
        this.keyword = keyword;
        presenter.search(keyword, false, true);
    }

    /**
     * toolbar 菜单点击事件
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        searchFragment.show(getSupportFragmentManager(), SearchFragment.TAG);
        return true;
    }
}

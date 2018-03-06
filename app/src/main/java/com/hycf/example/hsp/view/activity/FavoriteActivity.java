package com.hycf.example.hsp.view.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hycf.example.hsp.MyApplication;
import com.hycf.example.hsp.R;
import com.hycf.example.hsp.adapter.MovieAdapter;
import com.hycf.example.hsp.base.BaseActivity;
import com.hycf.example.hsp.model.MovieSubjectsModel;
import com.hycf.example.hsp.presenter.FavoritePresenter;
import com.hycf.example.hsp.view.iview.IFavoriteView;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Hui on 2018/3/6.
 */

public class FavoriteActivity extends BaseActivity<IFavoriteView, FavoritePresenter> implements IFavoriteView, MovieAdapter.IOnItemClickListener, View.OnClickListener, Toolbar.OnMenuItemClickListener {
    @BindView(R.id.tb_favorite_detail)
    Toolbar tbFavoriteDetail;
    @BindView(R.id.tv_tip_favorite_detail)
    TextView tvTipFavoriteDetail;
    @BindView(R.id.rv_favorite_detail)
    RecyclerView rvFavoriteDetail;
    @BindView(R.id.srl_favorite_detail)
    SwipeRefreshLayout srlFavoriteDetail;
    @BindView(R.id.root_view)
    LinearLayout rootView;

    //收藏电影条目列表
    private ArrayList<MovieSubjectsModel> movieModelBeans=new ArrayList<>();
    //添加footView的适配器
    private HeaderAndFooterWrapper favoriteWrapperAdapter;
    //适配器
    private MovieAdapter favoriteAdapter;
    //footerView文字显示
    private TextView favoriteFooterViewInfo;
    //选中的条目
    private MovieSubjectsModel movieModelBean;
    //是否在选择状态
    private boolean isCheck = false;

    /**
     * 返回对应Presenter
     *
     * @return
     */
    @Override
    protected FavoritePresenter initPresenter() {
        return new FavoritePresenter();
    }

    /**
     * 返回对应布局文件
     *
     * @return
     */
    @Override
    protected int setLayoutId() {
        return R.layout.activity_favorite;
    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {
        //toolbar设置初始标题
        tbFavoriteDetail.setTitle(getResources().getString(R.string.favorite));
        //以上属性必须在setSupportActionBar(toolbar)之前调用
        setSupportActionBar(tbFavoriteDetail);
        tbFavoriteDetail.setBackgroundColor(getIntent().getIntExtra("color", getResources().getColor(R.color.colorMovie)));
        tbFavoriteDetail.setNavigationIcon(R.drawable.ic_back_white_24dp);
        //配置下拉刷新控件
        srlFavoriteDetail.setEnabled(false);
        srlFavoriteDetail.setColorSchemeColors(getIntent().getIntExtra("color", getResources().getColor(R.color.colorMovie)));
        //适配器
        favoriteAdapter = new MovieAdapter(movieModelBeans);
        favoriteAdapter.setiOnItemClickListener(this);
        favoriteWrapperAdapter = new HeaderAndFooterWrapper(favoriteAdapter);
        //添加footerView
        View footerView = LayoutInflater.from(this).inflate(R.layout.footerview, null);
        favoriteFooterViewInfo = (TextView) footerView.findViewById(R.id.footerview_info);
        favoriteWrapperAdapter.addFootView(footerView);
        //配置RecyclerView
        rvFavoriteDetail.setLayoutManager(new LinearLayoutManager(this));//设置list列风格
        rvFavoriteDetail.setAdapter(favoriteWrapperAdapter);
        //监听点击提示文本
        tvTipFavoriteDetail.setOnClickListener(this);
        //添加子菜单点击事件
        tbFavoriteDetail.setOnMenuItemClickListener(this);
        tbFavoriteDetail.setNavigationOnClickListener(this);
        //加载数据
        presenter.loadData();
    }


    /**
     * 加载toolbar菜单布局
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单文件
        getMenuInflater().inflate(R.menu.menu_favorite,menu);
        return true;
    }

    /**
     * 加载数据时
     */
    @Override
    public void showLoading() {
       srlFavoriteDetail.setRefreshing(true);
    }

    /**
     * 加载数据为空时
     */
    @Override
    public void showEmpty() {
        srlFavoriteDetail.setRefreshing(false);
        if (movieModelBeans.size() > 0) {
            if (favoriteFooterViewInfo.getVisibility() == View.GONE)
                favoriteFooterViewInfo.setVisibility(View.VISIBLE);
            favoriteFooterViewInfo.setText(getText(R.string.no_data_tips));
        } else {
            if (tvTipFavoriteDetail.getVisibility() == View.GONE)
                tvTipFavoriteDetail.setVisibility(View.VISIBLE);
            tvTipFavoriteDetail.setText(getText(R.string.favorite_empty_tips));
        }
    }

    /**
     * 加载数据完成
     *
     * @param movieSubjectsModels
     */
    @Override
    public void showComplete(List<MovieSubjectsModel> movieSubjectsModels) {
        tvTipFavoriteDetail.setVisibility(View.GONE);
        srlFavoriteDetail.setRefreshing(false);
        movieModelBeans.addAll(movieSubjectsModels);
        favoriteWrapperAdapter.notifyDataSetChanged();
        if (favoriteFooterViewInfo.getVisibility() == View.GONE)
            favoriteFooterViewInfo.setVisibility(View.VISIBLE);
        favoriteFooterViewInfo.setText(getText(R.string.no_data_tips));
    }

    /**
     * toolbar菜单点击回调
     * @param item
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (isCheck) {
            Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    //删除数据
                    subscriber.onNext(presenter.deleteData(favoriteAdapter.getSelectIds()));
                }
            })
                    .subscribeOn(Schedulers.io())               // 指定 subscribe() 发生在 IO 线程
                    .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean isDelete) {
                                  if (isDelete){
                                      movieModelBeans.removeAll(favoriteAdapter.getSelectModels());
                                      item.setIcon(R.drawable.ic_check_white_24dp);
                                      item.setTitle(R.string.favorite);
                                      isCheck=false;
                                      favoriteAdapter.setCheck(false);
                                      favoriteWrapperAdapter.notifyDataSetChanged();
                                  }
                        }
                    });

        }else {
            item.setIcon(R.drawable.ic_delete_white_24dp);
            item.setTitle(R.string.delete);
            isCheck = true;
            favoriteAdapter.setCheck(true);
            favoriteWrapperAdapter.notifyDataSetChanged();
        }
        return true;
    }

    /**
     * 提示文本点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tip_favorite_detail:
                //重新加载
                presenter.loadData();
                break;
            default:
                finish();
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
        movieModelBean = movieModelBeans.get(position);
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("theme", getIntent().getIntExtra("theme", R.style.MovieThemeTransNav));
        intent.putExtra("img_url", img_url);
        intent.putExtra("title", title);
        intent.putExtra("movieSubject", movieModelBeans.get(position));
        intent.putExtra("color", getIntent().getIntExtra("color", getResources().getColor(R.color.colorMovie)));
        startActivityForResult(intent, MyApplication.REQUESTCODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyApplication.REQUESTCODE && resultCode == MyApplication.RESULTCODE) {
            if (!data.getBooleanExtra("isFavorite", true))
                movieModelBeans.remove(movieModelBean);
            favoriteWrapperAdapter.notifyDataSetChanged();
        }
    }

}

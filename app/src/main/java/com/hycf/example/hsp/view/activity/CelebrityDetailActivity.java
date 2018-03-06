package com.hycf.example.hsp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hycf.example.hsp.MyApplication;
import com.hycf.example.hsp.R;
import com.hycf.example.hsp.adapter.MovieAdapter;
import com.hycf.example.hsp.base.BaseActivity;
import com.hycf.example.hsp.model.CelebrityDetailModel;
import com.hycf.example.hsp.model.MovieSubjectsModel;
import com.hycf.example.hsp.presenter.CelebrityDetailPresenter;
import com.hycf.example.hsp.view.iview.ICelebrityDetailView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Hui on 2018/3/6.
 */

public class CelebrityDetailActivity extends BaseActivity<ICelebrityDetailView, CelebrityDetailPresenter> implements ICelebrityDetailView, View.OnClickListener, MovieAdapter.IOnItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_celebrity_avatar)
    ImageView ivCelebrityAvatar;
    @BindView(R.id.tv_celebrity_name)
    TextView tvCelebrityName;
    @BindView(R.id.tv_celebrity_name_en)
    TextView tvCelebrityNameEn;
    @BindView(R.id.tv_celebrity_place)
    TextView tvCelebrityPlace;
    @BindView(R.id.tv_celebrity_roles)
    TextView tvCelebrityRoles;
    @BindView(R.id.card_view)
    CardView cardView;
    @BindView(R.id.rv_works)
    RecyclerView rvWorks;
    @BindView(R.id.tv_celebrity_more)
    TextView tvCelebrityMore;
    @BindView(R.id.ll_celebrity_detail)
    LinearLayout llCelebrityDetail;
    @BindView(R.id.srl_celebrity_detail)
    SwipeRefreshLayout srlCelebrityDetail;
    @BindView(R.id.root_view)
    CoordinatorLayout rootView;


    private String avatars_large;
    //电影条目列表
    private ArrayList<MovieSubjectsModel> movieModelBeans = new ArrayList<>();

    /**
     * 返回对应Presenter
     *
     * @return
     */
    @Override
    protected CelebrityDetailPresenter initPresenter() {
        return new CelebrityDetailPresenter();
    }

    /**
     * 返回对应布局
     *
     * @return
     */
    @Override
    protected int setLayoutId() {
        return R.layout.activity_celebrity_detail;
    }

    /**
     * 初始化
     */
    @Override
    protected void initView() {
          //toolbar设置标题
        toolbar.setTitle(getIntent().getStringExtra("title"));
        //以上属性必须在setSupportActionBar(toolbar)之前调用
        setSupportActionBar(toolbar);
        //设置导航Icon
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
        //配置下拉刷新控件
        srlCelebrityDetail.setEnabled(false);
        srlCelebrityDetail.setColorSchemeColors(getIntent().getIntExtra("color", getResources().getColor(R.color.colorMovie)));
        //检查是否夜间模式
        checkIsNightMode();
        //配置RecycleView
        rvWorks.setLayoutManager(new LinearLayoutManager(this));//设置list列风格
        //取消RecycleView滑动,解决嵌套滑动冲突
        rvWorks.setNestedScrollingEnabled(false);
        //导航点击事件,即返回
        toolbar.setNavigationOnClickListener(this);
        //加载数据
        presenter.loadingData(getIntent().getStringExtra("id"));
    }

    /**
     * 是否夜间模式
     */
    private void checkIsNightMode() {
        if (MyApplication.NIGHT_MODE) {
            srlCelebrityDetail.setColorSchemeColors(getResources().getColor(R.color.colorNight));
            cardView.setBackgroundColor(getResources().getColor(R.color.colorNight));
            tvCelebrityName.setTextColor(getResources().getColor(R.color.colorWhite));
            tvCelebrityPlace.setTextColor(getResources().getColor(R.color.colorWhite));
            tvCelebrityRoles.setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    /**
     * 加载数据时
     */
    @Override
    public void showLoading() {
       srlCelebrityDetail.setRefreshing(true);
    }

    /**
     * 加载数据为空时
     */
    @Override
    public void showEmpty() {
      srlCelebrityDetail.setRefreshing(false);
    }

    /**
     * 加载数据失败时
     */
    @Override
    public void showError() {
        srlCelebrityDetail.setRefreshing(false);
        Toast.makeText(this, getText(R.string.error_tips), Toast.LENGTH_SHORT).show();
    }

    /**
     * 加载数据完成时
     * 赋值
     * @param celebrityDetailModel
     */
    @Override
    public void showComplete(CelebrityDetailModel celebrityDetailModel) {
        srlCelebrityDetail.setRefreshing(false);
        llCelebrityDetail.setVisibility(View.VISIBLE);
        //头像
        avatars_large=celebrityDetailModel.getAvatars().getLarge();
        Glide.with(this).load(avatars_large).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(ivCelebrityAvatar);
        //名字
        tvCelebrityName.setText(celebrityDetailModel.getName());
        tvCelebrityNameEn.setText(celebrityDetailModel.getName_en());
        //地区
        tvCelebrityPlace.setText(celebrityDetailModel.getBorn_place());
        //角色
        ArrayList<String> roles=new ArrayList<>();
        for (CelebrityDetailModel.WorksBean worksBean : celebrityDetailModel.getWorks()) {
            movieModelBeans.add(worksBean.getSubject());
            for (String role : worksBean.getRoles()) {
                if (!roles.contains(role)) {
                    roles.add(role);
                }
            }
        }
        String role = "";
        for (int i = 0; i < roles.size(); i++) {
            if (i == roles.size() - 1) {
                role = role + roles.get(i);
            } else {
                role = role + roles.get(i) + " / ";
            }
        }
        tvCelebrityRoles.setText(role);
        //作品
        MovieAdapter worksAdapter=new MovieAdapter(movieModelBeans);
        rvWorks.setAdapter(worksAdapter);
        worksAdapter.setiOnItemClickListener(this);
        //更多作品
        if (MyApplication.NIGHT_MODE) {
            tvCelebrityMore.setBackgroundColor(getResources().getColor(R.color.colorNight));
        } else {
            tvCelebrityMore.setBackgroundColor(getIntent().getIntExtra("color", getResources().getColor(R.color.colorMovie)));
        }
    }


    /**
     * 点击图片和浏览更多
     * @param view
     */
    @OnClick({R.id.iv_celebrity_avatar, R.id.tv_celebrity_more})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.iv_celebrity_avatar:
                intent = new Intent(this, PhotoActivity.class);
                intent.putExtra("img_url", avatars_large);
                startActivity(intent);
                break;
            case R.id.tv_celebrity_more:
                intent = new Intent(this, SearchDetailActivity.class);
                intent.putExtra("theme", getIntent().getIntExtra("theme", R.style.MovieThemeTransNav));
                intent.putExtra("color", getIntent().getIntExtra("color", getResources().getColor(R.color.colorMovie)));
                intent.putExtra("keyword", getIntent().getStringExtra("title"));
                startActivity(intent);
                break;
        }

    }



    /**
     * 返回
     * @param view
     */
    @Override
    public void onClick(View view) {
        finish();
    }

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
}

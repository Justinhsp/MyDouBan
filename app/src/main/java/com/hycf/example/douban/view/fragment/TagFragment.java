package com.hycf.example.douban.view.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.hycf.example.douban.R;
import com.hycf.example.douban.adapter.TagAdapter;
import com.hycf.example.douban.assistview.FullyGridLayoutManager;
import com.hycf.example.douban.assistview.SpaceItemDecoration;
import com.hycf.example.douban.base.LazyFragment;
import com.hycf.example.douban.model.TagData;
import com.hycf.example.douban.msg.MessageEvent;
import com.hycf.example.douban.view.activity.SearchDetailActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by Hui on 2018/3/5.
 */

public class TagFragment extends LazyFragment implements TagAdapter.IOnItemClickListener {
    @BindView(R.id.rv_type)
    RecyclerView rvType;
    @BindView(R.id.rv_country)
    RecyclerView rvCountry;
    @BindView(R.id.rv_artist)
    RecyclerView rvArtist;
    @BindView(R.id.rv_year)
    RecyclerView rvYear;
    @BindView(R.id.ll_tag)
    LinearLayout llTag;

    private static final String TAG = "TagFragment";


    /**
     * 返回对应布局文件
     * @return
     */
    @Override
    protected int setLayoutId() {
        return R.layout.main_tag;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
         initRecycleView(rvType, TagData.types);
         initRecycleView(rvCountry, TagData.countrys);
         initRecycleView(rvArtist, TagData.artists);
         initRecycleView(rvYear, TagData.years);
    }

    /**
     * RecycleView相关配置
     * @param myRecyclerView
     * @param datas
     */
    private void initRecycleView(RecyclerView myRecyclerView, String[] datas) {
        int spacingInPixels=getResources().getDimensionPixelSize(R.dimen.item_space);
        myRecyclerView.setLayoutManager(new FullyGridLayoutManager(getContext(), 4));//Gird类型
        myRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));//设置间隔
        myRecyclerView.setNestedScrollingEnabled(false);
        TagAdapter tagAdapter=new TagAdapter(datas);
        myRecyclerView.setAdapter(tagAdapter);
        //RecycleView Item点击
        tagAdapter.setmIOnItemClickListener(this);
    }


    /**
     * 实现懒加载,当屏幕显示这个界面的时候才会触发次方法
     */
    @Override
    protected void lazyLoad() {

    }

    /**
     * 刷新颜色
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.isNight) {
            llTag.setBackgroundColor(getResources().getColor(R.color.colorNightBg));
        } else {
            llTag.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }
    }


    /**
     * RecycleView Item 点击事件
     * @param tag
     */
    @Override
    public void onItemClick(String tag) {
        Intent intent = new Intent(getContext(), SearchDetailActivity.class);
        intent.putExtra("theme", R.style.TagThemeTransNav);
        intent.putExtra("color", getResources().getColor(R.color.colorTag));
        intent.putExtra("tag", tag);
        startActivity(intent);
    }
}

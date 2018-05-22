package com.hycf.example.douban.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hycf.example.douban.MyApplication;
import com.hycf.example.douban.R;
import com.hycf.example.douban.adapter.FragmentAdapter;
import com.hycf.example.douban.assistview.NoScrollViewPager;
import com.hycf.example.douban.msg.MessageEvent;
import com.hycf.example.douban.view.fragment.AnimeFragment;
import com.hycf.example.douban.view.fragment.MovieFragment;
import com.hycf.example.douban.view.fragment.TVFragment;
import com.hycf.example.douban.view.fragment.TagFragment;
import com.hycf.example.douban.view.fragment.Top250Fragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements IOnSearchClickListener, View.OnClickListener, Toolbar.OnMenuItemClickListener, NavigationView.OnNavigationItemSelectedListener, OnTabSelectListener, OnTabReselectListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;
    @BindView(R.id.bottomBar)
    BottomBar bottomBar;
    @BindView(R.id.navigationview)
    NavigationView navigationview;
    @BindView(R.id.drawerlayout)
    DrawerLayout drawerlayout;

    //Fragment适配器
    private FragmentAdapter fragmentAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    //Github
    private TextView my_github;
    //导航头像
    private CircleImageView user_icon;
    //搜索框
    private SearchFragment searchFragment;
    private Intent intent;
    //当前tab页面
    private int tabAtPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        //toolbar设置标题
        toolbar.setTitle(getResources().getText(R.string.movie));
        //以上属性必须在setSupportActionBar(toolbar)之前调用
        setSupportActionBar(toolbar);
        //设置导航icon
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        //navigation导航的headerLayout控件
        View headerLayout = navigationview.getHeaderView(0);
        my_github = (TextView) headerLayout.findViewById(R.id.my_github);
        user_icon = (CircleImageView) headerLayout.findViewById(R.id.user_icon);
        //初始化Adapter
        fragments.add(new MovieFragment());
        fragments.add(new AnimeFragment());
        fragments.add(new TVFragment());
        fragments.add(new Top250Fragment());
        fragments.add(new TagFragment());
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        //viewpager配置
        viewpager.setNoScroll(true);//viewpager禁止滑动
        viewpager.setOffscreenPageLimit(5);//默认加载5页
        viewpager.setAdapter(fragmentAdapter);
        //监听drawerLayout , 改变导航图标
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.app_name, R.string.app_name);
        toggle.syncState();
        drawerlayout.setDrawerListener(toggle);
        //实例化搜索框
        searchFragment = SearchFragment.newInstance();
        //搜索监听事件
        searchFragment.setOnSearchClickListener(this);
        //导航Icon点击事件
        toolbar.setNavigationOnClickListener(this);
        //添加toolbar子菜单点击事件
        toolbar.setOnMenuItemClickListener(this);
        //右侧抽屉导航子菜单选择事件
        navigationview.setNavigationItemSelectedListener(this);
        //底部导航选择点击事件
        bottomBar.setOnTabSelectListener(this);
        bottomBar.setOnTabReselectListener(this);
    }

    /**
     * 该方法是用来加载toolbar菜单布局的
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单文件
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * 底部导航点击回调
     *
     * @param tabId
     */
    @Override
    public void onTabSelected(int tabId) {
        selectBottomId(tabId);
    }

    @Override
    public void onTabReSelected(int tabId) {
        selectBottomId(tabId);
    }

    private void selectBottomId(int tabId) {
        switch (tabId) {
            case R.id.tab_movie://电影
                setTitleAndColor(0, getResources().getString(R.string.movie), getResources().getColor(R.color.colorMovie), R.style.MovieThemeTransNav);
                break;
            case R.id.tab_anime://动漫
                setTitleAndColor(1, getResources().getString(R.string.anime), getResources().getColor(R.color.colorAnime), R.style.AnimeThemeTransNav);
                break;
            case R.id.tab_tv://电视剧
                setTitleAndColor(2, getResources().getString(R.string.tv), getResources().getColor(R.color.colorTV), R.style.TVThemeTransNav);
                break;
            case R.id.tab_top250://top250
                setTitleAndColor(3, getResources().getString(R.string.top250), getResources().getColor(R.color.colorTop250), R.style.Top250ThemeTransNav);
                break;
            case R.id.tab_tag://分类
                setTitleAndColor(4, getResources().getString(R.string.tag), getResources().getColor(R.color.colorTag), R.style.TagThemeTransNav);
                break;
        }
    }


    /**
     * 设置title和主题颜色
     */
    private void setTitleAndColor(int item, String title, int color, int styleid) {
        tabAtPosition = item;
        if (MyApplication.NIGHT_MODE) {
            color = getResources().getColor(R.color.colorNight);
            styleid = R.style.NightThemeTransNav;
        }
        viewpager.setCurrentItem(item, false);
        toolbar.setTitle(title);
        toolbar.setBackgroundColor(color);
        navigationview.setBackgroundColor(color);
        my_github.setLinkTextColor(color);
        intent = new Intent();
        intent.putExtra("theme", styleid);
        intent.putExtra("color", color);
    }

    /**
     * 搜索回调
     *
     * @param keyword
     */
    @Override
    public void OnSearchClick(String keyword) {
        intent.setClass(this, SearchDetailActivity.class);
        intent.putExtra("keyword", keyword);
        startActivity(intent);
    }

    /**
     * 导航Icon点击回调
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        drawerlayout.openDrawer(Gravity.LEFT);
    }

    /**
     * toolbar子菜单点击回调
     *
     * @param item
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        searchFragment.show(getSupportFragmentManager(), SearchFragment.TAG);
        return true;
    }

    /**
     * 右侧抽屉子菜单点击回调
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setCheckable(false); //取消点击后的阴影
        switch (item.getItemId()) {
            case R.id.nav_exit: //退出
                System.exit(0);
                break;
            case R.id.nav_favorite://收藏
                intent.setClass(this, FavoriteActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_gift://福利
                intent.setClass(this, GiftActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_sun_mode://日间模式
                setDayNightMode(false);
                break;
            case R.id.nav_night_mode://夜间模式
                setDayNightMode(true);
                break;
            default:
                Snackbar.make(toolbar, item.getTitle(), Snackbar.LENGTH_SHORT).show();
                break;
        }
        drawerlayout.closeDrawers();
        return true;
    }

    /**
     * 设置夜间模式
     *
     * @param isNight 是否夜间模式
     */
    private void setDayNightMode(boolean isNight) {
        if (isNight) {
            bottomBar.setItems(R.xml.bottombar_night_tabs);
            viewpager.setBackgroundColor(getResources().getColor(R.color.colorNightBg));
        } else {
            bottomBar.setItems(R.xml.bottombar_tabs);
            viewpager.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }
        MyApplication.NIGHT_MODE = isNight;
        bottomBar.selectTabAtPosition(tabAtPosition);
        EventBus.getDefault().post(new MessageEvent(isNight));
    }


    /**
     * 重写返回
     */
    @Override
    public void onBackPressed() {
        showQuitTips();
    }

    private long firstPressTime = -1;// 记录第一次按下的时间
    private long lastPressTime;// 记录第二次按下的时间

    /**
     * 双击返回退出
     */
    private void showQuitTips() {
        // 如果是第一次按下 直接提示
        if (firstPressTime == -1) {
            firstPressTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
        }
        // 如果是第二次按下，需要判断与上一次按下的时间间隔，这里设置2秒
        else {
            lastPressTime = System.currentTimeMillis();
            if (lastPressTime - firstPressTime <= 2000) {
                System.exit(0);
            } else {
                firstPressTime = lastPressTime;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

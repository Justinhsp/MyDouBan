package com.hycf.example.hsp.view.activity;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hycf.example.hsp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoActivity extends AppCompatActivity implements View.OnLongClickListener {

    @BindView(R.id.photoview)
    PhotoView photoview;

    //图片BitMap
    private Bitmap bitmap;
    //保存图片文件的路径
    private String filePath = Environment.getExternalStorageDirectory() + "/MyDouBan/";
    //保存图片文件的名字
    private String fileName;
    // 要申请的权限
    private static String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        //启用图片缩放功能
        photoview.enable();
        Glide.with(this).load(getIntent().getStringExtra("img_url")).asBitmap().
                diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                bitmap = resource;
                photoview.setImageBitmap(bitmap);
            }
        });
        //长按保存图片
        photoview.setOnLongClickListener(this);
    }


    /**
     * 点击退出
     * @param view
     */
    @OnClick({R.id.photoview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.photoview:
                finish();
                break;
        }
    }

    /**
     * 长按保存图片到手机根目录
     * @param view
     * @return
     */
    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}

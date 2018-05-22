package com.hycf.example.douban.view.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hycf.example.douban.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
     *
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
     *
     * @param view
     * @return
     */
    @Override
    public boolean onLongClick(View view) {
        final Dialog saveDialog = new AlertDialog.Builder(this).create();
        View saveView = LayoutInflater.from(this).inflate(R.layout.dialog_save, null);
        saveView.findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileName = filePath + System.currentTimeMillis() + ".jpg";
                //版本判断 当手机系统大于23时 才有必要去判断权限是否获取
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //检查权限是否获取
                    int i = ContextCompat.checkSelfPermission(PhotoActivity.this, permissions[0]);
                    //权限是否已经授权  GRANTED---授权 DINIED----拒绝
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        //如果没有权限,就去请求权限
                        showDialogTipUserRequestPermission();
                    } else {
                        if (savePicture()) {
                            Toast.makeText(PhotoActivity.this, String.format(getString(R.string.save_success), fileName), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (savePicture()) {
                        Toast.makeText(PhotoActivity.this, String.format(getString(R.string.save_success), fileName), Toast.LENGTH_SHORT).show();
                    }
                }
                saveDialog.dismiss();
            }
        });
        saveDialog.show();
        saveDialog.setContentView(saveView);
        return false;
    }


    /**
     * 保存图片到根目录
     *
     * @return
     */
    private boolean savePicture() {
        boolean isSave;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }

            FileOutputStream out = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            isSave = true;
            //发送一个系统广播通知手机有图片更新
            sendBroadcastToAlbum();
        } catch (IOException e) {
            e.printStackTrace();
            isSave=false;
        }
        return isSave;
    }

    /**
     * 发送一个系统广播通知手机有图片更新
     */
    private void sendBroadcastToAlbum() {
        Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri=Uri.fromFile(new File(fileName));
        intent.setData(uri);
        sendBroadcast(intent);
    }


    /**
     * 提示用户请求权限的弹出框
     */
    private void showDialogTipUserRequestPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.permissions_tips_title))
                .setMessage(getString(R.string.permissions_tips_content))
                .setPositiveButton(getString(R.string.open), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 开始提交请求权限
                        ActivityCompat.requestPermissions(PhotoActivity.this, permissions, 321);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false).show();
    }
}

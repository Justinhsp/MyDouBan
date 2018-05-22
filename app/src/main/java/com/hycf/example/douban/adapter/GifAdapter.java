package com.hycf.example.douban.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hycf.example.douban.R;
import com.hycf.example.douban.model.GiftModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hui on 2018/3/6.
 */

public class GifAdapter extends RecyclerView.Adapter<GifAdapter.MyViewHolder> {

    private List<GiftModel.ResultsBean> gifModels = new ArrayList<>();

    public GifAdapter(List<GiftModel.ResultsBean> gifModels) {
        this.gifModels = gifModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gift_item, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //加载图片
        Glide.with(holder.itemView.getContext()).load(gifModels.get(position).getUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(holder.ivGift);
        //Item点击
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return gifModels.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_gift)
        ImageView ivGift;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private IOnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(IOnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface  IOnItemClickListener{
        void onItemClick(int position);
    }

}

package com.hycf.example.douban.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hycf.example.douban.MyApplication;
import com.hycf.example.douban.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hui on 2018/3/6.
 */

public class MovieDetailAdapter extends RecyclerView.Adapter<MovieDetailAdapter.MyViewHolder> {
    private ArrayList<String> imgs = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();

    /**
     * 通过构造器传值
     *
     * @param imgs
     * @param names
     * @param ids
     */
    public MovieDetailAdapter(ArrayList<String> imgs, ArrayList<String> names, ArrayList<String> ids) {
        this.imgs = imgs;
        this.names = names;
        this.ids = ids;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_detail_item, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        if (MyApplication.NIGHT_MODE) {
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.colorNight));
        }
        //设置头像
        Glide.with(context).load(imgs.get(position)).into(holder.ivMovieDetailItem);
        //设置名字
        holder.tvMovieDetailItem.setText(names.get(position));
        /**
         * item点击事件
         */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(ids.get(holder.getAdapterPosition()), names.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_movie_detail_item)
        ImageView ivMovieDetailItem;
        @BindView(R.id.tv_movie_detail_item)
        TextView tvMovieDetailItem;
        @BindView(R.id.card_view)
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    private mOnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(MovieDetailAdapter.mOnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public  interface mOnItemClickListener{
        void onItemClick(String id, String name);
    }

}

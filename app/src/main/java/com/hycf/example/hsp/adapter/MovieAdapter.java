package com.hycf.example.hsp.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hycf.example.hsp.MyApplication;
import com.hycf.example.hsp.R;
import com.hycf.example.hsp.model.MovieSubjectsModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hui on 2018/3/5.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private static final String TAG = "MovieAdapter";

    private ArrayList<MovieSubjectsModel> movieModel = new ArrayList<>();
    //是否在选择状态
    private boolean isCheck = false;
    //被选择的数据编号
    private ArrayList<Integer> selectPositions = new ArrayList<>();



    public MovieAdapter(ArrayList<MovieSubjectsModel> movieModel) {
        this.movieModel=movieModel;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        Context context = holder.itemView.getContext();
        final MovieSubjectsModel subjectsBean = movieModel.get(position);
        if (MyApplication.NIGHT_MODE) {
            holder.llItem.setBackgroundColor(context.getResources().getColor(R.color.colorNight));
            holder.tvMovieTitle.setTextColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.llItem.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            holder.tvMovieTitle.setTextColor(context.getResources().getColor(R.color.colorMovieText1));
        }
        //控制checkBox显示与影藏
        if (isCheck){
            holder.checkbox.setVisibility(View.VISIBLE);
            //判断是否选择
            if (selectPositions.contains(position)){
                holder.checkbox.setChecked(true);
            }else {
                holder.checkbox.setChecked(false);
            }
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectPositions.contains(holder.getAdapterPosition())){
                        //存在集合中 移除
                        selectPositions.remove((Integer) holder.getAdapterPosition());
                        holder.checkbox.setChecked(false);
                    }else {
                        //不存在添加
                        selectPositions.add(holder.getAdapterPosition());
                        holder.checkbox.setChecked(true);
                    }
                }
            });
        }else {
            holder.checkbox.setVisibility(View.GONE);
        }

        //加载图片
        Glide.with(context).load(subjectsBean.getImages().getMedium()).into(holder.imgMovie);
        //设置title
        holder.tvMovieTitle.setText(subjectsBean.getTitle());
        //设置导演
        String directors = "";
        if (subjectsBean.getDirectors() != null) {
            for (int i = 0; i < subjectsBean.getDirectors().size(); i++) {
                MovieSubjectsModel.DirectorsBean director = subjectsBean.getDirectors().get(i);
                if (i == subjectsBean.getDirectors().size() - 1) {
                    directors = directors + director.getName();
                } else {
                    directors = directors + director.getName() + "、";
                }
            }
        } else {
            directors = context.getResources().getString(R.string.unknown);
        }
        holder.tvMovieDirector.setText(String.format(context.getResources().getString(R.string.director), directors));
        //设置主演
        String casts = "";
        if (subjectsBean.getCasts() != null) {
            for (int i = 0; i < subjectsBean.getCasts().size(); i++) {
                MovieSubjectsModel.CastsBean cats = subjectsBean.getCasts().get(i);
                if (i == subjectsBean.getCasts().size() - 1) {
                    casts = casts + cats.getName();
                } else {
                    casts = casts + cats.getName() + "、";
                }
            }
        } else {
            casts = context.getResources().getString(R.string.unknown);
        }
        holder.tvMovieCast.setText(String.format(context.getResources().getString(R.string.cast), casts));
        //设置分数
        holder.tvMovieAverage.setText(String.format(context.getResources().getString(R.string.average), "" + subjectsBean.getRating().getAverage()));
        //点击回调
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: "+isCheck);
                if (!isCheck) {
                    mOnItemClickListener.onItemClick(holder.getAdapterPosition(), subjectsBean.getMovie_id(), subjectsBean.getImages().getLarge(), subjectsBean.getTitle());
                }else {
                    if (selectPositions.contains(holder.getAdapterPosition())){
                        //存在集合-移除
                        selectPositions.remove((Integer) holder.getAdapterPosition());
                        holder.checkbox.setChecked(false);
                    }else {
                        //不存在集合-添加
                        selectPositions.add(holder.getAdapterPosition());
                        holder.checkbox.setChecked(true);
                    }
                }
            }
        });
    }

    /**
     * 设置选择状态
     * @param isCheck
     */
    public  void setCheck(boolean isCheck){
        this.isCheck=isCheck;
        selectPositions.clear();
    }

    /**
     * 返回被选择的Id
     * @return
     */
    public ArrayList<String> getSelectIds(){
        ArrayList<String> selectIds=new ArrayList<>();
        for (Integer i:selectPositions){
            selectIds.add(movieModel.get(i).getMovie_id());
        }
        return selectIds;
    }

    /**
     * 返回被选择的position
     */
    public ArrayList<MovieSubjectsModel> getSelectModels() {
        ArrayList<MovieSubjectsModel> selectModels = new ArrayList<>();
        for (Integer i : selectPositions) {
            selectModels.add(movieModel.get(i));
        }
        return selectModels;
    }


    @Override
    public int getItemCount() {
        return movieModel.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.checkbox)
        AppCompatCheckBox checkbox;
        @BindView(R.id.img_movie)
        ImageView imgMovie;
        @BindView(R.id.tv_movie_title)
        TextView tvMovieTitle;
        @BindView(R.id.tv_movie_director)
        TextView tvMovieDirector;
        @BindView(R.id.tv_movie_cast)
        TextView tvMovieCast;
        @BindView(R.id.tv_movie_average)
        TextView tvMovieAverage;
        @BindView(R.id.ll_item)
        LinearLayout llItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //接口回调
    private IOnItemClickListener mOnItemClickListener;

    public void setiOnItemClickListener(IOnItemClickListener iOnItemClickListener) {
        this.mOnItemClickListener = iOnItemClickListener;
    }
    public  interface  IOnItemClickListener{
        void onItemClick(int position,String id,String img_url,String title);
    }
}

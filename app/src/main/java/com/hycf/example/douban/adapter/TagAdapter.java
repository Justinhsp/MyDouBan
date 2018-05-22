package com.hycf.example.douban.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hycf.example.douban.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hui on 2018/3/7.
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.MyViewHolder> {

    private String[] tags = null;

    public TagAdapter(String[] tags) {
        this.tags = tags;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_tag_item, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {
         holder.tvMainTagItem.setText(tags[position]);
         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 mIOnItemClickListener.onItemClick(tags[position]);
             }
         });
    }

    @Override
    public int getItemCount() {
        return tags==null ? 0:tags.length;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_main_tag_item)
        TextView tvMainTagItem;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    private IOnItemClickListener mIOnItemClickListener;

    public void setmIOnItemClickListener(IOnItemClickListener mIOnItemClickListener) {
        this.mIOnItemClickListener = mIOnItemClickListener;
    }

    public interface IOnItemClickListener{
        void onItemClick(String tag);
    }

}

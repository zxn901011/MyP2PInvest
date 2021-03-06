package com.zxn.myp2pinvest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by zxn on 2017-08-14.
 */

public abstract class MyBaseAdapter2<T> extends BaseAdapter {
    public List<T> list;

    /**
     * 通过构造器初始化集合数据
     * @param list
     */
    public MyBaseAdapter2(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //将具体的集合数据装配到具体的item layout中
    //问题一、item layout的布局是不确定的
    //问题二、将集合中指定位置的数据装配到item时，是不确定的
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = initView(parent.getContext());
            holder=new ViewHolder(convertView);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        //获取集合数据
        T t=list.get(position);
        //装配数据
        setData(convertView,t);
        return convertView;
    }

    protected abstract void setData(View convertView, T t);

    protected abstract View initView(Context context);

    static class ViewHolder {
        public ViewHolder(View view) {
            view.setTag(this);
        }
    }
}

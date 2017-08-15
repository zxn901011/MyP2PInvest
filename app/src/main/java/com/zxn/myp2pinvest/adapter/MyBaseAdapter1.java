package com.zxn.myp2pinvest.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by zxn on 2017-08-14.
 */

public abstract class MyBaseAdapter1<T> extends BaseAdapter {
    public List<T> list;
    /**
     * 通过构造器初始化集合数据
     * @param list
     */
    public MyBaseAdapter1(List<T> list){
        this.list=list;
    }

    @Override
    public int getCount() {
        return list==null? 0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=myGetView(position,convertView,parent);
        return convertView;
    }

    public abstract View myGetView(int position, View convertView, ViewGroup parent);
}

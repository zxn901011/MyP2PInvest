package com.zxn.myp2pinvest.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.zxn.myp2pinvest.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxn on 2017-08-15.
 * 自定义流式布局
 */

public class FlowLayout extends ViewGroup {
    public FlowLayout(Context context) {
        this(context,null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    //初始化写在这里
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取用户设置的宽高的和具体的值
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);//宽的模式
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);//高的模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);//获取最多的宽度
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);//获取最多的高度

        //如果用户使用的至多模式，那么使用如下两个变量计算真实的宽高
        int width=0;
        int height=0;
        //每一行的宽度
        int lineWidth=0;
        int lineHeight=0;
        //获取子视图
        int childCount=getChildCount();
        for (int i=0;i<childCount;i++){
            View childView=getChildAt(i);
            //要想获取子视图的宽高必须先调用这个方法
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
            //获取子视图的宽高
            int childWidth=childView.getMeasuredWidth();
            int childHeight=childView.getMeasuredHeight();
            //要想保证可以获取子视图的边距参数对象，必须调用generateLayoutParams方法
            MarginLayoutParams params= (MarginLayoutParams) childView.getLayoutParams();
            if ((lineWidth+childWidth+params.leftMargin+params.rightMargin)<=widthSize){
                lineWidth+=childWidth+params.leftMargin+params.rightMargin;
                lineHeight=Math.max(lineHeight,childHeight+params.topMargin+params.bottomMargin);
            }else {//换行
                width=Math.max(width,lineWidth);
                height+=lineHeight;
                lineWidth=childWidth+params.leftMargin+params.rightMargin;
                lineHeight=childHeight+params.topMargin+params.bottomMargin;
            }
            if (i==childCount-1){
                width=Math.max(width,lineWidth);
                height+=lineHeight;
            }
        }

        LogUtil.e("widthSize======="+widthSize+"，heightSize====="+heightSize);
        LogUtil.e("width======="+width+"，height====="+height);


        //设置当前流式布局的宽高
        setMeasuredDimension((widthMode==MeasureSpec.EXACTLY)?widthSize:width,(heightMode==MeasureSpec.EXACTLY)?heightSize:height);
    }
    //重写的目的是给每一个子视图指定显示的位置:childView.layout(l,t,r,b)方法
    private List<List<View>> allViews=new ArrayList<>();//每一行子视图的集合构成的集合
    private List<Integer> allHeights=new ArrayList<>();//每一行的高度构成的集合

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //1.给两个集合添加元素
        int lineWidth=0;
        int lineHeight=0;

        //提供一个集合保存每一行的childView
        List<View> lineViews=new ArrayList<>();
        //获取布局的宽度
        int width=this.getMeasuredWidth();

        int childCount=getChildCount();
        for (int i=0;i<childCount;i++){
            View childView=getChildAt(i);
            //获取子视图的测量宽高
            int childWidth=childView.getMeasuredWidth();
            int childHeight=childView.getMeasuredHeight();
            MarginLayoutParams mp= (MarginLayoutParams) childView.getLayoutParams();
            if ((lineWidth+childWidth+mp.leftMargin+mp.rightMargin)<=width){
                lineViews.add(childView);
                lineWidth+=childWidth+mp.leftMargin+mp.rightMargin;
                lineHeight=Math.max(lineHeight,childHeight+mp.bottomMargin+mp.topMargin);
            }else {//换行
                allViews.add(lineViews);
                allHeights.add(lineHeight);

                lineWidth=childWidth+mp.leftMargin+mp.rightMargin;
                lineHeight=childHeight+mp.bottomMargin+mp.topMargin;
                lineViews=new ArrayList<>();
                lineViews.add(childView);
            }
            if (childCount-1==i){
                allViews.add(lineViews);
                allHeights.add(lineHeight);
            }
        }
        LogUtil.e("allViews.size====="+allViews.size()+",allHeights.size====="+allHeights.size());
        //2.给每个子视图指定显示的位置
        int x=0;
        int y=0;
        for (int i=0;i<allViews.size();i++){//没遍历一次，对应一行元素
            List<View> listView=allViews.get(i);
            for (int j=0;j<listView.size();j++){
                View childView=listView.get(j);
                int childWidth=childView.getMeasuredWidth();
                int childHeight=childView.getMeasuredHeight();
                MarginLayoutParams mp= (MarginLayoutParams) childView.getLayoutParams();
                int left=x+mp.leftMargin;
                int top=y+mp.topMargin;
                int right=left+childWidth;
                int bottom=top+childHeight;
                childView.layout(left,top,right,bottom);

                x+=childWidth+mp.leftMargin+mp.rightMargin;

            }
            y+=allHeights.get(i);
            x=0;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        MarginLayoutParams marginLayoutParams = new MarginLayoutParams(getContext(), attrs);
        return marginLayoutParams;
    }
}

package com.zxn.myp2pinvest.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import com.zxn.myp2pinvest.utils.LogUtil;
import com.zxn.myp2pinvest.utils.UiUtils;

/**
 * Created by zxn on 2017-08-12.
 * 自定义viewGroup
 */

public class MyScrollView extends ScrollView {
    private View childView;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /**
     * 获取子视图
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            childView = getChildAt(0);
        }
    }

    private int lastY;//上一次y轴方向操作的坐标位置
    private Rect normal = new Rect();//记录一下临界状态的上下左右
    private boolean isFinishAnimation = true;//是否动画结束

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (childView == null || !isFinishAnimation) {//当动画没有结束的时候,不让用户触摸
            return super.onTouchEvent(ev);
        }
        int eventY = (int) ev.getY();//获取当前的y轴坐标
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = eventY;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = eventY - lastY;//微小的移动量
                if (isNeedMove()) {
                    if (normal.isEmpty()) {
                        //记录了childView的值，设置一下上下左右
                        normal.set(childView.getLeft(), childView.getTop(), childView.getRight(), childView.getBottom());
                    }
                    //对LinearLayout重新布局
                    childView.layout(childView.getLeft(), childView.getTop() + dy / 2, childView.getRight(), childView.getBottom() + dy / 2);
                }
                lastY = eventY;//重新赋值
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    int translateY = childView.getBottom() - normal.bottom;
                    TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -translateY);
                    translateAnimation.setDuration(1000);
                    translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            isFinishAnimation = false;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isFinishAnimation = true;
                            childView.clearAnimation();
                            childView.layout(normal.left, normal.top, normal.right, normal.bottom);
                            //当动画结束的时候要清除normal的数据
                            normal.setEmpty();//因为每次如果回复之后,不是空,则下次不会走isEmpty()方法
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    childView.startAnimation(translateAnimation);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    //滑出视图
    private boolean isNeedMove() {
        int childMeasuredHeight = childView.getMeasuredHeight();//获取子视图的高度
        int scrollMeasuredHeight = this.getMeasuredHeight();
        LogUtil.e("childMeasuredHeight= " + childMeasuredHeight);
        LogUtil.e("scrollMeasuredHeight= " + scrollMeasuredHeight);
        int dy = childMeasuredHeight - scrollMeasuredHeight;
        int scrollY = this.getScrollY();//获取用户在y轴上移动的偏移量，上+下-，是用户实际上滑动的距离，可能很大
        if (scrollY <= 0 || scrollY >= dy) {
            return true;
        }
        //其他处在临界范围内的，返回false，即表示仍按照ScrollView的方式处理
        return false;
    }

    private int lastX, downX, downY;

    //拦截:实现父视图对子视图的拦截
    //是否拦截成功,取决于方法的返回值,返回true:拦截成功,反之,拦截失败
    //这个方法是在onTouchEvent方法之前
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        int eventX = (int) ev.getX();
        int eventY = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = downX = eventX;
                lastY = downY = eventY;
                break;
            case MotionEvent.ACTION_MOVE:
                //获得水平和垂直方向的移动距离
                int deltaX = Math.abs(eventX - downX);
                int deltaY = Math.abs(eventY - downY);
                if (deltaY>deltaX&&deltaY>= UiUtils.dip2px(10)){
                    isIntercept=true;//执行拦截
                }
                lastX = eventX;
                lastY = eventY;
                break;
        }
        return isIntercept;
    }
}

package com.zxn.myp2pinvest.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.utils.UiUtils;

/**
 * Created by zxn on 2017-08-12.
 * 自定义视图
 */

public class RoundProgress extends View {
    //下面这些是属性，在代码层面定义的
    //可以在属性文件中定义，attr，替代了
    //设置绘制的圆环及文本的属性
//    private int roundCorlor= Color.GRAY;//圆环的颜色
//    private int roundProgressColor=Color.RED;//圆弧的颜色
//    private int textColor= Color.BLUE;//文本的颜色
//
//    private int roundWidth= UiUtils.dip2px(10);
//    private int textSize=UiUtils.dip2px(20);
//
//    private float max=100;//圆环的最大值

    //自定义属性的声明，使用自定义属性来初始化如下的变量
    private int roundCorlor;//圆环的颜色
    private int roundProgressColor;//圆弧的颜色
    private int textColor;//文本的颜色
    private float roundWidth;
    private float textSize;
    private int max;//圆环的最大值
    private int progress;//圆环的进度

    private int width;//当前视图的宽度(=高度)
    private Paint paint;

    public RoundProgress(Context context) {
        this(context,null);
    }

    public RoundProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint=new Paint();
        paint.setAntiAlias(true);//去除锯齿
        //1.获取自定义的属性,调用两个参数的方法
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.RoundProgress);
        //2.取出所有的自定义属性
        roundCorlor=array.getColor(R.styleable.RoundProgress_roundColor,Color.GRAY);
        roundProgressColor=array.getColor(R.styleable.RoundProgress_roundProgressColor,Color.RED);
        textColor=array.getColor(R.styleable.RoundProgress_textColor,Color.GREEN);
        roundWidth=array.getDimension(R.styleable.RoundProgress_roundWidth,UiUtils.dip2px(10));
        textSize=array.getDimension(R.styleable.RoundProgress_textSize,UiUtils.dip2px(20));
        max=array.getInteger(R.styleable.RoundProgress_max,100);
        progress=array.getInteger(R.styleable.RoundProgress_progress,30);
        //回收处理,不要忘了
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width=this.getMeasuredWidth();
    }


    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1.绘制圆环
        //获取圆心坐标
        int cx=width/2;
        int cy=width/2;
        float radius=  width/2-roundWidth/2;
        paint.setColor(roundCorlor);
        paint.setStyle(Paint.Style.STROKE);//设置圆环的样式
        paint.setStrokeWidth(roundWidth);//设置圆环的宽度
        canvas.drawCircle(cx,cy,radius,paint);
        //2.绘制圆弧
        RectF rectF=new RectF(roundWidth/2,roundWidth/2,width-roundWidth/2,width-roundWidth/2);
        paint.setColor(roundProgressColor);
        canvas.drawArc(rectF,0,progress*360/max,false,paint);//true和false是否使用中心点，使用的话就会连起来，形成两条线

        //3.绘制文本
        String text=progress*100 / max +"%";
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setStrokeWidth(0);
        Rect rect=new Rect();//创建一个矩形，此时矩形没有具体的宽度和高度
        paint.getTextBounds(text,0,text.length(),rect);//此时的矩形宽高即为整好包裹文本的矩形的宽高
        //获取左下顶点的坐标
        int x=width/2-rect.width()/2;
        int y=width/2+rect.height()/2;
        canvas.drawText(text,x,y,paint);
    }
}

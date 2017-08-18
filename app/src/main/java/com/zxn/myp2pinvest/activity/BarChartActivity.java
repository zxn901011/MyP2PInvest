package com.zxn.myp2pinvest.activity;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.common.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class BarChartActivity extends BaseActivity {

    @Bind(R.id.iv_title)
    ImageView ivTitle;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.bar_chart)
    BarChart barChart;
    private Typeface mTf;

    @Override
    protected void initTitle() {
        ivTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("柱状图");
        ivSetting.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.iv_title)
    public void back(View view) {
        removeCurrentActivity();
    }

    @Override
    protected void initData() {
        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        barChart.setDescription("三星note7爆炸之后，三星品牌度情况");
        barChart.setDrawGridBackground(false);
        //是否绘制柱状图的背景，不太好看
        barChart.setDrawBarShadow(false);

        //获取x轴对象
        XAxis xAxis = barChart.getXAxis();
        //设置x轴的显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置x轴的字体
        xAxis.setTypeface(mTf);
        //是否绘制x轴的网格线
        xAxis.setDrawGridLines(false);
        //是否绘制x轴的轴线
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(20f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(20f);

        BarData barData = generateDataBar();
        barData.setValueTypeface(mTf);

        // set data
        barChart.setData( barData);
        // do not forget to refresh the chart
//        holder.chart.invalidate();
        barChart.animateY(700);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bar_chart;
    }
    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private BarData generateDataBar() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < 12; i++) {
            entries.add(new BarEntry((int) (Math.random() * 70) + 30, i));
        }

        BarDataSet d = new BarDataSet(entries, "New DataSet1");
        //设置相邻柱状图之间的距离
        d.setBarSpacePercent(20f);
        //设置颜色
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        //设置高亮透明度，点击上的颜色变化
        d.setHighLightAlpha(255);

        BarData cd = new BarData(getMonths(), d);
        return cd;
    }
    private ArrayList<String> getMonths() {

        ArrayList<String> m = new ArrayList<String>();
        m.add("Jan");
        m.add("Feb");
        m.add("Mar");
        m.add("Apr");
        m.add("May");
        m.add("Jun");
        m.add("Jul");
        m.add("Aug");
        m.add("Sep");
        m.add("Okt");
        m.add("Nov");
        m.add("Dec");

        return m;
    }

}

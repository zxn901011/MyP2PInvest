package com.zxn.myp2pinvest.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.zxn.myp2pinvest.R;
import com.zxn.myp2pinvest.common.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class PieChartActivity extends BaseActivity {

    @Bind(R.id.iv_title)
    ImageView ivTitle;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.pie_chart)
    PieChart pieChart;

    private Typeface mTf;
    @Override
    protected void initTitle() {
        ivTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("饼状图");
        ivSetting.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.iv_title)
    public void back(View view) {
        removeCurrentActivity();
    }

    @Override
    protected void initData() {

        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        // apply styling
        pieChart.setDescription("Android厂商2016年手机占有率");
        pieChart.setHoleRadius(52f);//最内层的圆的半径
        pieChart.setTransparentCircleRadius(57f);//包裹内层圆的半径
        pieChart.setCenterText("Android\n厂商占比");
        pieChart.setCenterTextTypeface(mTf);
        pieChart.setCenterTextSize(18f);
        //是否使用百分比，true各部分的百分比的和是100%
        pieChart.setUsePercentValues(true);

        PieData pieData = generateDataPie();
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTypeface(mTf);
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.WHITE);
        // set data
        pieChart.setData(pieData);
        //获取右上角描述结构
        Legend l =pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setYEntrySpace(0f);//相邻的entry在y轴上的间距
        l.setYOffset(0f);//第一个entry距离最顶端的距离

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        pieChart.animateXY(900, 900);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pie_chart;
    }
    /**
     * generates a random ChartData object with just one DataSet
     * @return
     */
    private PieData generateDataPie() {

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < 4; i++) {
            entries.add(new Entry((int) (Math.random() * 70) + 30, i));
        }
        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);//各部分之间的距离
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(getQuarters(), d);//四块
        return cd;
    }
    private ArrayList<String> getQuarters() {

        ArrayList<String> q = new ArrayList<String>();
        q.add("三星");
        q.add("华为");
        q.add("小米");
        q.add("ViVo");
        return q;
    }

}

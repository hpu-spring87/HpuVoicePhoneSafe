package com.hpuvoice.phonesafe;

import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendPosition;
import com.hpuvoice.phonesafe.bean.AppInfo;
import com.hpuvoice.phonesafe.engine.AppInfoService;
import com.hpuvoice.phonesafe.ui.CharDemoBase;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.TrafficStats;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class TrafficMangerActivity extends CharDemoBase implements 
OnChartValueSelectedListener {

	private PieChart mChart;
    
    String[] mParties = new String[1000];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trafficmanger);
		//------------------------初始化数据----------------------
		AppInfoService appservice = new AppInfoService(this);
    	List<AppInfo> infos = appservice.getAllApps();
    	
    		int index = 0;
    		for (AppInfo entry : infos) {
    			if(!(entry.isSystemApp())){
    				mParties[index] = entry.getAppname();
    				index++;
    			}
    		}
    		//-------流量--------
    		long receive = TrafficStats.getMobileRxBytes();
    		long send = TrafficStats.getMobileTxBytes();
    		long tottal = TrafficStats.getTotalRxBytes();
    		String receiveStr = Formatter.formatFileSize(this, receive);
    		String sendStr = Formatter.formatFileSize(this, send);
    		String total = Formatter.formatFileSize(this, tottal);
    		System.out.println("---------start-------------");
    		System.out.println(receiveStr+"==="+sendStr);
    		System.out.println("---------end-------------");
		//------------------------初始化数据----------------------
		




        mChart = (PieChart) findViewById(R.id.chart1);

        // change the color of the center-hole
        mChart.setHoleColor(Color.rgb(235, 235, 235));

        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart.setValueTypeface(tf);
        mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));

        mChart.setHoleRadius(60f);

        mChart.setDescription("");

        mChart.setDrawYValues(true);
        mChart.setDrawCenterText(true);

        mChart.setDrawHoleEnabled(true);

        mChart.setRotationAngle(0);

        // draws the corresponding description value into the slice
        mChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);

        // display percentage values
        mChart.setUsePercentValues(true);
        // mChart.setUnit(" 鈧�");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);
        //System.out.println(receiveStr+"==="+sendStr);
        mChart.setCenterText("流量使用统计:\n"+"接受:"+receiveStr+"\n发送:"+sendStr+"\n总计:"+total);
        setData(5, 100);

        mChart.animateXY(1500, 1500);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
		
	}
	
	
	public String getTrafficByUid( String packageName){
		return null;
		
	}

	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.pie, menu);
	        return true;
	    }

	 /**
	  * 条目点击监听--
	  */
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {

	        switch (item.getItemId()) {
	            case R.id.actionToggleValues: {
	                if (mChart.isDrawYValuesEnabled())
	                    mChart.setDrawYValues(false);
	                else
	                    mChart.setDrawYValues(true);
	                mChart.invalidate();
	                break;
	            }
	            case R.id.actionTogglePercent: {
	                if (mChart.isUsePercentValuesEnabled())
	                    mChart.setUsePercentValues(false);
	                else
	                    mChart.setUsePercentValues(true);
	                mChart.invalidate();
	                break;
	            }
	            case R.id.actionToggleHole: {
	                if (mChart.isDrawHoleEnabled())
	                    mChart.setDrawHoleEnabled(false);
	                else
	                    mChart.setDrawHoleEnabled(true);
	                mChart.invalidate();
	                break;
	            }
	            case R.id.actionDrawCenter: {
	                if (mChart.isDrawCenterTextEnabled())
	                    mChart.setDrawCenterText(false);
	                else
	                    mChart.setDrawCenterText(true);
	                mChart.invalidate();
	                break;
	            }
	            case R.id.actionToggleXVals: {
	                if (mChart.isDrawXValuesEnabled())
	                    mChart.setDrawXValues(false);
	                else
	                    mChart.setDrawXValues(true);
	                mChart.invalidate();
	                break;
	            }
	            case R.id.actionSave: {
	                // mChart.saveToGallery("title"+System.currentTimeMillis());
	                mChart.saveToPath("title" + System.currentTimeMillis(), "");
	                break;
	            }
	            case R.id.animateX: {
	                mChart.animateX(1800);
	                break;
	            }
	            case R.id.animateY: {
	                mChart.animateY(1800);
	                break;
	            }
	            case R.id.animateXY: {
	                mChart.animateXY(1800, 1800);
	                break;
	            }
	        }
	        return true;
	    }

	   
	    /**
	     * 设置数据--
	     * @param count
	     * @param range
	     */
	    private void setData(int count, float range) {

	        float mult = range;

	        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
	        
	        
	       

	        // IMPORTANT: In a PieChart, no values (Entry) should have the same
	        // xIndex (even if from different DataSets), since no values can be
	        // drawn above each other.
	        for (int i = 0; i < count + 1; i++) {
	            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
	        }

	        ArrayList<String> xVals = new ArrayList<String>();

			for (int i = 0; i < count + 1; i++)
	           xVals.add(mParties[i]);

	        PieDataSet set1 = new PieDataSet(yVals1, "App Name");
	        set1.setSliceSpace(3f);
	        
	        // add a lot of colors

	        ArrayList<Integer> colors = new ArrayList<Integer>();

	        for (int c : ColorTemplate.VORDIPLOM_COLORS)
	            colors.add(c);

	        for (int c : ColorTemplate.JOYFUL_COLORS)
	            colors.add(c);

	        for (int c : ColorTemplate.COLORFUL_COLORS)
	            colors.add(c);

	        for (int c : ColorTemplate.LIBERTY_COLORS)
	            colors.add(c);
	        
	        for (int c : ColorTemplate.PASTEL_COLORS)
	            colors.add(c);
	        
	        colors.add(ColorTemplate.getHoloBlue());

	        set1.setColors(colors);

	        PieData data = new PieData(xVals, set1);
	        mChart.setData(data);

	        // undo all highlights
	        mChart.highlightValues(null);

	        mChart.invalidate();
	    }

	    /**
	     * 选择数据监听--
	     */
	    @Override
	    public void onValueSelected(Entry e, int dataSetIndex) {

	        if (e == null)
	            return;
	        Log.i("VAL SELECTED",
	                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
	                        + ", DataSet index: " + dataSetIndex);
	    }

	    @Override
	    public void onNothingSelected() {
	        Log.i("PieChart", "nothing selected");
	    }



	    // private void removeLastEntry() {
	    //
	    // PieData data = mChart.getDataOriginal();
	    //
	    // if (data != null) {
	    //
	    // PieDataSet set = data.getDataSet();
	    //
	    // if (set != null) {
	    //
	    // Entry e = set.getEntryForXIndex(set.getEntryCount() - 1);
	    //
	    // data.removeEntry(e, 0);
	    // // or remove by index
	    // // mData.removeEntry(xIndex, dataSetIndex);
	    //
	    // mChart.notifyDataSetChanged();
	    // mChart.invalidate();
	    // }
	    // }
	    // }
	    //
	    // private void addEntry() {
	    //
	    // PieData data = mChart.getDataOriginal();
	    //
	    // if (data != null) {
	    //
	    // PieDataSet set = data.getDataSet();
	    // // set.addEntry(...);
	    //
	    // data.addEntry(new Entry((float) (Math.random() * 25) + 20f,
	    // set.getEntryCount()), 0);
	    //
	    // // let the chart know it's data has changed
	    // mChart.notifyDataSetChanged();
	    //
	    // // redraw the chart
	    // mChart.invalidate();
	    // }
	    // }
	    
	    
	    /**
		 * 返回主界面
		 * 
		 * @param view
		 */
		public void gohome(View view) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
}

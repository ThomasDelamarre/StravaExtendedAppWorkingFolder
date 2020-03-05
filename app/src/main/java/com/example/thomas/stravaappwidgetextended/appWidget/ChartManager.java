package com.example.thomas.stravaappwidgetextended.appWidget;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.thomas.stravaappwidgetextended.Constants;
import com.example.thomas.stravaappwidgetextended.R;
import com.example.thomas.stravaappwidgetextended.appWidget.DataPreparator;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.min;
import static java.lang.Math.round;


public class ChartManager {

    private Context context;
    private DataPreparator data_prep;
    private float total_distance;

    private final static float TEXT_SIZE = 12f;
    private final static int GRAPH_MAX_WIDTH = 800;

    public ChartManager(Context context) {
        this.context = context;
        this.data_prep = new DataPreparator(this.context);
    }

    public Bitmap getBarChartInBitmap(String sport_type) {

        Bitmap bitmap;

        BarChart barchart = new BarChart(this.context);

        DataPreparator.ConvenientReturnFormat r = data_prep.getChartData(sport_type); //Graph type handled in the ft
        BarData data = r.getBarData();
        ArrayList<String> labels = r.getLabels();
        int today_index = r.getTodayIndex();
        this.total_distance = r.getTotalDistance();

        //Configure then set BarData
        data.setValueFormatter(new MyValueFormatter()); //Remove label if data <0.4
        data.setValueTextSize(TEXT_SIZE); //set text size on top of bars
        barchart.setData(data); // set the data

        //Configure barchart properties
        barchart.setDrawGridBackground(false);
        barchart.setDrawBorders(false);
        barchart.getDescription().setEnabled(false);
        barchart.getLegend().setEnabled(false);
        barchart.setFitBars(true); // make the x-axis fit exactly all bars
        barchart.getAxisLeft().setEnabled(false);
        barchart.getAxisLeft().setAxisMinimum(0f);
        barchart.getAxisRight().setEnabled(false);
        barchart.setExtraOffsets(0, 0, 0, 4);

        //Below handles xAxis label colors
        barchart.setXAxisRenderer(new ColoredLabelXAxisRenderer(barchart.getViewPortHandler(), barchart.getXAxis(), barchart.getTransformer(YAxis.AxisDependency.LEFT), today_index));

        //Set Xaxis parameters
        XAxis x = barchart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setDrawAxisLine(false);
        x.setValueFormatter(new IndexAxisValueFormatter(labels)); //Set Xaxis labels
        x.setTextSize(TEXT_SIZE); // Set Xaxis labels text sixe
        x.setGranularity(0.1f); // Ca sert à quoi ?

        //TODO handle number of labels : max = 25,
        x.setLabelCount(labels.size(), Boolean.FALSE); //Max number is 25 -> maybe write only

        //Refresh barchart with all the update parameters
        barchart.invalidate();

        //Required to convert in Bitmap
        //TODO understand ce que ca fait pour l'instant c'est des valeurs au pif

        barchart.measure(220, 60);

        //Set graph size
        int graph_width = GRAPH_MAX_WIDTH;
        if(labels.size() > 0) {
            graph_width = min(80*labels.size(), GRAPH_MAX_WIDTH);
        }
        barchart.layout(0, 0, graph_width, 180); //Set the graph size

        bitmap = barchart.getChartBitmap();
        return bitmap;
    }

    public float getTotalDistance(){
        //TODO Fix CAREFUL : YOU MUST ONLY USE AFTER FETCHING NEW BARCHART ...
        //So far no issues
        return this.total_distance;
    }

    private class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if(value > 0.4f) {
                return mFormat.format(value);
            } else {
                return "";
            }
        }
    }

    private class ColoredLabelXAxisRenderer extends XAxisRenderer {

        List<Integer> labelColors;
        private int today_index;

        public ColoredLabelXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans, int today_index) {
            super(viewPortHandler, xAxis, trans);
            labelColors = new ArrayList<Integer>();
            labelColors.add(ContextCompat.getColor(context, R.color.green));
            labelColors.add(ContextCompat.getColor(context,R.color.black));
            labelColors.add(ContextCompat.getColor(context,R.color.grey));
            this.today_index = today_index;

        }

        @Override
        protected void drawLabels(Canvas c, float pos, MPPointF anchor) {
            final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
            boolean centeringEnabled = mXAxis.isCenterAxisLabelsEnabled();

            float[] positions = new float[mXAxis.mEntryCount * 2];

            for (int i = 0; i < positions.length; i += 2) {

                // only fill x values
                if (centeringEnabled) {
                    positions[i] = mXAxis.mCenteredEntries[i / 2];
                } else {
                    positions[i] = mXAxis.mEntries[i / 2];
                }
            }

            mTrans.pointValuesToPixel(positions);

            for (int i = 0; i < positions.length; i += 2) {

                float x = positions[i];

                if (mViewPortHandler.isInBoundsX(x)) {

                    String label = mXAxis.getValueFormatter().getFormattedValue(mXAxis.mEntries[i / 2], mXAxis);

                    int color = 0;
                    if (i < 2*today_index){
                        color = labelColors.get(0);
                    } else if (i > 2*today_index){
                        color = labelColors.get(2);
                    } else {
                        color = labelColors.get(1);
                    }

                    mAxisLabelPaint.setColor(color);

                    if (mXAxis.isAvoidFirstLastClippingEnabled()) {

                        // avoid clipping of the last
                        if (i == mXAxis.mEntryCount - 1 && mXAxis.mEntryCount > 1) {
                            float width = Utils.calcTextWidth(mAxisLabelPaint, label);

                            if (width > mViewPortHandler.offsetRight() * 2
                                    && x + width > mViewPortHandler.getChartWidth())
                                x -= width / 2;

                            // avoid clipping of the first
                        } else if (i == 0) {

                            float width = Utils.calcTextWidth(mAxisLabelPaint, label);
                            x += width / 2;
                        }
                    }

                    drawLabel(c, label, x, pos, anchor, labelRotationAngleDegrees);
                }
            }
        }

        private int getColorForXValue(int index) {
            if (index >= labelColors.size()) return mXAxis.getTextColor();

            if (index < 0) return mXAxis.getTextColor();

            return labelColors.get(index);
        }
    }
}
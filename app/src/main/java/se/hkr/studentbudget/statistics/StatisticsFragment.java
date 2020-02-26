package se.hkr.studentbudget.statistics;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import se.hkr.studentbudget.R;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class StatisticsFragment extends Fragment {
    private String tag = "Info";

    private BarChart mBarChart;
    private PieChart mPieChart;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        Log.d(tag, "In the StatisticsFragment");
        // use view to get things from the windows
        TextView textView = view.findViewById(R.id.textTest);
        textView.setText("Statistics");
        mBarChart = view.findViewById(R.id.barchart);
        mPieChart = view.findViewById(R.id.pieshart);
        pieChart();
        return view;
    }

    public void barchar(){
    List<BarEntry> barEntries = new ArrayList<>();
    barEntries.add(new BarEntry(2020,56));
    barEntries.add(new BarEntry(2020,56));

        BarDataSet barDataSet = new BarDataSet(barEntries,"test");
        barDataSet.setColor(getResources().getColor(R.color.colorExpense));

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);

        mBarChart.setVisibility(View.VISIBLE);
        mBarChart.animateY(3000);
        mBarChart.setData(barData);
        mBarChart.setFitBars(true);
        mBarChart.invalidate();

    }

    public void pieChart(){
        List<PieEntry> pieEntries = new ArrayList<>();

        // first entry data value, second is data descriptor
        pieEntries.add(new PieEntry(200.14f,""));
        pieEntries.add(new PieEntry(4600,""));

     //   mPieChart.setVisibility(View.VISIBLE);
        mPieChart.animateXY(5000,5000);
        mPieChart.setHoleRadius(1);
        mPieChart.setTransparentCircleRadius(1);
        //mPieChart.setUsePercentValues(true);
        mPieChart.setDrawEntryLabels(false);


        PieDataSet pieDataSet = new PieDataSet(pieEntries,"Test");
        pieDataSet.setSliceSpace(1);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setDrawValues(false);
        PieData pieData = new PieData(pieDataSet);
        mPieChart.setData(pieData);
        mPieChart.getLegend().setEnabled(false);
        Description description = new Description();
        description.setText("Testing description");
        mPieChart.setDescription(description);
        mPieChart.invalidate();
    }
}
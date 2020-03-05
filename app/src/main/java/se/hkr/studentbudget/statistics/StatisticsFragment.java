package se.hkr.studentbudget.statistics;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import se.hkr.studentbudget.AppConstants;
import se.hkr.studentbudget.Category;
import se.hkr.studentbudget.R;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import se.hkr.studentbudget.StaticStrings;
import se.hkr.studentbudget.database.DataBaseAccess;
import se.hkr.studentbudget.transactions.CategoryRowItem;
import se.hkr.studentbudget.transactions.DatePickerFragment;

public class StatisticsFragment extends Fragment implements DatePickerDialog.OnDateSetListener, OnChartValueSelectedListener {
    private String tag = "Info";

    private BarChart mBarChart;
    private PieChart mPieChart;
    private Button mainBtn, fromBtn, toBtn;
    private int datePickerStyle, radioIdMain, radioIdGraph;
    private boolean fromClicked, toClicked;
    private String strDateFrom, strDateTo;
    private RadioGroup radioGroupMain, radioGroupChart;
    private Handler handler;
    private ArrayList<Category> categories;
    private ValueFormatter formatter;
    private CardView graphCard;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        Log.d(tag, "In the StatisticsFragment");
        handler = new Handler();
        graphCard = view.findViewById(R.id.grahCard);
        mBarChart = view.findViewById(R.id.barchart);
        mainBtn = view.findViewById(R.id.showgraphBtn);
        fromBtn = view.findViewById(R.id.fromDateBtn);
        toBtn = view.findViewById(R.id.toDateBtn);
        radioGroupMain = view.findViewById(R.id.radio_group_main);
        radioGroupChart = view.findViewById(R.id.radiogroup_chart);
        mPieChart = view.findViewById(R.id.pieshart);
        mPieChart.setOnChartValueSelectedListener(this);
        datePickerStyle = R.style.DatePickerStanTheme;
        fromClicked = false;
        toClicked = false;
        currentDate();

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioIdMain = radioGroupMain.getCheckedRadioButtonId();
                radioIdGraph = radioGroupChart.getCheckedRadioButtonId();
                threadUpdateTextView(radioIdMain, radioIdGraph);
            }
        });

        fromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromClicked = true;
                openCaleden();
            }
        });

        toBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toClicked = true;
                openCaleden();
            }
        });

        return view;
    }

    //Background thread that updates UI
    private void threadUpdateTextView(final int radioIdMain, final int radioIdGraph) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                categories = new ArrayList<>();
                ArrayList<String> xAxisLabel = new ArrayList<>();
                ArrayList<CategoryRowItem> list;
                xAxisLabel.clear();
                switch (radioIdMain) {
                    case R.id.radioExpense:
                        initDataChart(AppConstants.expenseList, categories, StaticStrings.EXPENSE);
                        formatter = test(xAxisLabel,categories);
                        break;
                    case R.id.radioIncome:
                        initDataChart(AppConstants.incomeList, categories, StaticStrings.INCOME);
                        formatter = test(xAxisLabel,categories);
                        break;
                    case R.id.radioBoth:
                        initDataBoth(categories);
                        formatter = test(xAxisLabel,categories);
                        break;
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        graphCard.setVisibility(View.GONE);
                        mBarChart.setVisibility(View.GONE);
                        mPieChart.setVisibility(View.GONE);
                        switch (radioIdGraph) {
                            case R.id.radioPieChart:
                                pieChart(categories);
                                break;
                            case R.id.radioBarChart:
                                barchar(categories, formatter);
                                break;
                        }
                    }
                });
            }
        });
        th.start();
    }

    private ValueFormatter test(final ArrayList<String> xAxisLabel,ArrayList<Category> categories){
        if (radioIdMain == R.id.radioBoth) {
            xAxisLabel.add(StaticStrings.INCOME);
            xAxisLabel.add(StaticStrings.EXPENSE);
        }else {
            for (int i = 0; i < categories.size(); i++) {
                xAxisLabel.add(categories.get(i).getCategoryname());
            }
        }
        return new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                try {
                    return xAxisLabel.get((int) value);
                } catch (Exception e) {
                    return "";
                }
            }
        };
    }

    private void initDataChart(ArrayList<CategoryRowItem> list, ArrayList<Category> categories, String type) {
        Category m;
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getContext());
        dataBaseAccess.openDatabase();
        for (int i = 0; i < list.size(); i++) {
            double value = Math.abs(dataBaseAccess.getTotalSumCategory(list.get(i).getmCategoryName(),type, strDateFrom, strDateTo));
            if (value > 0.0) {
                m = new Category(list.get(i).getmCategoryName(), value);
                categories.add(m);
            }
        }
        dataBaseAccess.closeDatabe();
    }

    private void initDataBoth(ArrayList<Category> categories) {
        Category m;
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getContext());
        dataBaseAccess.openDatabase();
        double value = Math.abs(dataBaseAccess.getTotalSumTransactionType(StaticStrings.INCOME, strDateFrom, strDateTo));
        m = new Category(StaticStrings.INCOME, value);
        categories.add(m);
        value = Math.abs(dataBaseAccess.getTotalSumTransactionType(StaticStrings.EXPENSE, strDateFrom, strDateTo));
        m = new Category(StaticStrings.EXPENSE, value);
        categories.add(m);
        dataBaseAccess.closeDatabe();
    }

    private void barchar(ArrayList<Category> categories, ValueFormatter formatter) {

        List<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            barEntries.add(new BarEntry(i, (float) categories.get(i).getTotalValue()));
        }
        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        mBarChart.notifyDataSetChanged();
        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        if (radioIdMain == R.id.radioBoth) {
            int[] color = {R.color.colorIncome, R.color.colorExpense};
            barDataSet.setColors(color, getContext());
        }else {
            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        }
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        barData.setDrawValues(true);
        barData.setHighlightEnabled(false);
        mBarChart.setData(barData);
        mBarChart.setFitBars(true);
        mBarChart.getLegend().setEnabled(false);
        mBarChart.getDescription().setEnabled(false);
        graphCard.setVisibility(View.VISIBLE);
        mBarChart.setVisibility(View.VISIBLE);
        mBarChart.animateY(1000);
        mBarChart.invalidate();

    }

    private void pieChart(ArrayList<Category> categories) {
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            pieEntries.add(new PieEntry((float) categories.get(i).getTotalValue(), categories.get(i).getCategoryname()));
        }
        mPieChart.setHoleRadius(45);
        mPieChart.setTransparentCircleRadius(55);
        //  mPieChart.setUsePercentValues(true);
        mPieChart.setDrawEntryLabels(true);
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setSliceSpace(1);
        if (radioIdMain == R.id.radioBoth) {
            int[] color = {R.color.colorIncome, R.color.colorExpense};
            pieDataSet.setColors(color, getContext());
        }else {
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        }
        pieDataSet.setDrawValues(false);
        PieData pieData = new PieData(pieDataSet);
        mPieChart.setCenterText("");
        mPieChart.setDrawCenterText(true);
        mPieChart.setData(pieData);
        mPieChart.getLegend().setEnabled(false);
        mPieChart.getDescription().setEnabled(false);
        graphCard.setVisibility(View.VISIBLE);
        mPieChart.setVisibility(View.VISIBLE);
        mPieChart.animateXY(1000, 1000);
        mPieChart.invalidate();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = null;
        Date date2 = null;
        if (fromClicked) {
            strDateFrom = setDateFormat(year, month, day, "yyyy-MM-dd");
            fromBtn.setText(strDateFrom);
            fromClicked = false;
        }
        if (toClicked) {
            strDateTo = setDateFormat(year, month, day, "yyyy-MM-dd");
            try {
                date1 = sdf.parse(strDateFrom);
                date2 = sdf.parse(strDateTo);
            } catch (ParseException e) {
                Log.e(tag, e.getMessage());
            }
            if (date2.before(date1)) {
                AppConstants.toastMessage(getContext(), "End date have to be after From date!");
            } else {
                toBtn.setText(strDateTo);
            }
            toClicked = false;
        }
    }

    private String setDateFormat(int year, int month, int day, String s) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat dateFormatter = new SimpleDateFormat(s, Locale.getDefault());
        return dateFormatter.format(calendar.getTime());
    }

    private void currentDate() {
        LocalDate start = YearMonth.now().atDay(1);
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        strDateFrom = start.format(dateFormatter);
        strDateTo = today.format(dateFormatter);
        fromBtn.setText(strDateFrom);
        toBtn.setText(strDateTo);
    }

    private void openCaleden() {
        //Select theme for datepicker Popup
        DialogFragment datePickerFragment = new DatePickerFragment(datePickerStyle); // creating DialogFragment which creates DatePickerDialog
        datePickerFragment.setTargetFragment(StatisticsFragment.this, 0);  // Passing this fragment DatePickerFragment.
        // display fragment
        datePickerFragment.show(Objects.requireNonNull(getFragmentManager()), "datePicker");
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i(tag, "Value: " + e.getY() + ", index: " + h.getX());
        int i = (int) h.getX();
        float valueClick = e.getY();
        String text = String.format("%s:\n%s Kr", categories.get(i).getCategoryname(), String.format(Locale.getDefault(), "%,.2f", valueClick));
        mPieChart.setCenterText(text);
        mPieChart.setCenterTextSize(16f);
    }

    @Override
    public void onNothingSelected() {

    }
}
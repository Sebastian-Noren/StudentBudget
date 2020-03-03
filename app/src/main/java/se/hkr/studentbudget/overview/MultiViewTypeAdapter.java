package se.hkr.studentbudget.overview;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import se.hkr.studentbudget.AppConstants;
import se.hkr.studentbudget.AppMathCalc;
import se.hkr.studentbudget.R;


public class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<TestModel> dataSet;
    private Context mContext;

    public MultiViewTypeAdapter(Context context, ArrayList<TestModel> data) {
        this.dataSet = data;
        this.mContext = context;
    }


    public static class SummaryTypeViewHolder extends RecyclerView.ViewHolder {

        TextView income, expense, total, currentMonth;
        PieChart mPieChart;

        public SummaryTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.currentMonth = itemView.findViewById(R.id.sum_card_month_text);
            this.income = itemView.findViewById(R.id.sum_income_card);
            this.expense = itemView.findViewById(R.id.sum_expense_card);
            this.total = itemView.findViewById(R.id.sum_total_card);
            this.mPieChart = itemView.findViewById(R.id.overview_pieshart);
        }
    }

    public static class ExpenseBarChart extends RecyclerView.ViewHolder {

        BarChart mBarChart;

        public ExpenseBarChart(@NonNull View itemView) {
            super(itemView);
            this.mBarChart = itemView.findViewById(R.id.barchart_overview);
        }
    }

    public class AccountTypeViewHolder extends RecyclerView.ViewHolder {

        private TextView txtType;
        private RecyclerView accountRecyclerView;


        public AccountTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtType = itemView.findViewById(R.id.textView5);
            this.accountRecyclerView = itemView.findViewById(R.id.accoun_rec_overview);


        }
    }

    public static class TransTypeViewHolder extends RecyclerView.ViewHolder {

        private TextView txtType;
        private RecyclerView recyclerView;

        public TransTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtType = itemView.findViewById(R.id.textView5);
            this.recyclerView = itemView.findViewById(R.id.planet_rec);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TestModel.CARD0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_seven_day_card_overview, parent, false);
                return new ExpenseBarChart(view);
            case TestModel.CARD1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_card_overview, parent, false);
                return new SummaryTypeViewHolder(view);
            case TestModel.CARD2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_card_overview, parent, false);
                return new AccountTypeViewHolder(view);
            case TestModel.CARD3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_card_overview, parent, false);
                return new TransTypeViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).getType()) {
            case 0:
                return TestModel.CARD0;
            case 1:
                return TestModel.CARD1;
            case 2:
                return TestModel.CARD2;
            case 3:
                return TestModel.CARD3;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        TestModel object = dataSet.get(listPosition);
        if (object != null) {
            switch (object.getType()) {
                case TestModel.CARD0:
                    barchar((((ExpenseBarChart) holder).mBarChart));
                    break;
                case TestModel.CARD1:
                    AppMathCalc math = new AppMathCalc();
                    float expense = (float) math.countTransactionsExpense();
                    float income = (float) math.countTransactionsIncome();
                    float total = income + expense;
                    ((SummaryTypeViewHolder) holder).currentMonth.setText(currentMonth());
                    ((SummaryTypeViewHolder) holder).income.setText(String.format(Locale.getDefault(),"%,.2f kr", income));
                    ((SummaryTypeViewHolder) holder).expense.setText(String.format(Locale.getDefault(),"%,.2f kr", expense));
                    ((SummaryTypeViewHolder) holder).total.setText(String.format(Locale.getDefault(),"%,.2f kr", total));
                    if (total >= 0) {
                        ((SummaryTypeViewHolder) holder).total.setTextColor(mContext.getColor(R.color.colorIncome));
                    }
                    expense = Math.abs(expense);
                    pieChart(((SummaryTypeViewHolder) holder).mPieChart, expense, income);
                    break;
                case TestModel.CARD2:
                    AppConstants.fillAccountsOverview(mContext);
                    ((AccountTypeViewHolder) holder).accountRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                    ((AccountTypeViewHolder) holder).accountRecyclerView.setAdapter(AppConstants.accountOverviewAdapter);
                    break;
                case TestModel.CARD3:
                    AppConstants.fillTransactions(mContext);
                    ((TransTypeViewHolder) holder).recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                    ((TransTypeViewHolder) holder).recyclerView.setAdapter(AppConstants.transactionAdapter);
                    break;
            }
        }
    }

    private String currentMonth() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM yyyy", Locale.US);
        return dateFormatter.format(c.getTime());
    }

    private void barchar(BarChart mBarChart) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM", Locale.US);
        AppMathCalc mathCalc = new AppMathCalc();
        String currentDay = dateFormatter.format(c.getTime());
        int currentDayValue = Math.abs(mathCalc.countTransactionExpense(c.getTime()));

        c.add(Calendar.DAY_OF_YEAR,-1);
        String currentDay1 = dateFormatter.format(c.getTime());
        int dayValue1 = Math.abs(mathCalc.countTransactionExpense(c.getTime()));
        c.add(Calendar.DAY_OF_YEAR,-1);
        String currentDay2 = dateFormatter.format(c.getTime());
        int dayValue2 = Math.abs(mathCalc.countTransactionExpense(c.getTime()));
        c.add(Calendar.DAY_OF_YEAR,-1);
        String currentDay3 = dateFormatter.format(c.getTime());
        int dayValue3 = Math.abs(mathCalc.countTransactionExpense(c.getTime()));
        c.add(Calendar.DAY_OF_YEAR,-1);
        String currentDay4 = dateFormatter.format(c.getTime());
        int dayValue4 = Math.abs(mathCalc.countTransactionExpense(c.getTime()));
        c.add(Calendar.DAY_OF_YEAR,-1);
        String currentDay5 = dateFormatter.format(c.getTime());
        int dayValue5 = Math.abs(mathCalc.countTransactionExpense(c.getTime()));
        c.add(Calendar.DAY_OF_YEAR,-1);
        String currentDay6 = dateFormatter.format(c.getTime());
        int dayValue6 = Math.abs(mathCalc.countTransactionExpense(c.getTime()));

        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, dayValue6));
        barEntries.add(new BarEntry(1, dayValue5));
        barEntries.add(new BarEntry(2, dayValue4));
        barEntries.add(new BarEntry(3, dayValue3));
        barEntries.add(new BarEntry(4, dayValue2));
        barEntries.add(new BarEntry(5, dayValue1));
        barEntries.add(new BarEntry(6, currentDayValue)); // Current value

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add(currentDay6);
        xAxisLabel.add(currentDay5);
        xAxisLabel.add(currentDay4);
        xAxisLabel.add(currentDay3);
        xAxisLabel.add(currentDay2);
        xAxisLabel.add(currentDay1);
        xAxisLabel.add(currentDay); // Current Value

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xAxisLabel.get((int) value);
            }
        };

        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        int[] color = {R.color.colorExpense};
        barDataSet.setColors(color, mContext);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        barData.setDrawValues(false);

        // mBarChart.setVisibility(View.VISIBLE);
        mBarChart.animateY(1000);
        mBarChart.setData(barData);
        // mBarChart.setFitBars(true);
        mBarChart.getLegend().setEnabled(false);
        mBarChart.getDescription().setEnabled(false);
        mBarChart.invalidate();
    }

    private void pieChart(PieChart mPieChart, float expense, float income) {
        List<PieEntry> pieEntries = new ArrayList<>();

        // first entry data value, second is data descriptor
        pieEntries.add(new PieEntry(income, ""));
        pieEntries.add(new PieEntry(expense, ""));

        // mPieChart.setVisibility(View.VISIBLE);
        mPieChart.animateXY(1000, 1000);
        mPieChart.setHoleRadius(1);
        mPieChart.setTransparentCircleRadius(1);
        //mPieChart.setUsePercentValues(true);
        mPieChart.setDrawEntryLabels(false);

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setSliceSpace(1);
        int[] color = {R.color.colorIncome, R.color.colorExpense};
        pieDataSet.setColors(color, mContext);
        pieDataSet.setDrawValues(false);
        PieData pieData = new PieData(pieDataSet);
        mPieChart.setData(pieData);
        mPieChart.getLegend().setEnabled(false);
        mPieChart.getDescription().setEnabled(false);
        //Description description = new Description();
        //description.setText("Testing description");
        //mPieChart.setDescription(description);
        mPieChart.invalidate();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
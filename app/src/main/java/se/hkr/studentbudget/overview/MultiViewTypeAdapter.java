package se.hkr.studentbudget.overview;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import se.hkr.studentbudget.AppConstants;
import se.hkr.studentbudget.AppMathCalc;
import se.hkr.studentbudget.R;
import se.hkr.studentbudget.transactions.TransactionAdapter;
import se.hkr.studentbudget.transactions.Transactions;


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

    public class AccountItemTestCardHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, value, notes;


        public AccountItemTestCardHolder(@NonNull View itemView) {
            super(itemView);

            this.image = itemView.findViewById(R.id.account_row_img);
            this.title = itemView.findViewById(R.id.account_row_title);
            this.value = itemView.findViewById(R.id.account_value_text);
            this.notes = itemView.findViewById(R.id.account_notes);

        }
    }

    public static class TransTypeViewHolder extends RecyclerView.ViewHolder {

        private TextView txtType;
        private RecyclerView recyclerView;

        public TransTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtType = itemView.findViewById(R.id.textView5);
            this.recyclerView =itemView.findViewById(R.id.planet_rec);

        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case TestModel.CARD1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_card_row, parent, false);
                return new SummaryTypeViewHolder(view);
            case TestModel.CARD2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_card_row, parent, false);
                return new AccountItemTestCardHolder(view);
            case TestModel.CARD3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_card_row, parent, false);
                return new TransTypeViewHolder(view);
        }
        return null;


    }


    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).getType()) {
            case 0:
                return TestModel.CARD1;
            case 1:
                return TestModel.CARD2;
            case 2:
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
                case TestModel.CARD1:
                    AppMathCalc math = new AppMathCalc();
                    ((SummaryTypeViewHolder) holder).total.setText(String.format("%,.2f kr",math.countTransactions()));
                    pieChart(((SummaryTypeViewHolder) holder).mPieChart);
                    break;
                case TestModel.CARD2:
                    ((AccountItemTestCardHolder) holder).title.setText("Wallet");
                   ((AccountItemTestCardHolder) holder).value.setText(String.format("%,.2f", 500.14));
                    ((AccountItemTestCardHolder) holder).notes.setText("THis is a long note");
                    ((AccountItemTestCardHolder) holder).image.setImageResource(R.drawable.ic_placeholder); // used images resource. will use images
                    break;
                case TestModel.CARD3:

                    AppConstants.fillTransactions(mContext);
                    ((TransTypeViewHolder) holder).recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                    ((TransTypeViewHolder) holder).recyclerView.setAdapter(AppConstants.transactionAdapter);
                   // createListData(adapter, tractions);
                    break;
            }
        }
    }


    private void pieChart(PieChart mPieChart){
        List<PieEntry> pieEntries = new ArrayList<>();

        // first entry data value, second is data descriptor
        pieEntries.add(new PieEntry(200.14f,""));
        pieEntries.add(new PieEntry(4600,""));

       // mPieChart.setVisibility(View.VISIBLE);
        mPieChart.animateXY(1000,1000);
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

    //TODO remove this
    private void createListData(TransactionAdapter adapter, ArrayList<Transactions> planetArrayList) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;

        try {
            date = format.parse("2012-08-22");

        } catch (ParseException e) {

        }
        Transactions trans = new  Transactions("Coca cola",-2546.45,"Food","expense", "wallet",date,R.drawable.ic_placeholder);
        planetArrayList.add(trans);

        trans = new  Transactions("CSN",10436,"CSN","income", "wallet",date,R.drawable.ic_placeholder);
        planetArrayList.add(trans);

        adapter.notifyDataSetChanged();
    }
}
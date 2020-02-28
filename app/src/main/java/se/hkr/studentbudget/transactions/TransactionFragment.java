package se.hkr.studentbudget.transactions;

import android.app.DatePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import se.hkr.studentbudget.AppConstants;
import se.hkr.studentbudget.AppMathCalc;
import se.hkr.studentbudget.R;
import se.hkr.studentbudget.database.DataBaseAccess;

public class TransactionFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private Spinner categorySpinner, accountSpinner;
    private Button saveBtn, cancelBtn, dateCalender;
    private NavController navController;
    private TextView spendTextInput, spendValueInput;
    private String clickedCategoryName = "";
    private String clickedAccountName = "";
    private String selectedDate;
    private int clickedCategoryImage;
    private String TRANSACTION_TYPE = "";
    private int datePickerStyle;
    private int choice;
    private int accountChoiceIndex;
    AppMathCalc calc = new AppMathCalc();

    private String tag = "Info";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);
        Log.d(tag, "In the TransactionFragment");
        // init view elements
        initFragmentView(view);


        categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryRowItem clickedItem = (CategoryRowItem) parent.getItemAtPosition(position);
                clickedCategoryName = clickedItem.getmCategoryName();
                clickedCategoryImage = clickedItem.getmCategoryIcon();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        accountSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryRowItem clickedItem = (CategoryRowItem) parent.getItemAtPosition(position);
                clickedAccountName = clickedItem.getmCategoryName();
                accountChoiceIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Save all transactions
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = spendValueInput.getText().toString().trim();
                String text = spendTextInput.getText().toString().trim();

                if (value.isEmpty()) {
                    AppConstants.toastMessage(getContext(), "Chose a value");
                } else {
                    if ((AppConstants.accounts.get(accountChoiceIndex).getAccountValue() - Double.parseDouble(value)) <=0 && TRANSACTION_TYPE.equals("expense")){
                        AppConstants.toastMessage(getContext(), "No money in this account!");
                    }else {
                        addNewTransaction(text, value, clickedCategoryName, TRANSACTION_TYPE, clickedAccountName, selectedDate, clickedCategoryImage);
                        navController.navigate(R.id.nav_overView);
                    }
                }
            }
        });

        dateCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Select theme for datepicker Popup
                DialogFragment datePickerFragment = new DatePickerFragment(datePickerStyle); // creating DialogFragment which creates DatePickerDialog
                datePickerFragment.setTargetFragment(TransactionFragment.this, 0);  // Passing this fragment DatePickerFragment.
                // display fragment
                datePickerFragment.show(Objects.requireNonNull(getFragmentManager()), "datePicker");
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_overView);

            }
        });

        return view;
    }

    //Main method for adding new transactions
    private void addNewTransaction(String text, String value, String clickedCategoryName, String TRANSACTION_TYPE, String clickedAccountName, String selectedDate, int clickedCategoryImage) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = null;
        double amountValue = 0;
        try {
            date = format.parse(selectedDate);
            if (choice == 1) {
                amountValue = -Double.parseDouble(value);
            } else {
                amountValue = Double.parseDouble(value);
            }
        } catch (ParseException e) {
            Log.e(tag, e.getMessage() + "date/amount conversation fail!");
        }

        insertInDatabase(text, amountValue, clickedCategoryName, TRANSACTION_TYPE, clickedAccountName, selectedDate, clickedCategoryImage);
        Transactions m = new Transactions(text, amountValue, clickedCategoryName, TRANSACTION_TYPE, clickedAccountName, date, clickedCategoryImage);
        AppConstants.transactions.add(m);

        calc.updateAccountAmount(accountChoiceIndex, amountValue, clickedAccountName, getContext());

    }

    private void initFragmentView(View view) {
        //Get navvontroler
        AppConstants.fillAccountSpinner(getContext());
        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);
        categorySpinner = view.findViewById(R.id.category_spinner);
        accountSpinner = view.findViewById(R.id.transactionsSpinner);
        saveBtn = view.findViewById(R.id.btn_save);
        cancelBtn = view.findViewById(R.id.btn_cancel);
        dateCalender = view.findViewById(R.id.btn_date);
        spendTextInput = view.findViewById(R.id.spendingTextInput);
        spendValueInput = view.findViewById(R.id.spendingValueInput);
        accountSpinner.setAdapter(AppConstants.accountAdapter);
        currentDate();
        choice = getArguments().getInt("choice");
        switch (choice) {
            case 1:
                saveBtn.setBackgroundColor(getResources().getColor(R.color.colorExpense));
                cancelBtn.setBackgroundColor(getResources().getColor(R.color.colorExpense));
                datePickerStyle = R.style.DatePickerExpenseTheme;

                //Change the color and text of titlebar
                ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(
                        new ColorDrawable(getResources().getColor(R.color.colorExpense)));
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("New Expense");
                TRANSACTION_TYPE = "expense";
                categorySpinner.setAdapter(AppConstants.expenseAdapter);
                break;
            case 2:
                saveBtn.setBackgroundColor(getResources().getColor(R.color.colorIncome));
                cancelBtn.setBackgroundColor(getResources().getColor(R.color.colorIncome));
                datePickerStyle = R.style.DatePickerIncomeTheme;
                //Change the color and text of titlebar
                ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(
                        new ColorDrawable(getResources().getColor(R.color.colorIncome)));
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("New Income");
                TRANSACTION_TYPE = "income";
                categorySpinner.setAdapter(AppConstants.incomeAdapter);
                break;
            default:
                break;
        }
    }

    private void currentDate() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        selectedDate = setDateFormat(year, month, day, "yyyy-MM-dd HH:mm:ss");
        String strDate = setDateFormat(year, month, day, "yyyy-MM-dd");
        dateCalender.setText(strDate);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        selectedDate = setDateFormat(year, month, day, "yyyy-MM-dd HH:mm:ss");
        String strDate = setDateFormat(year, month, day, "yyyy-MM-dd");
        dateCalender.setText(strDate);
    }

    private String setDateFormat(int year, int month, int day, String s) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat dateFormatter = new SimpleDateFormat(s, Locale.getDefault());
        return dateFormatter.format(calendar.getTime());
    }

    private void resetToolbarColor() {
        //Change the color of titlebar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
    }

    @Override
    public void onDestroyView() {
        resetToolbarColor();
        super.onDestroyView();
    }

    private void insertInDatabase(final String text, final double amountValue, final String clickedCategoryName, final String TRANSACTION_TYPE, final String clickedAccountName, final String selectedDate, final int clickedCategoryImage) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                DataBaseAccess dataBaseAcess = DataBaseAccess.getInstance(getContext());
                dataBaseAcess.openDatabase();
                boolean insertData = dataBaseAcess.insertTransactionInDatabase(text, amountValue, clickedCategoryName, TRANSACTION_TYPE, clickedAccountName, selectedDate, clickedCategoryImage);
                if (insertData) {
                    Log.i(tag, "Transaction saved in database!");
                } else {
                    Log.e(tag, "Something Went Wrong!");
                }
                dataBaseAcess.closeDatabe();
            }
        });
        th.start();
    }

}
package se.hkr.studentbudget.transactions;

import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import se.hkr.studentbudget.AppConstants;
import se.hkr.studentbudget.AppMathCalc;
import se.hkr.studentbudget.R;
import se.hkr.studentbudget.StaticStrings;
import se.hkr.studentbudget.database.DataBaseAccess;

public class TransactionFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private Spinner categorySpinner, accountSpinner;
    private Button saveBtn, cancelBtn, dateCalender;
    private NavController navController;
    private EditText spendTextInput, spendValueInput;
    private TextView acountTotal;
    private String clickedCategoryName = "";
    private String clickedAccountName = "";
    private String selectedDate;
    private int clickedCategoryImage;
    private String TRANSACTION_TYPE = "";
    private int datePickerStyle;
    private int choice;
    private int accountChoiceIndex;

    private String tag = "Info";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);
        Log.d(tag, "TransactionFragment: In the OnCreateView Event()");
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
                countAnimationSaldo();
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
                    AppConstants.toastMessage(getContext(), "Chose a value!");
                } else {
                    if ((AppConstants.accounts.get(accountChoiceIndex).getAccountValue() - Double.parseDouble(value)) < 0 && TRANSACTION_TYPE.equals(StaticStrings.EXPENSE)) {
                        AppConstants.toastMessage(getContext(), "No money in this account!");
                    } else {
                        addNewTransaction(text, value, clickedCategoryName, TRANSACTION_TYPE, clickedAccountName, selectedDate, clickedCategoryImage);
                        navController.navigate(R.id.nav_overView);
                    }
                }
                AppConstants.hideSoftKeyboard(getActivity());
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
                AppConstants.hideSoftKeyboard(getActivity());

            }
        });

        return view;
    }

    //Main method for adding new transactions
    private void addNewTransaction(String text, String value, String clickedCategoryName, String TRANSACTION_TYPE, String clickedAccountName, String selectedDate, int clickedCategoryImage) {
        Log.i(tag, "Saving and adding new transaction!");
        double amountValue = 0;
        if (choice == 1) {
            amountValue = -Double.parseDouble(value);
        } else {
            amountValue = Double.parseDouble(value);
        }
        double updateAccountValue = updateAccountAmount(accountChoiceIndex, amountValue);
        insertInDatabase(text, amountValue, clickedCategoryName, TRANSACTION_TYPE, clickedAccountName, selectedDate, clickedCategoryImage, updateAccountValue);
    }

    private void insertInDatabase(final String text, final double amountValue, final String clickedCategoryName, final String TRANSACTION_TYPE, final String clickedAccountName, final String selectedDate, final int clickedCategoryImage,final double v) {
                Log.i(tag, "Transaction save Thread starting!");
                DataBaseAccess dataBaseAcess = new DataBaseAccess(getContext());
                dataBaseAcess.openDatabase();
                boolean insertTransactionData = dataBaseAcess.insertTransactionInDatabase(text, amountValue, clickedCategoryName, TRANSACTION_TYPE, clickedAccountName, selectedDate, clickedCategoryImage);
                if (insertTransactionData) {
                    Log.i(tag, "Transaction saved in database!");
                } else {
                    Log.e(tag, "Something went Wrong!");
                }
                boolean updateAccountData = dataBaseAcess.updateAccountInDatabase(v, clickedAccountName);
                if (updateAccountData) {
                    Log.i(tag, "Account update saved in database!");
                } else {
                    Log.e(tag, "Something went Wrong!");
                }
                dataBaseAcess.closeDatabe();
                Log.i(tag, "Transaction save Thread Ended!");
    }

    private double updateAccountAmount(int accountChoiceIndex, double amountValue) {
        double newValue = AppConstants.accounts.get(accountChoiceIndex).getAccountValue() + amountValue;
        AppConstants.accounts.get(accountChoiceIndex).setAccountValue(newValue);
        return newValue;
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
        acountTotal = view.findViewById(R.id.text_currentValue);
        spendTextInput = view.findViewById(R.id.spendingTextInput);
        spendValueInput = view.findViewById(R.id.spendingValueInput);
        accountSpinner.setAdapter(AppConstants.accountAdapter);
        currentDate();
        choice = getArguments().getInt("choice");
        switch (choice) {
            case 1:
                //New expense style
                saveBtn.setBackgroundColor(getResources().getColor(R.color.colorExpense));
                cancelBtn.setBackgroundColor(getResources().getColor(R.color.colorExpense));
                datePickerStyle = R.style.DatePickerExpenseTheme;

                //Change the color and text of titlebar
                ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(
                        new ColorDrawable(getResources().getColor(R.color.colorExpense)));
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.menu_newexpense);
                TRANSACTION_TYPE = StaticStrings.EXPENSE;
                categorySpinner.setAdapter(AppConstants.expenseAdapter);
                break;
            case 2:
                //New income style
                saveBtn.setBackgroundColor(getResources().getColor(R.color.colorIncome));
                cancelBtn.setBackgroundColor(getResources().getColor(R.color.colorIncome));
                datePickerStyle = R.style.DatePickerIncomeTheme;
                //Change the color and text of titlebar
                ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(
                        new ColorDrawable(getResources().getColor(R.color.colorIncome)));
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.menu_newincome);
                TRANSACTION_TYPE = StaticStrings.INCOME;
                categorySpinner.setAdapter(AppConstants.incomeAdapter);
                break;
            default:
                break;
        }
    }

    private void countAnimationSaldo() {
        AppMathCalc calc = new AppMathCalc();
        ValueAnimator animator = ValueAnimator.ofFloat(0, (float) AppConstants.accounts.get(accountChoiceIndex).getAccountValue()); //0 is min number, 600 is max number
        animator.setDuration(1000); //Duration is in milliseconds
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float amount = (float) animation.getAnimatedValue();
                acountTotal.setText(String.format("%s kr", String.format(Locale.getDefault(),"%,.2f", amount)));
            }
        });
        animator.start();
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
        Log.d(tag, "TransactionFragment: In the onDestroyView() event");
    }

    // 1
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(tag, "TransactionFragment: In the onAttach() event");
    }
    //2
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(tag, "TransactionFragment: In the OnCreate event()");
    }
    //4
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(tag, "TransactionFragment: In the onActivityCreated() event");
    }
    //5
    @Override
    public void onStart() {
        super.onStart();
        Log.d(tag, "TransactionFragment: In the onStart() event");
    }
    //6
    @Override
    public void onResume() {
        super.onResume();
        Log.d(tag, "TransactionFragment: In the onResume() event");
    }
    //7
    @Override
    public void onPause() {
        super.onPause();
        Log.d(tag, "TransactionFragment: In the onPause() event");
    }
    //8
    @Override
    public void onStop() {
        super.onStop();
        Log.d(tag, "TransactionFragment: In the onStop() event");
    }

    //10
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(tag, "TransactionFragment: In the onDestroy() event");
    }
    //11
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(tag, "TransactionFragment: In the onDetach() event");
    }


}
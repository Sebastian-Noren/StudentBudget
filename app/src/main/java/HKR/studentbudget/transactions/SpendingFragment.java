package HKR.studentbudget.transactions;

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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import HKR.studentbudget.AppConstants;
import HKR.studentbudget.R;
import HKR.studentbudget.database.DataBaseAccess;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class SpendingFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private Spinner spinner, accountSpinner;
    private Button saveBtn, cancelBtn, dateCalender;
    private NavController navController;
    private TextView spendTextInput, spendValueInput;
    private String clickedCategoryName = "";
    private String selectedDate;

    private String tag = "Info";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spending, container, false);
        Log.d(tag, "In the SpendingFragment");
        // init view elements
        initFragmentView(view);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryRowItem clickedItem = (CategoryRowItem) parent.getItemAtPosition(position);
                clickedCategoryName = clickedItem.getmCategoryName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        accountSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = spendValueInput.getText().toString().trim();
                String text = spendTextInput.getText().toString().trim();

                if (value.isEmpty()) {
                    Toast.makeText(getContext(), "Chose a value", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(getContext(), value + " " + text + " " + clickedCategoryName + " " + selectedDate, Toast.LENGTH_SHORT).show();
                    navController.navigate(R.id.nav_overView);
                }
            }
        });

        dateCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Select theme for datepicker Popup
                int datePickerStyle = R.style.DatePickerSpendingTheme;
                DialogFragment datePickerFragment = new DatePickerFragment(datePickerStyle); // creating DialogFragment which creates DatePickerDialog
                datePickerFragment.setTargetFragment(SpendingFragment.this, 0);  // Passing this fragment DatePickerFragment.
                // display fragment
                datePickerFragment.show(Objects.requireNonNull(getFragmentManager()), "datePicker");
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navController.navigate(R.id.nav_overView);
                DataBaseAccess dataBaseAcess = DataBaseAccess.getInstance(getContext());
                dataBaseAcess.openDatabase();
                String n = dataBaseAcess.getItem();
                dataBaseAcess.closeDatabe();
                Toast.makeText(getContext(), n, Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

    private void initFragmentView(View view) {
        //Change the color of titlebar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorSpending)));
        //Get navvontroler
        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);
        spinner = view.findViewById(R.id.spinner);
        accountSpinner = view.findViewById(R.id.transactionsSpinner);
        saveBtn = view.findViewById(R.id.btn_save);
        cancelBtn = view.findViewById(R.id.btn_cancel);
        dateCalender = view.findViewById(R.id.btn_date);
        spendTextInput = view.findViewById(R.id.spendingTextInput);
        spendValueInput = view.findViewById(R.id.spendingValueInput);
        currentDate();
        accountSpinner.setAdapter(AppConstants.accountAdapter);
        spinner.setAdapter(AppConstants.expenseAdapter);
    }

    private void currentDate() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        selectedDate = setDateFormat(year, month, day);
        dateCalender.setText(selectedDate);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        selectedDate = setDateFormat(year, month, day);
        dateCalender.setText(selectedDate);
    }

    private String setDateFormat(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
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
}
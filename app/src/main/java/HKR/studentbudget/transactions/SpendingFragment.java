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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import HKR.studentbudget.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class SpendingFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private ArrayList<SpendingRowItem> spendingList;
    private SpendingAdapter mAdapter;
    private Spinner spinner;
    private Button saveBtn, cancelBtn, dateCalender;
    private NavController navController;
    private TextView spendTextInput, spendValueInput;
    private String clickedCategoryName = "", date;

    private String tag = "Info";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spending, container, false);
        Log.d(tag, "In the SpendFragment");
        // init view elements
        initFragmentView(view);

        mAdapter = new SpendingAdapter(getContext(), spendingList);
        spinner.setAdapter(mAdapter);
        spinner.setSelected(false);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpendingRowItem clickedItem = (SpendingRowItem) parent.getItemAtPosition(position);
                clickedCategoryName = clickedItem.getmCategoryName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = spendValueInput.getText().toString().trim();
                String text = spendTextInput.getText().toString();

                if (value.isEmpty() || text.isEmpty()){
                    Toast.makeText(getContext(), "Chose a value", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), value + " "+ text + " " + clickedCategoryName +" " + date, Toast.LENGTH_SHORT).show();
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
                navController.navigate(R.id.nav_overView);
                Toast.makeText(getContext(), " Selected", Toast.LENGTH_SHORT).show();

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
        saveBtn = view.findViewById(R.id.save_btn);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        dateCalender = view.findViewById(R.id.date_btn);
        spendTextInput = view.findViewById(R.id.spendingTextInput);
        spendValueInput = view.findViewById(R.id.spendingValueInput);
        currentDate();
        //fill the category list
        initList();
    }

    private void initList() {
        spendingList = new ArrayList<>();
        spendingList.add(new SpendingRowItem("House", R.drawable.ic_placeholder));
        spendingList.add(new SpendingRowItem("Loan", R.drawable.ic_placeholder));
        spendingList.add(new SpendingRowItem("Food", R.drawable.ic_placeholder));
    }

    private void currentDate(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        date = String.format(Locale.getDefault(), "%d/%d/%d", day, month, year);
        dateCalender.setText(date);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = String.format(Locale.getDefault(), "%d/%d/%d", dayOfMonth, month, year);
        dateCalender.setText(date);
    }

    private void resetToolbarColor(){
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
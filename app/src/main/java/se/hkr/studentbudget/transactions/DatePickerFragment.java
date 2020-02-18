package se.hkr.studentbudget.transactions;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import java.util.Calendar;
import java.util.Objects;

import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {

    private int datePickerStyle;
    public DatePickerFragment(int datePickerStyle) {
        this.datePickerStyle = datePickerStyle;
    }

    private DatePickerDialog datePickerDialog;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // listener object to get calling fragment listener
        DatePickerDialog.OnDateSetListener dateSetListener = (DatePickerDialog.OnDateSetListener) getTargetFragment(); // getting passed fragment
        datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()),datePickerStyle, dateSetListener, year, month, day); // DatePickerDialog gets callBack listener as 2nd parameter
        // Create a new instance of DatePickerDialog and return it
        return datePickerDialog;
    }


}

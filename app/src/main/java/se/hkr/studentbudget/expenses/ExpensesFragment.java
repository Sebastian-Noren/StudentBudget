package se.hkr.studentbudget.expenses;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.hkr.studentbudget.R;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class ExpensesFragment extends Fragment {
    private String tag = "Info";
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        Log.d(tag, "In the ExpensesFragment");
        // use view to get things from the windows
        TextView textView = view.findViewById(R.id.textTest);
        textView.setText("Expenses");

        return view;
    }
}
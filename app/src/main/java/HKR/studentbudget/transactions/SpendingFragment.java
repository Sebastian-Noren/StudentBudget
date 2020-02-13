package HKR.studentbudget.transactions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import java.util.ArrayList;

import HKR.studentbudget.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/*
This is made for testing shit out
 */

public class SpendingFragment extends Fragment {
    private ArrayList<SpendingRowItem> spendingList;
    private SpendingAdapter mAdapter;
    private Spinner spinner;

    private String tag = "Info";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spending, container, false);
        Log.d(tag, "In the SpendFragment");

        initList();
        spinner = view.findViewById(R.id.spinner);
        mAdapter = new SpendingAdapter(getContext(), spendingList);
        spinner.setAdapter(mAdapter);
        spinner.setSelected(false);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            SpendingRowItem clickedItem = (SpendingRowItem) parent.getItemAtPosition(position);
            String clickedCountryName = clickedItem.getmCategoryName();
                Toast.makeText(getActivity(),clickedCountryName+" Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void initList() {
        spendingList = new ArrayList<>();
        spendingList.add(new SpendingRowItem("House", R.drawable.ic_placeholder));
        spendingList.add(new SpendingRowItem("Loan", R.drawable.ic_placeholder));
        spendingList.add(new SpendingRowItem("Food", R.drawable.ic_placeholder));
    }
}
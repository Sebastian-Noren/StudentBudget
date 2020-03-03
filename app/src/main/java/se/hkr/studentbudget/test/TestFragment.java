package se.hkr.studentbudget.test;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.hkr.studentbudget.AppConstants;
import se.hkr.studentbudget.R;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static se.hkr.studentbudget.AppConstants.transactions;

/*
This is made for testing shit out
 */
public class TestFragment extends Fragment {

    private String tag = "Info";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        Log.d(tag, "In the TestFragment");

        for (int i = 0; i < transactions.size(); i++) {
            Log.e(tag, transactions.get(i).toString());
        }

        return view;
    }


}
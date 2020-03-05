package se.hkr.studentbudget.test;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.hkr.studentbudget.AppConstants;
import se.hkr.studentbudget.R;
import se.hkr.studentbudget.database.DataBaseAccess;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


/*
This is made for testing shit out
 */
public class TestFragment extends Fragment {

    private String tag = "Info";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        Log.d(tag, "In the TestFragment");




        return view;
    }


}
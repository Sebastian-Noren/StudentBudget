package se.hkr.studentbudget.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import se.hkr.studentbudget.R;

/*
This is made for testing shit out
 */

public class StartFragment extends Fragment {
    private String tag = "Info";
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        Log.d(tag, "In the StartFragment");

        TextView textView = view.findViewById(R.id.textStart);

        textView.setText("Yolo Jenkins");


        return view;
    }
}
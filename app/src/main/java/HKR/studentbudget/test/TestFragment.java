package HKR.studentbudget.test;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import HKR.studentbudget.R;
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
        // use view to get things from the windows
        TextView textView = view.findViewById(R.id.textTest);
        textView.setText("This is text changed by code");

        return view;
    }
}
package HKR.studentbudget.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import HKR.studentbudget.R;

/*
This is made for testing shit out
 */

public class LoginFragment extends Fragment {
    private String tag = "Info";
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Log.d(tag, "In the LoginFragment");

        TextView textView = view.findViewById(R.id.textLogin);

        textView.setText("Enter a 4-digit PIN");



        return view;
    }
}
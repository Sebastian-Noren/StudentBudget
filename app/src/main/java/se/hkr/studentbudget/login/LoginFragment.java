package se.hkr.studentbudget.login;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import se.hkr.studentbudget.LoginActivity;
import se.hkr.studentbudget.R;

/*
This is made for testing shit out
 */

public class LoginFragment extends Fragment {
    private String tag = "Info";
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Log.d(tag, "In the LoginFragment");

        TextView textView = view.findViewById(R.id.textLogin);

        textView.setText("Enter PIN");

        EditText editText = view.findViewById(R.id.textPin);

        return view;
    }

}
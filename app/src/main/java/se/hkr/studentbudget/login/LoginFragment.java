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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.security.SecureRandom;

import se.hkr.studentbudget.AppConstants;
import se.hkr.studentbudget.LoginActivity;
import se.hkr.studentbudget.R;
import se.hkr.studentbudget.database.DataBaseAccess;

/*
This is made for testing shit out
 */

public class LoginFragment extends Fragment {

    boolean showEmail;
    String helpText;

    public LoginFragment(boolean showEmail, String helpText) {

        this.showEmail = showEmail;
        this.helpText = helpText;

    }

    private String tag = "Info";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Log.d(tag, "In the LoginFragment");

        final TextView textView = view.findViewById(R.id.textLogin);
        TextView textForgot = view.findViewById(R.id.textForgot);

        textForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPin = randomizeNewPIN();
                Log.i(tag, newPin);
                String hashedPin = "";
                try {
                    hashedPin = LoginActivity.hashPin(newPin);
                    Log.i(tag, hashedPin);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DataBaseAccess dba = DataBaseAccess.getInstance(getContext());
                dba.openDatabase();
                final String email = dba.getEmail();
                final String subject = "New Pin";
                final String msg = "Your new PIN is " + newPin;

                textView.setText("A new PIN has been sent to your Email.\nYou can change it in Settings.");
                AppConstants.toastMessage(getContext(),"Success!");
                try {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JavaMailUtil.send(email, subject, msg);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dba.updatePIN(hashedPin);
                dba.closeDatabe();
            }
        });

        textView.setText(helpText);
        EditText textEmail = view.findViewById(R.id.textEmail);

        if (showEmail) {
            textEmail.setVisibility(View.VISIBLE);
            textForgot.setVisibility(View.INVISIBLE);
        } else {
            textEmail.setVisibility(View.INVISIBLE);
            textForgot.setVisibility(View.VISIBLE);
        }


        return view;
    }

    public String randomizeNewPIN() {
        SecureRandom random = new SecureRandom();
        int newInt = random.nextInt(10000);
        String newPin = String.valueOf(newInt);

        if (newPin.length() == 1) {
            return "000" + newPin;
        } else if (newPin.length() == 2) {
            return "00" + newPin;
        } else if (newPin.length() == 3) {
            return "0" + newPin;
        } else {
            return newPin;
        }
    }


}
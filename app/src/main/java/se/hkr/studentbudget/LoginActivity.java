package se.hkr.studentbudget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;

import se.hkr.studentbudget.database.DataBaseAccess;
import se.hkr.studentbudget.login.Hash;
import se.hkr.studentbudget.login.LoginFragment;
import se.hkr.studentbudget.login.StartFragment;
import se.hkr.studentbudget.notifications.ReminderBroadcast;

public class LoginActivity extends AppCompatActivity {

    int currentFragment;
    int loginAttempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginAttempts =3;

        //currentFragments explained:
        //New User
        //1. Click to continue
        //2. Enter new Email and PIN
        //3. Log in with Pin
        //Existing users start on 3.

        DataBaseAccess dba = DataBaseAccess.getInstance(getApplicationContext());
        dba.openDatabase();
        dba.readUsers();
        dba.closeDatabe();

        Button buttonContinue = findViewById(R.id.buttonContinue);

        //Select which fragment that should be displayed first
        if (newUser()) {
            //Display Start Fragment
            StartFragment startFragment = new StartFragment();
            startFragment.hideStartText(findViewById(R.id.textStart));
            changeFragment(startFragment);
            setCurrentFragment(1);
        } else {
            //Display Login Fragment to login
            LoginFragment loginFragment = new LoginFragment(false, "Enter PIN");
            changeFragment(loginFragment);
            setCurrentFragment(3);
        }

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //New user, check so format is ok
                if (getCurrentFragment() == 1) {

                    //Display Login Fragment to create Pin
                    LoginFragment loginFragment = new LoginFragment(true, "Enter email and a 4-digit PIN to continue");
                    changeFragment(loginFragment);

                    setCurrentFragment(2);

                } else if (getCurrentFragment() == 2) {
                    //New user continued
                    if (checkInputFormat()) {
                        //Input entered is according to format
                        //input Pin
                        EditText textPin = findViewById(R.id.textPin);
                        String pin = textPin.getText().toString();
                        //input Email
                        EditText textEmail = findViewById(R.id.textEmail);
                        String email = textEmail.getText().toString();

                        //Hash and save to database
                        try {
                            saveUserToDatabase(email, hashPin(pin));

                        } catch (Exception e) {

                        }
                        //Display success msg.
                        view.clearFocus();
                        ReminderBroadcast.createNotificationChannel(LoginActivity.this,"daily", "channelSB");
                        ReminderBroadcast.setupRepeatingNotifications(LoginActivity.this,System.currentTimeMillis()+1000*10,1000*60*60*24);
                        changeActivity();
                        Toast.makeText(getApplicationContext(), "Success, welcome!", Toast.LENGTH_SHORT).show();

                    } else {
                        //Displays what input is wrong format and do nothing
                    }
                } else if (getCurrentFragment() == 3) {
                    //Existing user, Check so pin is correct
                    EditText textPin = findViewById(R.id.textPin);
                    String pin = textPin.getText().toString();


                    //Insert checkPin when it is complete
                    boolean isValid = false;
                    try {
                        isValid = checkPin(pin);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (isValid && loginAttempts>0) {
                        //Go to Main Activity
                        view.clearFocus();
                        changeActivity();
                    } else {
                        //Else stay on Login
                        //(Lock login for few minutes on many attempts)
                        TextView textView = findViewById(R.id.textLogin);
                        loginAttempts--;
                        if (loginAttempts <= 0) {
                            textView.setText("Locked out,\nRestart to try again.");
                        } else

                            textView.setText("PIN is invalid\n" + loginAttempts + " attempts left.");

                    }
                }
            }
        });
    }





    public int getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(int currentFragment) {
        this.currentFragment = currentFragment;
    }

    private void changeFragment(Fragment fragment) {
        Fragment login = fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPlaceholder, login);
        fragmentTransaction.commit();
    }

    private void changeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        //Kills LoginActivity so can´t go back by pressing back button
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        this.finish();
    }

    private boolean newUser() {

        DataBaseAccess dba = DataBaseAccess.getInstance(getApplicationContext());
        dba.openDatabase();

        boolean exist = dba.existingUser();
        dba.closeDatabe();
        //Check database if any user, True if correct/False if not
        return !exist;
    }

    private boolean checkPin(String pinToCheck) throws Exception {

        DataBaseAccess dba = DataBaseAccess.getInstance(getApplicationContext());
        dba.openDatabase();
        String pinFromDb = dba.getPinFromDatabase();
        dba.closeDatabe();

        //temporary

        return Hash.validatePin(pinToCheck, pinFromDb);
    }

    private boolean checkInputFormat() {
        //Check for @ and . in email, True if correct/False if not
        boolean pinOK;
        boolean emailOK;
        String temp = "";

        //Find textView to give feedback.
        TextView textView = findViewById(R.id.textLogin);

        //input Pin
        EditText textPin = findViewById(R.id.textPin);
        String pin = textPin.getText().toString();
        //Check Pin
        if (pin.length() == 4) {
            pinOK = true;
        } else {
            temp = "PIN have to be 4 digits";
            pinOK = false;
        }

        //input Email
        EditText textEmail = findViewById(R.id.textEmail);
        String email = textEmail.getText().toString();
        if (email.contains("@") && email.contains(".")) {
            emailOK = true;
        } else {
            temp = temp + "\nEmail have to contain @ and .";
            emailOK = false;
        }

        if (pinOK && emailOK) {
            return true;
        } else {
            textView.setText(temp);
            return false;
        }
    }

    public static String hashPin(String pin) throws Exception {

        return Hash.generateHash(pin);
    }

    private void saveUserToDatabase(String email, String hashedPin) {

        DataBaseAccess dba = DataBaseAccess.getInstance(getApplicationContext());
        dba.openDatabase();
        dba.insertUserToDatabase(email, hashedPin);
        dba.closeDatabe();

    }
}

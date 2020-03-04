package se.hkr.studentbudget.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import se.hkr.studentbudget.AppConstants;
import se.hkr.studentbudget.R;
import se.hkr.studentbudget.database.DataBaseAccess;
import se.hkr.studentbudget.login.Hash;
import se.hkr.studentbudget.login.JavaMailUtil;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public class SettingsFragment extends Fragment {
    private String tag = "Info";

    boolean toggleEditPin;
    boolean toggleSendMail;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Log.d(tag, "In the settingsFragment");

        //EditPIN Setting
        final EditText oldPin = view.findViewById(R.id.oldPin);
        final EditText newPin = view.findViewById(R.id.newPin);
        final Button saveNewPin = view.findViewById(R.id.saveNewPin);

        saveNewPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((oldPin.length()==4)&&(newPin.length()==4)) {
                    Boolean isValid = false;
                    try {
                        isValid = checkPin(oldPin.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (isValid) {
                        String pin = newPin.getText().toString();
                        String hashedPIN = null;
                        try {
                            hashedPIN = Hash.generateHash(pin);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        }
                        DataBaseAccess dba = DataBaseAccess.getInstance(getContext());
                        dba.openDatabase();
                        dba.updatePIN(hashedPIN);
                        dba.closeDatabe();
                        AppConstants.toastMessage(getContext(), "Successfully changed PIN!");
                        newPin.setText("");
                        oldPin.setText("");
                    } else {
                        AppConstants.toastMessage(getContext(), "Incorrect password");
                    }
                }else{
                    AppConstants.toastMessage(getContext(),"PIN must be 4 digits");
                }

            }
        });

        //SendFeedback Setting
        final EditText subjectText = view.findViewById(R.id.feedbackSubject);
        final EditText messageText = view.findViewById(R.id.feedbackMessage);
        final Button sendButton = view.findViewById(R.id.feedbackSend);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(subjectText.getText().toString().equals("") || (messageText.getText().toString().equals("")))){

                    DataBaseAccess dba = DataBaseAccess.getInstance(getContext());
                    dba.openDatabase();
                    final String email = dba.getEmail();
                    final String subject = "Feedback from "+email;
                    final String msg = "Subject: "+subjectText.getText().toString()+"\nMessage: "+messageText.getText().toString();

                    AppConstants.toastMessage(getContext(),"Success!");
                    try {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JavaMailUtil.send("Studentbudget.noreply@gmail.com", subject, msg);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        thread.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        subjectText.setText("");
                        messageText.setText("");
                    }
                    dba.closeDatabe();

                }else{
                    AppConstants.toastMessage(getContext(),"Write something before you click send!");
                }

            }
        });

        //Unfold cards
        TextView editPinText = view.findViewById(R.id.editPinText);
        TextView sendFeedbackText = view.findViewById(R.id.sendFeedbackText);
        toggleEditPin = false;
        toggleSendMail = false;
        oldPin.setVisibility(View.GONE);
        newPin.setVisibility(View.GONE);
        saveNewPin.setVisibility(View.GONE);
        subjectText.setVisibility(View.GONE);
        messageText.setVisibility(View.GONE);
        sendButton.setVisibility(View.GONE);

        editPinText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!toggleEditPin){
                    oldPin.setVisibility(View.VISIBLE);
                    newPin.setVisibility(View.VISIBLE);
                    saveNewPin.setVisibility(View.VISIBLE);
                    toggleEditPin = !toggleEditPin;
                }else{
                    oldPin.setVisibility(View.GONE);
                    newPin.setVisibility(View.GONE);
                    saveNewPin.setVisibility(View.GONE);
                    toggleEditPin = !toggleEditPin;
                }
            }
        });

        sendFeedbackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!toggleSendMail){
                    subjectText.setVisibility(View.VISIBLE);
                    messageText.setVisibility(View.VISIBLE);
                    sendButton.setVisibility(View.VISIBLE);
                    toggleSendMail = !toggleSendMail;
                }else{
                    subjectText.setVisibility(View.GONE);
                    messageText.setVisibility(View.GONE);
                    sendButton.setVisibility(View.GONE);
                    toggleSendMail = !toggleSendMail;
                }
            }
        });


        return view;
    }

    private boolean checkPin(String pinToCheck) throws Exception {

        DataBaseAccess dba = DataBaseAccess.getInstance(getContext());
        dba.openDatabase();
        String pinFromDb = dba.getPinFromDatabase();
        dba.closeDatabe();

        //temporary

        return Hash.validatePin(pinToCheck, pinFromDb);
    }



}
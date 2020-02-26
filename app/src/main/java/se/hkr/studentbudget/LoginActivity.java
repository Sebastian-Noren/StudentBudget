package se.hkr.studentbudget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import se.hkr.studentbudget.login.LoginFragment;
import se.hkr.studentbudget.login.StartFragment;

public class LoginActivity extends AppCompatActivity {

    int currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //currentFragments explained:
        //New User
        //1. Enter email
        //2. Enter new PIN
        //3. Log in with Pin
        //Existing users start on 3.

        Button buttonContinue = findViewById(R.id.buttonContinue);

        //Select which fragment that should be displayed first
        if (newUser()) {
            //Display Start Fragment
            changeFragment(new StartFragment());
            setCurrentFragment(1);
        } else {
            //Display Login Fragment to login
            changeFragment(new LoginFragment());
            setCurrentFragment(3);

        }


        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //New user, check so format is ok
                if (getCurrentFragment() == 1) {

                    //Display Login Fragment to create Pin
                    changeFragment(new LoginFragment());
                    setCurrentFragment(2);

                } else if (getCurrentFragment() == 2) {
                    //New user continued
                    if (checkInputFormat()) {
                        //Input entered is according to format
                        //Select Pin as password
                        EditText textPin = findViewById(R.id.textPin);
                        String pin = textPin.getText().toString();
                        //Hash and save to database
                        try {
                            savePinToDatabase(hashPin(pin));
                        } catch (Exception e) {

                        }
                        //Display success msg.
                        setCurrentFragment(3);
                    } else {
                        //Display what input is wrong format
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

                    if (isValid) {
                        //Go to Main Activity
                        changeActivity();
                    } else {
                        //Else stay on Login
                        //(Lock login for few minutes on many attempts)
                        //(Get e-mail for pwd reset)
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
        //Check database if any user, True if correct/False if not
        return true;
    }

    private boolean checkPin(String pinToCheck) throws Exception {

        String dbPin = generateHash("2222"); //temporary

        boolean isValid = validatePin(pinToCheck,dbPin);

        return isValid;
    }

    private boolean checkInputFormat() {
        //Check for @ and . in email, True if correct/False if not
        return true;
    }

    private String hashPin(String pin) throws Exception {

        return generateHash(pin);
    }

    private void savePinToDatabase(String hashedPin) {

    }

    //HASHING USING PBKDF2WithHmacSHA1


    private static String generateHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }

    private static boolean validatePin(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }
    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

}

package HKR.studentbudget.Account;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import HKR.studentbudget.R;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class AccountFragment extends Fragment {
    private String tag = "Info";
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Log.d(tag, "In the settingsFragment");
        // use view to get things from the windows
        TextView textView = view.findViewById(R.id.textTest);
        textView.setText("Account");

        return view;
    }
}
package HKR.studentbudget.test;

import android.os.Bundle;
import android.os.Handler;
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
    private TextView textView;
    private Handler handler;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        Log.d(tag, "In the TestFragment");
        // use view to get things from the windows
        textView = view.findViewById(R.id.textTest);
        handler = new Handler();

        threadUpdateTextView();

        return view;
    }



    //Background thread that updates UI
    private void threadUpdateTextView(){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("This is text changed by code");
                    }
                });
            }
        });
        th.start();
    }
}
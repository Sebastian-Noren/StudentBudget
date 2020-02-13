package HKR.studentbudget.overview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import HKR.studentbudget.R;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class OverViewFragment extends Fragment {
    private String tag = "Info";
    private NavController navController;
    private FloatingActionButton fabMain, fabMinus, fabPlus;
    private TextView textMinus, textPlus;
    private float tranlationY = 100f;
    private boolean isMenuOpen = false;
    private OvershootInterpolator interpolator = new OvershootInterpolator();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_overview, container, false);
        Log.d(tag, "In the OverViewFragment");
        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()),R.id.nav_host_fragment);
        initFabMenu(view);
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuOpen){
                    closeMenu();
                }else {
                    openMenu();
                }
            }
        });

        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "Open plus transaction");
                closeMenu();
                //TODO get this shit working
            }
        });

        fabMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "Open minus Transaction");
                closeMenu();
                navController.navigate(R.id.nav_spending);

            }
        });


        return view;
    }

    private void openMenu(){
        isMenuOpen = !isMenuOpen;
        fabMain.animate().rotation(45f).setInterpolator(interpolator).setDuration(300).start();
        fabMinus.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        textMinus.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabPlus.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(500).start();
        textPlus.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(500).start();
    }

    private void closeMenu(){
      isMenuOpen =!isMenuOpen;
        fabMain.animate().rotation(0f).setInterpolator(interpolator).setDuration(300).start();
        fabMinus.animate().translationY(tranlationY).alpha(0f).setInterpolator(interpolator).setDuration(500).start();
        textMinus.animate().translationY(tranlationY).alpha(0f).setInterpolator(interpolator).setDuration(500).start();
        fabPlus.animate().translationY(tranlationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        textPlus.animate().translationY(tranlationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void initFabMenu(View view) {
        textMinus = view.findViewById(R.id.text_spending);
        textPlus = view.findViewById(R.id.text_income);
        fabMain = view.findViewById(R.id.fabMain);
        fabMinus = view.findViewById(R.id.fabMinus);
        fabPlus = view.findViewById(R.id.fabPlus);

        fabMinus.setAlpha(0f);
        fabPlus.setAlpha(0f);
        textMinus.setAlpha(0f);
        textPlus.setAlpha(0f);

        fabMinus.setTranslationY(tranlationY);
        fabPlus.setTranslationY(tranlationY);
        textMinus.setTranslationY(tranlationY);
        textPlus.setTranslationY(tranlationY);

    }
}
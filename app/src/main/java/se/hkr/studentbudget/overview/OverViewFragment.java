package se.hkr.studentbudget.overview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import se.hkr.studentbudget.R;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class OverViewFragment extends Fragment {

    private String tag = "Info";
    private NavController navController;
    private FloatingActionButton fabMain, fabMinus, fabPlus;
    private TextView textMinus, textPlus;
    private float translationY = 100f;
    private boolean isMenuOpen = false;
    private OvershootInterpolator interpolator = new OvershootInterpolator();

    private RecyclerView recyclerView;
    private MultiViewTypeAdapter multiAdapter;
    private ArrayList<TestModel> testModels;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_overview, container, false);
        Log.d(tag, "In the OverViewFragment");
        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);
        testModels = new ArrayList<>();

        testModels.add(new TestModel(TestModel.CARD1));
        testModels.add(new TestModel(TestModel.CARD0));
        testModels.add(new TestModel(TestModel.CARD2));
        testModels.add(new TestModel(TestModel.CARD3));

        multiAdapter = new MultiViewTypeAdapter(getContext(),testModels);

        recyclerView = view.findViewById(R.id.overview_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // will create recyclerview in linearlayoyt
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(multiAdapter);



        initFabMenu(view);
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuOpen) {
                    closeMenu();
                } else {
                    openMenu();
                }
            }
        });

        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "Open plus transaction");
                closeMenu();
                Bundle bundle = new Bundle();
                int theme = 2;
                bundle.putInt("choice",theme);
                navController.navigate(R.id.nav_transaction,bundle);
            }
        });

        fabMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "Open minus Transaction");
                closeMenu();
                Bundle bundle = new Bundle();
                int theme = 1;
                bundle.putInt("choice",theme);
                navController.navigate(R.id.nav_transaction,bundle);
            }
        });


        return view;
    }

    private void openMenu() {
        isMenuOpen = !isMenuOpen;
        fabMain.animate().rotation(45f).setInterpolator(interpolator).setDuration(300).start();
        fabMinus.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        textMinus.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabPlus.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(500).start();
        textPlus.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(500).start();
        fabMinus.setEnabled(true);
        fabPlus.setEnabled(true);
    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;
        fabMinus.setEnabled(false);
        fabPlus.setEnabled(false);
        fabMain.animate().rotation(0f).setInterpolator(interpolator).setDuration(300).start();
        fabMinus.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(500).start();
        textMinus.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(500).start();
        fabPlus.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        textPlus.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
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
        fabMinus.setEnabled(false);
        fabPlus.setEnabled(false);

        fabMinus.setTranslationY(translationY);
        fabPlus.setTranslationY(translationY);
        textMinus.setTranslationY(translationY);
        textPlus.setTranslationY(translationY);

    }

}
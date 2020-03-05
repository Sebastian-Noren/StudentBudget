package se.hkr.studentbudget.overview;

import android.content.Context;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import se.hkr.studentbudget.AppConstants;
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

        Log.d(tag, "OverViewFragment: In the OnCreateView event()");
        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);
        testModels = new ArrayList<>();
        recyclerView = view.findViewById(R.id.overview_recycler);
        testModels.add(new TestModel(TestModel.SUMMARY_CARD));
        testModels.add(new TestModel(TestModel.EXPENSES_CARD));
        testModels.add(new TestModel(TestModel.ACCOUNT_CARD));
        testModels.add(new TestModel(TestModel.BUDGET_CARD));
        testModels.add(new TestModel(TestModel.TRANSACTIONS_CARD));

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


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy < 3) {
                    fabMain.show();

                } else if (dy > 3) {
                    fabMain.hide();
                }
            }
        });
        // ending recyler view


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

    // 1
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(tag, "OverViewFragment: In the onAttach() event");
    }
    //2
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(tag, "OverViewFragment: In the OnCreate event() - Start read in current month.");
        AppConstants.initProgressBars(getContext());
        AppConstants.initCurrentMonthsTransactions(getContext());
    }
    //4
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(tag, "OverViewFragment: In the onActivityCreated() event");

    }
    //5
    @Override
    public void onStart() {
        super.onStart();
        Log.d(tag, "OverViewFragment: In the onStart() event");
    }
    //6
    @Override
    public void onResume() {
        super.onResume();
        fabMain.show();
        Log.d(tag, "OverViewFragment: In the onResume() event");
        multiAdapter = new MultiViewTypeAdapter(getContext(),testModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // will create recyclerview in linearlayoyt
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(multiAdapter);
    }
    //7
    @Override
    public void onPause() {
        super.onPause();
        Log.d(tag, "OverViewFragment: In the onPause() event");
    }
    //8
    @Override
    public void onStop() {
        super.onStop();
        Log.d(tag, "OverViewFragment: In the onStop() event");
    }
    //9
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(tag, "OverViewFragment: In the onDestroyView() event");
    }
    //10
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(tag, "OverViewFragment: In the onDestroy() event");
    }
    //11
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(tag, "OverViewFragment: In the onDetach() event");
    }

}
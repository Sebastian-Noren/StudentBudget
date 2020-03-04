package se.hkr.studentbudget;

import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;

/*
Don´t change anything in here if you really don´t know what you doing please.
 */

public class MainActivity extends AppCompatActivity {
    private String tag = "Info";
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(tag, "MainActivity: In the onCreate() event");
        AppConstants.applicationInitialization(this.getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        //Disable the little arrow pointing back to overview and open the menu
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_overView, R.id.nav_budget, R.id.nav_statistics, R.id.nav_expenses, R.id.nav_account,
                R.id.nav_settings, R.id.nav_test)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        if (!AppConstants.accountExist()) {
            Log.i(tag, "No account exist, creating one.");
            navController.navigate(R.id.nav_account);
        }else {
            Log.i(tag, "Account exist, open Overview.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    // Makes the menu goes back if open when back button is pressed
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            super.onBackPressed();
        }
    }

    protected void onStart() {
        super.onStart();
        Log.d(tag, "MainActivity: In the onStart() event");
    }

    protected void onRestart() {
        super.onRestart();
        Log.d(tag, "MainActivity: In the onRestart() event");
    }

    protected void onResume() {
        super.onResume();
        Log.d(tag, "MainActivity: In the onResume() event");
    }

    protected void onPause() {
        super.onPause();
        Log.d(tag, "MainActivity: In the onPause() event");
    }

    protected void onStop() {
        super.onStop();
        Log.d(tag, "MainActivity: In the onStop() event");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(tag, "MainActivity: In the onDestroy() event");
    }

}

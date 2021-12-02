package com.sconti.studentcontinent.activity.knowledge;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sconti.studentcontinent.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class KnowledgeBottomNavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_bottom_nav);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as outdooricon set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_outdoor, R.id.navigation_games)
                .build();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   // toolbar.setTitle("Shop");
                    return true;
                case R.id.navigation_outdoor:
                   // toolbar.setTitle("My Gifts");
                    return true;
                case R.id.navigation_games:
                  //  toolbar.setTitle("Cart");
                    return true;
               /* case R.id.navigation_profile:
                    toolbar.setTitle("Profile");
                    return true;*/
            }
            return false;
        }
    };

}

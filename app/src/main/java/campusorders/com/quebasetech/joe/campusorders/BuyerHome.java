package campusorders.com.quebasetech.joe.campusorders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import campusorders.com.quebasetech.joe.campusorders.fragments.BuyerDashboard;
import campusorders.com.quebasetech.joe.campusorders.fragments.Transactions;
import campusorders.com.quebasetech.joe.campusorders.fragments.UserSettings;

public class BuyerHome extends AppCompatActivity {

    private Context context;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_home);

        // Load default fragment
        loadFragment(new BuyerDashboard());
        // Listen for clicks on menu
        setupBottomNavigation();

        context = this;
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void setupBottomNavigation() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        fragment = new BuyerDashboard();
                        break;
                    case R.id.navigation_transactions:
                        fragment = new Transactions();
                        break;
                    case R.id.navigation_settings:
                        fragment = new UserSettings();
                        break;
                }
                return loadFragment(fragment);
            }
        });
    }
}

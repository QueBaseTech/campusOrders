package campusorders.com.quebasetech.joe.campusorders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class buyer_home extends AppCompatActivity {

    private TextView mTextMessage;
    private Context context;
    private ListView itemsList;
    private String[] items = {
            "Smokies",
            "Smokies & eggs",
            "Eggs",
            "Eggs",
            "Apples",
            "Apples",
            "Groceries",
            "Milk",
            "Milk",
    };
    private Double[] prices = {
            22.0,
            40.0,
            40.0,
            20.0,
            25.0,
            25.0,
            75.0,
            35.0,
            35.0,
    };
    private String[] sellers = {
            "Jane Doe",
            "Awesome Dude",
            "Awesome Dude",
            "John Doe",
            "Joe Nyugoh",
            "Joe Nyugoh",
            "Fisher Maxwell",
            "Milly Bett",
            "Milly Bett",
    };
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Toast.makeText(context, "You selected: Campus orders home", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_transactions:
                    Toast.makeText(context, "You selected: Transactions" , Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_settings:
                    // Launch settings ~ user prefs and a/c
                    Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(settings);
                    Toast.makeText(context, "You selected: settings" , Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_home);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        context = this;
        itemsList = (ListView) findViewById(R.id.items_list_view);
        Items_Adapter adapter = new Items_Adapter(context, items, prices, sellers);
        itemsList.setAdapter(adapter);

        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO:: Launch dialog for each
                Toast.makeText(context, "You selected: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

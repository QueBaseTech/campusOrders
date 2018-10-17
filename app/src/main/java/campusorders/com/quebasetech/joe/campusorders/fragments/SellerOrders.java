package campusorders.com.quebasetech.joe.campusorders.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.adapters.OrderAdapter;

public class SellerOrders extends Fragment {
    BottomNavigationView topMenu;
    private ListView ordersList;
    private String[] orders = { "Block D Rm 5",  "Riesta Rm 1",  "Block C Rm 10",  "Runda C11",  "Ghost House Rm 9",  "Meka Rm 40",  "Sunset A Rm 9",  "Mwangeka Rm 7",  "Kwa mathe Rm 17",};
    private int[] qty = {2, 4, 4, 2, 2, 2, 7, 3, 3,};
    private double[] prices = { 22.0,  40.0,  40.0,  20.0,  25.0,  25.0,  75.0,  35.0,  35.0,};
    private String[] buyers = { "Jane Doe",  "Awesome Dude",  "Awesome Dude",  "John Doe",  "Joe Nyugoh",  "Joe Nyugoh",  "Fisher Maxwell",  "Milly Bett",  "Milly Bett",};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_dashboard, container, false);
        topMenu = (BottomNavigationView) view.findViewById(R.id.orders_navigation);
        topMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // TODO:: Filter list based on selected tab
                return true;
            }
        });
        ordersList = (ListView) view.findViewById(R.id.orders);
        OrderAdapter adapter = new OrderAdapter(getContext(), orders, buyers, qty, prices);
        ordersList.setAdapter(adapter);
        return view;
    }


}

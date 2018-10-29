package campusorders.com.quebasetech.joe.campusorders.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.model.Gig;
import campusorders.com.quebasetech.joe.campusorders.model.Order;
import campusorders.com.quebasetech.joe.campusorders.model.User;
import campusorders.com.quebasetech.joe.campusorders.utils.utils;

public class SellerOrders extends Fragment {
    BottomNavigationView topMenu;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    public boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.orders_fragments, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_dashboard, container, false);
        topMenu = (BottomNavigationView) view.findViewById(R.id.orders_navigation);
        topMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.new_orders:
                        fragment = new NewOrders();
                        break;
                    case R.id.complete_orders:
                        fragment = new CompleteOrders();
                        break;
                    case R.id.rejected_orders:
                        fragment = new RejectedOrders();
                        break;
                    case R.id.cancelled_orders:
                        fragment = new CanceledOrders();
                        break;
                }
                return loadFragment(fragment);
            }
        });
        loadFragment(new NewOrders());
        return view;
    }
}

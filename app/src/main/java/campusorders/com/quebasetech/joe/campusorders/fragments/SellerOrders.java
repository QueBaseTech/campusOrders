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
import android.widget.ArrayAdapter;
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
import campusorders.com.quebasetech.joe.campusorders.adapters.OrderAdapter;
import campusorders.com.quebasetech.joe.campusorders.model.Gig;
import campusorders.com.quebasetech.joe.campusorders.model.Order;
import campusorders.com.quebasetech.joe.campusorders.model.User;
import campusorders.com.quebasetech.joe.campusorders.utils.utils;

public class SellerOrders extends Fragment {
    BottomNavigationView topMenu;
    private ListView ordersList;
    private Context context;
    private DatabaseReference ordersRefs;
    private DatabaseReference usersRefs;
    private List<Order> orders;
    private HashMap clients;
    private HashMap gigs;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

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

        orders = new ArrayList<>();
        clients = new HashMap();
        gigs = new HashMap();
        ordersList = (ListView) view.findViewById(R.id.orders);
        DatabaseReference gigsRef = FirebaseDatabase.getInstance().getReference("gigs");
        ordersRefs = FirebaseDatabase.getInstance().getReference("orders");
        usersRefs = FirebaseDatabase.getInstance().getReference("users");
        usersRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot user: dataSnapshot.getChildren()) {
                    User client = user.getValue(User.class);
                    clients.put(client.getId(), client.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        gigsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot gig: dataSnapshot.getChildren()){
                    Gig g = gig.getValue(Gig.class);
                    gigs.put(g.getId(), g.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Query yourOrders = ordersRefs.orderByChild("seller").equalTo(context.getSharedPreferences(utils.CURRENT_USER, Context.MODE_PRIVATE).getString(utils.USER_ID, ""));
        yourOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot orderSnapshot: dataSnapshot.getChildren()){
                    orders.add(orderSnapshot.getValue(Order.class));
                    OrderAdapter adapter = new OrderAdapter(context, orders, clients, gigs);
                    ordersList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Error :"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


}

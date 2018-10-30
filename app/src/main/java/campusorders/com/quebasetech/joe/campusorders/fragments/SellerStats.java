package campusorders.com.quebasetech.joe.campusorders.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.adapters.GigsAdapter;
import campusorders.com.quebasetech.joe.campusorders.adapters.OrderAdapter;
import campusorders.com.quebasetech.joe.campusorders.adapters.OrderStatsAdapter;
import campusorders.com.quebasetech.joe.campusorders.model.Gig;
import campusorders.com.quebasetech.joe.campusorders.model.Order;
import campusorders.com.quebasetech.joe.campusorders.model.OrderStatistics;
import campusorders.com.quebasetech.joe.campusorders.utils.utils;

public class SellerStats extends Fragment {
    private List<Gig> mGigList;
    private List<Order> orders;
    private List<OrderStatistics> statz;
    private DatabaseReference gigsRef;
    private DatabaseReference ordersRef;
    private View statsGigsView = null;
    Context context;
    private HashMap stats;
    private ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mGigList = new ArrayList<>();
        orders = new ArrayList<>();
        statz = new ArrayList<>();
        stats = new HashMap<String, OrderStatistics>();
        gigsRef = FirebaseDatabase.getInstance().getReference("gigs");
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        statsGigsView = inflater.inflate(R.layout.seller_stats, container, false);
        listView = (ListView) statsGigsView.findViewById(R.id.stats);
        return statsGigsView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Get all orders for the current seller
        final String currentUser = context.getSharedPreferences(utils.CURRENT_USER, Context.MODE_PRIVATE).getString(utils.USER_ID, "");
        Query yourOrders = ordersRef.orderByChild("seller").equalTo(currentUser);
        yourOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders.clear();
                for(DataSnapshot orderSnapshot: dataSnapshot.getChildren()){
                    orders.add(orderSnapshot.getValue(Order.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Error :"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Query gigs = gigsRef.orderByChild("sellerId").equalTo(currentUser);
        gigs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mGigList.clear();
                for(DataSnapshot gigSnapShot: dataSnapshot.getChildren()){
                    Gig gig = gigSnapShot.getValue(Gig.class);
                    mGigList.add(gig);
                }
                Collections.reverse(mGigList);
                //Display items
                generateStats();
                OrderStatsAdapter adapter = new OrderStatsAdapter(context, statz);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void generateStats() {
        statz.clear();
        for(Gig g : mGigList) {
            OrderStatistics orderStatistics = new OrderStatistics(g.getName(), 0, 0, 0, 0, 0, 0);
            stats.put(g.getId(), orderStatistics);
        }
        for(Order o : orders) {
            OrderStatistics stat = (OrderStatistics) stats.get(o.getGigId());
            if(o.getStatus() == Order.orderStatus.NEW)
                stat.setNEW(stat.getNEW()+1);

            if(o.getStatus() == Order.orderStatus.PENDING)
                stat.setPending(stat.getPending()+1);

            if(o.getStatus() == Order.orderStatus.FULFILLED )
                stat.setDelivered(stat.getDelivered()+1);

            if(o.getStatus() == Order.orderStatus.REJECTED )
                stat.setRejected(stat.getRejected()+1);

            if(o.getStatus() == Order.orderStatus.CANCELLED )
                stat.setCancelled(stat.getCancelled()+1);

            stat.setTotal(stat.getTotal()+1);
            stats.put(o.getGigId(), stat);
        }
        for(Gig g : mGigList){
            statz.add((OrderStatistics) stats.get(g.getId()));
        }
    }
}

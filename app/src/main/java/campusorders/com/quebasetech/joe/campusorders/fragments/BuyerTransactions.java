package campusorders.com.quebasetech.joe.campusorders.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.adapters.BuyerTransactionAdapter;
import campusorders.com.quebasetech.joe.campusorders.adapters.Items_Adapter;
import campusorders.com.quebasetech.joe.campusorders.model.Gig;
import campusorders.com.quebasetech.joe.campusorders.model.Order;
import campusorders.com.quebasetech.joe.campusorders.model.Reason;
import campusorders.com.quebasetech.joe.campusorders.model.User;
import campusorders.com.quebasetech.joe.campusorders.utils.utils;

import static android.content.Context.MODE_PRIVATE;

/**
 * Display all orders made by a certain buyer
 */
public class BuyerTransactions extends Fragment {
    private List<Order> orderList;
    private ListView ordersListView;
    private DatabaseReference databaseReference;
    private HashMap gigsList, usersList, reasons;
    private TextView noTransactionsNotice;
    private ProgressBar loading;
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buyer_trasactions, container, false);
        ordersListView = (ListView) view.findViewById(R.id.items_list_view);
        orderList = new ArrayList<>();
        gigsList = new HashMap();
        usersList = new HashMap();
        reasons = new HashMap();
        noTransactionsNotice = (TextView) view.findViewById(R.id.noTransactionsNotice);
        loading = (ProgressBar) view.findViewById(R.id.loading_transactions);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference gigsRef = databaseReference.child("gigs");
        DatabaseReference usersRef = databaseReference.child("users");
        DatabaseReference ordersRef = databaseReference.child("orders");
        DatabaseReference reasonRef = databaseReference.child("reasons");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot user: dataSnapshot.getChildren()) {
                    usersList.put(user.getValue(User.class).getId(), user.getValue(User.class).getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reasonRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot reason: dataSnapshot.getChildren()) {
                    reasons.put(reason.getValue(Reason.class).getOrderId(), reason.getValue(Reason.class).getReason());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        gigsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot gig: dataSnapshot.getChildren()) {
                    gigsList.put(gig.getValue(Gig.class).getId(), gig.getValue(Gig.class).getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Query sellingItems = ordersRef.orderByChild("client").equalTo(context.getSharedPreferences(utils.CURRENT_USER, MODE_PRIVATE).getString(utils.USER_ID, "None"));
        sellingItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                loading.setVisibility(View.GONE);
                for(DataSnapshot order: dataSnapshot.getChildren()) {
                    orderList.add(order.getValue(Order.class));
                }
                Collections.reverse(orderList);
                if(orderList.isEmpty())
                    noTransactionsNotice.setVisibility(View.VISIBLE);
                BuyerTransactionAdapter adapter = new BuyerTransactionAdapter(context, orderList, gigsList, usersList, reasons);
                ordersListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}

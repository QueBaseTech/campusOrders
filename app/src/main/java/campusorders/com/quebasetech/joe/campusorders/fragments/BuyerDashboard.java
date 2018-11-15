package campusorders.com.quebasetech.joe.campusorders.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
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

import campusorders.com.quebasetech.joe.campusorders.adapters.Items_Adapter;
import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.model.Gig;
import campusorders.com.quebasetech.joe.campusorders.model.User;

public class BuyerDashboard extends Fragment implements SearchView.OnQueryTextListener{
    private ListView itemsList;
    private DatabaseReference databaseReference;
    private Context context;
    private List<Gig> gigsList;
    private HashMap usersList;
    private SearchView searchView;
    private Items_Adapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buyer_dashboard, container, false);
        itemsList = (ListView) view.findViewById(R.id.items_list_view);
        gigsList = new ArrayList<>();
        usersList = new HashMap();
        searchView = (SearchView) view.findViewById(R.id.search_item);

        searchView.setOnQueryTextListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference gigsRef = databaseReference.child("gigs");
        DatabaseReference usersRef = databaseReference.child("users");
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
        Query sellingItems = gigsRef.orderByChild("selling").equalTo(true);
        sellingItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gigsList.clear();
                for(DataSnapshot gig: dataSnapshot.getChildren()) {
                    gigsList.add(gig.getValue(Gig.class));
                }
                Collections.reverse(gigsList);
                adapter = new Items_Adapter(context, gigsList, usersList);
                itemsList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        if(adapter != null)
            adapter.getFilter().filter(text);
        return false;
    }
}

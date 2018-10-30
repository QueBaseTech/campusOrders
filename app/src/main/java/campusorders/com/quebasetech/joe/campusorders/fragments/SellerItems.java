package campusorders.com.quebasetech.joe.campusorders.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.adapters.GigsAdapter;
import campusorders.com.quebasetech.joe.campusorders.dialogs.AddItemDialog;
import campusorders.com.quebasetech.joe.campusorders.model.Gig;
import campusorders.com.quebasetech.joe.campusorders.utils.utils;

public class SellerItems extends Fragment {
    private FloatingActionButton floatingAddItemButton;
    private List<Gig> mGigList;
    private DatabaseReference gigsRef;
    private View sellerGigsView = null;
    private TextView notice;
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sellerGigsView = inflater.inflate(R.layout.seller_items, container, false);

        mGigList = new ArrayList<>();
        gigsRef = FirebaseDatabase.getInstance().getReference("gigs");
        notice = (TextView) sellerGigsView.findViewById(R.id.no_gigs_notice);

        //Add add Floating button
        floatingAddItemButton = (FloatingActionButton) sellerGigsView.findViewById(R.id.floatingAddItemButton);
        floatingAddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItemDialog dialog = new AddItemDialog();
                dialog.show(getFragmentManager(), "add item");
            }
        });
        return sellerGigsView;
    }

    @Override
    public void onStart() {
        super.onStart();
        final String currentUser = context.getSharedPreferences(utils.CURRENT_USER, Context.MODE_PRIVATE).getString(utils.USER_ID, "");
        // TODO:: Add query filter here to only fetch current user items
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
                if(mGigList.isEmpty())
                    notice.setVisibility(View.VISIBLE);
                //Display items
                ListView gigsList = (ListView) sellerGigsView.findViewById(R.id.items_for_sale);
                GigsAdapter gigsAdapter = new GigsAdapter(context, mGigList);
                gigsList.setAdapter(gigsAdapter);

                // TODO:: Fix this code smell (- ^ -)
                /*gigsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Gig gig = mGigList.get(position);
                        Toast.makeText(getContext(), gig.toString(), Toast.LENGTH_SHORT).show();
                    }
                });*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

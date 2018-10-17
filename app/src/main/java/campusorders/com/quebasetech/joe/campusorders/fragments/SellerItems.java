package campusorders.com.quebasetech.joe.campusorders.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.adapters.GigsAdapater;
import campusorders.com.quebasetech.joe.campusorders.dialogs.AddItemDialog;

public class SellerItems extends Fragment {
    private FloatingActionButton floatingAddItemButton;
    private Double[] prices = { 22.0,  40.0,  40.0,  20.0,  25.0,  25.0,  75.0,  35.0,  35.0,};
    private String[] gigs = { "Smokies",  "Smokies & eggs",  "Eggs",  "Eggs",  "Apples",  "Apples",  "Groceries",  "Milk",  "Milk",};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seller_items, container, false);

        //Display items
        ListView gigsList = (ListView) view.findViewById(R.id.items_for_sale);
        GigsAdapater gigsAdapater = new GigsAdapater(getContext(), gigs, prices);
        gigsList.setAdapter(gigsAdapater);


        //Add add Floating button
        floatingAddItemButton = (FloatingActionButton) view.findViewById(R.id.floatingAddItemButton);
        floatingAddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItemDialog dialog = new AddItemDialog();
                dialog.show(getFragmentManager(), "add item");
            }
        });
        return view;
    }
}

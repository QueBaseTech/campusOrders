package campusorders.com.quebasetech.joe.campusorders.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.dialogs.AddItemDialog;

public class SellerItems extends Fragment {
    private FloatingActionButton floatingAddItemButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seller_items, container, false);
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

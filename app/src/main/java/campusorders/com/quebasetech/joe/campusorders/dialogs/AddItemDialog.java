package campusorders.com.quebasetech.joe.campusorders.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.model.Gig;

public class AddItemDialog extends DialogFragment {
    private EditText mName;
    private EditText mUnitMeasure;
    private EditText mPrice;
    private Button mAddButton, mCancelButton;
    private DatabaseReference gigsDatabase;
    private ProgressDialog progressDialog;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View addDialog = inflater.inflate(R.layout.add_item_dialog, null);

        // Init view
        mName = (EditText) addDialog.findViewById(R.id.item_name);
        mUnitMeasure = (EditText) addDialog.findViewById(R.id.item_unit);
        mPrice = (EditText) addDialog.findViewById(R.id.item_price);
        gigsDatabase = FirebaseDatabase.getInstance().getReference("gigs");
        mCancelButton = (Button) addDialog.findViewById(R.id.add_item_cancel_btn);
        mAddButton = (Button) addDialog.findViewById(R.id.add_item_btn);
        progressDialog = new ProgressDialog(getContext());

        // Handle button clicks
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO:: Save item to database
                progressDialog.setMessage("Adding item...");
                progressDialog.show();
                Gig gig = addGig();
                if(gig != null) {
                    gigsDatabase.child(gig.getId())
                            .setValue(gig)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Failed adding item: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });

        builder.setView(addDialog)
                .setMessage("Add item");

        return builder.create();
    }

    private Gig addGig(){
        Gig gig = null;
        String name = mName.getText().toString().trim();
        String unit = mUnitMeasure.getText().toString().trim();
        String priceString = mPrice.getText().toString().trim();

        if(name.isEmpty()){
            mName.setError("Name is required");
            return gig;
        }

        if(unit.isEmpty()){
            mUnitMeasure.setError("Unit is required");
            return gig;
        }

        if(priceString.isEmpty()){
            mPrice.setError("Price is required");
            return gig;
        }
        int price = Integer.parseInt(priceString);
        String seller = getCurrentUserEmail();

        if(seller.isEmpty())
            return gig;

        // Generate unique id for item
        String id = gigsDatabase.push().getKey();
        gig = new Gig(id, name, 10000, price, seller, true, unit);
        return gig;
    }

    private String getCurrentUserEmail() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        assert firebaseUser != null;
        if(firebaseUser.getEmail() != null)
            return firebaseUser.getEmail();
        else
            return "";
    }

}

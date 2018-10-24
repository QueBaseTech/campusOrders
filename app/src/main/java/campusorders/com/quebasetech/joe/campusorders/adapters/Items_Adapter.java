package campusorders.com.quebasetech.joe.campusorders.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.model.Gig;
import campusorders.com.quebasetech.joe.campusorders.model.Order;
import campusorders.com.quebasetech.joe.campusorders.utils.utils;

public class Items_Adapter extends ArrayAdapter<Gig> {
    private  final Context context;
    private final List<Gig> gigsList;
    private final HashMap sellers;
    private EditText qty, newLocation;
    private TextView itemName, itemPrice, totalPrice, itemId, itemSeller,  defaultLocation;
    private Button orderButton, cancelButton, changeLocation;
    private DatabaseReference gigsDatabase;
    private ProgressDialog progressDialog;
    private String defaultUserLocation, clientId;

    public Items_Adapter(@NonNull Context context, List<Gig> gigs, HashMap sellers) {
        super(context, R.layout.items_list, gigs);
        this.context = context;
        this.gigsList = gigs;
        this.sellers = sellers;
        defaultUserLocation = context.getSharedPreferences(utils.CURRENT_USER, Context.MODE_PRIVATE).getString(utils.USER_LOCATION, "None");
        clientId = context.getSharedPreferences(utils.CURRENT_USER, Context.MODE_PRIVATE).getString(utils.USER_ID, "None");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.items_list, parent, false);
        final TextView itemName = (TextView) rowView.findViewById(R.id.order_location);
        TextView itemPrice = (TextView) rowView.findViewById(R.id.item_unit);
        TextView itemSeller = (TextView) rowView.findViewById(R.id.seller_value);
        final Gig gig = gigsList.get(position);
        itemName.setText(gig.getName());
        itemPrice.setText(""+gig.getPrice());
        itemSeller.setText(sellers.get(gig.getSellerId()).toString());
        final Button order = (Button) rowView.findViewById(R.id.orderBtn);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeOrder(gig);
            }
        });
        return rowView;
    }

    private void makeOrder(final Gig gig) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View makeOrderDialog = inflater.inflate(R.layout.make_order_dialog, null);

        // Init view
        itemName = (TextView) makeOrderDialog.findViewById(R.id.item_to_order);
        itemName.setText(gig.getName());
        itemPrice = (TextView) makeOrderDialog.findViewById(R.id.price);
        itemPrice.setText(""+gig.getPrice());
        totalPrice = (TextView) makeOrderDialog.findViewById(R.id.total_price);
        totalPrice.setText(""+gig.getPrice());
        itemId = (TextView) makeOrderDialog.findViewById(R.id.item_id);
        itemId.setText(gig.getId());
        itemSeller = (TextView) makeOrderDialog.findViewById(R.id.item_seller);
        itemSeller.setText(gig.getSellerId());
        defaultLocation = (TextView) makeOrderDialog.findViewById(R.id.location);
        defaultLocation.setText(defaultUserLocation);
        newLocation = (EditText) makeOrderDialog.findViewById(R.id.new_location);
        qty = (EditText) makeOrderDialog.findViewById(R.id.item_qty);
        qty.setText("1");
        changeLocation = (Button) makeOrderDialog.findViewById(R.id.change_location);
        gigsDatabase = FirebaseDatabase.getInstance().getReference("orders");
        progressDialog = new ProgressDialog(context);

        qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String qtyString = qty.getText().toString();
                if(qtyString.trim().isEmpty()) {
                    qty.setError("Qty is required !");
                    return;
                }
                double amount = Double.parseDouble(qtyString);
                totalPrice.setText("" + amount*gig.getPrice());
            }
        });

        // Handle button clicks
        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newLocation.getVisibility() == View.GONE) {
                    newLocation.setVisibility(View.VISIBLE);
                    changeLocation.setText("Use default");
                } else {
                    newLocation.setVisibility(View.GONE);
                    changeLocation.setText("Change location");
                }
            }
        });
        DialogInterface.OnClickListener dialogClickListener = new  DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        sendOrder();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        builder.setView(makeOrderDialog)
                .setPositiveButton("ORDER", dialogClickListener)
                .setNegativeButton("CANCEL", dialogClickListener)
                .setMessage("Order item").show();

    }

    private void sendOrder() {
        String amount = qty.getText().toString().trim();
        int amountQty = 1;
        String location = "";
        String id = "";


        if(amount.isEmpty()){
            qty.setError("Name is required");
            return;
        } else {
            amountQty  = Integer.parseInt(amount);
        }

        if(newLocation.getVisibility() == View.VISIBLE){
            location = newLocation.getText().toString();
        } else {
            location = defaultUserLocation;
        }
        progressDialog.setMessage("Placing order...");
        progressDialog.show();
        long now = new Date().getTime();
        id = gigsDatabase.push().getKey();
        Order order = new Order(id, itemId.getText().toString(), amountQty, amountQty* Double.parseDouble(itemPrice.getText().toString()), now, now, location, clientId, itemSeller.getText().toString(), Order.orderStatus.NEW);
        gigsDatabase.child(order.getId())
                .setValue(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        // TODO: Take user to transactions page
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Failed editing item: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

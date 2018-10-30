package campusorders.com.quebasetech.joe.campusorders.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.model.Gig;
import campusorders.com.quebasetech.joe.campusorders.model.Order;
import campusorders.com.quebasetech.joe.campusorders.utils.utils;

public class Items_Adapter extends ArrayAdapter<Gig> implements Filterable{
    private  final Context context;
    private List<Gig> gigsList;
    private final HashMap sellers;
    private EditText qty, newLocation;
    private TextView itemName, itemPrice, totalPrice, itemId, itemSeller,  defaultLocation;
    private Button orderButton, cancelButton, changeLocation;
    private DatabaseReference gigsDatabase;
    private ProgressDialog progressDialog;
    private String defaultUserLocation, clientId;
    private List<Gig> filteredGigs;
    private CustomFilter filter;

    public Items_Adapter(@NonNull Context context, List<Gig> gigs, HashMap sellers) {
        super(context, R.layout.items_list, gigs);
        this.context = context;
        this.gigsList = gigs;
        this.filteredGigs = gigs;
        this.sellers = sellers;
        defaultUserLocation = context.getSharedPreferences(utils.CURRENT_USER, Context.MODE_PRIVATE).getString(utils.USER_LOCATION, "None");
        clientId = context.getSharedPreferences(utils.CURRENT_USER, Context.MODE_PRIVATE).getString(utils.USER_ID, "None");
    }

    @Override
    public int getCount() {
        return gigsList == null? 0:gigsList.size();
    }

    @Nullable
    @Override
    public Gig getItem(int position) {
        return gigsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return gigsList.indexOf(getItem(position));
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

        qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String qtyString = s.toString();
                if(qtyString.trim().isEmpty()) {
                    qty.setError("Qty is required !");
                    return;
                }
                if(Double.parseDouble(qtyString.trim()) == 0) {
                    qty.setError("Qty is required !");
                    return;
                }
                double amount = Double.parseDouble(qtyString);
                totalPrice.setText("" + amount*gig.getPrice());
            }

            @Override
            public void afterTextChanged(Editable s) { }
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
                if(newLocation.getVisibility() == View.VISIBLE && newLocation.getText().toString().trim().isEmpty()) {
                    newLocation.setError("Enter a location");
                    Toast.makeText(context, "Order not sent, add a location", Toast.LENGTH_LONG).show();
                    return;
                }
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
                .show();

    }

    @Override
    public void add(@Nullable Gig object) {
        super.add(object);

    }

    @NonNull
    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new CustomFilter();
        }
        return filter;
    }

    class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if(constraint != null && constraint.length() > 0){
                constraint = constraint.toString().toLowerCase();
                ArrayList<Gig> filters = new ArrayList<>();
                for(int i = 0 ;i<filteredGigs.size();i++){
                    if(filteredGigs.get(i).getName().toLowerCase().contains(constraint)){
                        Gig g = filteredGigs.get(i);
                        Gig gig = new Gig(g.getId(), g.getName(), g.getImage(), g.getPrice(), g.getSellerId(), g.isSelling(), g.getUnit());
                        filters.add(gig);
                    }
                }
                filterResults.count = filters.size();
                filterResults.values = filters;
            } else {
                filterResults.count = filteredGigs.size();
                filterResults.values = filteredGigs;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            gigsList = (List<Gig>) results.values;
            notifyDataSetChanged();
        }
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
            location = newLocation.getText().toString().trim();
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
                        Toast.makeText(context, "Order sent.", Toast.LENGTH_SHORT).show();
                        // TODO: Take user to transactions page
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Failed placing order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

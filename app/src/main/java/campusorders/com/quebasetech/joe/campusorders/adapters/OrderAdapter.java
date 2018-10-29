package campusorders.com.quebasetech.joe.campusorders.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.model.Order;
import campusorders.com.quebasetech.joe.campusorders.model.Reason;


public class OrderAdapter extends ArrayAdapter<Order> {
    private  final Context context;
    private List<Order> orderList;
    private DatabaseReference ordersRef;
    private HashMap clients;
    private HashMap gigs;
    private TextView itemName, location, qtyLabel, price, buyer,timeElapsed;
    private Button reject, deliver;
    private EditText customReason;
    private RadioButton otherReason;
    private RadioGroup reasons;
    String reason = "";

    public OrderAdapter(@NonNull Context context, List<Order> orders, HashMap clients, HashMap gigs) {
        super(context, R.layout.orders_list, orders);
        this.context = context;
        this.orderList = orders;
        this.clients = clients;
        this.gigs = gigs;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.orders_list,  parent, false);
        // Populate view with content
        itemName = (TextView) view.findViewById(R.id.order_item);
        location = (TextView) view.findViewById(R.id.order_location);
        qtyLabel = (TextView) view.findViewById(R.id.item_unit);
        price = (TextView) view.findViewById(R.id.order_value);
        buyer = (TextView) view.findViewById(R.id.buyer_name);
        timeElapsed = (TextView) view.findViewById(R.id.timeElapsed);
        reject = (Button) view.findViewById(R.id.rejectOrderBtn);
        deliver = (Button) view.findViewById(R.id.deliverOrderBtn);
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        final Order order = orderList.get(position);
        itemName.setText(gigs.get(order.getGigId()).toString());
        location.setText(order.getLocation());
        qtyLabel.setText(""+order.getQty());
        price.setText(""+order.getTotal());
        buyer.setText(clients.get(order.getClient()).toString());
        timeElapsed.setText(getElapsedTime(order.getOrderTime()));

        // Show reject
        if(order.getStatus() == Order.orderStatus.NEW){
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rejectOrder(order);
                }
            });
            deliver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // order is new to on will deliver should go to the pending delivery stage
                    changeOrderStatus(order, Order.orderStatus.PENDING);
                }
            });
        } else {
            reject.setVisibility(View.GONE);
            deliver.setVisibility(View.GONE);
        }

        // Handle will deliver button
        if(order.getStatus() == Order.orderStatus.PENDING){
            reject.setVisibility(View.VISIBLE);// Ensure they can cancel the order
            deliver.setVisibility(View.VISIBLE);// Ensure they can cancel the order
            deliver.setText("COMPLETE");
            deliver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // change from pending state to fulfilled state
                    changeOrderStatus(order, Order.orderStatus.FULFILLED);
                }
            });
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rejectOrder(order);
                }
            });
        }

        // Change reject button to delete button

        return view;
    }

    private void changeOrderStatus(Order order, Order.orderStatus status) {
        ordersRef.child(order.getId()).child("status").setValue(status).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Toast.makeText(context, "Order cancelled successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Order failed to cancel"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        if(status == Order.orderStatus.FULFILLED)
            ordersRef.child(order.getId()).child("deliveryTime").setValue(new Date().getTime());
    }

    private String getElapsedTime(long orderTime) {
        Date ordered = new Date(orderTime);
        Date now = new Date();
        long difference = now.getTime() - orderTime;
        long seconds = difference/1000;
        long minutes = seconds/60;
        long hours = minutes/60;
        long days = hours/24;
        String stmt;
        if (days != 0) {
            stmt = days ==1 ? " day ago":" days ago";
            return ""+ days + stmt;
        }
        if (hours != 0){
            stmt = hours ==1 ? " hour ago":" hours ago";
            return ""+ hours + stmt;
        }
        if (minutes != 0){
            stmt = minutes ==1 ? " minute ago":" minutes ago";
            return ""+ minutes + stmt;
        }
        if (seconds != 0){
            stmt = seconds ==1 ? " second ago":" seconds ago";
            return ""+ seconds + stmt;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("DD/MMMM/yyyy");
        return dateFormat.format(ordered);
    }

    private void rejectOrder(final Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rejectDialog = inflater.inflate(R.layout.reject_order_dialog, null);

        reasons = (RadioGroup) rejectDialog.findViewById(R.id.reject_radio_group);
        customReason = (EditText) rejectDialog.findViewById(R.id.custom_reason);
        otherReason = (RadioButton) rejectDialog.findViewById(R.id.otherReason);

        // Handle button clicks
        reasons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setReason(checkedId);
            }
        });
        DialogInterface.OnClickListener dialogClickListener = new  DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        reject(order);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        builder.setView(rejectDialog)
                .setPositiveButton("REJECT", dialogClickListener)
                .setNegativeButton("CANCEL", dialogClickListener)
                .setMessage("Reject order").show();

    }

    private void reject(Order order) {
        if(customReason.getVisibility() == View.VISIBLE)
            reason = customReason.getText().toString().trim();
        if(reason.isEmpty()) {
            rejectOrder(order);
            Toast.makeText(context, "Choose a reason", Toast.LENGTH_SHORT).show();
            return;
        }

        ordersRef.child(order.getId()).child("status").setValue(Order.orderStatus.REJECTED).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Order cancelled successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Order failed to cancel"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        /*
         * Store reason for canceling order
         * */
        DatabaseReference reasonRef = FirebaseDatabase.getInstance().getReference("reasons");
        String id = reasonRef.push().getKey();
        Date now = new Date();
        Reason cause = new Reason(order.getId(), order.getClient(), order.getSeller(), reason, id, Order.orderStatus.REJECTED, now.getTime());
        reasonRef.child(id).setValue(cause);
    }

    private void setReason(int checkedId) {
        customReason.setVisibility(View.GONE);
        switch (checkedId) {
            case R.id.otherReason:
                if(customReason.getVisibility() == View.GONE)
                    customReason.setVisibility(View.VISIBLE);
                break;
            case R.id.stock_rb:
                reason = "Out of stock";
                break;
            case R.id.too_far_rb:
                reason = "Too far to deliver";
                break;
            case R.id.spammer_rb:
                reason = "Spammer";
                break;
            default:
        }
    }
}

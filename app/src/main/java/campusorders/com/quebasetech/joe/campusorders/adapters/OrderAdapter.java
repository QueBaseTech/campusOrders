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

import java.util.List;

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.model.Order;


public class OrderAdapter extends ArrayAdapter<Order> {
    private  final Context context;
    private List<Order> orderList;
    private DatabaseReference ordersRef;

    public OrderAdapter(@NonNull Context context, List<Order> orders) {
        super(context, R.layout.orders_list, orders);
        this.context = context;
        this.orderList = orders;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.orders_list,  parent, false);
        // Populate view with content
        TextView location = (TextView) view.findViewById(R.id.item_name);
        TextView qtyLabel = (TextView) view.findViewById(R.id.item_unit);
        TextView price = (TextView) view.findViewById(R.id.order_value);
        TextView buyer = (TextView) view.findViewById(R.id.buyer_name);
        Button reject = (Button) view.findViewById(R.id.rejectOrderBtn);
        Button deliver = (Button) view.findViewById(R.id.deliverOrderBtn);
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        final Order order = orderList.get(position);
        location.setText(order.getLocation());
        qtyLabel.setText(""+order.getQty());
        price.setText(""+order.getTotal());
        buyer.setText(order.getClient());

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectOrder(order);
            }
        });

        return view;
    }

    private void rejectOrder(final Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rejectDialog = inflater.inflate(R.layout.reject_order_dialog, null);

        RadioGroup reasons = (RadioGroup) rejectDialog.findViewById(R.id.reject_radio_group);
        final EditText customReason = (EditText) rejectDialog.findViewById(R.id.custom_reason);
        RadioButton otherReason = (RadioButton) rejectDialog.findViewById(R.id.otherReason);

        // Handle button clicks
        reasons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                customReason.setVisibility(View.GONE);
                switch (checkedId) {
                    case R.id.otherReason:
                        if(customReason.getVisibility() == View.GONE)
                            customReason.setVisibility(View.VISIBLE);
                        break;
                    default:

                }
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
        // TODO:: Implement a collection of reject reasons and store associated orders for analysis
        ordersRef.child(order.getId()).child("status").setValue(Order.orderStatus.REJECTED).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //TODO:: Update UI or initiate notificatio to user
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO:: Display relevant information
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

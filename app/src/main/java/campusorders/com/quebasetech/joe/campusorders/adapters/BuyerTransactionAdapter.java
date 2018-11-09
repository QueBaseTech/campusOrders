/*
 * Copyright (c) 2018.  Que Base Technologies
 *
 * Joe Nyugoh 24/ 10/ 2018.
 * MIT License
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package campusorders.com.quebasetech.joe.campusorders.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.model.Order;
import campusorders.com.quebasetech.joe.campusorders.model.Reason;
import campusorders.com.quebasetech.joe.campusorders.utils.utils;

public class BuyerTransactionAdapter extends ArrayAdapter<Order> {
    private  final Context context;
    private List<Order> orderList;
    private DatabaseReference ordersRef;
    private HashMap sellers;
    private HashMap gigs;
    private HashMap reasons;
    private EditText customReason;
    private String reason;

    public BuyerTransactionAdapter(@NonNull Context context, List<Order> orders, HashMap gigs, HashMap sellers, HashMap reasons) {
        super(context, R.layout.buyer_orders, orders);
        this.context = context;
        this.orderList = orders;
        this.sellers = sellers;
        this.reasons = reasons;
        this.gigs = gigs;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.buyer_orders, parent, false);

        TextView qty = (TextView) view.findViewById(R.id.order_qty);
        TextView item = (TextView) view.findViewById(R.id.order_item);
        TextView location = (TextView) view.findViewById(R.id.order_location);
        TextView date = (TextView) view.findViewById(R.id.order_date);
        TextView status = (TextView) view.findViewById(R.id.order_status);
        TextView seller = (TextView) view.findViewById(R.id.textView24);
        TextView total = (TextView) view.findViewById(R.id.textView22);
        TextView reason = (TextView) view.findViewById(R.id.reason);
        final Button button = (Button) view.findViewById(R.id.button);

        final Order order = orderList.get(position);
        qty.setText(""+order.getQty()+",");
        item.setText(gigs.get(order.getGigId()).toString());
        location.setText(order.getLocation());
        date.setText(utils.getElapsedTime(order.getOrderTime()));
        status.setText(""+order.getStatus());
        seller.setText(sellers.get(order.getSeller()).toString());
        total.setText(""+order.getTotal());

        ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.equals(button.getText().toString().toUpperCase(), "CANCEL".toUpperCase())) {
                    cancelOrder(order);
                }
            }
        });

        if(order.getStatus() == Order.orderStatus.FULFILLED) {
            button.setVisibility(View.GONE);
            view.setBackgroundColor(context.getResources().getColor(R.color.completeOrderBackground));
        }

        if(order.getStatus() == Order.orderStatus.NEW) {
            view.setBackgroundColor(context.getResources().getColor(R.color.colorNewOrder));
        }

        if(order.getStatus() == Order.orderStatus.PENDING) {
            view.setBackgroundColor(context.getResources().getColor(R.color.pending_order_background_color));
        }

        if(order.getStatus() == Order.orderStatus.REJECTED || order.getStatus() == Order.orderStatus.CANCELLED) {
            button.setVisibility(View.GONE);
            reason.setVisibility(View.VISIBLE);
            reason.setText(""+(reasons.get(order.getId()) == null? "": reasons.get(order.getId())));
            view.setBackgroundColor(context.getResources().getColor(R.color.colorRejectedOrder));
        }
        return view;
    }

    private void cancelOrder(final Order order) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View cancelDialog = inflater.inflate(R.layout.cancel_order_dialog, null);

        RadioGroup reasons = (RadioGroup) cancelDialog.findViewById(R.id.cancel_radio_group);
        customReason = (EditText) cancelDialog.findViewById(R.id.custom_cancel_reason);
        RadioButton otherReason = (RadioButton) cancelDialog.findViewById(R.id.cancel_other_reason_rb);
        reason = "";
        // Handle button clicks
        reasons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setReason(checkedId);
            }
        });
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        performCancel(order);
                }
            }
        };
        builder.setView(cancelDialog)
                .setPositiveButton("CANCEL", onClickListener)
                .setNegativeButton("Don't Cancel", onClickListener)
                .show();

    }

    private void setReason(int checkedId) {
        customReason.setVisibility(View.GONE);
        switch (checkedId) {
            case R.id.cancel_other_reason_rb:
                if(customReason.getVisibility() == View.GONE)
                    customReason.setVisibility(View.VISIBLE);
                break;
            case R.id.high_price:
                reason = "High price";
                break;
            case R.id.not_responding:
                reason = "Took too long to respond";
                break;
            case R.id.changed_mind_rb:
                reason = "Changed my mind";
                break;
            default:
        }
    }

    private void performCancel(final Order order) {
        if(customReason.getVisibility() == View.VISIBLE)
            reason = customReason.getText().toString().trim();
        if(reason.isEmpty()) {
            cancelOrder(order);
            Toast.makeText(context, "Choose a reason", Toast.LENGTH_SHORT).show();
            return;
        }

        ordersRef.child(order.getId()).child("status").setValue(Order.orderStatus.CANCELLED).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Order cancelled successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Order failed to cancel", Toast.LENGTH_SHORT).show();
            }
        });

        /*
        * Store reason for canceling order
        * */
        DatabaseReference reasonRef = FirebaseDatabase.getInstance().getReference("reasons");
        String id = reasonRef.push().getKey();
        Date now = new Date();
        Reason cause = new Reason(order.getId(), order.getClient(), order.getSeller(), reason, id, Order.orderStatus.CANCELLED, now.getTime());
        reasonRef.child(id).setValue(cause);
    }
}

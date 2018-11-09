/*
 * Copyright (c) 2018.  Que Base Technologies
 *
 * Joe Nyugoh 28/ 10/ 2018.
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

package campusorders.com.quebasetech.joe.campusorders.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.adapters.OrderAdapter;
import campusorders.com.quebasetech.joe.campusorders.model.Gig;
import campusorders.com.quebasetech.joe.campusorders.model.Order;
import campusorders.com.quebasetech.joe.campusorders.model.Reason;
import campusorders.com.quebasetech.joe.campusorders.model.User;
import campusorders.com.quebasetech.joe.campusorders.utils.utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class RejectedOrders extends Fragment {
    private ListView ordersList;
    private Context context;
    private DatabaseReference ordersRefs, usersRefs, reasonsRef;
    private List<Order> orders;
    private HashMap clients, gigs, reasons;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    public RejectedOrders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rejected_orders, container, false);
        orders = new ArrayList<>();
        clients = new HashMap();
        gigs = new HashMap();
        reasons = new HashMap();
        ordersList = (ListView) view.findViewById(R.id.orders);
        final TextView notice = (TextView) view.findViewById(R.id.no_rejected_orders);
        final ProgressBar loading = (ProgressBar) view.findViewById(R.id.loading_rejected_orders);
        DatabaseReference gigsRef = FirebaseDatabase.getInstance().getReference("gigs");
        ordersRefs = FirebaseDatabase.getInstance().getReference("orders");
        usersRefs = FirebaseDatabase.getInstance().getReference("users");
        reasonsRef = FirebaseDatabase.getInstance().getReference("reasons");

        reasonsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot reason: dataSnapshot.getChildren()) {
                    Reason res = reason.getValue(Reason.class);
                    reasons.put(res.getOrderId(), res.getReason());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        usersRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot user: dataSnapshot.getChildren()) {
                    User client = user.getValue(User.class);
                    clients.put(client.getId(), client.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        gigsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot gig: dataSnapshot.getChildren()){
                    Gig g = gig.getValue(Gig.class);
                    gigs.put(g.getId(), g.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Query yourOrders = ordersRefs.orderByChild("seller").equalTo(context.getSharedPreferences(utils.CURRENT_USER, Context.MODE_PRIVATE).getString(utils.USER_ID, ""));
        yourOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders.clear();
                for(DataSnapshot orderSnapshot: dataSnapshot.getChildren()){
                    Order order = orderSnapshot.getValue(Order.class);
                    if(order.getStatus() == Order.orderStatus.REJECTED)
                        orders.add(order);
                    OrderAdapter adapter = new OrderAdapter(context, orders, clients, gigs, reasons);
                    ordersList.setAdapter(adapter);
                }
                Collections.reverse(orders);
                // Hide loading bar
                loading.setVisibility(View.GONE);
                if(orders.isEmpty())
                    notice.setVisibility(View.VISIBLE);
                else
                    notice.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Error :"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}

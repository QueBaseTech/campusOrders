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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.model.Order;

public class BuyerTransactionAdapter extends ArrayAdapter<Order> {
    private  final Context context;
    private List<Order> orderList;
    private DatabaseReference ordersRef;
    private HashMap sellers;
    private HashMap gigs;

    public BuyerTransactionAdapter(@NonNull Context context, List<Order> orders, HashMap gigs, HashMap sellers) {
        super(context, R.layout.buyer_orders, orders);
        this.context = context;
        this.orderList = orders;
        this.sellers = sellers;
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
        Button button = (Button) view.findViewById(R.id.button);

        Order order = orderList.get(position);
        qty.setText(""+order.getQty()+",");
        item.setText(gigs.get(order.getGigId()).toString());
        location.setText(order.getLocation());
        Date orderTime = new Date(order.getOrderTime());
        SimpleDateFormat format = new SimpleDateFormat("E dd/MM/yy,  hh:mm a");
        date.setText(format.format(orderTime));
        status.setText(""+order.getStatus());
        seller.setText(sellers.get(order.getSeller()).toString());
        total.setText(""+order.getTotal());
        return view;
    }
}

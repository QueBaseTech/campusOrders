/*
 * Copyright (c) 2018.  Que Base Technologies
 *
 * Joe Nyugoh 30/ 10/ 2018.
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
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.model.OrderStatistics;

public class OrderStatsAdapter extends ArrayAdapter {
    private List<OrderStatistics> stats;
    private TextView name, NEW, pending, complete, rejected, cancelled, total;
    public OrderStatsAdapter(@NonNull Context context, List<OrderStatistics> stats) {
        super(context, R.layout.order_statistics, stats);
        this.stats = stats;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View statRow = inflater.inflate(R.layout.order_statistics, parent, false);
        name = (TextView) statRow.findViewById(R.id.gig_name);
        NEW = (TextView) statRow.findViewById(R.id.new_orders);
        pending = (TextView) statRow.findViewById(R.id.pending_orders);
        complete = (TextView) statRow.findViewById(R.id.complete_orders);
        rejected = (TextView) statRow.findViewById(R.id.rejected_orders);
        cancelled = (TextView) statRow.findViewById(R.id.cancelled_orders);
        total = (TextView) statRow.findViewById(R.id.total_order);

      OrderStatistics orderStatistics = stats.get(position);
        name.setText(orderStatistics.getName());
        NEW.setText(""+orderStatistics.getNEW());
        pending.setText(""+orderStatistics.getPending());
        complete.setText(""+orderStatistics.getDelivered());
        rejected.setText(""+orderStatistics.getRejected());
        cancelled.setText(""+orderStatistics.getCancelled());
        total.setText(""+orderStatistics.getTotal());
        return statRow;
    }
}

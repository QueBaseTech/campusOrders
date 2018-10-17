package campusorders.com.quebasetech.joe.campusorders.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import campusorders.com.quebasetech.joe.campusorders.R;

public class GigsAdapater extends ArrayAdapter<String> {
    private  final Context context;
    private final String[] items;
    private final Double[] prices;

    public GigsAdapater(@NonNull Context context, String[] items, Double[] prices) {
        super(context, R.layout.items_list, items);
        this.context = context;
        this.items = items;
        this.prices = prices;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.gigs_listing, parent, false);
        TextView itemName = (TextView) view.findViewById(R.id.item_name);
        TextView itemPrice = (TextView) view.findViewById(R.id.item_price);
        itemName.setText(items[position]);
        itemPrice.setText(""+prices[position]);
        return view;
    }
}

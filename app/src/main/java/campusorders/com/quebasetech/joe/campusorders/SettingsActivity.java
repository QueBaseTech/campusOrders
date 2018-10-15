package campusorders.com.quebasetech.joe.campusorders;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private Boolean isBuyer;
    private Context context;
    private Switch profileSWitch;
    private TextView buyerTextView, sellerTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        context = this;
        initMemory();
        setupEvents();
    }

    private void initMemory() {
        profileSWitch = (Switch) findViewById(R.id.profile_switch);
        buyerTextView = (TextView) findViewById(R.id.buyer_label);
        sellerTextView = (TextView) findViewById(R.id.seller_label);

        //Fetch shared prefs and set isBuyer
        isBuyer = true;
        switchProfile();
    }

    private void setupEvents() {
        profileSWitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isBuyer = !isChecked;
                switchProfile();
            }
        });
    }

    protected void switchProfile() {
        // Change UI to appropriate one
        if(isBuyer) {
            buyerTextView.setTextColor(getResources().getColor(R.color.colorOrange));
//            buyerTextView.setTextSize(18);
            Toast.makeText(context, "Switched profile to buyer", Toast.LENGTH_SHORT).show();;
            profileSWitch.setChecked(false);
            sellerTextView.setTextColor(getResources().getColor(R.color.colorGrey));
//            sellerTextView.setTextSize(14);
        } else {
            sellerTextView.setTextColor(getResources().getColor(R.color.colorOrange));
//            sellerTextView.setTextSize(18);
            Toast.makeText(context, "Switched profile to seller", Toast.LENGTH_SHORT).show();;
            profileSWitch.setChecked(true);
            buyerTextView.setTextColor(getResources().getColor(R.color.colorGrey));
//            buyerTextView.setTextSize(14);
        }
    }
}

package campusorders.com.quebasetech.joe.campusorders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Settings_Profile extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private boolean isBuyer;
    private Switch profileSwitch;
    public static final String IS_BUYER = "isBuyer";
    public static final String PROFILE_SETTINGS = "ProfileSettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_profile);

        initApp();
        setupEvents();
    }

    private void initApp() {
        preferences = getSharedPreferences(PROFILE_SETTINGS, MODE_PRIVATE);
        editor = preferences.edit();

        // Default profile is buyer
        isBuyer = preferences.getBoolean(IS_BUYER, true);
        profileSwitch = (Switch) findViewById(R.id.profile_switch);
        profileSwitch.setChecked(isBuyer);
    }

    private void setupEvents() {
        profileSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isBuyer = isChecked;
                editor.putBoolean(IS_BUYER, isChecked);
                editor.commit();
                changeProfile();
            }
        });
    }

    // Implement the switch in profile
    private void changeProfile() {
        startActivity(new Intent(context, BuyerHome.class));
        finish();
    }
}

package campusorders.com.quebasetech.joe.campusorders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class Settings_Profile extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;
    public static final String PROFILE_SETTINGS = "ProfileSettings";
    private TextView help, about, account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_profile);

        context = this;
        initApp();
        setupEvents();
    }

    private void initApp() {
        help = (TextView) findViewById(R.id.settings_help);
        about = (TextView) findViewById(R.id.settings_about);
        account = (TextView) findViewById(R.id.settings_account);
    }

    private void setupEvents() {
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, HelpPage.class));
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AboutPage.class));
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AccountInfo.class));
            }
        });
    }
}

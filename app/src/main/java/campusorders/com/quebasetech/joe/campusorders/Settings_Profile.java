package campusorders.com.quebasetech.joe.campusorders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;

public class Settings_Profile extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;
    public static final String PROFILE_SETTINGS = "ProfileSettings";
    private TextView help, about, account, shareApp;
    public static final int REQUEST_INVITE = 635;
    public static final String TAG = "campus.orders.com";

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
        shareApp = (TextView) findViewById(R.id.share_app);
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

        shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitationMessage))
                        .setMessage("Make your life easier making orders right from your phone using Campus Orders")
                        .setDeepLink(Uri.parse("bit.ly/campusOrders"))
                        .setCallToActionText("Share")
                        .build();
                startActivityForResult(intent, REQUEST_INVITE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                Toast.makeText(context, "Error sharing app", Toast.LENGTH_LONG).show();
            }
        }
    }
}

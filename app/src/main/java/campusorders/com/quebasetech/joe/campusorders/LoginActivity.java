package campusorders.com.quebasetech.joe.campusorders;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = (Button) findViewById(R.id.login_button);
    }

    public void showHome(View view) {
        // TODO:: remove this and add actual login auth
        Intent home = new Intent(getApplicationContext(), BuyerHome.class);
        startActivity(home);
    }
}

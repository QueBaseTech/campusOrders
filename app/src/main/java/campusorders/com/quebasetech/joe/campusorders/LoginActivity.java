package campusorders.com.quebasetech.joe.campusorders;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    /*private FirebaseAuth mAuth;
    private EditText mEmail;
    private EditText mPassword;
    private Button loginBtn, registerButton;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*loginBtn = (Button) findViewById(R.id.login_button);
        registerButton = (Button) findViewById(R.id.login_button);
        mEmail = (EditText) findViewById(R.id.password_field);
        mPassword = (EditText) findViewById(R.id.email_field);
        mAuth = FirebaseAuth.getInstance();*/
    }

    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    public void registerUser(View view) {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "All fields are required.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }*/

    public void showHome(View view) {
        // TODO:: remove this and add actual login auth
        Intent home = new Intent(getApplicationContext(), BuyerHome.class);
        startActivity(home);
    }
}

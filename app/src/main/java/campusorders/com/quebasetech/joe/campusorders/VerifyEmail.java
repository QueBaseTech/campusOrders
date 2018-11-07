/*
 * Copyright (c) 2018.  Que Base Technologies
 *
 * Joe Nyugoh 7/ 11/ 2018.
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

package campusorders.com.quebasetech.joe.campusorders;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import campusorders.com.quebasetech.joe.campusorders.utils.utils;

public class VerifyEmail extends AppCompatActivity {

    private static final String TAG = "VerifyEMail";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Button mVerifyButton;
    private Button mLogoutButton;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

         mVerifyButton = (Button) findViewById(R.id.verify_account_button);
         mLogoutButton = (Button) findViewById(R.id.logout_btn);
         mLoginButton = (Button) findViewById(R.id.go_home_button);

         mAuth = FirebaseAuth.getInstance();
         mUser = mAuth.getCurrentUser();
         mVerifyButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mVerifyButton.setEnabled(false);
                 mUser.sendEmailVerification()
                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                 if (task.isSuccessful()) {
                                     Toast.makeText(VerifyEmail.this, "Verification email sent to " + mUser.getEmail(), Toast.LENGTH_SHORT).show();
                                     Toast.makeText(VerifyEmail.this, "Verify and come back here to login", Toast.LENGTH_LONG).show();
                                 } else {
                                     Log.e(TAG, "sendEmailVerification", task.getException());
                                     Toast.makeText(VerifyEmail.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                 }
                                 mVerifyButton.setEnabled(true);
                             }
                         });
             }
         });

         mLogoutButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 utils.logout(VerifyEmail.this);
                 finish();
             }
         });

         mLoginButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(VerifyEmail.this, CampusOrders.class));
                 finish();
             }
         });
    }
}

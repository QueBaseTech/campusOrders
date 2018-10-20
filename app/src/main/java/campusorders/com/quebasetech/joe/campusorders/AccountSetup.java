/*
 * Copyright (c) 2018.  Que Base Technologies
 *
 * Joe Nyugoh 20/ 10/ 2018.
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

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import campusorders.com.quebasetech.joe.campusorders.model.User;
import campusorders.com.quebasetech.joe.campusorders.utils.utils;

public class AccountSetup extends AppCompatActivity {
    private Button mCompleteButton;
    private EditText mName, mPhoneNumber, mHostel, mRoom;
    private CheckBox mContestCheckbox;
    private FirebaseUser currentUser;
    DatabaseReference usersRef;
    Context context;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup);

        context = this;
        initMemory();
        setupEvents();
    }

    private void initMemory() {
        mCompleteButton = (Button) findViewById(R.id.save_button);
        mName = (EditText) findViewById(R.id.full_name);
        mPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mHostel = (EditText) findViewById(R.id.location_hostel);
        mRoom = (EditText) findViewById(R.id.location_room);
        mContestCheckbox = (CheckBox) findViewById(R.id.consent_check);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        progressDialog = new ProgressDialog(context);
    }

    private void setupEvents() {
        mCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString().trim();
                String hostel = mHostel.getText().toString().trim();
                String room = mRoom.getText().toString().trim();
                String phoneNumber = mPhoneNumber.getText().toString().trim();
                String id;

                if(!mContestCheckbox.isChecked()) {
                    mContestCheckbox.setError("You must agree to terms before continuing.");
                    mContestCheckbox.setTextColor(getResources().getColor(R.color.colorOrange));
                    return;
                }
                progressDialog.setMessage("Setting up account...");
                progressDialog.show();
                if(name.isEmpty() || hostel.isEmpty() || room.isEmpty() || phoneNumber.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(context, "All Fields are Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                // generate a user id
                id = usersRef.push().getKey();
                // get user email
                String email = utils.getCurrentUserEmail();
                if(email.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(context, "Error setting your account try again", Toast.LENGTH_SHORT).show();
                    return;
                }
//                String location = String.join(" ", hostel, room);
                String location = hostel + " " + room;
                User user = new User(id, name, email, phoneNumber, location);
                usersRef.child(user.getId())
                        .setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Successfully added", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Error setting your account try again: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}

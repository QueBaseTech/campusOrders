/*
 * Copyright (c) 2018.  Que Base Technologies
 *
 * Joe Nyugoh 23/ 10/ 2018.
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

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import campusorders.com.quebasetech.joe.campusorders.utils.utils;

public class AccountInfo extends AppCompatActivity {
    private TextView name, phoneNumber, hostel, room, email;
    private SharedPreferences userInfo;
    private SharedPreferences.Editor editor;
    private ImageView editName, editPhone, editLocation;
    Context context;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        context =  this;
        initMemory();
        setupEvents();
    }

    private void initMemory() {
        name = (TextView) findViewById(R.id.fullName);
        phoneNumber = (TextView) findViewById(R.id.phoneNumber);
        hostel = (TextView) findViewById(R.id.hostel_name);
        room = (TextView) findViewById(R.id.room);
        email = (TextView) findViewById(R.id.your_email);
        editName = (ImageView) findViewById(R.id.edit_name);
        editPhone = (ImageView) findViewById(R.id.edit_phone_number);
        editLocation = (ImageView) findViewById(R.id.edit_hostel);
        userInfo = getSharedPreferences(utils.CURRENT_USER, MODE_PRIVATE);
        editor = userInfo.edit();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        name.setText(userInfo.getString(utils.USER_NAME, "Not set"));
        phoneNumber.setText(userInfo.getString(utils.USER_PHONE, "Not set"));
        processHostel(userInfo.getString(utils.USER_LOCATION, "Not set"));
        hostel.setText(userInfo.getString(utils.USER_LOCATION, ""));
        email.setText(utils.getCurrentUserEmail());
    }

    private void setupEvents() {
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View editNameDialog = inflater.inflate(R.layout.edit_name_dialog, null);
                final TextView name = (TextView) editNameDialog.findViewById(R.id.user_full_name);
                name.setText(userInfo.getString(utils.USER_NAME, ""));

                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                            case DialogInterface.BUTTON_POSITIVE:
                                saveName(name);
                        }
                    }
                };
                builder.setView(editNameDialog)
                        .setNegativeButton("Cancel", onClickListener)
                        .setPositiveButton("Save", onClickListener)
                        .show();
            }
        });

        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View editNameDialog = inflater.inflate(R.layout.edit_name_dialog, null);
                final TextView phoneNumber = (TextView) editNameDialog.findViewById(R.id.user_full_name);
                phoneNumber.setText(userInfo.getString(utils.USER_PHONE, ""));
                phoneNumber.setHint("Enter a phone number");

                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                            case DialogInterface.BUTTON_POSITIVE:
                                saveNumber(phoneNumber);
                        }
                    }
                };
                builder.setView(editNameDialog)
                        .setNegativeButton("Cancel", onClickListener)
                        .setPositiveButton("Save", onClickListener)
                        .show();
            }
        });

        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View editNameDialog = inflater.inflate(R.layout.edit_location_dialog, null);
                final TextView hostelName = (TextView) editNameDialog.findViewById(R.id.hostel_name);
                final TextView roomNumber = (TextView) editNameDialog.findViewById(R.id.room_number);

                String location = userInfo.getString(utils.USER_LOCATION, "Pirate");
                String[] locs = location.split(" ");
                if(locs.length == 1 || locs.length >= 5)
                    hostelName.setText(location);
                if(locs.length == 2){
                    hostelName.setText(locs[0]);
                    roomNumber.setText(locs[1]);
                }
                if(locs.length == 3) {
                    hostelName.setText(locs[0]+" "+locs[1]);
                    roomNumber.setText(locs[2]);
                }
                if(locs.length == 4) {
                    hostelName.setText(locs[0]+" "+locs[1]);
                    roomNumber.setText(locs[2]+" "+locs[3]);
                }

                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                            case DialogInterface.BUTTON_POSITIVE:
                                saveLocation(hostelName, roomNumber);
                        }
                    }
                };
                builder.setView(editNameDialog)
                        .setNegativeButton("Cancel", onClickListener)
                        .setPositiveButton("Save", onClickListener)
                        .show();
            }
        });
    }

    private void processHostel(String location) {
        if(TextUtils.isEmpty(location)) {
            hostel.setText("Not set");
            return;
        }
        String[] locs = location.split(" ");
        if(locs.length == 1 || locs.length >= 5)
            hostel.setText(location);
        if(locs.length == 2){
            hostel.setText(locs[0]);
            room.setText(locs[1]);
        }
        if(locs.length == 3) {
            hostel.setText(locs[0]+" "+ locs[1]);
            room.setText(locs[1]+" "+locs[2]);
        }
        if(locs.length == 4) {
            hostel.setText(locs[0]+" "+locs[1]);
            room.setText(locs[2]+" "+locs[3]);
        }
    }

    private void saveName(TextView newName) {
        String username = newName.getText().toString();
        if(TextUtils.isEmpty(username.trim())){
            Toast.makeText(context, "Enter a name please", Toast.LENGTH_SHORT).show();
            return;
        }
        String id = userInfo.getString(utils.USER_ID, "");
        userRef.child(id).child("name").setValue(username);
        editor.putString(utils.USER_NAME, username).commit();
        name.setText(username);
    }

    private void saveNumber(TextView newNumber) {
        String phone = newNumber.getText().toString();
        //TODO:: Check for a valid number
        if(TextUtils.isEmpty(phone.trim())){
            Toast.makeText(context, "Enter a phone number please", Toast.LENGTH_SHORT).show();
            return;
        }
        String id = userInfo.getString(utils.USER_ID, "");
        userRef.child(id).child("phoneNumber").setValue(phone);
        editor.putString(utils.USER_PHONE, phone).commit();
        phoneNumber.setText(phone);
    }

    private void saveLocation(TextView hostelName, TextView roomNumber) {
        String hostel_name = hostelName.getText().toString();
        String room_number = roomNumber.getText().toString();
        if(TextUtils.isEmpty(hostel_name.trim())){
            Toast.makeText(context, "Enter a phone number please", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(hostel_name.trim()) && TextUtils.isEmpty(room_number.trim())){
            Toast.makeText(context, "Enter a phone number please", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = userInfo.getString(utils.USER_ID, "");
        userRef.child(id).child("location").setValue(hostel_name+" "+room_number);
        editor.putString(utils.USER_LOCATION, hostel_name+" "+room_number).commit();
        hostel.setText(hostel_name);
        room.setText(room_number);
    }
}

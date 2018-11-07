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

package campusorders.com.quebasetech.joe.campusorders.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import campusorders.com.quebasetech.joe.campusorders.AccountSetup;
import campusorders.com.quebasetech.joe.campusorders.CampusOrders;
import campusorders.com.quebasetech.joe.campusorders.Settings_Profile;
import campusorders.com.quebasetech.joe.campusorders.model.User;

public class utils extends AppCompatActivity{
    public static final String CURRENT_USER = "CURRENT_USER";
    public static final String USER_ID = "USER_ID";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_LOCATION = "USER_LOCATION";
    public static final String USER_PHONE = "USER_PHONE";
    public static final String USER_ISBUYER = "USER_ISBUYER";


    /**
     * Returns the current user email
     * @return String or empty string
     */
    public static String getCurrentUserEmail() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        assert firebaseUser != null;
        if(firebaseUser.getEmail() != null)
            return firebaseUser.getEmail();
        else
            return "";
    }

    /*
    * Return current user profile
    * Defaults to buyer
    * */
    public static boolean isBuyer(Context context) {
        SharedPreferences mPreferences = context.getSharedPreferences(CURRENT_USER, MODE_PRIVATE);
        return mPreferences.getBoolean(USER_ISBUYER, true);
    }

    /*
    * Return true if user is logged in
    * */
    public static boolean isUserLoggedIn() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        return firebaseUser != null;
    }

    /**
     * Get the current user and persist them to Shared Prefs
     */
    public void addedUserToPrefs() {
        final SharedPreferences userPrefs = getSharedPreferences(CURRENT_USER, MODE_PRIVATE);
        final SharedPreferences.Editor editor = userPrefs.edit();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        Query userRef = usersRef.orderByChild("email").equalTo(getCurrentUserEmail());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if they have setup a/c details
                if(!dataSnapshot.exists()){
                    //Take user to account setup
                    startActivity(new Intent(getApplicationContext(), AccountSetup.class));
                    return;
                }
                // Get their profile
                User currentUser = dataSnapshot.getValue(User.class);
                editor.clear(); // Remove any stored prefs
                editor.putString(USER_ID, currentUser.getId());
                editor.putString(USER_NAME, currentUser.getName());
                editor.putString(USER_LOCATION, currentUser.getLocation());
                editor.putString(USER_PHONE, currentUser.getPhoneNumber());
                editor.putBoolean(USER_ISBUYER, currentUser.isBuyer());
                editor.commit();// Save all
                Intent home = new Intent(getApplicationContext(), CampusOrders.class);
                startActivity(home);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(utils.this, "Prefs error: "+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

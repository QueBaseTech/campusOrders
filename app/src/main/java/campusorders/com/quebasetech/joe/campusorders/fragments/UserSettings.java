package campusorders.com.quebasetech.joe.campusorders.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import campusorders.com.quebasetech.joe.campusorders.R;

public class UserSettings extends Fragment {
    private Boolean isBuyer;
    private Context context;
    private Switch profileSWitch;
    private TextView buyerTextView, sellerTextView;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_profile, container, false);
        initMemory();
        setupEvents();
        return view;
    }

    private void initMemory() {
        profileSWitch = (Switch) view.findViewById(R.id.profile_switch);
        buyerTextView = (TextView) view.findViewById(R.id.seller_label);
        sellerTextView = (TextView) view.findViewById(R.id.seller_label);

        //Fetch shared prefs and set isBuyer
        isBuyer = true;
        switchProfile();
    }

    private void setupEvents() {
        
    }

    protected void switchProfile() {
        if(isBuyer) {
            buyerTextView.setTextColor(getResources().getColor(R.color.colorOrange));
            sellerTextView.setTextColor(getResources().getColor(R.color.colorGrey));
            profileSWitch.setChecked(false);
        } else {
            sellerTextView.setTextColor(getResources().getColor(R.color.colorOrange));
            buyerTextView.setTextColor(getResources().getColor(R.color.colorGrey));
            profileSWitch.setChecked(true);
        }
    }
}

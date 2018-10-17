package campusorders.com.quebasetech.joe.campusorders.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import campusorders.com.quebasetech.joe.campusorders.R;

public class AddItemDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View addDialog = inflater.inflate(R.layout.add_item_dialog, null);

        Button cancelBtn = (Button) addDialog.findViewById(R.id.add_item_cancel_btn);
        Button addBtn = (Button) addDialog.findViewById(R.id.add_item_btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO:: Save item to database
                Toast.makeText(getContext(), "Item name:: "+((EditText) addDialog.findViewById(R.id.item_name)).getText().toString(), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        builder.setView(addDialog)
                .setMessage("Add item");

        return builder.create();
    }

}

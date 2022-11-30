package edu.uga.cs.finalproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.List;

public class AddProductDialogFragment extends DialogFragment {

    private EditText itemNameView;

    // This interface will be used to obtain the new job lead from an AlertDialog.
    // A class implementing this interface will handle the new job lead, i.e. store it
    // in Firebase and add it to the RecyclerAdapter.
    public interface AddListItemDialogListener {
        void addListItem(ListItem listItem);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.add_item_dialog,
                getActivity().findViewById(R.id.root));

        //get the view objects in the AlertDialog
        itemNameView = layout.findViewById(R.id.editText);

        //create a new AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle); //maybe use another style later
        //set its view to the one inflated above
        builder.setView(layout);

        // Set the title of the AlertDialog
        builder.setTitle("New List Item");
        // Provide the negative button listener
        builder.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int whichButton) {
               //close the dialog
               dialog.dismiss();
           }
        });

        // Provide the positive button listener
        builder.setPositiveButton( android.R.string.ok, new AddListItemListener());

       // Create the AlertDialog and show it
        return builder.create();
    }

    private class AddListItemListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which ) {

            // get the new list item data from user
            String itemName = itemNameView.getText().toString();

            // create a new ListItem object
            ListItem listItem = new ListItem(itemName);

            // get the Activity's listener to add the new list item
            AddListItemDialogListener listener = (AddListItemDialogListener) getActivity();
            // add the new list item
            listener.addListItem( listItem );

            //close the dialog
            dismiss();
        }
    }

}

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

public class EditProductDialogFragment extends DialogFragment {

    // indicate the type of an edit
    public static final int SAVE = 1;   // update an existing job lead
    public static final int DELETE = 2; // delete an existing job lead

    private EditText itemNameView;

    // have a button for mark as purchased which will ask you to enter the price
    // then it will add that item to the recently purchased list and delete it from item list

    int position;
    String key;
    String itemName;


    // A callback listener interface to finish up the editing of a ListItem.
    // ViewListActivity implements this listener interface, as it will
    // need to update the list of ListItems and also update teh RecyclerAdapter to reflect the
    // changes.
    public interface EditProductDialogListener {
        void updateListItem(int position, ListItem listItem, int action);
    }

    public static EditProductDialogFragment newInstance( int position, String key, String itemName ) {
        EditProductDialogFragment dialog = new EditProductDialogFragment();

        // Supply list item values as an argument
        Bundle args = new Bundle();
        args.putString("key", key);
        args.putInt("position", position);
        args.putString("item", itemName);

        dialog.setArguments( args );

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState ) {
        key = getArguments().getString("key");
        position = getArguments().getInt( "position" );
        itemName = getArguments().getString("item");

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate( R.layout.add_item_dialog, getActivity().findViewById( R.id.root ) );

        itemNameView = layout.findViewById(R.id.editText);

        // Pre-fill the edit texts with the current values for this job lead
        // The user will be able to modify them
        itemNameView.setText( itemName );

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity(), R.style.AlertDialogStyle );
        builder.setView( layout );

        // Set the title of the AlertDialog
        builder.setTitle( "Edit List Item ");

        // The Cancel Button Handler
        builder.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton ){
                dialog.dismiss();
            }
        });

        // The Save Button Handler
        builder.setPositiveButton( "SAVE", new SaveButtonClickListener());

        // The Delete button handler
        builder.setNeutralButton( "DELETE", new DeleteButtonClickListener());

        // AlertDialog only supports three buttons
        // Do recently purchased in the RecyclerView
        // TODO
        // https://stackoverflow.com/questions/36369913/how-to-implement-multi-select-in-recyclerview
        //

        // Create the AlertDialog and show it
        return builder.create();
    }

    private class SaveButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String itemName = itemNameView.getText().toString();
            ListItem listItem = new ListItem( itemName );

            listItem.setKey( key );

            // get the Activity's listener to add the new List Item
            EditProductDialogListener listener = (EditProductDialogFragment.EditProductDialogListener) getActivity();

            // add the new list item
            listener.updateListItem(position, listItem, SAVE);

            //close the dialog
            dialog.dismiss();
        }
    }

    private class DeleteButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            ListItem listItem = new ListItem(itemName);
            listItem.setKey(key);

            // get the Activity's listener to add the new list item
            EditProductDialogFragment.EditProductDialogListener listener = (EditProductDialogFragment.EditProductDialogListener) getActivity();

            // delete the item from the list
            listener.updateListItem(position, listItem, DELETE);

            //close the dialog
            dialog.dismiss();
        }
    }






}

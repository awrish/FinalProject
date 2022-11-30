package edu.uga.cs.finalproject;

import android.app.Dialog;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import java.util.List;

public class AddProductDialogFragment extends DialogFragment {

    private EditText itemName;

    // This interface will be used to obtain the new job lead from an AlertDialog.
    // A class implementing this interface will handle the new job lead, i.e. store it
    // in Firebase and add it to the RecyclerAdapter.
    public interface AddJobLeadDialogListener {
        void addJobLead(ListItem listItem);
    }

}

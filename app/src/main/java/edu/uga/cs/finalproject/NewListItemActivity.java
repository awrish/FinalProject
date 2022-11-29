package edu.uga.cs.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewListItemActivity extends AppCompatActivity {

    public static final String DEBUG_TAG = "NewJobLeadActivity";

    //what do i need to add to list???
    //price, name, current user name
    //have edit texts for all except user name
    //store the user who added item to the list's name somehow in the database with that item

    /**
     * For purchased items, create a seperate table.
     * same steps as list item, but table is named recentlyPurchased
     */

    private EditText productNameView;
    // private EditText price; price is entered when you mark it purchased?

    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_new_list_item);

        Button saveButton = findViewById(R.id.button);
        productNameView = findViewById(R.id.editText1);

        saveButton.setOnClickListener( new ButtonClickListener()) ;

    }


    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick( View v ) {
            String productName = productNameView.getText().toString();
            final ListItem listItem = new ListItem(productName);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("shoppinglist");

            FirebaseAuth user = FirebaseAuth.getInstance();
            String roommate = user.getCurrentUser().getEmail();
            //user.getCurrentUser().getDisplayName() could work

            // First, a call to push() appends a new node to the existing list (one is created
            // if this is done for the first time).  Then, we set the value in the newly created
            // list node to store the new job lead.
            // This listener will be invoked asynchronously, as no need for an AsyncTask, as in
            // the previous apps to maintain job leads.
            myRef.push().setValue( listItem )
                    .addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Show a quick confirmation
                            Toast.makeText(getApplicationContext(), "List Item created for " + roommate,
                                    Toast.LENGTH_SHORT).show();

                            // Clear the EditTexts for next use.
                            productNameView.setText("");
                        }
                    })
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure( @NonNull Exception e ) {
                            Toast.makeText( getApplicationContext(), "Failed to create a List Item for " + roommate,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }




}

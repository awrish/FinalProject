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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * For adding new items to the list from the main screen.
 */
public class NewListItemActivity extends AppCompatActivity {

    public static final String DEBUG_TAG = "NewJobLeadActivity";

    private EditText itemNameView;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_new_list_item);

        itemNameView = findViewById(R.id.editText);

        Button saveButton = findViewById(R.id.button);

        saveButton.setOnClickListener(new ButtonClickListener());
    }

    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String itemName = itemNameView.getText().toString();

            final ListItem listItem = new ListItem(itemName);

            // Add a new element (ListItem) to the list of items in firebase
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("shoppinglist");

            ref.push().setValue( listItem )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // Show quick confirmation
                            Toast.makeText(getApplicationContext(), "List Item created: " + listItem.getProductName(),
                                    Toast.LENGTH_SHORT).show();

                            // Clear editText for next use
                            itemNameView.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to create a list item for: " + listItem.getProductName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }



}

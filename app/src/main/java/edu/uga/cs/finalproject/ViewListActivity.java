package edu.uga.cs.finalproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewListActivity
        extends AppCompatActivity
        implements AddProductDialogFragment.AddListItemDialogListener,
                    EditProductDialogFragment.EditProductDialogListener {


    public static final String DEBUG_TAG = "ReviewJobLeadsActivity";

    private RecyclerView recyclerView;

    private ListItemRecyclerAdapter recyclerAdapter;

    private List<ListItem> productsList;

    private FirebaseDatabase database;

    // TODO: Implement recently purchased
    // https://stackoverflow.com/questions/36369913/how-to-implement-multi-select-in-recyclerview


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(DEBUG_TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

        recyclerView = findViewById(R.id.recyclerView);

        FloatingActionButton floatingButton = findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new AddProductDialogFragment();
                newFragment.show(getSupportFragmentManager(), null);
            }
        });

        // initialize the Job Lead list
        productsList = new ArrayList<ListItem>();

        // use a linear layout manager for the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // the recycler adapter with job leads is empty at first; it will be updated later
        recyclerAdapter = new ListItemRecyclerAdapter(productsList, ViewListActivity.this);
        recyclerView.setAdapter(recyclerAdapter);

        // get a Firebase DB instance reference
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shoppinglist");

        // Set up a listener (event handler) to receive a value for the database reference.
        // This type of listener is called by Firebase once by immediately executing its onDataChange method
        // and then each time the value at Firebase changes.
        //
        // This listener will be invoked asynchronously, hence no need for an AsyncTask class, as in the previous apps
        // to maintain job leads.
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Once we have a DataSnapshot object, we need to iterate over the elements and place them on our job lead list.
                productsList.clear(); // clear the current content; this is inefficient!
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ListItem listItem = postSnapshot.getValue(ListItem.class);
                    listItem.setKey(postSnapshot.getKey());
                    productsList.add(listItem);
                    Log.d(DEBUG_TAG, "ValueEventListener: added: " + listItem);
                    Log.d(DEBUG_TAG, "ValueEventListener: key: " + postSnapshot.getKey());
                }

                Log.d(DEBUG_TAG, "ValueEventListener: notifying recyclerAdapter");
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("ValueEventListener: reading failed: " + databaseError.getMessage());
            }
        });
    }

    // this is our own callback for a AddListItemDialogFragment which adds a new product to the list.
    public void addListItem(ListItem listItem) {
        // add the new list item
        // Add a new element (ListItem) to the list in Firebase.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shoppinglist");

        // First, a call to push() appends a new node to the existing list (one is created
        // if this is done for the first time).  Then, we set the value in the newly created
        // list node to store the new list item.
        // This listener will be invoked asynchronously, as no need for an AsyncTask, as in
        // the previous apps to maintain job leads.
        myRef.push().setValue(listItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // Reposition the RecyclerView to show the ListItem most recently added (as the last item on the list).
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.smoothScrollToPosition(productsList.size() - 1);
                            }
                        });

                        Log.d(DEBUG_TAG, "List Item saved: " + listItem);
                        // Show a quick confirmation
                        Toast.makeText(getApplicationContext(), "List item added named " + listItem.getProductName(),
                                Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to add list item " + listItem.getProductName(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Callback for a DialogFragment which edits an existing ListItem.
    // The edit may be an update or a deletion of this ListItem.
    // It is called from the EditProductDialogFragment
    public void updateListItem(int position, ListItem listItem, int action) {

        if (action == EditProductDialogFragment.SAVE) {
            Log.d(DEBUG_TAG, "Updating List Item at: " + position + "(" + listItem.getProductName() + ")");

            // Update teh recycler view to show the changes
            recyclerAdapter.notifyItemChanged(position);

            // Update list item in firebase
            DatabaseReference ref = database
                    .getReference()
                    .child("shoppinglist")
                    .child(listItem.getKey());

            // This listener call will be invoked asynchronously, no need for Async class
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().setValue(listItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(DEBUG_TAG, "updated list item at: " + position + "(" + listItem.getProductName() + ")");
                            Toast.makeText(getApplicationContext(), "List item updated: " + listItem.getProductName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(DEBUG_TAG, "failed to update list item at: " + position + "(" + listItem.getProductName() + ")");
                    Toast.makeText(getApplicationContext(), "Failed to update " + listItem.getProductName(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (action == EditProductDialogFragment.DELETE) {
            Log.d(DEBUG_TAG, "Deleting list item at: " + position + "(" + listItem.getProductName() + ")");

            // remove the deleted list item from the list
            productsList.remove(position);

            // Update the recycler view to remove the deleted item
            recyclerAdapter.notifyItemRemoved(position);

            // Delete this list item in Firebase
            DatabaseReference ref = database
                    .getReference()
                    .child("shoppinglist")
                    .child(listItem.getKey());

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(DEBUG_TAG, "deleted list item at: " + position + "(" + listItem.getProductName() + ")");
                            Toast.makeText(getApplicationContext(), "List Item deleted: " + listItem.getProductName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(DEBUG_TAG, "Failed to delete list item at: " + position + "(" + listItem.getProductName() + ")");
                    Toast.makeText(getApplicationContext(), "Failed to delete: " + listItem.getProductName(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}

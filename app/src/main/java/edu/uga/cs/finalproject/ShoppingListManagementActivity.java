package edu.uga.cs.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ShoppingListManagementActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "ListActivity";

    private TextView signedInTextView;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_management);
        Log.d( DEBUG_TAG, "ShoppingListManagementActivity.onCreate()" );

        signedInTextView = findViewById( R.id.textView3 );

        Button newListItemButton = findViewById( R.id.button1 );
        Button viewListButton = findViewById( R.id.button2 );

        newListItemButton.setOnClickListener( new newListItemButtonClickListener() );
        viewListButton.setOnClickListener( new viewListButtonClickListener() );


        //newLeadButton.setOnClickListener( new NewLeadButtonClickListener() );
        //reviewLeadsButton.setOnClickListener( new ReviewLeadsButtonClickListener() );

        // Setup a listener for a change in the sign in status (authentication status change)
        // when it is invoked, check if a user is signed in and update the UI text view string,
        // as needed.
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if( currentUser != null ) {
                    // User is signed in
                    Log.d(DEBUG_TAG, "onAuthStateChanged:signed_in:" + currentUser.getUid());
                    String userEmail = currentUser.getEmail();
                    signedInTextView.setText( "Signed in as: " + userEmail );
                } else {
                    // User is signed out
                    Log.d( DEBUG_TAG, "onAuthStateChanged:signed_out" );
                    signedInTextView.setText( "Signed in as: not signed in" );
                }
            }
        });
    }

    private class newListItemButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view ) {
            Intent intent = new Intent(view.getContext(), NewListItemActivity.class );
            view.getContext().startActivity( intent );
        }
    }

    private class viewListButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {
            Intent intent = new Intent(view.getContext(), ViewListActivity.class );
            view.getContext().startActivity(intent);
        }
    }
}

package edu.uga.cs.finalproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShoppingListManagementActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "ListActivity";

    private TextView signedInTextView;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_management);
        Log.d( DEBUG_TAG, "JobLeadManagementActivity.onCreate()" );


    }
}

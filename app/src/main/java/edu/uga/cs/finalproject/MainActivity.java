package edu.uga.cs.finalproject;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode( AppCompatDelegate.MODE_NIGHT_YES );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d( DEBUG_TAG, "FinalProject: MainActivity.onCreate()" );

        Button signInButton = findViewById(R.id.button1);
        Button registerButton = findViewById(R.id.button2);

        signInButton.setOnClickListener( new SignInButtonClickListener() );
        registerButton.setOnClickListener( new RegistrationButtonClickListener() );

    }

    private class RegistrationButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick( View view ) {
            // start the user registration activity
            Intent intent = new Intent(view.getContext(), RegisterActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    private class SignInButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick( View v ) {
            // Here, we are just using email/password sign in.
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build()
            );

            Log.d( DEBUG_TAG, "MainActivity.SignInButtonClickListener: Signing in started" );

            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    // this sets our own theme (color scheme, sizing, etc.) for the AuthUI's appearance
                    .setTheme(R.style.LoginTheme)
                    .build();
            signInLauncher.launch(signInIntent);
        }
    }

    private ActivityResultLauncher<Intent> signInLauncher =
            registerForActivityResult(
                    new FirebaseAuthUIActivityResultContract(),
                    new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                        @Override
                        public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                            onSignInResult(result);
                        }
                    }
            );

    // This method is called once the Firebase sign-in activity (lqunched above) returns (completes).
    // Then, the current (logged-in) Firebase user can be obtained.
    // Subsequently, there is a transition to the JobLeadManagementActivity.
    private void onSignInResult( FirebaseAuthUIAuthenticationResult result ) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            if( response != null ) {
                Log.d( DEBUG_TAG, "MainActivity.onSignInResult: response.getEmail(): " + response.getEmail() );
            }

            //Log.d( DEBUG_TAG, "MainActivity.onSignInResult: Signed in as: " + user.getEmail() );

            // after a successful sign in, start the job leads management activity
            Intent intent = new Intent( this, ShoppingListManagementActivity.class );
            startActivity( intent );
        }
        else {
            Log.d( DEBUG_TAG, "MainActivity.onSignInResult: Failed to sign in" );
            // Sign in failed. If response is null the user canceled the
            Toast.makeText( getApplicationContext(),
                    "Sign in failed",
                    Toast.LENGTH_SHORT).show();
        }
    }


}
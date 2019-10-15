package edu.illinois.cs.cs125.fall2019.mp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;
import java.util.List;

/**
 * LaunchActivity is for the purpose of logging in at the start of the launch of the app.
 */
public class LaunchActivity extends AppCompatActivity {

    /**
     * RC_SIGN_IN is a int constant for logging in.
     */
    private static final int RC_SIGN_IN = 10;

    /**
     * Overrides oncreate to check log in info.
     * @param savedInstanceState is the savedInstanceState.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final Button button = findViewById(R.id.goLogin);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                startLogInIntent();
            }
        });
        if (user == null) { // see below discussion
            startLogInIntent();
        } else {
            startMainActivity();
            finish();
        }
    }

    /**
     * this is just an intent to start the main activity.
     */
    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * this is if the user wanted to start the LogIn attempt.
     */
    private void startLogInIntent() {

        // start login activity for result - see below discussion
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.EmailBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    /**
     * Invoked by the Android system when a request launched by startActivityForResult completes.
     * @param requestCode the request code passed by to startActivityForResult
     * @param resultCode a value indicating how the request finished (e.g. completed or canceled)
     * @param data an Intent containing results (e.g. as a URI or in extras)
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in

                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
    }
}

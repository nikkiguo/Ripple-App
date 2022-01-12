package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener {

    public static final String USERID = "userID";
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefEditor;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    private static final String USERNAME_KEY = "name", USERID_KEY = "user id", USEREMAIL_KEY = "user email";
    public static String name = "", userID = "", userEmail = "";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference loginData = db.collection("Login Data");
    private DocumentReference mDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefEditor = sharedPreferences.edit();
        prefEditor.putString("userID", "");
        prefEditor.apply();

        // [START configure_signin]
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        // [END configure_signin]

        // [START build_client]
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END build_client]

        mAuth = FirebaseAuth.getInstance();

        // [START customize_button]
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
        // [END customize_button]
    }

    @Override
    public void onStart() {
        super.onStart();
        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        // [END on_start_sign_in]
    }

    // [START signIn]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                //
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Check if user exists in database, goes to survey activity
                            if (isNew) {
                                assert user != null;
                                userID = user.getUid();
                                name = user.getDisplayName();
                                userEmail = user.getEmail();
                                prefEditor.putString("userID", userID);
                                prefEditor.apply();
                                Log.d(TAG, userID);
                                mDocRef = db.document("Login Data/" + userID);

                                Map <String, Object> newLoginData = new HashMap<String, Object>();
                                newLoginData.put(USERNAME_KEY, name);
                                newLoginData.put(USERID_KEY, userID);
                                newLoginData.put(USEREMAIL_KEY, userEmail);

                                mDocRef.set(newLoginData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Doc has been shared!");
                                        } else {
                                            Log.d(TAG, "Doc failed!");
                                        }
                                    }
                                });
                                //updateUI(user);
                                toSurvey();
                            } else {
                                updateUI(user);
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                    }
                });
    }




    // [START revokeAccess]
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    // [START updateUI]
    private void updateUI(@Nullable FirebaseUser account) {
        if (account != null) {
            getData();
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            Intent mainIntent = new Intent (this, MainActivity.class);
            startActivity(mainIntent);
        } else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        }
    }
    // [END updateUI]

    // [START toSurvey]
    private void toSurvey() {
        Intent surveyIntent = new Intent (this, WelcomeActivity.class);
        startActivity(surveyIntent);
    }
    // [END toSurvey]

    // [START getData]
    static Map<String, String> getData() {
        return new HashMap<String, String>() {{
            put("Name", name);
            put("User Id", userID);
            put("Email",  userEmail);
        }};
    }
    // [END getData]

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_button) {
            signIn();
        }
    }

}
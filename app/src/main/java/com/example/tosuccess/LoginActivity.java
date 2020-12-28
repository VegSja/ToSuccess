package com.example.tosuccess;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Serializable {

    GoogleSignInClient mGoogleSignInClient;

    int RC_SIGN_IN = 0;

    Logger logger = new Logger();
    API_Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        //Set dimensions of the sign-in button
        SignInButton signInButton = findViewById(R.id.google_sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        //Set click listener for button
        findViewById(R.id.google_sign_in_button).setOnClickListener(this);
    }

    @Override
    protected void onStart(){
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            //THIS IS JUST FOR DEBUGGING!!!!! ---------------------------------
            signOut();

            //Start app
            System.out.println("[LOGGER]: " + "Already logged in");
            startMainActivity(account.getIdToken());
        }
        else if(account == null){
            //Start login
            System.out.println("[LOGGER]: " + "Starting loggin process");
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.google_sign_in_button:
                signIn();
        }
    }

    public void startMainActivity(String idtoken){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("IDToken", idtoken);
        startActivity(intent);
    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut(){
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //Result returned form launching the Intent from signIn();
        if (requestCode == RC_SIGN_IN){
            //The task returned from this call is always completed, no need to attach a listener
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
            //Signed in successfully
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            loginToBackend(idToken);

        }catch (ApiException e){
            //Failed login
            logger.errorMessage("signInResult:failedCode:" + e.getStatusCode());
        }
    }

    public void loginToBackend(final String idToken){
        connection = new API_Connection(this);
        connection.loginRequest(idToken, new API_Connection.VolleyLoginCallBack() {
            @Override
            public void onSuccess(String response) {
                createPopUpMessage("Successfully connected to backend as user");
                logger.loggerMessage("Acccess Token retrieved from backend: " + connection.backendAccessToken);
                //Start main activity
                startMainActivity(idToken);
            }

            @Override
            public void onError(String errorMessage) {
                createPopUpMessage("ERROR: " + errorMessage);
            }
        });
    }
    public void createPopUpMessage(String message){
        //Display pop-up message
        Snackbar popUpMessage = Snackbar.make(this.findViewById(R.id.login), message, Snackbar.LENGTH_SHORT);
        popUpMessage.show();
    }
}

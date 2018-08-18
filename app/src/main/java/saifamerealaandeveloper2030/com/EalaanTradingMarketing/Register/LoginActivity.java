package saifamerealaandeveloper2030.com.EalaanTradingMarketing.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import saifamerealaandeveloper2030.com.EalaanTradingMarketing.Home;
import saifamerealaandeveloper2030.com.EalaanTradingMarketing.Login.MainActivity;
import saifamerealaandeveloper2030.com.EalaanTradingMarketing.PhoneNumber.PhoneNumber;
import saifamerealaandeveloper2030.com.EalaanTradingMarketing.R;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private Button googleButton;
    private Button phoneButton;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private String TAG = "LoginActivity";
    private int RC_SIGN_IN = 9001;
    private LinearLayout loadingProgress;
    private LinearLayout loginScreen;
    long time;

    private TextInputEditText email, password, confirmPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (TextInputEditText) findViewById(R.id.register_email);
        password = (TextInputEditText) findViewById(R.id.register_password);
        confirmPassword = (TextInputEditText) findViewById(R.id.register_confirm_password);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        findViewById(R.id.loginBtn).setOnClickListener(this);


        loadingProgress = (LinearLayout) findViewById(R.id.loadingProgress);
        loadingProgress.setVisibility(View.INVISIBLE);
        loginScreen = (LinearLayout) findViewById(R.id.login_screen);
        //initialize my Firebase Auth (get an instance of it)
        mAuth = FirebaseAuth.getInstance();

        initializeGooglePlusSettings();

        googleButton = (Button) findViewById(R.id.g_plus);
        phoneButton = (Button) findViewById(R.id.phone);

        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, PhoneNumber.class);
                startActivity(i);
                finish();
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });


        //on create ends here
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }

    public void Register() {
        final String emailText, passwordText, confirmPasswordText;

        emailText = email.getText().toString();
        passwordText = password.getText().toString();
        confirmPasswordText = confirmPassword.getText().toString();

        if (emailText.isEmpty() || emailText.equals(" ")) {
            email.setError("Fill here please !");
            return;
        }

        if (passwordText.isEmpty() || passwordText.equals(" ")) {
            password.setError("Fill here please !");
            return;
        }

        if (confirmPasswordText.isEmpty() || confirmPasswordText.equals(" ")) {
            confirmPassword.setError("Fill here please !");
            return;
        }

        if (passwordText.length() < 8) {
            password.setError("Length must be greater than 8");
            return;
        }


        if (!confirmPasswordText.equals(passwordText)) {
            confirmPassword.setError("Password not match !");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, Home.class));

                            Toast.makeText(LoginActivity.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();

                            //display a failure message
                        } else {
                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
/*
                        if (task.isSuccessful()) {

                            User user = new User(
                                    email,
                                    password,
                                    confirmPassword

                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(LoginActivity.this,Home.class));

                                        Toast.makeText(LoginActivity.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                    } else {
                                        //display a failure message
                                    }
                                }
                            });

                        } else {


                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }


*/


                    }
                });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                Register();
                break;


        }
    }


    public void openLogin(View view) {
        Intent login = new Intent(this, MainActivity.class);
        startActivity(login);
        finish();
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void initializeGooglePlusSettings() {

        //intialize the google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //get an instance of the google sign in
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                Intent i = new Intent(LoginActivity.this, Home.class);
                startActivity(i);
                finish();
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //here we take the account that was passed to this method when the authentication with Gmail was successful, and then use that to perform
        //a firebase authentication
        showView(loadingProgress);
        hideView(loginScreen);
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.getDisplayName(); //this is the name gotten from the Google Account, you can choose to store this in a Shared pref and use in all activities or whatever

                            //you can add an intent of the new activity where you want the user to go to next when the authentication is successful

                            Intent i = new Intent(LoginActivity.this, Home.class);
                            i.putExtra("userName", user.getDisplayName()); //just passing the name to the next activity
                            startActivity(i);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void showView(View... views) {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);

        }

    }

    private void hideView(View... views) {
        for (View v : views) {
            v.setVisibility(View.INVISIBLE);

        }

    }

    //////////////////// Back Exit ////////////////////////
    @Override
    public void onBackPressed() {


        if (time + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(LoginActivity.this, "Press Again To Exit....!", Toast.LENGTH_LONG).show();
        }
        time = System.currentTimeMillis();

    }

}

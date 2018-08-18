package saifamerealaandeveloper2030.com.EalaanTradingMarketing.PhoneNumber;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import saifamerealaandeveloper2030.com.EalaanTradingMarketing.Home;
import saifamerealaandeveloper2030.com.EalaanTradingMarketing.R;


public class PhoneNumber extends AppCompatActivity {
    private static final String TAG = "PhoneLogin";
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    TextView t1,t2;
    ImageView i1;
    EditText e1,e2;
    Button b1,b2;
    long time;
    private LinearLayout loadingProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        loadingProgress = (LinearLayout) findViewById(R.id.loadingProgress);
        loadingProgress.setVisibility(View.INVISIBLE);

        e1 = (EditText) findViewById(R.id.Phonenoedittext);
        b1 = (Button) findViewById(R.id.PhoneVerify);
        t1 = (TextView)findViewById(R.id.textView2Phone);
        i1 = (ImageView)findViewById(R.id.imageView2Phone);
        e2 = (EditText) findViewById(R.id.OTPeditText);
        b2 = (Button)findViewById(R.id.OTPVERIFY);
        t2 = (TextView)findViewById(R.id.textViewVerified);
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;
                Toast.makeText(PhoneNumber.this,"Verification Complete",Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                // Log.w(TAG, "onVerificationFailed", e);
                loadingProgress.setVisibility(View.INVISIBLE);
                Toast.makeText(PhoneNumber.this,"Verification Failed",Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                    // Invalid request
                    loadingProgress.setVisibility(View.INVISIBLE);
                    Toast.makeText(PhoneNumber.this,"InValid Phone Number",Toast.LENGTH_SHORT).show();
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // Log.d(TAG, "onCodeSent:" + verificationId);
                loadingProgress.setVisibility(View.INVISIBLE);
                Toast.makeText(PhoneNumber.this,"Verification code has been send on your number",Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                e1.setVisibility(View.GONE);
                b1.setVisibility(View.GONE);
                t1.setVisibility(View.GONE);
                i1.setVisibility(View.GONE);
                t2.setVisibility(View.VISIBLE);
                e2.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);
                // ...
            }
        };

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgress.setVisibility(View.VISIBLE);
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        e1.getText().toString(),
                        60,
                        java.util.concurrent.TimeUnit.SECONDS,
                        PhoneNumber.this,
                        mCallbacks);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, e2.getText().toString());
                // [END verify_with_code]
                signInWithPhoneAuthCredential(credential);
            }
        });


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        loadingProgress.setVisibility(View.VISIBLE);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loadingProgress.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {
                            // Log.d(TAG, "signInWithCredential:success");
                            startActivity(new Intent(PhoneNumber.this,Home.class));
                            Toast.makeText(PhoneNumber.this,"Verification Done",Toast.LENGTH_SHORT).show();
                            finish();
                            // ...
                        } else {
                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(PhoneNumber.this,"Invalid Verification",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    //////////////////// Back Exit ////////////////////////

    @Override
    public void onBackPressed() {


        if (time + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(PhoneNumber.this, "Press Again To Exit....!", Toast.LENGTH_LONG).show();
        }
        time = System.currentTimeMillis();

    }
}

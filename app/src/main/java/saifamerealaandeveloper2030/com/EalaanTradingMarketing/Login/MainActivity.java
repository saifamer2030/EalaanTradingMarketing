package saifamerealaandeveloper2030.com.EalaanTradingMarketing.Login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import saifamerealaandeveloper2030.com.EalaanTradingMarketing.Home;
import saifamerealaandeveloper2030.com.EalaanTradingMarketing.Register.LoginActivity;
import saifamerealaandeveloper2030.com.EalaanTradingMarketing.R;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    TextInputEditText password,email;
    private Button button;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            Toast.makeText(getApplicationContext(), " Please Connection Internet And Restart App", Toast.LENGTH_LONG).show();
            return ;
        }




        email=(TextInputEditText)findViewById(R.id.login_email);
        password=(TextInputEditText)findViewById(R.id.login_password);
        progressBar = findViewById(R.id.progressbar_2);
        progressBar.setVisibility(View.GONE);

        button = (Button)findViewById(R.id.loginBtn_1);
        animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.translate);
        button.setAnimation(animation);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            Intent home=new Intent(MainActivity.this,Home.class);
            startActivity(home);
            finish();
        }

    }



    public void Login(View view) {

       final String passwordText, emailText;


        passwordText = password.getText().toString();
        emailText = email.getText().toString();

        if ( password.equals(" ")) {
            password.setError("Fill here please !");
            return;


        }
        if (email.equals(" ")){
            email.setError("Fill here please !");
            return;
        }
        else {




            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Intent home = new Intent(MainActivity.this, Home.class);
                                startActivity(home);
                                finish();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Log.e("my_store", "failure", task.getException());
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }


                        }
                    });


        }
    }

    public void openRegister(View view) {
        Intent register =new Intent (this , LoginActivity.class);
        startActivity(register);
        finish();
    }

}

package saifamerealaandeveloper2030.com.EalaanTradingMarketing.NotificationOnlin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.iid.FirebaseInstanceId;

import saifamerealaandeveloper2030.com.EalaanTradingMarketing.R;

public class MyToken extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_token);
        System.out.println("MY Token :"+ FirebaseInstanceId.getInstance().getToken());
    }
}

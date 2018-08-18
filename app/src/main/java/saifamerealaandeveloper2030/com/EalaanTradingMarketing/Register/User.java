package saifamerealaandeveloper2030.com.EalaanTradingMarketing.Register;

import android.support.design.widget.TextInputEditText;

/**
 * Created by Belal on 4/15/2018.
 */

public class User {
    public String email , password, confirmPassword;

    public User(TextInputEditText email, TextInputEditText password, TextInputEditText confirmPassword) {

    }

    public User( String email,String password, String confirmPassword) {

        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}

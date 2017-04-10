package grupo19.locmess19.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import grupo19.locmess19.R;

/**
 * Created by super on 05/04/2017.
 */

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    }

    public void register_click(View v){
        startActivity(new Intent(SignUpActivity.this, MessagesActivity.class)); //change register transition to main menu (messages?)
    }
    public void backtologin_click(View v){
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
    }
}

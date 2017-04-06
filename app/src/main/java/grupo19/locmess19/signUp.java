package grupo19.locmess19;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by super on 05/04/2017.
 */

public class signUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    }

    public void register_click(View v){
        startActivity(new Intent(signUp.this, Messages.class)); //change register transition to main menu (messages?)
    }
    public void backtologin_click(View v){
        startActivity(new Intent(signUp.this, MainActivity.class));
    }
}

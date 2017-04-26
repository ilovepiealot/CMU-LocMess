package grupo19.locmess19.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.OutputStreamWriter;

import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;

/**
 * Created by super on 05/04/2017.
 */

public class SignUpActivity extends AppCompatActivity {

    private ServerCommunication server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        server = new ServerCommunication("10.0.2.2", 11113);

    }

    public void register_click(View v){

        String username = ((EditText) findViewById(R.id.newusername)).getText().toString();
        String password = ((EditText) findViewById(R.id.newpassword)).getText().toString();

        if (server.register(username, password)) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        } else {
            Toast.makeText(SignUpActivity.this, "User already exists.", Toast.LENGTH_SHORT).show();
        }
    }
    public void backtologin_click(View v){
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
    }


}

package grupo19.locmess19.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;

public class MainActivity extends AppCompatActivity  {

    private ServerCommunication server;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        server = new ServerCommunication("10.0.2.2", 11113);
    }

    public void sign_up_click(View v){
       startActivity(new Intent(MainActivity.this, SignUpActivity.class));

    }
    /*public void signIn_click(View v){
        startActivity(new Intent(MainActivity.this, MessagesActivity.class));

    }*/

    public void signIn_click(View v) {

        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

//        if (server.login(username, password)) {
             startActivity(new Intent(MainActivity.this, MessagesActivity.class));
//        } else {
//            Toast.makeText(MainActivity.this, "Failed to login.", Toast.LENGTH_SHORT).show();
//        }
    }
}

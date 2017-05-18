package grupo19.locmess19.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.OutputStreamWriter;

import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;


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

        new RegisterOperation().execute(new RegisterParams(username, password));

    }

    private static class RegisterParams {
        String username;
        String password;

        RegisterParams(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    private class RegisterOperation extends AsyncTask<RegisterParams, Void, String> {

        @Override
        protected String doInBackground(RegisterParams... params) {
            if (server.register(params[0].username, params[0].password)) {
                return "Success";
            } else {
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Success")){
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();   // To prevent the back button to return to this activity
            }
            else if(result.equals("Failed")){
                Toast.makeText(SignUpActivity.this, "User already exists.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    public void backtologin_click(View v){
        finish();
    }


}

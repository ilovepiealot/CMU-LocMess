package grupo19.locmess19.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

    public void signIn_click(View v) {

        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        new LoginOperation().execute(new LoginParams(username, password));

    }

    private static class LoginParams {
        String username;
        String password;

        LoginParams(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    private class LoginOperation extends AsyncTask<LoginParams, Void, String> {

        @Override
        protected String doInBackground(LoginParams... params) {
            if (server.login(params[0].username, params[0].password)) {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("loggedUser", params[0].username);
                editor.putString("userPassword", params[0].password);
                editor.commit();

                return "Success";
            } else {
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Success")){
                startActivity(new Intent(MainActivity.this, MessagesActivity.class));
                finish();   // To prevent the back button to return to this activity
            }
            else if(result.equals("Failed")){
                Toast.makeText(MainActivity.this, "Failed to login.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}

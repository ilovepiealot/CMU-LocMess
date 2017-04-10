package grupo19.locmess19.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import grupo19.locmess19.R;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void sign_up_click(View v){
       startActivity(new Intent(MainActivity.this, SignUpActivity.class));

    }
    public void signIn_click(View v){
        startActivity(new Intent(MainActivity.this, MessagesActivity.class));

    }
}

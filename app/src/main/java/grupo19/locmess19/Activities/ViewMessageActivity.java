package grupo19.locmess19.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;

/**
 * Created by super on 29/04/2017.
 */

public class ViewMessageActivity extends AppCompatActivity  {

    private ServerCommunication server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewmessage);
        server = new ServerCommunication("10.0.2.2", 11113);

    }
}

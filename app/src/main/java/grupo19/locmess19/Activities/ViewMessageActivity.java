package grupo19.locmess19.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;

/**
 * Created by super on 29/04/2017.
 */

public class ViewMessageActivity extends AppCompatActivity  {

    private ServerCommunication server;
    String s;
    String[] receivedMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewmessage);
        server = new ServerCommunication("10.0.2.2", 11113);

       Bundle extras = getIntent().getExtras();
        if (extras != null) {
            s = extras.getString("ID");
            //Toast.makeText(ViewMessageActivity.this, s, Toast.LENGTH_SHORT).show();
            //The key argument here must match that used in the other activity
        }
        //RECEBE CONTEUDO DE MENSAGEM E POPULA O VIEWMESSAGE

        receivedMessage = server.getMessage(s).toString().split("#YOLO#");
        //Toast.makeText(ViewMessageActivity.this, receivedMessage, Toast.LENGTH_SHORT).show();

        TextView message_title = (TextView) findViewById(R.id.message_title);
        TextView messageContent = (TextView) findViewById(R.id.messageContent);
        messageContent.setMovementMethod(new ScrollingMovementMethod());
        TextView start_date = (TextView) findViewById(R.id.start_date);
        TextView end_date = (TextView) findViewById(R.id.end_date);
        TextView location = (TextView) findViewById(R.id.location);
        TextView sender = (TextView) findViewById(R.id.sender);

        message_title.setText(receivedMessage[0]);
        messageContent.setText(receivedMessage[1]);
        start_date.setText(receivedMessage[2]);
        end_date.setText(receivedMessage[3]);
        location.setText(receivedMessage[4]);
        sender.setText(receivedMessage[5]);

    }
    public void return_click(View v){
        startActivity(new Intent(ViewMessageActivity.this, InboxActivity.class));
    }
}

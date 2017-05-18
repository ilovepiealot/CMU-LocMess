package grupo19.locmess19.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;

/**
 * Created by super on 29/04/2017.
 */

public class ViewMessageActivity extends AppCompatActivity  {

    private ServerCommunication server;
    String s;
    String[] receivedMessage;
    int sessionID = 0;
    Calendar dateStart = Calendar.getInstance();
    Calendar dateEnd = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewmessage);
        server = new ServerCommunication("10.0.2.2", 11113);

       Bundle extras = getIntent().getExtras();
        if (extras != null) {
            s = extras.getString("ID");
        }
        //RECEBE CONTEUDO DE MENSAGEM E POPULA O VIEWMESSAGE
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sessionID = sharedPreferences.getInt("sessionID",0);

        receivedMessage = server.getMessage(s, sessionID).toString().split("#YOLO#");

        TextView message_title = (TextView) findViewById(R.id.message_title);
        TextView messageContent = (TextView) findViewById(R.id.messageContent);
        messageContent.setMovementMethod(new ScrollingMovementMethod());
        TextView start_date = (TextView) findViewById(R.id.start_date);
        TextView end_date = (TextView) findViewById(R.id.end_date);
        TextView location = (TextView) findViewById(R.id.location);
        TextView sender = (TextView) findViewById(R.id.sender);

        dateStart.setTimeInMillis(Long.parseLong(receivedMessage[2]));
        dateEnd.setTimeInMillis(Long.parseLong(receivedMessage[3]));

        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a");

        String STARTdateANDtime = format.format(dateStart.getTime());
        String ENDdateANDtime = format.format(dateEnd.getTime());

        message_title.setText(receivedMessage[0]);
        messageContent.setText(receivedMessage[1]);
        start_date.setText(STARTdateANDtime);
        end_date.setText(ENDdateANDtime);
        location.setText(receivedMessage[4]);
        sender.setText(receivedMessage[5]);

    }
    public void return_click(View v){
        this.finish();
    }
}

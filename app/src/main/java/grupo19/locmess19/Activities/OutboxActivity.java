package grupo19.locmess19.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Map;
import grupo19.locmess19.Adapters.CustomTitlesOutboxAdapter;
import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;

/**
 * Created by super on 10/04/2017.
 */

public class OutboxActivity extends AppCompatActivity{
    private ServerCommunication server;
    ListView outboxlistview;
    private Map<String, String> messageTitles;
    String[] extra;
    String username;
    String outbox = "outbox";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outbox);
        server = new ServerCommunication("10.0.2.2", 11113);

        //retrieves message titles for this box and current user
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = sharedPreferences.getString("loggedUser", "");

        messageTitles = server.getTitles(username, outbox);
        ArrayList<String> receivedTitles = new ArrayList<String>();
        //receives map of titles and filters both key and value and adds to array list
        for (Map.Entry<String,String> entry : messageTitles.entrySet()) {
            String title = entry.getKey();
            String id = entry.getValue();
            String womboCombo = (id + ":" + title);
            receivedTitles.add(womboCombo);

        }
        //populates the listview with the received titles
        ListView outboxlistview = (ListView) findViewById(R.id.outboxlistview);
        CustomTitlesOutboxAdapter outboxlist = new CustomTitlesOutboxAdapter(this, receivedTitles);
        //ListAdapter outboxlist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, receivedTitles);
        outboxlistview.setAdapter(outboxlist);
        //listener for click on listview row, identifies the entry by the message ID and passes it as an extra for viewactivity
        outboxlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title=(String)parent.getItemAtPosition(position);
                extra = title.split(":");
                Intent i = new Intent(OutboxActivity.this, ViewMessageActivity.class);
                i.putExtra("ID", extra[0]);
                startActivity(i);
            }
        });
    }
}

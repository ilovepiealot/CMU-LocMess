package grupo19.locmess19.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Map;
import grupo19.locmess19.Adapters.CustomTitlesInboxAdapter;
import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;
import grupo19.locmess19.Services.LocationUpdatesService;

/**
 * Created by super on 10/04/2017.
 */

public class InboxActivity extends AppCompatActivity{

    private ServerCommunication server;
    ListView inboxlistview;
    //private List<AbstractMap.SimpleEntry<String,String>> messageTitles;
    private Map<String, String> messageTitles;
    String[] extra;
    String username;
    String inbox = "inbox";

    private static final String TAG = InboxActivity.class.getSimpleName();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        server = new ServerCommunication("10.0.2.2", 11113);

        Intent intent = getIntent();
        String[] finalMessageStringArray = intent.getStringArrayExtra("messageStringArray");
        Log.e(TAG,"Hey");
        if (finalMessageStringArray != null) {
            Log.e(TAG,finalMessageStringArray[0]);
            server.saveMessageToInbox(finalMessageStringArray[0], finalMessageStringArray[1], finalMessageStringArray[2],
                    finalMessageStringArray[3], finalMessageStringArray[4], finalMessageStringArray[5],
                    finalMessageStringArray[6]);
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = sharedPreferences.getString("loggedUser", "");
        //retrieves message titles for this box and current user
        messageTitles = server.getTitles(username,inbox);
        ArrayList<String> receivedTitles = new ArrayList<String>();
        //receives map of titles and filters both key and value and adds to array list
        for (Map.Entry<String,String> entry : messageTitles.entrySet()) {
            String title = entry.getKey();
            String id = entry.getValue();
            String womboCombo = (id + ":" + title);
            receivedTitles.add(womboCombo);

        }
        //populates the listview with the received titles
        ListView inboxlistview = (ListView) findViewById(R.id.inboxlistview);
        CustomTitlesInboxAdapter inboxlist = new CustomTitlesInboxAdapter(this, receivedTitles);
        //ListAdapter inboxlist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, receivedTitles);
        inboxlistview.setAdapter(inboxlist);
        //listener for click on listview row, identifies the entry by the message ID and passes it as an extra for viewactivity
        inboxlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title=(String)parent.getItemAtPosition(position);
                extra = title.split(":");
                Intent i = new Intent(InboxActivity.this, ViewMessageActivity.class);
                i.putExtra("ID", extra[0]);
                startActivity(i);
            }
        });
    }
}

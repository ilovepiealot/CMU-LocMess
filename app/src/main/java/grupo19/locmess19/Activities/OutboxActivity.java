package grupo19.locmess19.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Map;

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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = sharedPreferences.getString("loggedUser", "");

        messageTitles = server.getTitles(username, outbox);
        ArrayList<String> receivedTitles = new ArrayList<String>();

        for (Map.Entry<String,String> entry : messageTitles.entrySet()) {
            String title = entry.getKey();
            String id = entry.getValue();
            String womboCombo = (id + ":" + title);
            receivedTitles.add(womboCombo);

        }

        ListView outboxlistview = (ListView) findViewById(R.id.outboxlistview);

        ListAdapter outboxlist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, receivedTitles);

        outboxlistview.setAdapter(outboxlist);

        outboxlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title=(String)parent.getItemAtPosition(position);
                extra = title.split(":");
                Toast.makeText(OutboxActivity.this, extra[0], Toast.LENGTH_SHORT).show();
                //if (position == 1){
                Intent i = new Intent(OutboxActivity.this, ViewMessageActivity.class);
                i.putExtra("ID", extra[0]);
                startActivity(i);
                //}

            }
        });
    }
}

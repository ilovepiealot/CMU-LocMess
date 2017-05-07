package grupo19.locmess19.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Array;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grupo19.locmess19.Adapters.KeyValueListAdapter;
import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;

import static grupo19.locmess19.R.id.parent;

/**
 * Created by super on 10/04/2017.
 */

public class InboxActivity extends AppCompatActivity{

    private ServerCommunication server;
    ListView inboxlistview;
    //private List<AbstractMap.SimpleEntry<String,String>> messageTitles;
    private Map<String, String> messageTitles;
    String[] extra;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        server = new ServerCommunication("10.0.2.2", 11113);

        /*
        receivedTitles = server.getTitles().toString().split("#YOLO#");
        n = receivedTitles.length;
        for (i=0; i<n; i++){
            messageTitles.add(receivedTitles[i]);
        }*/

        messageTitles = server.getTitles();
        ArrayList<String> receivedTitles = new ArrayList<String>();

        for (Map.Entry<String,String> entry : messageTitles.entrySet()) {
            String title = entry.getKey();
            String id = entry.getValue();
            String womboCombo = (id + ":" + title);
            receivedTitles.add(womboCombo);

        }

        ListView inboxlistview = (ListView) findViewById(R.id.inboxlistview);

        ListAdapter inboxlist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, receivedTitles);

        inboxlistview.setAdapter(inboxlist);

        inboxlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title=(String)parent.getItemAtPosition(position);
                extra = title.split(":");
                Toast.makeText(InboxActivity.this, extra[0], Toast.LENGTH_SHORT).show();
                //if (position == 1){
                    Intent i = new Intent(InboxActivity.this, ViewMessageActivity.class);
                    i.putExtra("ID", extra[0]);
                    startActivity(i);
                //}

            }
        });


    }
}

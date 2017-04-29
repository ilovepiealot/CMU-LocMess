package grupo19.locmess19.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;

import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;

import static grupo19.locmess19.R.id.parent;

/**
 * Created by super on 10/04/2017.
 */

public class InboxActivity extends AppCompatActivity{

    private ServerCommunication server;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        if (server.getTitles()) {
            startActivity(new Intent(InboxActivity.this, InboxActivity.class));
        } else {
            Toast.makeText(InboxActivity.this, "Error on retriving title message.", Toast.LENGTH_SHORT).show();
        }

        String[] messagetitlesORid = {"MeetingIn", "ConcertIn", "PartyIn", "ClubIn"};
        ListAdapter inboxlist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messagetitlesORid );
        ListView inboxlistview = (ListView) findViewById(R.id.inboxlistview);
        inboxlistview.setAdapter(inboxlist);

    }
}

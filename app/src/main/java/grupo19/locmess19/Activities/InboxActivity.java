package grupo19.locmess19.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.sql.Array;
import java.util.ArrayList;

import grupo19.locmess19.R;

import static grupo19.locmess19.R.id.parent;

/**
 * Created by super on 10/04/2017.
 */

public class InboxActivity extends AppCompatActivity{


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        String[] messagetitlesORid = {"MeetingIn", "ConcertIn", "PartyIn", "ClubIn"};
        ListAdapter inboxlist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messagetitlesORid );
        ListView inboxlistview = (ListView) findViewById(R.id.inboxlistview);
        inboxlistview.setAdapter(inboxlist);

    }
}

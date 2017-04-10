package grupo19.locmess19.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.sql.Array;
import java.util.ArrayList;

import grupo19.locmess19.R;

/**
 * Created by super on 10/04/2017.
 */

public class OutboxActivity extends AppCompatActivity{


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outbox);

        String[] messagetitlesORid = {"MeetingOut", "ConcertOut", "PartyOut", "ClubOut"};
        ListAdapter outboxlist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messagetitlesORid );
        ListView outboxlistview = (ListView) findViewById(R.id.outboxlistview);
        outboxlistview.setAdapter(outboxlist);

    }
}

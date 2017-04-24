package grupo19.locmess19.Fragments;

//import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import grupo19.locmess19.Activities.InboxActivity;
import grupo19.locmess19.Activities.NewMessageActivity;
import grupo19.locmess19.Activities.OutboxActivity;
import grupo19.locmess19.R;


/**
 * Created by super on 10/04/2017.
 */

public class MessagesMenu extends Fragment {

    public static MessagesMenu newInstance() {
        MessagesMenu fragment = new MessagesMenu();
        return fragment;
    }
    public MessagesMenu(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_messages_menu, container, false);


        FloatingActionButton newMessageButton = (FloatingActionButton)  v.findViewById(R.id.newMessageButton);
        newMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewMessageActivity.class);
                startActivity(intent);
            }
        });

        Button inboxButton = (Button) v.findViewById(R.id.inbox);
        inboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InboxActivity.class);
                startActivity(intent);
            }
        });

        Button outboxButton = (Button) v.findViewById(R.id.outbox);
        outboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OutboxActivity.class);
                startActivity(intent);
            }
        });

       return v;
    }

}

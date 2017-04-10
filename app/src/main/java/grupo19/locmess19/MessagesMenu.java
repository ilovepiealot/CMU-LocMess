package grupo19.locmess19;

//import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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
                Intent intent = new Intent(getActivity(), NewMessage.class);
                startActivity(intent);
            }
        });
       return v;
    }


}

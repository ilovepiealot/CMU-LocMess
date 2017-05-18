package grupo19.locmess19.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import grupo19.locmess19.Activities.NewKeyActivity;
import grupo19.locmess19.Activities.NewMessageActivity;
import grupo19.locmess19.Adapters.KeyValueListAdapter;
import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;



public class ProfilesFragment extends Fragment {

    private String username;
    private HashMap<String,String> keyValueItems;
    private List<SimpleEntry<String, String>> list;
    private int sessionID = 0;

    public static ProfilesFragment newInstance() {
        ProfilesFragment profilesFragment = new ProfilesFragment();
        return profilesFragment;
    }     public ProfilesFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getArguments().getString("username", "");
        sessionID = getArguments().getInt("sessionID", 0);

        ServerCommunication server = new ServerCommunication("10.0.2.2", 11113);
        keyValueItems = server.getUserKeys(sessionID);
        list = processKeyList();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        Button newKeyBtn = (Button) v.findViewById(R.id.add_key_button);

        newKeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), NewKeyActivity.class);
                intent.putExtra("sessionID", sessionID);
                startActivity(intent);
                getActivity().finish();
            }
        });

        TextView usernameText = (TextView) v.findViewById(R.id.UsernameText);
        usernameText.setText(username);

        ListView keyList = (ListView) v.findViewById(R.id.keys_preview_list);
        KeyValueListAdapter adapter = new KeyValueListAdapter(getActivity(), R.layout.item_key, list);
        keyList.setAdapter(adapter);

       return v;
    }

    public List<SimpleEntry<String, String>> processKeyList(){
        List<SimpleEntry<String, String>> list = new ArrayList<>();
        for(String key: keyValueItems.keySet()){
            list.add(new SimpleEntry<>(key, keyValueItems.get(key)));
        }
        return list;
    }

}

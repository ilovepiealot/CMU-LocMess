package grupo19.locmess19.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import grupo19.locmess19.Adapters.KeyValueListAdapter;
import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;



public class ProfilesFragment extends Fragment {

    private ListView keyList;
    private String username;
    private HashMap<String,String> keyValueItems;
    private ServerCommunication server;
    private List<SimpleEntry<String, String>> list;

    public static ProfilesFragment newInstance() {
        ProfilesFragment profilesFragment = new ProfilesFragment();
        return profilesFragment;
    }     public ProfilesFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getArguments().getString("username", "");
        server = new ServerCommunication("10.0.2.2", 11113);
        keyValueItems = server.getUserKeys(username);
        list = processKeyList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        Button newKeyBtn = (Button) v.findViewById(R.id.add_key_button);

        newKeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewKey();
            }
        });

        TextView usernameText = (TextView) v.findViewById(R.id.UsernameText);
        usernameText.setText(username);


        keyList = (ListView) v.findViewById(R.id.keys_preview_list);
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

    public void createNewKey(){
        LayoutInflater factory = LayoutInflater.from(getActivity());

        //text_entry is an Layout XML file containing two text field to display in alert dialog
        final View textEntryView = factory.inflate(R.layout.custom_dialog, null);

        final EditText input1 = (EditText) textEntryView.findViewById(R.id.dialogKeyText);
        final EditText input2 = (EditText) textEntryView.findViewById(R.id.dialogValueText);

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("New Key Value Pair:").setView(textEntryView)
                .setPositiveButton("Create",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        String key = input1.getText().toString();
                        String value = input2.getText().toString();
                        //keyValueItems.put(key, value);
                        list.add(new SimpleEntry<>(key,value));
                        new SaveNewKeyOperation().execute(key, value);
                        ((BaseAdapter) keyList.getAdapter()).notifyDataSetChanged();
                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        // do nothing
                    }
                });
        alert.show();
    }

    private class SaveNewKeyOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (server.saveNewKey(username, params[0], params[1])) {
                return "Success";
            } else {
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Success")){
                Toast.makeText(getActivity(), "Key created with success!", Toast.LENGTH_LONG).show();
            }
            else if(result.equals("Failed")){
                Toast.makeText(getActivity(), "Key failed to create!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}

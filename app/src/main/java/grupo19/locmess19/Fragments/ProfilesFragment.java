package grupo19.locmess19.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import grupo19.locmess19.Adapters.KeyValueListAdapter;
import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;



public class ProfilesFragment extends Fragment {

    private ListView keyList;
    EditText editUsername, editPassword;
    Button applyBtn;
    private String username, password;
    private List<SimpleEntry<String,String>> keyValueItems;
    private ServerCommunication server;


    public static ProfilesFragment newInstance() {
        ProfilesFragment fragment = new ProfilesFragment();
        return fragment;
    }
    public ProfilesFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        server = new ServerCommunication("10.0.2.2", 11113);

        Button newKeyBtn = (Button) v.findViewById(R.id.add_key_button);
        applyBtn = (Button) v.findViewById(R.id.apply_profile_button);

        newKeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewKey();
            }
        });
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyChanges();
            }
        });


        username = getArguments().getString("username", "");
        password = getArguments().getString("password", "");

        EditText editUsername = (EditText) v.findViewById(R.id.editUsername);
        EditText editPassword = (EditText) v.findViewById(R.id.editPassword);

        editUsername.setText(username);
        editPassword.setText(password);

        TextWatcher tw = new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(username) || !s.toString().equals(password)){
                    applyBtn.setEnabled(true);          // TODO: check this better :(
                }
            }
        };

        editUsername.addTextChangedListener(tw);


        keyList = (ListView) v.findViewById(R.id.keys_preview_list);

        populateKeyList(v);

       return v;
    }


    public void populateKeyList(View v){

//        keyValueItems = new ArrayList<SimpleEntry<String,String>>();
//        for (int i = 0; i < 2; i++) {
//            SimpleEntry<String,String> item = new SimpleEntry<String,String>("Key " + i, "Value " + i);
//            keyValueItems.add(item);
//        }

        keyValueItems = server.getUserKeys(username);

        keyList = (ListView) v.findViewById(R.id.keys_preview_list);
        KeyValueListAdapter adapter = new KeyValueListAdapter(getActivity(), R.layout.item_key, keyValueItems);
        keyList.setAdapter(adapter);
        keyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                Toast toast = Toast.makeText(getActivity(),
                        "Item " + (position + 1) + ": " + keyValueItems.get(position),
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

            }
        });

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
                        SimpleEntry<String,String> item = new SimpleEntry<String,String>(input1.getText().toString(), input2.getText().toString());
                        keyValueItems.add(item);
                        new SaveNewKeyOperation().execute(item);
                        ((BaseAdapter) keyList.getAdapter()).notifyDataSetChanged();
                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {

                    }
                });
        alert.show();
    }

    private class SaveNewKeyOperation extends AsyncTask<SimpleEntry<String,String>, Void, String> {

        @Override
        protected String doInBackground(SimpleEntry<String,String>... params) {
            if (server.saveNewKey(username, params[0].getKey(), params[0].getValue())) {
                return "Success";
            } else {
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Success")){
            }
            else if(result.equals("Failed")){
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    public void applyChanges(){
        //TODO: Change the users.txt enter and change the username and the password of the current user, and change the SharedPreferences!
    }


}

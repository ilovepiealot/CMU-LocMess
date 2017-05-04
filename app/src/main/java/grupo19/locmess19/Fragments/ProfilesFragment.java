package grupo19.locmess19.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import grupo19.locmess19.Adapters.KeyValueListAdapter;
import grupo19.locmess19.DataTypes.KeyValuePair;
import grupo19.locmess19.R;



public class ProfilesFragment extends Fragment {

    private ListView keyList;
    private EditText editUsername;
    private EditText editPassword;
    private String username;
    private String password;
    List<KeyValuePair> keyValueItems;

    public static ProfilesFragment newInstance() {
        ProfilesFragment fragment = new ProfilesFragment();
        return fragment;
    }
    public ProfilesFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        username = getArguments().getString("username", "");
        password = getArguments().getString("password", "");

        editUsername = (EditText) v.findViewById(R.id.editUsername);
        editPassword = (EditText) v.findViewById(R.id.editPassword);

        editUsername.setText(username);
        editPassword.setText(password);

        keyList = (ListView) v.findViewById(R.id.keys_preview_list);

        Button newKeyBtn = (Button) v.findViewById(R.id.add_key_button);
        newKeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewKey();
            }
        });

        populateKeyList(v);

       return v;
    }


    public void populateKeyList(View v){

        keyValueItems = new ArrayList<KeyValuePair>();
        for (int i = 0; i < 2; i++) {
            KeyValuePair item = new KeyValuePair("Key " + i, "Value " + i);
            keyValueItems.add(item);
        }

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
                        KeyValuePair item = new KeyValuePair(input1.getText().toString(), input2.getText().toString());
                        keyValueItems.add(item);
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


}

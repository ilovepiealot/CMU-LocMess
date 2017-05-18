package grupo19.locmess19.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grupo19.locmess19.Adapters.KeyValueListAdapter;
import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;

public class NewKeyActivity extends AppCompatActivity {

    private ServerCommunication server;
    private Spinner keysSpinner;
    private Spinner valuesSpinner;
    private EditText newKeyText;
    private EditText newValueText;
    private Button addNewKeyButton;
    private HashMap<String,String> keyValues;
    private HashMap<String,String> userKeyValues;
    private List<String> keysArray, valuesArray;
    private String key, value;
    private String username;
    private Context context;
    private int sessionID = 0;
    private boolean keyEdited, valueEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_key);

        this.context = this;

        server = new ServerCommunication("10.0.2.2", 11113);
        sessionID = getIntent().getExtras().getInt("sessionID", 0);

        userKeyValues = server.getUserKeys(sessionID);

        keysSpinner = (Spinner) findViewById(R.id.keys_spinner);
        valuesSpinner = (Spinner) findViewById(R.id.values_spinner);
        addNewKeyButton = (Button) findViewById(R.id.add_new_key_button);
        Button cancelNewKeyButton = (Button) findViewById(R.id.new_key_cancel_button);
        newKeyText = (EditText) findViewById(R.id.newKeyText);
        newValueText = (EditText) findViewById(R.id.newValueText);

        keysSpinner.setEnabled(true);
        valuesSpinner.setEnabled(false);
        addNewKeyButton.setEnabled(false);
        newKeyText.setVisibility(View.INVISIBLE);
        newValueText.setVisibility(View.INVISIBLE);
        keysSpinner.setVisibility(View.VISIBLE);
        valuesSpinner.setVisibility(View.VISIBLE);

        keyEdited = false;
        valueEdited = false;

        populateKeysSpinner();

        keysSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valuesSpinner.setEnabled(true);
                if(position == keysSpinner.getCount()-1){
                    keysSpinner.setVisibility(View.INVISIBLE);
                    newKeyText.setVisibility(View.VISIBLE);
                    valuesSpinner.setVisibility(View.INVISIBLE);
                    newValueText.setVisibility(View.VISIBLE);
                    keyEdited = true;
                    valueEdited = true;
                }
                else{
                    String key = keysArray.get(position);
                    populateValuesSpinner(key);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                valuesSpinner.setEnabled(false);
            }
        });

        valuesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addNewKeyButton.setEnabled(true);
                if(position == valuesSpinner.getCount()-1){
                    valuesSpinner.setVisibility(View.INVISIBLE);
                    newValueText.setVisibility(View.VISIBLE);
                    valueEdited = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                addNewKeyButton.setEnabled(false);
            }
        });

        cancelNewKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessagesActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addNewKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyEdited) {
                    key = newKeyText.getText().toString();
                } else {
                    key = keysSpinner.getSelectedItem().toString();
                }
                if (valueEdited) {
                    value = newValueText.getText().toString();
                } else {
                    value = valuesSpinner.getSelectedItem().toString();
                }
                String oldValue = userKeyValues.get(key);
                if (key.isEmpty() || value.isEmpty()) {
                    Toast.makeText(context, "The value and the key should not be empty!", Toast.LENGTH_LONG).show();
                } else {
                    if (value.equals(oldValue)) {
                        Toast.makeText(context, "This value is already assigned to this key!", Toast.LENGTH_LONG).show();
                    } else {
                        if (oldValue != null) {
                            AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                            alertbox.setMessage("Do you really want to change the value of key \"" + key + "\" to \"" + value + "\"?\n(Old value: \"" + oldValue + "\")");
                            alertbox.setTitle("Change Key's value");
                            alertbox.setPositiveButton("Change",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {
                                            new DeleteKeyOperation().execute(""+sessionID, key, value);
                                            new SaveNewKeyOperation().execute(key, value);
                                            Intent intent = new Intent(context, MessagesActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                            alertbox.setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {
                                            // do nothing
                                        }
                                    });
                            alertbox.show();
                        } else {
                            new SaveNewKeyOperation().execute(key, value);
                            Intent intent = new Intent(context, MessagesActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        });


    }

    private void populateKeysSpinner(){

        keyValues = server.getAllKeys();

        keysArray = new ArrayList<String>();
        valuesArray = new ArrayList<String>();

        for(Map.Entry<String,String> kv : keyValues.entrySet()){
            keysArray.add(kv.getKey());
        }
        keysArray.add(getString(R.string.create_new));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, keysArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        keysSpinner.setAdapter(adapter);
    }


    private void populateValuesSpinner(String key) {

        valuesArray.clear();
        for(Map.Entry<String,String> kv : keyValues.entrySet()){
            if(kv.getKey().equals(key)){
                valuesArray.add(kv.getValue());
            }
        }
        valuesArray.add(getString(R.string.create_new));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, valuesArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        valuesSpinner.setAdapter(adapter);
    }


    private class SaveNewKeyOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (server.saveNewKey(sessionID, params[0], params[1])) {
                return "Success";
            } else {
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Success")){
                Toast.makeText(context, "Key created with success!", Toast.LENGTH_LONG).show();
            }
            else if(result.equals("Failed")){
                Toast.makeText(context, "Key failed to create!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private class DeleteKeyOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (server.deleteKey(sessionID, params[1], params[2])) {
                return "Success";
            } else {
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Success")){
                Toast.makeText(context, "Key deleted with success!", Toast.LENGTH_LONG).show();
            }
            else if(result.equals("Failed")){
                Toast.makeText(context, "Key failed to delete!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


}

package grupo19.locmess19.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;

public class NewMessageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    DateFormat formatDateTime = DateFormat.getDateInstance();
    DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
    Calendar dateBegin = Calendar.getInstance();
    Calendar dateEnd = Calendar.getInstance();
    Calendar dateCurrent = Calendar.getInstance();
    Spinner spinner;
    private TextView text;
    private TextView text_end;
    private TextView text_time;
    private TextView text_time_end;
    private Button btn_date;
    private Button btn_date_end;
    private Button btn_time;
    private Button btn_time_end;
    private Button createnewmessage;
    public String username;
    private int sessionID = 0;

    private Button whitelist_keys;
    private TextView whitelist_keys_selected;
    private Button blacklist_keys;
    private TextView blacklist_keys_selected;

    String receivedLocationsTitlesString;
    //String[] receivedLocationsTitles;
    List<String> receivedLocationsTitles = new ArrayList<>();
    ArrayList<String[]> receivedLocations = new ArrayList<>();

    String[] keys;
    boolean[] checkedKeys;      //for whitelist
    boolean[] checkedKeysb;  //for blacklist
    ArrayList<Integer>  keysItems = new ArrayList<>();      //for whitelist
    ArrayList<Integer>  keysItemsb = new ArrayList<>();     //for blacklist
    private Map<String, String> getKeys;

    boolean validInterval = false;
    boolean validStart= false;

    private ServerCommunication server;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmessage);
        server = new ServerCommunication("10.0.2.2", 11113);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = sharedPreferences.getString("loggedUser", "");
        sessionID = sharedPreferences.getInt("sessionID", 0);

    //POPULATE THE SPINNER
        receivedLocationsTitles.add("Please pick an existing location");
        receivedLocations = server.getExistingLocations();
        for (int i = 0; i<receivedLocations.size(); i++){
            //receivedLocationsTitlesString = receivedLocations.get(i).split("#YOLO#")[0];
            receivedLocationsTitlesString = receivedLocations.get(i)[0];
            receivedLocationsTitles.add(receivedLocationsTitlesString);
        }

        createnewmessage = (Button) findViewById(R.id.createnewmessage);
    //POPULATE THE KEYS
        getKeys = server.getAllKeys();
        ArrayList<String> receivedKeys = new ArrayList<>();
       //receives map of titles and filters both key and value and adds to array list
        for (Map.Entry<String,String> entry : getKeys.entrySet()) {
            String field = entry.getKey();
            String fieldValue = entry.getValue();
            String womboCombo = (field + " -> " + fieldValue);
            receivedKeys.add(womboCombo);

        }
    //converts arraylist to string[]
        keys = new String[receivedKeys.size()];
        keys = receivedKeys.toArray(keys);
        checkedKeys = new boolean[keys.length];
        checkedKeysb = new boolean[keys.length]; //for blacklist items
    //SPINNER FOR LOCATIONS
        spinner = (Spinner) findViewById(R.id.location_selector);
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (NewMessageActivity.this,android.R.layout.simple_spinner_item, receivedLocationsTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    //BUTTON FOR TIME SELECTION
        btn_time = (Button) findViewById(R.id.start_time);
        text_time = (TextView) findViewById(R.id.text_start_time);
        btn_time_end = (Button) findViewById(R.id.end_time);
        text_time_end = (TextView) findViewById(R.id.text_end_time);
    //BUTTON FOR DATE SELECTION
        text = (TextView) findViewById(R.id.text_start_date);
        btn_date = (Button) findViewById(R.id.start_date);
        text_end = (TextView) findViewById(R.id.text_end_date);
        btn_date_end = (Button) findViewById(R.id.end_date);
   //BUTTON FOR KEYS SELECTION
        whitelist_keys = (Button) findViewById(R.id.whitelist);
        whitelist_keys_selected = (TextView) findViewById(R.id.text_whitelist);
        blacklist_keys = (Button) findViewById(R.id.blacklist);
        blacklist_keys_selected = (TextView) findViewById(R.id.text_blacklist);
    //LISTENER FOR WHITELIST KEYS SELECTION
        whitelist_keys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder keyBuilder = new AlertDialog.Builder(NewMessageActivity.this);
                keyBuilder.setTitle("Choose the desired whitelist keys.");
                keyBuilder.setMultiChoiceItems(keys, checkedKeys, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked){
                            if(!(keysItems.contains(position))){
                                keysItems.add(position);
                                checkedKeys[position] = true;
                            } /*else if (keysItems.contains(position)){
                                keysItems.remove(position);
                            }*/
                        } else {
                            checkedKeys[position] = false;
                            keysItems.remove(keysItems.indexOf(position));

                        }
                    }
                });
                keyBuilder.setCancelable(false);
                keyBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        for (int i = 0; i < keysItems.size(); i++){
                            item = item + keys[keysItems.get(i)];
                            if (i != keysItems.size() - 1) {
                                item = item + "#KEY#";
                            }
                        }
                        whitelist_keys_selected.setText(item);
                    }
                });
                keyBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                keyBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i< checkedKeys.length; i++){
                            checkedKeys[i] = false;
                            keysItems.clear();
                            whitelist_keys_selected.setText("Whitelist");
                        }
                    }
                });
                AlertDialog kDialog = keyBuilder.create();
                kDialog.show();
            }
        });

        //LISTENER FOR BLACKLIST KEYS SELECTION
        blacklist_keys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder keyBuilder2 = new AlertDialog.Builder(NewMessageActivity.this);
                keyBuilder2.setTitle("Choose the desired whitelist keys.");
                keyBuilder2.setMultiChoiceItems(keys, checkedKeysb, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog2, int position, boolean isChecked) {
                        if(isChecked){
                            if(!(keysItemsb.contains(position))){
                                keysItemsb.add(position);
                                checkedKeysb[position] = true;
                            } /*else if (keysItems.contains(position)){
                                keysItems.remove(position);
                            }*/
                        } else {
                            checkedKeysb[position] = false;
                            keysItemsb.remove(keysItemsb.indexOf(position));
                        }
                    }
                });
                keyBuilder2.setCancelable(false);
                keyBuilder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog2, int which) {
                        String item = "";
                        for (int i = 0; i < keysItemsb.size(); i++){
                            item = item + keys[keysItemsb.get(i)];
                            if (i != keysItemsb.size() - 1) {
                                item = item + "#KEY#";
                            }
                        }
                        blacklist_keys_selected.setText(item);
                    }
                });
                keyBuilder2.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog2, int which) {
                        dialog2.dismiss();
                    }
                });
                keyBuilder2.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i< checkedKeysb.length; i++){
                            checkedKeysb[i] = false;
                            keysItems.clear();
                            blacklist_keys_selected.setText("Blacklist");
                        }
                    }
                });
                AlertDialog kDialog2 = keyBuilder2.create();
                kDialog2.show();
            }
        });

    //LISTENER CREATION FOR PICKERS
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTime();
            }
        });
        btn_time_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTimeEnd();
            }
        });

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });
        btn_date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDateEnd();
            }
        });
        createnewmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createnewmessage(v);
            }
        });

        //updateLabel();
        //updateTimeLabel();
    }


    private void updateLabel(){
        text.setText(formatDateTime.format(dateBegin.getTime()));
    }
    private void updateLabelEnd(){
        text_end.setText(formatDateTime.format(dateEnd.getTime()));
    }

    private void updateTimeLabel(){
        text_time.setText(formatTime.format(dateBegin.getTime()));
    }
    private void updateTimeLabelEnd(){
        text_time_end.setText(formatTime.format(dateEnd.getTime()));
    }

    public void cancel_click(View v){
        this.finish();
        //startActivity(new Intent(NewMessageActivity.this, MessagesActivity.class));
    }

    private void updateTime(){
        new TimePickerDialog(this, t, dateBegin.get(Calendar.HOUR_OF_DAY), dateBegin.get(Calendar.MINUTE), true).show();
    }
    private void updateTimeEnd(){
        new TimePickerDialog(this, t_end, dateEnd.get(Calendar.HOUR_OF_DAY), dateEnd.get(Calendar.MINUTE), true).show();
    }

    private void updateDate(){
        new DatePickerDialog(this, d, dateBegin.get(Calendar.YEAR), dateBegin.get(Calendar.MONTH), dateBegin.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void updateDateEnd(){
        new DatePickerDialog(this, d_end, dateEnd.get(Calendar.YEAR), dateEnd.get(Calendar.MONTH), dateEnd.get(Calendar.DAY_OF_MONTH)).show();
    }
    //TIME PICKERS
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateBegin.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateBegin.set(Calendar.MINUTE, minute);
            updateTimeLabel();
        }
    };
    TimePickerDialog.OnTimeSetListener t_end = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateEnd.set(Calendar.MINUTE, minute);
            updateTimeLabelEnd();
        }
    };
    //DATE PICKERS
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateBegin.set(Calendar.YEAR, year);
            dateBegin.set(Calendar.MONTH, month);
            dateBegin.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    DatePickerDialog.OnDateSetListener d_end = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateEnd.set(Calendar.YEAR, year);
            dateEnd.set(Calendar.MONTH, month);
            dateEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelEnd();
        }
    };


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView mylocation = (TextView) view;
        //Toast.makeText(this, "Selected Location: " +mylocation.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void createnewmessage(View v){

        Long CurrentEpoch = dateCurrent.getTimeInMillis();

        String message_title = ((TextView) findViewById(R.id.message_title)).getText().toString();
        String messageContent = ((TextView) findViewById(R.id.messageContent)).getText().toString();
        String start_date = String.valueOf(dateBegin.getTimeInMillis());
        String end_date = String.valueOf(dateEnd.getTimeInMillis());
        String location = spinner.getSelectedItem().toString();
        String wkeys = ((TextView) findViewById(R.id.text_whitelist)).getText().toString();
        String bkeys = ((TextView) findViewById(R.id.text_blacklist)).getText().toString();

        if ( Long.valueOf(dateBegin.getTimeInMillis()).compareTo(Long.valueOf(dateEnd.getTimeInMillis())) < 0){
            validInterval = true;
        } else {
            Toast.makeText(this, "End date cannot be previous to start date", Toast.LENGTH_SHORT).show();
        }

        if ( Long.valueOf(dateBegin.getTimeInMillis()).compareTo(Long.valueOf(CurrentEpoch)) >= 0){
            validStart = true;
        } else {
            Toast.makeText(this, "Start date cannot be previous to the current date", Toast.LENGTH_SHORT).show();
        }

        if (message_title.matches("") || messageContent.matches("") || /*start_date.matches("") || end_date.matches("") || */ location.matches("Please pick an existing location") || !validInterval || !validStart) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();

        } else {
            if (server.createNewMessage(message_title, messageContent, start_date, end_date, location, sessionID, wkeys, bkeys)) {
                //startActivity(new Intent(NewMessageActivity.this, MessagesActivity.class));
                this.finish();
            } else {
                Toast.makeText(NewMessageActivity.this, "Error on creating message.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


package grupo19.locmess19.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;

public class NewMessageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    DateFormat formatDateTime = DateFormat.getDateInstance();
    DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
    Calendar date = Calendar.getInstance();
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
    String receivedLocationsTitlesString;
    //String[] receivedLocationsTitles;
    List<String> receivedLocationsTitles = new ArrayList<>();
    ArrayList<String[]> receivedLocations = new ArrayList<>();

    private ServerCommunication server;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmessage);
        server = new ServerCommunication("10.0.2.2", 11113);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = sharedPreferences.getString("loggedUser", "");

    //POPULATE THE SPINNER
        receivedLocationsTitles.add("Please pick an existing location");
        receivedLocations = server.getExistingLocations();
        for (int i = 0; i<receivedLocations.size(); i++){
            //receivedLocationsTitlesString = receivedLocations.get(i).split("#YOLO#")[0];
            receivedLocationsTitlesString = receivedLocations.get(i)[0];
            receivedLocationsTitles.add(receivedLocationsTitlesString);
        }

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

        createnewmessage = (Button) findViewById(R.id.createnewmessage);
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
        text.setText(formatDateTime.format(date.getTime()));
    }
    private void updateLabelEnd(){
        text_end.setText(formatDateTime.format(date.getTime()));
    }

    private void updateTimeLabel(){
        text_time.setText(formatTime.format(date.getTime()));
    }
    private void updateTimeLabelEnd(){
        text_time_end.setText(formatTime.format(date.getTime()));
    }

    public void cancel_click(View v){
        startActivity(new Intent(NewMessageActivity.this, MessagesActivity.class));
    }

    private void updateTime(){
        new TimePickerDialog(this, t, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), true).show();
    }
    private void updateTimeEnd(){
        new TimePickerDialog(this, t_end, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), true).show();
    }

    private void updateDate(){
        new DatePickerDialog(this, d, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void updateDateEnd(){
        new DatePickerDialog(this, d_end, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)).show();
    }
    //TIME PICKERS
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            date.set(Calendar.HOUR_OF_DAY, hourOfDay);
            date.set(Calendar.MINUTE, minute);
            updateTimeLabel();
        }
    };
    TimePickerDialog.OnTimeSetListener t_end = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            date.set(Calendar.HOUR_OF_DAY, hourOfDay);
            date.set(Calendar.MINUTE, minute);
            updateTimeLabelEnd();
        }
    };
    //DATE PICKERS
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, month);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    DatePickerDialog.OnDateSetListener d_end = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, month);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
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

        String message_title = ((TextView) findViewById(R.id.message_title)).getText().toString();
        String messageContent = ((TextView) findViewById(R.id.messageContent)).getText().toString();
        String start_date = ((TextView) findViewById(R.id.text_start_date)).getText().toString();
        String end_date = ((TextView) findViewById(R.id.text_end_date)).getText().toString();
        String start_time = ((TextView) findViewById(R.id.text_start_time)).getText().toString();
        String end_time = ((TextView) findViewById(R.id.text_end_time)).getText().toString();
        String location = spinner.getSelectedItem().toString();

        if (message_title.matches("") || messageContent.matches("") || start_date.matches("") || end_date.matches("") || message_title.matches("") || location.matches("") ) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();

        } else {
            if (server.createNewMessage(message_title, messageContent, start_date, end_date, location, username)) {
                startActivity(new Intent(NewMessageActivity.this, MessagesActivity.class));
            } else {
                Toast.makeText(NewMessageActivity.this, "Error on creating message.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


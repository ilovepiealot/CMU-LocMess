package grupo19.locmess19.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import grupo19.locmess19.Activities.MessagesActivity;
import grupo19.locmess19.R;

import static grupo19.locmess19.R.id.start_date;

public class NewMessageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    DateFormat formatDateTime = DateFormat.getDateTimeInstance();
    Calendar date = Calendar.getInstance();
    Spinner spinner;
    private TextView text;
    private TextView text_end;
    private Button btn_date;
    private Button btn_date_end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmessage);

        spinner = (Spinner) findViewById(R.id.location_selector);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.locations, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        text = (TextView) findViewById(R.id.text_start_date);
        btn_date = (Button) findViewById(R.id.start_date);
        text_end = (TextView) findViewById(R.id.text_end_date);
        btn_date_end = (Button) findViewById(R.id.end_date);
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

        updateLabel();
    }
    private void updateLabel(){
        text.setText(formatDateTime.format(date.getTime()));
    }
    private void updateLabelEnd(){
        text_end.setText(formatDateTime.format(date.getTime()));
    }

    public void cancel_click(View v){
        startActivity(new Intent(NewMessageActivity.this, MessagesActivity.class));
    }
    private void updateDate(){
        new DatePickerDialog(this, d, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void updateDateEnd(){
        new DatePickerDialog(this, d_end, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)).show();
    }


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
        Toast.makeText(this, "Selected" +mylocation.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


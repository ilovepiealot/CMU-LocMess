package grupo19.locmess19.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

import grupo19.locmess19.R;

public class NewMessageActivity extends AppCompatActivity {

    DateFormat formatDateTime = DateFormat.getDateTimeInstance();
    Calendar date = Calendar.getInstance();
    private TextView text;
    private Button btn_date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmessage);

        text = (TextView) findViewById(R.id.text_start_date);
        btn_date = (Button) findViewById(R.id.start_date);

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });

        updateLabel();
    }
    private void updateLabel(){
        text.setText(formatDateTime.format(date.getTime()));
    }
    public void cancel_click(View v){
        startActivity(new Intent(NewMessageActivity.this, MessagesActivity.class));
    }
    private void updateDate(){
        new DatePickerDialog(this, d, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        }
    };

    public void start_date_click(View view) {
    }
}

package grupo19.locmess19;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static grupo19.locmess19.R.id.start_date;

public class NewMessage extends AppCompatActivity {

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
        startActivity(new Intent(NewMessage.this, Messages.class));
    }
    private void updateDate(){
        new DatePickerDialog(this, d, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void OnDateSetListener(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            
        }
    };
}

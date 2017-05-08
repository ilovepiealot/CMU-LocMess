package grupo19.locmess19.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;

public class ViewLocationActivity extends AppCompatActivity {

    private ServerCommunication server;
    private String locName;
    String[] locationList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewlocation);
        server = new ServerCommunication("10.0.2.2", 11113);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            locName = extras.getString("location");
        }

        locationList = server.getLocationDetails(locName);

        TextView locNameText = (TextView)findViewById(R.id.location_name);
        TextView locLatitudeText = (TextView)findViewById(R.id.latitude);
        TextView locLongitudeText = (TextView)findViewById(R.id.longitude);
        TextView locRadiusText = (TextView)findViewById(R.id.radius);
        TextView locWifi = (TextView) findViewById(R.id.wifi_id);

        if (locationList.length == 4) {
            locNameText.setText("NAME: " + locationList[0]);
            locLatitudeText.setText("LATITUDE: " + locationList[1]);
            locLongitudeText.setText("LONGITUDE: " + locationList[2]);
            locRadiusText.setText("RADIUS: " + locationList[3]);
            locLatitudeText.setVisibility(View.VISIBLE);
            locLongitudeText.setVisibility(View.VISIBLE);
            locRadiusText.setVisibility(View.VISIBLE);
            locWifi.setVisibility(View.INVISIBLE);
        } else {
            locNameText.setText("NAME: " + locationList[0]);
            locWifi.setText("SSID: " + locationList[1]);
            locLatitudeText.setVisibility(View.INVISIBLE);
            locLongitudeText.setVisibility(View.INVISIBLE);
            locRadiusText.setVisibility(View.INVISIBLE);
            locWifi.setVisibility(View.VISIBLE);
        }



        Button backBtn = (Button) findViewById(R.id.back_button);

        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}

package grupo19.locmess19.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        locNameText.setText("NAME: " + locationList[0]);
        locLatitudeText.setText("LATITUDE: " + locationList[1]);
        locLongitudeText.setText("LONGITUDE: " + locationList[2]);
        locRadiusText.setText("RADIUS: " + locationList[3]);

    }


}

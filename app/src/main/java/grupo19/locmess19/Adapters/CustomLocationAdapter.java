package grupo19.locmess19.Adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import grupo19.locmess19.Activities.MainActivity;
import grupo19.locmess19.Activities.SignUpActivity;
import grupo19.locmess19.Activities.ViewLocationActivity;
import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;

public class CustomLocationAdapter extends ArrayAdapter<String> {

    private ArrayList<String> locations = new ArrayList<>();
    private ServerCommunication server;


    public CustomLocationAdapter(Context context, ArrayList<String> locations) {
        super(context, 0, locations);
        this.locations = locations;
        server = new ServerCommunication("10.0.2.2", 11113);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        final String location = locations.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_location, parent, false);
        }
        // Lookup view for data population
        final TextView locationRow = (TextView) convertView.findViewById(R.id.locationRow);
        Button deleteBtn = (Button) convertView.findViewById(R.id.delete_btn);
        Button detailsBtn = (Button) convertView.findViewById(R.id.details_btn);
        // If delete button is pressed, delete the row
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                boolean deleted = server.deleteLocation(locations.get(position));
                locations.remove(position);
                notifyDataSetChanged();
            }
        });
        // If details button is pressed, open new activity to show location details
        detailsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewLocationActivity.class);
                intent.putExtra("location", locations.get(position));
                v.getContext().startActivity(intent);

            }
        });
        // Populate the data into the view using the data object
        locationRow.setText(location);
        return convertView;
    }
}

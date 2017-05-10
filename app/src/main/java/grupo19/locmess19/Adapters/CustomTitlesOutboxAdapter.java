package grupo19.locmess19.Adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;

public class CustomTitlesOutboxAdapter extends ArrayAdapter<String> {

    private ArrayList<String> receivedTitles = new ArrayList<>();
    private ServerCommunication server;
    public String username;


    public CustomTitlesOutboxAdapter(Context context, ArrayList<String> receivedTitles) {
        super(context, 0, receivedTitles);
        this.receivedTitles = receivedTitles;
        server = new ServerCommunication("10.0.2.2", 11113);

       //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
       //username = sharedPreferences.getString("loggedUser", "");

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(convertView.getContext());
        //username = sharedPreferences.getString("loggedUser", "");

        // Get the data item for this position
        final String location = receivedTitles.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_titles, parent, false);
        }
        // Lookup view for data population
        final TextView titlerow = (TextView) convertView.findViewById(R.id.titlerow);
        Button deleteBtn = (Button) convertView.findViewById(R.id.delete_btn);
        // If delete button is pressed, delete the row
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                username = sharedPreferences.getString("loggedUser", "");

                //do something
                boolean deleted = server.deleteMessage(receivedTitles.get(position), username); //TODO DELETE MESSAGE ON SERVER
                receivedTitles.remove(position);
                notifyDataSetChanged();
            }
        });

        // Populate the data into the view using the data object
        titlerow.setText(location);
        return convertView;
    }
}

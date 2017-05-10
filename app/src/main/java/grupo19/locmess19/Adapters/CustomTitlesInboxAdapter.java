package grupo19.locmess19.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;

/**
 * Created by super on 10/05/2017.
 */

public class CustomTitlesInboxAdapter extends ArrayAdapter<String> {

    private ArrayList<String> receivedTitles = new ArrayList<>();
    private ServerCommunication server;

    public CustomTitlesInboxAdapter(Context context, ArrayList<String> receivedTitles){
        super(context, 0, receivedTitles);
        this.receivedTitles = receivedTitles;
        server = new ServerCommunication("10.0.2.2", 11113);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        final String location = receivedTitles.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_titles_no_delete, parent, false);
        }
        // Lookup view for data population
        final TextView titlerow = (TextView) convertView.findViewById(R.id.titlerow);

        // Populate the data into the view using the data object
        titlerow.setText(location);
        return convertView;
    }
}

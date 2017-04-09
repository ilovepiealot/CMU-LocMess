package grupo19.locmess19;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class CustomLocationAdapter extends ArrayAdapter<String> {

    private ArrayList<String> locations = new ArrayList<>();

    public CustomLocationAdapter(Context context, ArrayList<String> locations) {
        super(context, 0, locations);
        this.locations = locations;
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
        // If button is pressed, delete the row
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                locations.remove(position);
                notifyDataSetChanged();
            }
        });
        // Populate the data into the view using the data object
        locationRow.setText(location);
        return convertView;
    }
}

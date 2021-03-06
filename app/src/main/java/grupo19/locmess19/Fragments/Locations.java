package grupo19.locmess19.Fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import grupo19.locmess19.Activities.NewLocationActivity;
import grupo19.locmess19.Adapters.CustomLocationAdapter;
import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;

/**
 * Created by super on 06/04/2017.
 */

public class Locations extends Fragment {

    private View rootView;
    private ServerCommunication server;
    ArrayList<String[]> locationList;


    public static Locations newInstance() {
        Locations fragment = new Locations();
        return fragment;
    }

    public Locations() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_locations, container, false);
        server = new ServerCommunication("10.0.2.2", 11113);

        Bundle b = this.getArguments();

        ArrayList<String> locations = new ArrayList<>();

        Button newLocationBtn = (Button) v.findViewById(R.id.newLocationBtn);
        newLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewLocationActivity.class));
            }
        });

        locationList = server.getExistingLocations();

        for (String[] location : locationList) {
            locations.add(location[0]);
        }


        // Create the adapter to convert the array to views
        CustomLocationAdapter adapter = new CustomLocationAdapter(v.getContext(), locations);
        // Attach the adapter to a ListView
        ListView listView1 = (ListView) v.findViewById(R.id.locations_list);
        listView1.setAdapter(adapter);

        return v;
    }

}
